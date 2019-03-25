/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.http.parser.HttpParser;
/*     */ import org.apache.tomcat.util.net.openssl.ciphers.Cipher;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ 
/*     */ public class TLSClientHelloExtractor
/*     */ {
/*  38 */   private static final Log log = LogFactory.getLog(TLSClientHelloExtractor.class);
/*  39 */   private static final StringManager sm = StringManager.getManager(TLSClientHelloExtractor.class);
/*     */   
/*     */   private final ExtractorResult result;
/*     */   
/*     */   private final List<Cipher> clientRequestedCiphers;
/*     */   
/*     */   private final String sniValue;
/*     */   
/*     */   private final List<String> clientRequestedApplicationProtocols;
/*     */   private static final int TLS_RECORD_HEADER_LEN = 5;
/*     */   private static final int TLS_EXTENSION_SERVER_NAME = 0;
/*     */   private static final int TLS_EXTENSION_ALPN = 16;
/*  51 */   public static byte[] USE_TLS_RESPONSE = "HTTP/1.1 400 \r\nContent-Type: text/plain;charset=ISO-8859-1\r\nConnection: close\r\n\r\nBad Request\r\nThis combination of host and port requires TLS.\r\n"
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  56 */     .getBytes(StandardCharsets.ISO_8859_1);
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
/*     */   public TLSClientHelloExtractor(ByteBuffer netInBuffer)
/*     */     throws IOException
/*     */   {
/*  71 */     int pos = netInBuffer.position();
/*  72 */     int limit = netInBuffer.limit();
/*  73 */     ExtractorResult result = ExtractorResult.NOT_PRESENT;
/*  74 */     List<Cipher> clientRequestedCiphers = new ArrayList();
/*  75 */     List<String> clientRequestedApplicationProtocols = new ArrayList();
/*  76 */     String sniValue = null;
/*     */     try
/*     */     {
/*  79 */       netInBuffer.flip();
/*     */       
/*     */ 
/*     */ 
/*  83 */       if (!isAvailable(netInBuffer, 5)) {
/*  84 */         result = handleIncompleteRead(netInBuffer);
/*  85 */         return;
/*     */       }
/*     */       
/*  88 */       if (!isTLSHandshake(netInBuffer))
/*     */       {
/*  90 */         if (isHttp(netInBuffer)) {
/*  91 */           result = ExtractorResult.NON_SECURE;
/*     */         }
/*  93 */         return;
/*     */       }
/*     */       
/*  96 */       if (!isAllRecordAvailable(netInBuffer)) {
/*  97 */         result = handleIncompleteRead(netInBuffer);
/*  98 */         return;
/*     */       }
/*     */       
/* 101 */       if (!isClientHello(netInBuffer)) {
/* 102 */         return;
/*     */       }
/*     */       
/* 105 */       if (!isAllClientHelloAvailable(netInBuffer))
/*     */       {
/*     */ 
/* 108 */         log.warn(sm.getString("sniExtractor.clientHelloTooBig"));
/* 109 */         return;
/*     */       }
/*     */       
/*     */ 
/* 113 */       skipBytes(netInBuffer, 2);
/*     */       
/* 115 */       skipBytes(netInBuffer, 32);
/*     */       
/* 117 */       skipBytes(netInBuffer, netInBuffer.get() & 0xFF);
/*     */       
/*     */ 
/*     */ 
/* 121 */       int cipherCount = netInBuffer.getChar() / '\002';
/* 122 */       for (int i = 0; i < cipherCount; i++) {
/* 123 */         int cipherId = netInBuffer.getChar();
/* 124 */         clientRequestedCiphers.add(Cipher.valueOf(cipherId));
/*     */       }
/*     */       
/*     */ 
/* 128 */       skipBytes(netInBuffer, netInBuffer.get() & 0xFF);
/*     */       
/* 130 */       if (!netInBuffer.hasRemaining())
/*     */       {
/* 132 */         return;
/*     */       }
/*     */       
/*     */ 
/* 136 */       skipBytes(netInBuffer, 2);
/*     */       
/*     */ 
/* 139 */       while ((netInBuffer.hasRemaining()) && ((sniValue == null) || 
/* 140 */         (clientRequestedApplicationProtocols.size() == 0)))
/*     */       {
/* 142 */         char extensionType = netInBuffer.getChar();
/*     */         
/* 144 */         char extensionDataSize = netInBuffer.getChar();
/* 145 */         switch (extensionType) {
/*     */         case '\000': 
/* 147 */           sniValue = readSniExtension(netInBuffer);
/* 148 */           break;
/*     */         
/*     */         case '\020': 
/* 151 */           readAlpnExtension(netInBuffer, clientRequestedApplicationProtocols);
/* 152 */           break;
/*     */         default: 
/* 154 */           skipBytes(netInBuffer, extensionDataSize);
/*     */         }
/*     */         
/*     */       }
/* 158 */       result = ExtractorResult.COMPLETE;
/*     */     } catch (BufferUnderflowException|IllegalArgumentException e) {
/* 160 */       throw new IOException(sm.getString("sniExtractor.clientHelloInvalid"), e);
/*     */     } finally {
/* 162 */       this.result = result;
/* 163 */       this.clientRequestedCiphers = clientRequestedCiphers;
/* 164 */       this.clientRequestedApplicationProtocols = clientRequestedApplicationProtocols;
/* 165 */       this.sniValue = sniValue;
/*     */       
/* 167 */       netInBuffer.limit(limit);
/* 168 */       netInBuffer.position(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public ExtractorResult getResult()
/*     */   {
/* 174 */     return this.result;
/*     */   }
/*     */   
/*     */   public String getSNIValue()
/*     */   {
/* 179 */     if (this.result == ExtractorResult.COMPLETE) {
/* 180 */       return this.sniValue;
/*     */     }
/* 182 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */   public List<Cipher> getClientRequestedCiphers()
/*     */   {
/* 188 */     if ((this.result == ExtractorResult.COMPLETE) || (this.result == ExtractorResult.NOT_PRESENT)) {
/* 189 */       return this.clientRequestedCiphers;
/*     */     }
/* 191 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> getClientRequestedApplicationProtocols()
/*     */   {
/* 197 */     if ((this.result == ExtractorResult.COMPLETE) || (this.result == ExtractorResult.NOT_PRESENT)) {
/* 198 */       return this.clientRequestedApplicationProtocols;
/*     */     }
/* 200 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */   private static ExtractorResult handleIncompleteRead(ByteBuffer bb)
/*     */   {
/* 206 */     if (bb.limit() == bb.capacity())
/*     */     {
/* 208 */       return ExtractorResult.UNDERFLOW;
/*     */     }
/*     */     
/* 211 */     return ExtractorResult.NEED_READ;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean isAvailable(ByteBuffer bb, int size)
/*     */   {
/* 217 */     if (bb.remaining() < size) {
/* 218 */       bb.position(bb.limit());
/* 219 */       return false;
/*     */     }
/* 221 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean isTLSHandshake(ByteBuffer bb)
/*     */   {
/* 227 */     if (bb.get() != 22) {
/* 228 */       return false;
/*     */     }
/*     */     
/* 231 */     byte b2 = bb.get();
/* 232 */     byte b3 = bb.get();
/* 233 */     if ((b2 < 3) || ((b2 == 3) && (b3 == 0))) {
/* 234 */       return false;
/*     */     }
/* 236 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isHttp(ByteBuffer bb)
/*     */   {
/* 245 */     byte chr = 0;
/* 246 */     bb.position(0);
/*     */     
/*     */     do
/*     */     {
/* 250 */       if (!bb.hasRemaining()) {
/* 251 */         return false;
/*     */       }
/* 253 */       chr = bb.get();
/* 254 */     } while ((chr == 13) || (chr == 10));
/*     */     
/*     */     do
/*     */     {
/* 258 */       if ((!HttpParser.isToken(chr)) || (!bb.hasRemaining())) {
/* 259 */         return false;
/*     */       }
/* 261 */       chr = bb.get();
/* 262 */     } while ((chr != 32) && (chr != 9));
/*     */     
/*     */ 
/* 265 */     while ((chr == 32) || (chr == 9)) {
/* 266 */       if (!bb.hasRemaining()) {
/* 267 */         return false;
/*     */       }
/* 269 */       chr = bb.get();
/*     */     }
/*     */     
/*     */ 
/* 273 */     while ((chr != 32) && (chr != 9)) {
/* 274 */       if ((HttpParser.isNotRequestTarget(chr)) || (!bb.hasRemaining())) {
/* 275 */         return false;
/*     */       }
/* 277 */       chr = bb.get();
/*     */     }
/*     */     
/*     */ 
/* 281 */     while ((chr == 32) || (chr == 9)) {
/* 282 */       if (!bb.hasRemaining()) {
/* 283 */         return false;
/*     */       }
/* 285 */       chr = bb.get();
/*     */     }
/*     */     
/*     */     do
/*     */     {
/* 290 */       if ((!HttpParser.isHttpProtocol(chr)) || (!bb.hasRemaining())) {
/* 291 */         return false;
/*     */       }
/* 293 */       chr = bb.get();
/*     */     }
/* 295 */     while ((chr != 13) && (chr != 10));
/*     */     
/* 297 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static boolean isAllRecordAvailable(ByteBuffer bb)
/*     */   {
/* 304 */     int size = bb.getChar();
/* 305 */     return isAvailable(bb, size);
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean isClientHello(ByteBuffer bb)
/*     */   {
/* 311 */     if (bb.get() == 1) {
/* 312 */       return true;
/*     */     }
/* 314 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static boolean isAllClientHelloAvailable(ByteBuffer bb)
/*     */   {
/* 321 */     int size = ((bb.get() & 0xFF) << 16) + ((bb.get() & 0xFF) << 8) + (bb.get() & 0xFF);
/* 322 */     return isAvailable(bb, size);
/*     */   }
/*     */   
/*     */   private static void skipBytes(ByteBuffer bb, int size)
/*     */   {
/* 327 */     bb.position(bb.position() + size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String readSniExtension(ByteBuffer bb)
/*     */   {
/* 334 */     skipBytes(bb, 3);
/*     */     
/* 336 */     char serverNameSize = bb.getChar();
/* 337 */     byte[] serverNameBytes = new byte[serverNameSize];
/* 338 */     bb.get(serverNameBytes);
/* 339 */     return new String(serverNameBytes, StandardCharsets.UTF_8);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void readAlpnExtension(ByteBuffer bb, List<String> protocolNames)
/*     */   {
/* 345 */     char toRead = bb.getChar();
/* 346 */     byte[] inputBuffer = new byte['ÿ'];
/* 347 */     while (toRead > 0)
/*     */     {
/*     */ 
/* 350 */       int len = bb.get() & 0xFF;
/* 351 */       bb.get(inputBuffer, 0, len);
/* 352 */       protocolNames.add(new String(inputBuffer, 0, len, StandardCharsets.UTF_8));
/* 353 */       toRead = (char)(toRead - '\001');
/* 354 */       toRead = (char)(toRead - len);
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum ExtractorResult
/*     */   {
/* 360 */     COMPLETE, 
/* 361 */     NOT_PRESENT, 
/* 362 */     UNDERFLOW, 
/* 363 */     NEED_READ, 
/* 364 */     NON_SECURE;
/*     */     
/*     */     private ExtractorResult() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\TLSClientHelloExtractor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */