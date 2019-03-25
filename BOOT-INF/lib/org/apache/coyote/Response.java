/*     */ package org.apache.coyote;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.servlet.WriteListener;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ import org.apache.tomcat.util.http.parser.MediaType;
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
/*     */ public final class Response
/*     */ {
/*  50 */   private static final StringManager sm = StringManager.getManager(Response.class);
/*     */   
/*  52 */   private static final Log log = LogFactory.getLog(Response.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private static final Locale DEFAULT_LOCALE = Locale.getDefault();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   int status = 200;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */   String message = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   final MimeHeaders headers = new MimeHeaders();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   OutputBuffer outputBuffer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */   final Object[] notes = new Object[32];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   volatile boolean commited = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   volatile ActionHook hook;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   String contentType = null;
/* 110 */   String contentLanguage = null;
/* 111 */   Charset charset = null;
/*     */   
/*     */ 
/*     */ 
/* 115 */   String characterEncoding = null;
/* 116 */   long contentLength = -1L;
/* 117 */   private Locale locale = DEFAULT_LOCALE;
/*     */   
/*     */ 
/* 120 */   private long contentWritten = 0L;
/* 121 */   private long commitTime = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 126 */   Exception errorException = null;
/*     */   
/*     */   Request req;
/*     */   volatile WriteListener listener;
/*     */   
/*     */   public Request getRequest()
/*     */   {
/* 133 */     return this.req;
/*     */   }
/*     */   
/*     */   public void setRequest(Request req) {
/* 137 */     this.req = req;
/*     */   }
/*     */   
/*     */   public void setOutputBuffer(OutputBuffer outputBuffer)
/*     */   {
/* 142 */     this.outputBuffer = outputBuffer;
/*     */   }
/*     */   
/*     */   public MimeHeaders getMimeHeaders()
/*     */   {
/* 147 */     return this.headers;
/*     */   }
/*     */   
/*     */   protected void setHook(ActionHook hook)
/*     */   {
/* 152 */     this.hook = hook;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void setNote(int pos, Object value)
/*     */   {
/* 159 */     this.notes[pos] = value;
/*     */   }
/*     */   
/*     */   public final Object getNote(int pos)
/*     */   {
/* 164 */     return this.notes[pos];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void action(ActionCode actionCode, Object param)
/*     */   {
/* 171 */     if (this.hook != null) {
/* 172 */       if (param == null) {
/* 173 */         this.hook.action(actionCode, this);
/*     */       } else {
/* 175 */         this.hook.action(actionCode, param);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getStatus()
/*     */   {
/* 184 */     return this.status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatus(int status)
/*     */   {
/* 194 */     this.status = status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 204 */     return this.message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessage(String message)
/*     */   {
/* 214 */     this.message = message;
/*     */   }
/*     */   
/*     */   public boolean isCommitted()
/*     */   {
/* 219 */     return this.commited;
/*     */   }
/*     */   
/*     */   public void setCommitted(boolean v)
/*     */   {
/* 224 */     if ((v) && (!this.commited)) {
/* 225 */       this.commitTime = System.currentTimeMillis();
/*     */     }
/* 227 */     this.commited = v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getCommitTime()
/*     */   {
/* 236 */     return this.commitTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setErrorException(Exception ex)
/*     */   {
/* 248 */     this.errorException = ex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Exception getErrorException()
/*     */   {
/* 258 */     return this.errorException;
/*     */   }
/*     */   
/*     */   public boolean isExceptionPresent()
/*     */   {
/* 263 */     return this.errorException != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */     throws IllegalStateException
/*     */   {
/* 272 */     if (this.commited) {
/* 273 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 276 */     recycle();
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
/*     */   public boolean containsHeader(String name)
/*     */   {
/* 292 */     return this.headers.getHeader(name) != null;
/*     */   }
/*     */   
/*     */   public void setHeader(String name, String value)
/*     */   {
/* 297 */     char cc = name.charAt(0);
/* 298 */     if (((cc == 'C') || (cc == 'c')) && 
/* 299 */       (checkSpecialHeader(name, value))) {
/* 300 */       return;
/*     */     }
/* 302 */     this.headers.setValue(name).setString(value);
/*     */   }
/*     */   
/*     */   public void addHeader(String name, String value)
/*     */   {
/* 307 */     addHeader(name, value, null);
/*     */   }
/*     */   
/*     */   public void addHeader(String name, String value, Charset charset)
/*     */   {
/* 312 */     char cc = name.charAt(0);
/* 313 */     if (((cc == 'C') || (cc == 'c')) && 
/* 314 */       (checkSpecialHeader(name, value))) {
/* 315 */       return;
/*     */     }
/* 317 */     MessageBytes mb = this.headers.addValue(name);
/* 318 */     if (charset != null) {
/* 319 */       mb.setCharset(charset);
/*     */     }
/* 321 */     mb.setString(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean checkSpecialHeader(String name, String value)
/*     */   {
/* 333 */     if (name.equalsIgnoreCase("Content-Type")) {
/* 334 */       setContentType(value);
/* 335 */       return true;
/*     */     }
/* 337 */     if (name.equalsIgnoreCase("Content-Length")) {
/*     */       try {
/* 339 */         long cL = Long.parseLong(value);
/* 340 */         setContentLength(cL);
/* 341 */         return true;
/*     */       }
/*     */       catch (NumberFormatException ex)
/*     */       {
/* 345 */         return false;
/*     */       }
/*     */     }
/* 348 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendHeaders()
/*     */   {
/* 357 */     action(ActionCode.COMMIT, this);
/* 358 */     setCommitted(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 366 */     return this.locale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/* 377 */     if (locale == null) {
/* 378 */       return;
/*     */     }
/*     */     
/*     */ 
/* 382 */     this.locale = locale;
/*     */     
/*     */ 
/* 385 */     this.contentLanguage = locale.toLanguageTag();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentLanguage()
/*     */   {
/* 395 */     return this.contentLanguage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharacterEncoding(String characterEncoding)
/*     */   {
/* 407 */     if (isCommitted()) {
/* 408 */       return;
/*     */     }
/* 410 */     if (characterEncoding == null) {
/* 411 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 415 */       this.charset = B2CConverter.getCharset(characterEncoding);
/*     */     } catch (UnsupportedEncodingException e) {
/* 417 */       throw new IllegalArgumentException(e);
/*     */     }
/* 419 */     this.characterEncoding = characterEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/* 427 */     return this.characterEncoding;
/*     */   }
/*     */   
/*     */   public Charset getCharset()
/*     */   {
/* 432 */     return this.charset;
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
/*     */   public void setContentType(String type)
/*     */   {
/* 447 */     if (type == null) {
/* 448 */       this.contentType = null;
/* 449 */       return;
/*     */     }
/*     */     
/* 452 */     MediaType m = null;
/*     */     try {
/* 454 */       m = MediaType.parseMediaType(new StringReader(type));
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/* 458 */     if (m == null)
/*     */     {
/*     */ 
/* 461 */       this.contentType = type;
/* 462 */       return;
/*     */     }
/*     */     
/* 465 */     this.contentType = m.toStringNoCharset();
/*     */     
/* 467 */     String charsetValue = m.getCharset();
/*     */     
/* 469 */     if (charsetValue != null) {
/* 470 */       charsetValue = charsetValue.trim();
/* 471 */       if (charsetValue.length() > 0) {
/*     */         try {
/* 473 */           this.charset = B2CConverter.getCharset(charsetValue);
/*     */         } catch (UnsupportedEncodingException e) {
/* 475 */           log.warn(sm.getString("response.encoding.invalid", new Object[] { charsetValue }), e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setContentTypeNoCharset(String type) {
/* 482 */     this.contentType = type;
/*     */   }
/*     */   
/*     */   public String getContentType()
/*     */   {
/* 487 */     String ret = this.contentType;
/*     */     
/* 489 */     if ((ret != null) && (this.charset != null))
/*     */     {
/* 491 */       ret = ret + ";charset=" + this.characterEncoding;
/*     */     }
/*     */     
/* 494 */     return ret;
/*     */   }
/*     */   
/*     */   public void setContentLength(long contentLength) {
/* 498 */     this.contentLength = contentLength;
/*     */   }
/*     */   
/*     */   public int getContentLength() {
/* 502 */     long length = getContentLengthLong();
/*     */     
/* 504 */     if (length < 2147483647L) {
/* 505 */       return (int)length;
/*     */     }
/* 507 */     return -1;
/*     */   }
/*     */   
/*     */   public long getContentLengthLong() {
/* 511 */     return this.contentLength;
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
/*     */   @Deprecated
/*     */   public void doWrite(ByteChunk chunk)
/*     */     throws IOException
/*     */   {
/* 527 */     this.outputBuffer.doWrite(chunk);
/* 528 */     this.contentWritten += chunk.getLength();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doWrite(ByteBuffer chunk)
/*     */     throws IOException
/*     */   {
/* 540 */     int len = chunk.remaining();
/* 541 */     this.outputBuffer.doWrite(chunk);
/* 542 */     this.contentWritten += len - chunk.remaining();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 549 */     this.contentType = null;
/* 550 */     this.contentLanguage = null;
/* 551 */     this.locale = DEFAULT_LOCALE;
/* 552 */     this.charset = null;
/* 553 */     this.characterEncoding = null;
/* 554 */     this.contentLength = -1L;
/* 555 */     this.status = 200;
/* 556 */     this.message = null;
/* 557 */     this.commited = false;
/* 558 */     this.commitTime = -1L;
/* 559 */     this.errorException = null;
/* 560 */     this.headers.clear();
/*     */     
/* 562 */     this.listener = null;
/* 563 */     this.fireListener = false;
/* 564 */     this.registeredForWrite = false;
/*     */     
/*     */ 
/* 567 */     this.contentWritten = 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getContentWritten()
/*     */   {
/* 578 */     return this.contentWritten;
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
/*     */   public long getBytesWritten(boolean flush)
/*     */   {
/* 591 */     if (flush) {
/* 592 */       action(ActionCode.CLIENT_FLUSH, this);
/*     */     }
/* 594 */     return this.outputBuffer.getBytesWritten();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 603 */   private boolean fireListener = false;
/* 604 */   private boolean registeredForWrite = false;
/* 605 */   private final Object nonBlockingStateLock = new Object();
/*     */   
/*     */   public WriteListener getWriteListener() {
/* 608 */     return this.listener;
/*     */   }
/*     */   
/*     */   public void setWriteListener(WriteListener listener) {
/* 612 */     if (listener == null)
/*     */     {
/* 614 */       throw new NullPointerException(sm.getString("response.nullWriteListener"));
/*     */     }
/* 616 */     if (getWriteListener() != null)
/*     */     {
/* 618 */       throw new IllegalStateException(sm.getString("response.writeListenerSet"));
/*     */     }
/*     */     
/*     */ 
/* 622 */     AtomicBoolean result = new AtomicBoolean(false);
/* 623 */     action(ActionCode.ASYNC_IS_ASYNC, result);
/* 624 */     if (!result.get())
/*     */     {
/* 626 */       throw new IllegalStateException(sm.getString("response.notAsync"));
/*     */     }
/*     */     
/* 629 */     this.listener = listener;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 637 */     if (isReady()) {
/* 638 */       synchronized (this.nonBlockingStateLock)
/*     */       {
/*     */ 
/*     */ 
/* 642 */         this.registeredForWrite = true;
/*     */         
/*     */ 
/*     */ 
/* 646 */         this.fireListener = true;
/*     */       }
/* 648 */       action(ActionCode.DISPATCH_WRITE, null);
/* 649 */       if (!ContainerThreadMarker.isContainerThread())
/*     */       {
/* 651 */         action(ActionCode.DISPATCH_EXECUTE, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isReady() {
/* 657 */     if (this.listener == null) {
/* 658 */       if (log.isDebugEnabled()) {
/* 659 */         log.debug(sm.getString("response.notNonBlocking"));
/*     */       }
/* 661 */       return false;
/*     */     }
/*     */     
/* 664 */     boolean ready = false;
/* 665 */     synchronized (this.nonBlockingStateLock) {
/* 666 */       if (this.registeredForWrite) {
/* 667 */         this.fireListener = true;
/* 668 */         return false;
/*     */       }
/* 670 */       ready = checkRegisterForWrite();
/* 671 */       this.fireListener = (!ready);
/*     */     }
/* 673 */     return ready;
/*     */   }
/*     */   
/*     */   public boolean checkRegisterForWrite() {
/* 677 */     AtomicBoolean ready = new AtomicBoolean(false);
/* 678 */     synchronized (this.nonBlockingStateLock) {
/* 679 */       if (!this.registeredForWrite) {
/* 680 */         action(ActionCode.NB_WRITE_INTEREST, ready);
/* 681 */         this.registeredForWrite = (!ready.get());
/*     */       }
/*     */     }
/* 684 */     return ready.get();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onWritePossible()
/*     */     throws IOException
/*     */   {
/* 691 */     boolean fire = false;
/* 692 */     synchronized (this.nonBlockingStateLock) {
/* 693 */       this.registeredForWrite = false;
/* 694 */       if (this.fireListener) {
/* 695 */         this.fireListener = false;
/* 696 */         fire = true;
/*     */       }
/*     */     }
/* 699 */     if (fire) {
/* 700 */       this.listener.onWritePossible();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\Response.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */