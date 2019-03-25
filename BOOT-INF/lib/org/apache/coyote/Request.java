/*     */ package org.apache.coyote;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.servlet.ReadListener;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ import org.apache.tomcat.util.http.Parameters;
/*     */ import org.apache.tomcat.util.http.ServerCookies;
/*     */ import org.apache.tomcat.util.net.ApplicationBufferHandler;
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
/*     */ 
/*     */ 
/*     */ public final class Request
/*     */ {
/*  67 */   private static final StringManager sm = StringManager.getManager(Request.class);
/*     */   
/*     */ 
/*     */   private static final int INITIAL_COOKIE_SIZE = 4;
/*     */   
/*     */ 
/*     */   public Request()
/*     */   {
/*  75 */     this.parameters.setQuery(this.queryMB);
/*  76 */     this.parameters.setURLDecoder(this.urlDecoder);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  82 */   private int serverPort = -1;
/*  83 */   private final MessageBytes serverNameMB = MessageBytes.newInstance();
/*     */   
/*     */   private int remotePort;
/*     */   
/*     */   private int localPort;
/*  88 */   private final MessageBytes schemeMB = MessageBytes.newInstance();
/*     */   
/*  90 */   private final MessageBytes methodMB = MessageBytes.newInstance();
/*  91 */   private final MessageBytes uriMB = MessageBytes.newInstance();
/*  92 */   private final MessageBytes decodedUriMB = MessageBytes.newInstance();
/*  93 */   private final MessageBytes queryMB = MessageBytes.newInstance();
/*  94 */   private final MessageBytes protoMB = MessageBytes.newInstance();
/*     */   
/*     */ 
/*  97 */   private final MessageBytes remoteAddrMB = MessageBytes.newInstance();
/*  98 */   private final MessageBytes localNameMB = MessageBytes.newInstance();
/*  99 */   private final MessageBytes remoteHostMB = MessageBytes.newInstance();
/* 100 */   private final MessageBytes localAddrMB = MessageBytes.newInstance();
/*     */   
/* 102 */   private final MimeHeaders headers = new MimeHeaders();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   private final Map<String, String> pathParameters = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 113 */   private final Object[] notes = new Object[32];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */   private InputBuffer inputBuffer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   private final UDecoder urlDecoder = new UDecoder();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */   private long contentLength = -1L;
/* 132 */   private MessageBytes contentTypeMB = null;
/* 133 */   private Charset charset = null;
/*     */   
/*     */ 
/* 136 */   private String characterEncoding = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 141 */   private boolean expectation = false;
/*     */   
/* 143 */   private final ServerCookies serverCookies = new ServerCookies(4);
/* 144 */   private final Parameters parameters = new Parameters();
/*     */   
/* 146 */   private final MessageBytes remoteUser = MessageBytes.newInstance();
/* 147 */   private boolean remoteUserNeedsAuthorization = false;
/* 148 */   private final MessageBytes authType = MessageBytes.newInstance();
/* 149 */   private final HashMap<String, Object> attributes = new HashMap();
/*     */   
/*     */   private Response response;
/*     */   
/*     */   private volatile ActionHook hook;
/* 154 */   private long bytesRead = 0L;
/*     */   
/* 156 */   private long startTime = -1L;
/* 157 */   private int available = 0;
/*     */   
/* 159 */   private final RequestInfo reqProcessorMX = new RequestInfo(this);
/*     */   
/* 161 */   private boolean sendfile = true;
/*     */   volatile ReadListener listener;
/*     */   
/*     */   public ReadListener getReadListener()
/*     */   {
/* 166 */     return this.listener;
/*     */   }
/*     */   
/*     */   public void setReadListener(ReadListener listener) {
/* 170 */     if (listener == null)
/*     */     {
/* 172 */       throw new NullPointerException(sm.getString("request.nullReadListener"));
/*     */     }
/* 174 */     if (getReadListener() != null)
/*     */     {
/* 176 */       throw new IllegalStateException(sm.getString("request.readListenerSet"));
/*     */     }
/*     */     
/*     */ 
/* 180 */     AtomicBoolean result = new AtomicBoolean(false);
/* 181 */     action(ActionCode.ASYNC_IS_ASYNC, result);
/* 182 */     if (!result.get())
/*     */     {
/* 184 */       throw new IllegalStateException(sm.getString("request.notAsync"));
/*     */     }
/*     */     
/* 187 */     this.listener = listener;
/*     */   }
/*     */   
/* 190 */   private final AtomicBoolean allDataReadEventSent = new AtomicBoolean(false);
/*     */   
/*     */   public boolean sendAllDataReadEvent() {
/* 193 */     return this.allDataReadEventSent.compareAndSet(false, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MimeHeaders getMimeHeaders()
/*     */   {
/* 200 */     return this.headers;
/*     */   }
/*     */   
/*     */   public UDecoder getURLDecoder()
/*     */   {
/* 205 */     return this.urlDecoder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MessageBytes scheme()
/*     */   {
/* 212 */     return this.schemeMB;
/*     */   }
/*     */   
/*     */   public MessageBytes method() {
/* 216 */     return this.methodMB;
/*     */   }
/*     */   
/*     */   public MessageBytes requestURI() {
/* 220 */     return this.uriMB;
/*     */   }
/*     */   
/*     */   public MessageBytes decodedURI() {
/* 224 */     return this.decodedUriMB;
/*     */   }
/*     */   
/*     */   public MessageBytes queryString() {
/* 228 */     return this.queryMB;
/*     */   }
/*     */   
/*     */   public MessageBytes protocol() {
/* 232 */     return this.protoMB;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageBytes serverName()
/*     */   {
/* 243 */     return this.serverNameMB;
/*     */   }
/*     */   
/*     */   public int getServerPort() {
/* 247 */     return this.serverPort;
/*     */   }
/*     */   
/*     */   public void setServerPort(int serverPort) {
/* 251 */     this.serverPort = serverPort;
/*     */   }
/*     */   
/*     */   public MessageBytes remoteAddr() {
/* 255 */     return this.remoteAddrMB;
/*     */   }
/*     */   
/*     */   public MessageBytes remoteHost() {
/* 259 */     return this.remoteHostMB;
/*     */   }
/*     */   
/*     */   public MessageBytes localName() {
/* 263 */     return this.localNameMB;
/*     */   }
/*     */   
/*     */   public MessageBytes localAddr() {
/* 267 */     return this.localAddrMB;
/*     */   }
/*     */   
/*     */   public int getRemotePort() {
/* 271 */     return this.remotePort;
/*     */   }
/*     */   
/*     */   public void setRemotePort(int port) {
/* 275 */     this.remotePort = port;
/*     */   }
/*     */   
/*     */   public int getLocalPort() {
/* 279 */     return this.localPort;
/*     */   }
/*     */   
/*     */   public void setLocalPort(int port) {
/* 283 */     this.localPort = port;
/*     */   }
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
/*     */   public String getCharacterEncoding()
/*     */   {
/* 297 */     if (this.characterEncoding == null) {
/* 298 */       this.characterEncoding = getCharsetFromContentType(getContentType());
/*     */     }
/*     */     
/* 301 */     return this.characterEncoding;
/*     */   }
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
/*     */   public Charset getCharset()
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 316 */     if (this.charset == null) {
/* 317 */       getCharacterEncoding();
/* 318 */       if (this.characterEncoding != null) {
/* 319 */         this.charset = B2CConverter.getCharset(this.characterEncoding);
/*     */       }
/*     */     }
/*     */     
/* 323 */     return this.charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setCharacterEncoding(String enc)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 336 */     setCharset(B2CConverter.getCharset(enc));
/*     */   }
/*     */   
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 341 */     this.charset = charset;
/* 342 */     this.characterEncoding = charset.name();
/*     */   }
/*     */   
/*     */   public void setContentLength(long len)
/*     */   {
/* 347 */     this.contentLength = len;
/*     */   }
/*     */   
/*     */   public int getContentLength()
/*     */   {
/* 352 */     long length = getContentLengthLong();
/*     */     
/* 354 */     if (length < 2147483647L) {
/* 355 */       return (int)length;
/*     */     }
/* 357 */     return -1;
/*     */   }
/*     */   
/*     */   public long getContentLengthLong() {
/* 361 */     if (this.contentLength > -1L) {
/* 362 */       return this.contentLength;
/*     */     }
/*     */     
/* 365 */     MessageBytes clB = this.headers.getUniqueValue("content-length");
/* 366 */     this.contentLength = ((clB == null) || (clB.isNull()) ? -1L : clB.getLong());
/*     */     
/* 368 */     return this.contentLength;
/*     */   }
/*     */   
/*     */   public String getContentType() {
/* 372 */     contentType();
/* 373 */     if ((this.contentTypeMB == null) || (this.contentTypeMB.isNull())) {
/* 374 */       return null;
/*     */     }
/* 376 */     return this.contentTypeMB.toString();
/*     */   }
/*     */   
/*     */   public void setContentType(String type)
/*     */   {
/* 381 */     this.contentTypeMB.setString(type);
/*     */   }
/*     */   
/*     */   public MessageBytes contentType()
/*     */   {
/* 386 */     if (this.contentTypeMB == null) {
/* 387 */       this.contentTypeMB = this.headers.getValue("content-type");
/*     */     }
/* 389 */     return this.contentTypeMB;
/*     */   }
/*     */   
/*     */   public void setContentType(MessageBytes mb)
/*     */   {
/* 394 */     this.contentTypeMB = mb;
/*     */   }
/*     */   
/*     */   public String getHeader(String name)
/*     */   {
/* 399 */     return this.headers.getHeader(name);
/*     */   }
/*     */   
/*     */   public void setExpectation(boolean expectation)
/*     */   {
/* 404 */     this.expectation = expectation;
/*     */   }
/*     */   
/*     */   public boolean hasExpectation()
/*     */   {
/* 409 */     return this.expectation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Response getResponse()
/*     */   {
/* 416 */     return this.response;
/*     */   }
/*     */   
/*     */   public void setResponse(Response response) {
/* 420 */     this.response = response;
/* 421 */     response.setRequest(this);
/*     */   }
/*     */   
/*     */   protected void setHook(ActionHook hook) {
/* 425 */     this.hook = hook;
/*     */   }
/*     */   
/*     */   public void action(ActionCode actionCode, Object param) {
/* 429 */     if (this.hook != null) {
/* 430 */       if (param == null) {
/* 431 */         this.hook.action(actionCode, this);
/*     */       } else {
/* 433 */         this.hook.action(actionCode, param);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ServerCookies getCookies()
/*     */   {
/* 442 */     return this.serverCookies;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Parameters getParameters()
/*     */   {
/* 449 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void addPathParameter(String name, String value)
/*     */   {
/* 454 */     this.pathParameters.put(name, value);
/*     */   }
/*     */   
/*     */   public String getPathParameter(String name) {
/* 458 */     return (String)this.pathParameters.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAttribute(String name, Object o)
/*     */   {
/* 466 */     this.attributes.put(name, o);
/*     */   }
/*     */   
/*     */   public HashMap<String, Object> getAttributes() {
/* 470 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public Object getAttribute(String name) {
/* 474 */     return this.attributes.get(name);
/*     */   }
/*     */   
/*     */   public MessageBytes getRemoteUser() {
/* 478 */     return this.remoteUser;
/*     */   }
/*     */   
/*     */   public boolean getRemoteUserNeedsAuthorization() {
/* 482 */     return this.remoteUserNeedsAuthorization;
/*     */   }
/*     */   
/*     */   public void setRemoteUserNeedsAuthorization(boolean remoteUserNeedsAuthorization) {
/* 486 */     this.remoteUserNeedsAuthorization = remoteUserNeedsAuthorization;
/*     */   }
/*     */   
/*     */   public MessageBytes getAuthType() {
/* 490 */     return this.authType;
/*     */   }
/*     */   
/*     */   public int getAvailable() {
/* 494 */     return this.available;
/*     */   }
/*     */   
/*     */   public void setAvailable(int available) {
/* 498 */     this.available = available;
/*     */   }
/*     */   
/*     */   public boolean getSendfile() {
/* 502 */     return this.sendfile;
/*     */   }
/*     */   
/*     */   public void setSendfile(boolean sendfile) {
/* 506 */     this.sendfile = sendfile;
/*     */   }
/*     */   
/*     */   public boolean isFinished() {
/* 510 */     AtomicBoolean result = new AtomicBoolean(false);
/* 511 */     action(ActionCode.REQUEST_BODY_FULLY_READ, result);
/* 512 */     return result.get();
/*     */   }
/*     */   
/*     */   public boolean getSupportsRelativeRedirects() {
/* 516 */     if ((protocol().equals("")) || (protocol().equals("HTTP/1.0"))) {
/* 517 */       return false;
/*     */     }
/* 519 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InputBuffer getInputBuffer()
/*     */   {
/* 526 */     return this.inputBuffer;
/*     */   }
/*     */   
/*     */   public void setInputBuffer(InputBuffer inputBuffer)
/*     */   {
/* 531 */     this.inputBuffer = inputBuffer;
/*     */   }
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
/*     */   @Deprecated
/*     */   public int doRead(ByteChunk chunk)
/*     */     throws IOException
/*     */   {
/* 556 */     int n = this.inputBuffer.doRead(chunk);
/* 557 */     if (n > 0) {
/* 558 */       this.bytesRead += n;
/*     */     }
/* 560 */     return n;
/*     */   }
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
/*     */   public int doRead(ApplicationBufferHandler handler)
/*     */     throws IOException
/*     */   {
/* 581 */     int n = this.inputBuffer.doRead(handler);
/* 582 */     if (n > 0) {
/* 583 */       this.bytesRead += n;
/*     */     }
/* 585 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 593 */     return "R( " + requestURI().toString() + ")";
/*     */   }
/*     */   
/*     */   public long getStartTime() {
/* 597 */     return this.startTime;
/*     */   }
/*     */   
/*     */   public void setStartTime(long startTime) {
/* 601 */     this.startTime = startTime;
/*     */   }
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
/*     */   public final void setNote(int pos, Object value)
/*     */   {
/* 626 */     this.notes[pos] = value;
/*     */   }
/*     */   
/*     */   public final Object getNote(int pos)
/*     */   {
/* 631 */     return this.notes[pos];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 639 */     this.bytesRead = 0L;
/*     */     
/* 641 */     this.contentLength = -1L;
/* 642 */     this.contentTypeMB = null;
/* 643 */     this.charset = null;
/* 644 */     this.characterEncoding = null;
/* 645 */     this.expectation = false;
/* 646 */     this.headers.recycle();
/* 647 */     this.serverNameMB.recycle();
/* 648 */     this.serverPort = -1;
/* 649 */     this.localAddrMB.recycle();
/* 650 */     this.localNameMB.recycle();
/* 651 */     this.localPort = -1;
/* 652 */     this.remoteAddrMB.recycle();
/* 653 */     this.remoteHostMB.recycle();
/* 654 */     this.remotePort = -1;
/* 655 */     this.available = 0;
/* 656 */     this.sendfile = true;
/*     */     
/* 658 */     this.serverCookies.recycle();
/* 659 */     this.parameters.recycle();
/* 660 */     this.pathParameters.clear();
/*     */     
/* 662 */     this.uriMB.recycle();
/* 663 */     this.decodedUriMB.recycle();
/* 664 */     this.queryMB.recycle();
/* 665 */     this.methodMB.recycle();
/* 666 */     this.protoMB.recycle();
/*     */     
/* 668 */     this.schemeMB.recycle();
/*     */     
/* 670 */     this.remoteUser.recycle();
/* 671 */     this.remoteUserNeedsAuthorization = false;
/* 672 */     this.authType.recycle();
/* 673 */     this.attributes.clear();
/*     */     
/* 675 */     this.listener = null;
/* 676 */     this.allDataReadEventSent.set(false);
/*     */     
/* 678 */     this.startTime = -1L;
/*     */   }
/*     */   
/*     */   public void updateCounters()
/*     */   {
/* 683 */     this.reqProcessorMX.updateCounters();
/*     */   }
/*     */   
/*     */   public RequestInfo getRequestProcessor() {
/* 687 */     return this.reqProcessorMX;
/*     */   }
/*     */   
/*     */   public long getBytesRead() {
/* 691 */     return this.bytesRead;
/*     */   }
/*     */   
/*     */   public boolean isProcessing() {
/* 695 */     return this.reqProcessorMX.getStage() == 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getCharsetFromContentType(String contentType)
/*     */   {
/* 707 */     if (contentType == null) {
/* 708 */       return null;
/*     */     }
/* 710 */     int start = contentType.indexOf("charset=");
/* 711 */     if (start < 0) {
/* 712 */       return null;
/*     */     }
/* 714 */     String encoding = contentType.substring(start + 8);
/* 715 */     int end = encoding.indexOf(';');
/* 716 */     if (end >= 0) {
/* 717 */       encoding = encoding.substring(0, end);
/*     */     }
/* 719 */     encoding = encoding.trim();
/* 720 */     if ((encoding.length() > 2) && (encoding.startsWith("\"")) && 
/* 721 */       (encoding.endsWith("\""))) {
/* 722 */       encoding = encoding.substring(1, encoding.length() - 1);
/*     */     }
/*     */     
/* 725 */     return encoding.trim();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\Request.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */