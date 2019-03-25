/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.coyote.Adapter;
/*     */ import org.apache.coyote.CompressionConfig;
/*     */ import org.apache.coyote.Processor;
/*     */ import org.apache.coyote.Request;
/*     */ import org.apache.coyote.Response;
/*     */ import org.apache.coyote.UpgradeProtocol;
/*     */ import org.apache.coyote.UpgradeToken;
/*     */ import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
/*     */ import org.apache.coyote.http11.upgrade.UpgradeProcessorInternal;
/*     */ import org.apache.tomcat.util.buf.StringUtils;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Http2Protocol
/*     */   implements UpgradeProtocol
/*     */ {
/*     */   static final long DEFAULT_READ_TIMEOUT = 10000L;
/*     */   static final long DEFAULT_KEEP_ALIVE_TIMEOUT = -1L;
/*     */   static final long DEFAULT_WRITE_TIMEOUT = 10000L;
/*     */   static final long DEFAULT_MAX_CONCURRENT_STREAMS = 200L;
/*     */   static final int DEFAULT_MAX_CONCURRENT_STREAM_EXECUTION = 20;
/*     */   static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
/*     */   private static final String HTTP_UPGRADE_NAME = "h2c";
/*     */   private static final String ALPN_NAME = "h2";
/*  57 */   private static final byte[] ALPN_IDENTIFIER = "h2".getBytes(StandardCharsets.UTF_8);
/*     */   
/*     */ 
/*  60 */   private long readTimeout = 10000L;
/*  61 */   private long keepAliveTimeout = -1L;
/*  62 */   private long writeTimeout = 10000L;
/*  63 */   private long maxConcurrentStreams = 200L;
/*  64 */   private int maxConcurrentStreamExecution = 20;
/*     */   
/*     */ 
/*  67 */   private int initialWindowSize = 65535;
/*     */   
/*     */ 
/*  70 */   private Set<String> allowedTrailerHeaders = Collections.newSetFromMap(new ConcurrentHashMap());
/*  71 */   private int maxHeaderCount = 100;
/*  72 */   private int maxHeaderSize = 8192;
/*  73 */   private int maxTrailerCount = 100;
/*  74 */   private int maxTrailerSize = 8192;
/*  75 */   private boolean initiatePingDisabled = false;
/*     */   
/*  77 */   private final CompressionConfig compressionConfig = new CompressionConfig();
/*     */   
/*     */   public String getHttpUpgradeName(boolean isSSLEnabled)
/*     */   {
/*  81 */     if (isSSLEnabled) {
/*  82 */       return null;
/*     */     }
/*  84 */     return "h2c";
/*     */   }
/*     */   
/*     */ 
/*     */   public byte[] getAlpnIdentifier()
/*     */   {
/*  90 */     return ALPN_IDENTIFIER;
/*     */   }
/*     */   
/*     */   public String getAlpnName()
/*     */   {
/*  95 */     return "h2";
/*     */   }
/*     */   
/*     */ 
/*     */   public Processor getProcessor(SocketWrapperBase<?> socketWrapper, Adapter adapter)
/*     */   {
/* 101 */     UpgradeProcessorInternal processor = new UpgradeProcessorInternal(socketWrapper, new UpgradeToken(getInternalUpgradeHandler(adapter, null), null, null));
/* 102 */     return processor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InternalHttpUpgradeHandler getInternalUpgradeHandler(Adapter adapter, Request coyoteRequest)
/*     */   {
/* 109 */     Http2UpgradeHandler result = new Http2UpgradeHandler(adapter, coyoteRequest);
/*     */     
/* 111 */     result.setReadTimeout(getReadTimeout());
/* 112 */     result.setKeepAliveTimeout(getKeepAliveTimeout());
/* 113 */     result.setWriteTimeout(getWriteTimeout());
/* 114 */     result.setMaxConcurrentStreams(getMaxConcurrentStreams());
/* 115 */     result.setMaxConcurrentStreamExecution(getMaxConcurrentStreamExecution());
/* 116 */     result.setInitialWindowSize(getInitialWindowSize());
/* 117 */     result.setAllowedTrailerHeaders(this.allowedTrailerHeaders);
/* 118 */     result.setMaxHeaderCount(getMaxHeaderCount());
/* 119 */     result.setMaxHeaderSize(getMaxHeaderSize());
/* 120 */     result.setMaxTrailerCount(getMaxTrailerCount());
/* 121 */     result.setMaxTrailerSize(getMaxTrailerSize());
/* 122 */     result.setInitiatePingDisabled(this.initiatePingDisabled);
/* 123 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean accept(Request request)
/*     */   {
/* 130 */     Enumeration<String> settings = request.getMimeHeaders().values("HTTP2-Settings");
/* 131 */     int count = 0;
/* 132 */     while (settings.hasMoreElements()) {
/* 133 */       count++;
/* 134 */       settings.nextElement();
/*     */     }
/* 136 */     if (count != 1) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     Enumeration<String> connection = request.getMimeHeaders().values("Connection");
/* 141 */     boolean found = false;
/* 142 */     while ((connection.hasMoreElements()) && (!found)) {
/* 143 */       found = ((String)connection.nextElement()).contains("HTTP2-Settings");
/*     */     }
/* 145 */     return found;
/*     */   }
/*     */   
/*     */   public long getReadTimeout()
/*     */   {
/* 150 */     return this.readTimeout;
/*     */   }
/*     */   
/*     */   public void setReadTimeout(long readTimeout)
/*     */   {
/* 155 */     this.readTimeout = readTimeout;
/*     */   }
/*     */   
/*     */   public long getKeepAliveTimeout()
/*     */   {
/* 160 */     return this.keepAliveTimeout;
/*     */   }
/*     */   
/*     */   public void setKeepAliveTimeout(long keepAliveTimeout)
/*     */   {
/* 165 */     this.keepAliveTimeout = keepAliveTimeout;
/*     */   }
/*     */   
/*     */   public long getWriteTimeout()
/*     */   {
/* 170 */     return this.writeTimeout;
/*     */   }
/*     */   
/*     */   public void setWriteTimeout(long writeTimeout)
/*     */   {
/* 175 */     this.writeTimeout = writeTimeout;
/*     */   }
/*     */   
/*     */   public long getMaxConcurrentStreams()
/*     */   {
/* 180 */     return this.maxConcurrentStreams;
/*     */   }
/*     */   
/*     */   public void setMaxConcurrentStreams(long maxConcurrentStreams)
/*     */   {
/* 185 */     this.maxConcurrentStreams = maxConcurrentStreams;
/*     */   }
/*     */   
/*     */   public int getMaxConcurrentStreamExecution()
/*     */   {
/* 190 */     return this.maxConcurrentStreamExecution;
/*     */   }
/*     */   
/*     */   public void setMaxConcurrentStreamExecution(int maxConcurrentStreamExecution)
/*     */   {
/* 195 */     this.maxConcurrentStreamExecution = maxConcurrentStreamExecution;
/*     */   }
/*     */   
/*     */   public int getInitialWindowSize()
/*     */   {
/* 200 */     return this.initialWindowSize;
/*     */   }
/*     */   
/*     */   public void setInitialWindowSize(int initialWindowSize)
/*     */   {
/* 205 */     this.initialWindowSize = initialWindowSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAllowedTrailerHeaders(String commaSeparatedHeaders)
/*     */   {
/* 212 */     Set<String> toRemove = new HashSet();
/* 213 */     toRemove.addAll(this.allowedTrailerHeaders);
/* 214 */     if (commaSeparatedHeaders != null) {
/* 215 */       String[] headers = commaSeparatedHeaders.split(",");
/* 216 */       for (String header : headers) {
/* 217 */         String trimmedHeader = header.trim().toLowerCase(Locale.ENGLISH);
/* 218 */         if (toRemove.contains(trimmedHeader)) {
/* 219 */           toRemove.remove(trimmedHeader);
/*     */         } else {
/* 221 */           this.allowedTrailerHeaders.add(trimmedHeader);
/*     */         }
/*     */       }
/* 224 */       this.allowedTrailerHeaders.removeAll(toRemove);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getAllowedTrailerHeaders()
/*     */   {
/* 232 */     List<String> copy = new ArrayList(this.allowedTrailerHeaders.size());
/* 233 */     copy.addAll(this.allowedTrailerHeaders);
/* 234 */     return StringUtils.join(copy);
/*     */   }
/*     */   
/*     */   public void setMaxHeaderCount(int maxHeaderCount)
/*     */   {
/* 239 */     this.maxHeaderCount = maxHeaderCount;
/*     */   }
/*     */   
/*     */   public int getMaxHeaderCount()
/*     */   {
/* 244 */     return this.maxHeaderCount;
/*     */   }
/*     */   
/*     */   public void setMaxHeaderSize(int maxHeaderSize)
/*     */   {
/* 249 */     this.maxHeaderSize = maxHeaderSize;
/*     */   }
/*     */   
/*     */   public int getMaxHeaderSize()
/*     */   {
/* 254 */     return this.maxHeaderSize;
/*     */   }
/*     */   
/*     */   public void setMaxTrailerCount(int maxTrailerCount)
/*     */   {
/* 259 */     this.maxTrailerCount = maxTrailerCount;
/*     */   }
/*     */   
/*     */   public int getMaxTrailerCount()
/*     */   {
/* 264 */     return this.maxTrailerCount;
/*     */   }
/*     */   
/*     */   public void setMaxTrailerSize(int maxTrailerSize)
/*     */   {
/* 269 */     this.maxTrailerSize = maxTrailerSize;
/*     */   }
/*     */   
/*     */   public int getMaxTrailerSize()
/*     */   {
/* 274 */     return this.maxTrailerSize;
/*     */   }
/*     */   
/*     */   public void setInitiatePingDisabled(boolean initiatePingDisabled)
/*     */   {
/* 279 */     this.initiatePingDisabled = initiatePingDisabled;
/*     */   }
/*     */   
/*     */   public void setCompression(String compression)
/*     */   {
/* 284 */     this.compressionConfig.setCompression(compression);
/*     */   }
/*     */   
/* 287 */   public String getCompression() { return this.compressionConfig.getCompression(); }
/*     */   
/*     */   protected int getCompressionLevel() {
/* 290 */     return this.compressionConfig.getCompressionLevel();
/*     */   }
/*     */   
/*     */   public String getNoCompressionUserAgents()
/*     */   {
/* 295 */     return this.compressionConfig.getNoCompressionUserAgents();
/*     */   }
/*     */   
/* 298 */   protected Pattern getNoCompressionUserAgentsPattern() { return this.compressionConfig.getNoCompressionUserAgentsPattern(); }
/*     */   
/*     */   public void setNoCompressionUserAgents(String noCompressionUserAgents) {
/* 301 */     this.compressionConfig.setNoCompressionUserAgents(noCompressionUserAgents);
/*     */   }
/*     */   
/*     */   public String getCompressibleMimeType()
/*     */   {
/* 306 */     return this.compressionConfig.getCompressibleMimeType();
/*     */   }
/*     */   
/* 309 */   public void setCompressibleMimeType(String valueS) { this.compressionConfig.setCompressibleMimeType(valueS); }
/*     */   
/*     */   public String[] getCompressibleMimeTypes() {
/* 312 */     return this.compressionConfig.getCompressibleMimeTypes();
/*     */   }
/*     */   
/*     */   public int getCompressionMinSize()
/*     */   {
/* 317 */     return this.compressionConfig.getCompressionMinSize();
/*     */   }
/*     */   
/* 320 */   public void setCompressionMinSize(int compressionMinSize) { this.compressionConfig.setCompressionMinSize(compressionMinSize); }
/*     */   
/*     */ 
/*     */   public boolean useCompression(Request request, Response response)
/*     */   {
/* 325 */     return this.compressionConfig.useCompression(request, response);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Http2Protocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */