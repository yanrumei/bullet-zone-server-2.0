/*     */ package org.apache.catalina.connector;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.servlet.ReadListener;
/*     */ import org.apache.catalina.security.SecurityUtil;
/*     */ import org.apache.coyote.ActionCode;
/*     */ import org.apache.coyote.Constants;
/*     */ import org.apache.coyote.ContainerThreadMarker;
/*     */ import org.apache.coyote.Request;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk.ByteInputChannel;
/*     */ import org.apache.tomcat.util.collections.SynchronizedStack;
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
/*     */ public class InputBuffer
/*     */   extends Reader
/*     */   implements ByteChunk.ByteInputChannel, ApplicationBufferHandler
/*     */ {
/*  59 */   protected static final StringManager sm = StringManager.getManager(InputBuffer.class);
/*     */   
/*  61 */   private static final Log log = LogFactory.getLog(InputBuffer.class);
/*     */   
/*     */ 
/*     */   public static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   
/*     */ 
/*  67 */   public final int INITIAL_STATE = 0;
/*  68 */   public final int CHAR_STATE = 1;
/*  69 */   public final int BYTE_STATE = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   private static final ConcurrentMap<Charset, SynchronizedStack<B2CConverter>> encoders = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ByteBuffer bb;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CharBuffer cb;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private int state = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */   private boolean closed = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String enc;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected B2CConverter conv;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Request coyoteRequest;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */   private int markPos = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int readLimit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int size;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputBuffer()
/*     */   {
/* 147 */     this(8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputBuffer(int size)
/*     */   {
/* 159 */     this.size = size;
/* 160 */     this.bb = ByteBuffer.allocate(size);
/* 161 */     clear(this.bb);
/* 162 */     this.cb = CharBuffer.allocate(size);
/* 163 */     clear(this.cb);
/* 164 */     this.readLimit = size;
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
/*     */   public void setRequest(Request coyoteRequest)
/*     */   {
/* 178 */     this.coyoteRequest = coyoteRequest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 189 */     this.state = 0;
/*     */     
/*     */ 
/* 192 */     if (this.cb.capacity() > this.size) {
/* 193 */       this.cb = CharBuffer.allocate(this.size);
/* 194 */       clear(this.cb);
/*     */     } else {
/* 196 */       clear(this.cb);
/*     */     }
/* 198 */     this.readLimit = this.size;
/* 199 */     this.markPos = -1;
/* 200 */     clear(this.bb);
/* 201 */     this.closed = false;
/*     */     
/* 203 */     if (this.conv != null) {
/* 204 */       this.conv.recycle();
/* 205 */       ((SynchronizedStack)encoders.get(this.conv.getCharset())).push(this.conv);
/* 206 */       this.conv = null;
/*     */     }
/*     */     
/* 209 */     this.enc = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 220 */     this.closed = true;
/*     */   }
/*     */   
/*     */   public int available()
/*     */   {
/* 225 */     int available = 0;
/* 226 */     if (this.state == 2) {
/* 227 */       available = this.bb.remaining();
/* 228 */     } else if (this.state == 1) {
/* 229 */       available = this.cb.remaining();
/*     */     }
/* 231 */     if (available == 0) {
/* 232 */       this.coyoteRequest.action(ActionCode.AVAILABLE, 
/* 233 */         Boolean.valueOf(this.coyoteRequest.getReadListener() != null));
/* 234 */       available = this.coyoteRequest.getAvailable() > 0 ? 1 : 0;
/*     */     }
/* 236 */     return available;
/*     */   }
/*     */   
/*     */   public void setReadListener(ReadListener listener)
/*     */   {
/* 241 */     this.coyoteRequest.setReadListener(listener);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 251 */     if ((!this.coyoteRequest.isFinished()) && (isReady())) {
/* 252 */       this.coyoteRequest.action(ActionCode.DISPATCH_READ, null);
/* 253 */       if (!ContainerThreadMarker.isContainerThread())
/*     */       {
/* 255 */         this.coyoteRequest.action(ActionCode.DISPATCH_EXECUTE, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFinished()
/*     */   {
/* 262 */     int available = 0;
/* 263 */     if (this.state == 2) {
/* 264 */       available = this.bb.remaining();
/* 265 */     } else if (this.state == 1) {
/* 266 */       available = this.cb.remaining();
/*     */     }
/* 268 */     if (available > 0) {
/* 269 */       return false;
/*     */     }
/* 271 */     return this.coyoteRequest.isFinished();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isReady()
/*     */   {
/* 277 */     if (this.coyoteRequest.getReadListener() == null) {
/* 278 */       if (log.isDebugEnabled()) {
/* 279 */         log.debug(sm.getString("inputBuffer.requiresNonBlocking"));
/*     */       }
/* 281 */       return false;
/*     */     }
/* 283 */     if (isFinished())
/*     */     {
/*     */ 
/*     */ 
/* 287 */       if (!ContainerThreadMarker.isContainerThread()) {
/* 288 */         this.coyoteRequest.action(ActionCode.DISPATCH_READ, null);
/* 289 */         this.coyoteRequest.action(ActionCode.DISPATCH_EXECUTE, null);
/*     */       }
/* 291 */       return false;
/*     */     }
/* 293 */     boolean result = available() > 0;
/* 294 */     if (!result) {
/* 295 */       this.coyoteRequest.action(ActionCode.NB_READ_INTEREST, null);
/*     */     }
/* 297 */     return result;
/*     */   }
/*     */   
/*     */   boolean isBlocking()
/*     */   {
/* 302 */     return this.coyoteRequest.getReadListener() == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int realReadBytes()
/*     */     throws IOException
/*     */   {
/* 315 */     if (this.closed) {
/* 316 */       return -1;
/*     */     }
/* 318 */     if (this.coyoteRequest == null) {
/* 319 */       return -1;
/*     */     }
/*     */     
/* 322 */     if (this.state == 0) {
/* 323 */       this.state = 2;
/*     */     }
/*     */     
/* 326 */     int result = this.coyoteRequest.doRead(this);
/*     */     
/* 328 */     return result;
/*     */   }
/*     */   
/*     */   public int readByte() throws IOException
/*     */   {
/* 333 */     if (this.closed) {
/* 334 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 337 */     if (checkByteBufferEof()) {
/* 338 */       return -1;
/*     */     }
/* 340 */     return this.bb.get() & 0xFF;
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException
/*     */   {
/* 345 */     if (this.closed) {
/* 346 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 349 */     if (checkByteBufferEof()) {
/* 350 */       return -1;
/*     */     }
/* 352 */     int n = Math.min(len, this.bb.remaining());
/* 353 */     this.bb.get(b, off, n);
/* 354 */     return n;
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
/*     */   public int read(ByteBuffer to)
/*     */     throws IOException
/*     */   {
/* 370 */     if (this.closed) {
/* 371 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 374 */     if (checkByteBufferEof()) {
/* 375 */       return -1;
/*     */     }
/* 377 */     int n = Math.min(to.remaining(), this.bb.remaining());
/* 378 */     int orgLimit = this.bb.limit();
/* 379 */     this.bb.limit(this.bb.position() + n);
/* 380 */     to.put(this.bb);
/* 381 */     this.bb.limit(orgLimit);
/* 382 */     to.limit(to.position()).position(to.position() - n);
/* 383 */     return n;
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
/*     */   @Deprecated
/*     */   public void setEncoding(String s)
/*     */   {
/* 397 */     this.enc = s;
/*     */   }
/*     */   
/*     */   public int realReadChars() throws IOException
/*     */   {
/* 402 */     checkConverter();
/*     */     
/* 404 */     boolean eof = false;
/*     */     
/* 406 */     if (this.bb.remaining() <= 0) {
/* 407 */       int nRead = realReadBytes();
/* 408 */       if (nRead < 0) {
/* 409 */         eof = true;
/*     */       }
/*     */     }
/*     */     
/* 413 */     if (this.markPos == -1) {
/* 414 */       clear(this.cb);
/*     */     }
/*     */     else {
/* 417 */       makeSpace(this.bb.remaining());
/* 418 */       if ((this.cb.capacity() - this.cb.limit() == 0) && (this.bb.remaining() != 0))
/*     */       {
/* 420 */         clear(this.cb);
/* 421 */         this.markPos = -1;
/*     */       }
/*     */     }
/*     */     
/* 425 */     this.state = 1;
/* 426 */     this.conv.convert(this.bb, this.cb, this, eof);
/*     */     
/* 428 */     if ((this.cb.remaining() == 0) && (eof)) {
/* 429 */       return -1;
/*     */     }
/* 431 */     return this.cb.remaining();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 439 */     if (this.closed) {
/* 440 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 443 */     if (checkCharBufferEof()) {
/* 444 */       return -1;
/*     */     }
/* 446 */     return this.cb.get();
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(char[] cbuf)
/*     */     throws IOException
/*     */   {
/* 453 */     if (this.closed) {
/* 454 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 457 */     return read(cbuf, 0, cbuf.length);
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(char[] cbuf, int off, int len)
/*     */     throws IOException
/*     */   {
/* 464 */     if (this.closed) {
/* 465 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 468 */     if (checkCharBufferEof()) {
/* 469 */       return -1;
/*     */     }
/* 471 */     int n = Math.min(len, this.cb.remaining());
/* 472 */     this.cb.get(cbuf, off, n);
/* 473 */     return n;
/*     */   }
/*     */   
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 479 */     if (this.closed) {
/* 480 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 483 */     if (n < 0L) {
/* 484 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 487 */     long nRead = 0L;
/* 488 */     while (nRead < n) {
/* 489 */       if (this.cb.remaining() >= n) {
/* 490 */         this.cb.position(this.cb.position() + (int)n);
/* 491 */         nRead = n;
/*     */       } else {
/* 493 */         nRead += this.cb.remaining();
/* 494 */         this.cb.position(this.cb.limit());
/* 495 */         int nb = realReadChars();
/* 496 */         if (nb < 0) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 501 */     return nRead;
/*     */   }
/*     */   
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 507 */     if (this.closed) {
/* 508 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/* 510 */     if (this.state == 0) {
/* 511 */       this.state = 1;
/*     */     }
/* 513 */     return available() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 519 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void mark(int readAheadLimit)
/*     */     throws IOException
/*     */   {
/* 526 */     if (this.closed) {
/* 527 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 530 */     if (this.cb.remaining() <= 0) {
/* 531 */       clear(this.cb);
/*     */     }
/* 533 */     else if ((this.cb.capacity() > 2 * this.size) && (this.cb.remaining() < this.cb.position())) {
/* 534 */       this.cb.compact();
/* 535 */       this.cb.flip();
/*     */     }
/*     */     
/* 538 */     this.readLimit = (this.cb.position() + readAheadLimit + this.size);
/* 539 */     this.markPos = this.cb.position();
/*     */   }
/*     */   
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 546 */     if (this.closed) {
/* 547 */       throw new IOException(sm.getString("inputBuffer.streamClosed"));
/*     */     }
/*     */     
/* 550 */     if (this.state == 1) {
/* 551 */       if (this.markPos < 0) {
/* 552 */         clear(this.cb);
/* 553 */         this.markPos = -1;
/* 554 */         throw new IOException();
/*     */       }
/* 556 */       this.cb.position(this.markPos);
/*     */     }
/*     */     else {
/* 559 */       clear(this.bb);
/*     */     }
/*     */   }
/*     */   
/*     */   public void checkConverter() throws IOException
/*     */   {
/* 565 */     if (this.conv != null) {
/* 566 */       return;
/*     */     }
/*     */     
/* 569 */     Charset charset = null;
/*     */     
/* 571 */     if (this.coyoteRequest != null) {
/* 572 */       charset = this.coyoteRequest.getCharset();
/*     */     }
/*     */     
/* 575 */     if (charset == null) {
/* 576 */       if (this.enc == null) {
/* 577 */         charset = Constants.DEFAULT_BODY_CHARSET;
/*     */       } else {
/* 579 */         charset = B2CConverter.getCharset(this.enc);
/*     */       }
/*     */     }
/*     */     
/* 583 */     SynchronizedStack<B2CConverter> stack = (SynchronizedStack)encoders.get(charset);
/* 584 */     if (stack == null) {
/* 585 */       stack = new SynchronizedStack();
/* 586 */       encoders.putIfAbsent(charset, stack);
/* 587 */       stack = (SynchronizedStack)encoders.get(charset);
/*     */     }
/* 589 */     this.conv = ((B2CConverter)stack.pop());
/*     */     
/* 591 */     if (this.conv == null) {
/* 592 */       this.conv = createConverter(charset);
/*     */     }
/*     */   }
/*     */   
/*     */   private static B2CConverter createConverter(Charset charset) throws IOException
/*     */   {
/* 598 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 600 */         (B2CConverter)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public B2CConverter run() throws IOException
/*     */           {
/* 604 */             return new B2CConverter(this.val$charset);
/*     */           }
/*     */         });
/*     */       } catch (PrivilegedActionException ex) {
/* 608 */         Exception e = ex.getException();
/* 609 */         if ((e instanceof IOException)) {
/* 610 */           throw ((IOException)e);
/*     */         }
/* 612 */         throw new IOException(e);
/*     */       }
/*     */     }
/*     */     
/* 616 */     return new B2CConverter(charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setByteBuffer(ByteBuffer buffer)
/*     */   {
/* 624 */     this.bb = buffer;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/* 630 */     return this.bb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void expand(int size) {}
/*     */   
/*     */ 
/*     */   private boolean checkByteBufferEof()
/*     */     throws IOException
/*     */   {
/* 641 */     if (this.bb.remaining() == 0) {
/* 642 */       int n = realReadBytes();
/* 643 */       if (n < 0) {
/* 644 */         return true;
/*     */       }
/*     */     }
/* 647 */     return false;
/*     */   }
/*     */   
/*     */   private boolean checkCharBufferEof() throws IOException {
/* 651 */     if (this.cb.remaining() == 0) {
/* 652 */       int n = realReadChars();
/* 653 */       if (n < 0) {
/* 654 */         return true;
/*     */       }
/*     */     }
/* 657 */     return false;
/*     */   }
/*     */   
/*     */   private void clear(Buffer buffer) {
/* 661 */     buffer.rewind().limit(0);
/*     */   }
/*     */   
/*     */   private void makeSpace(int count) {
/* 665 */     int desiredSize = this.cb.limit() + count;
/* 666 */     if (desiredSize > this.readLimit) {
/* 667 */       desiredSize = this.readLimit;
/*     */     }
/*     */     
/* 670 */     if (desiredSize <= this.cb.capacity()) {
/* 671 */       return;
/*     */     }
/*     */     
/* 674 */     int newSize = 2 * this.cb.capacity();
/* 675 */     if (desiredSize >= newSize) {
/* 676 */       newSize = 2 * this.cb.capacity() + count;
/*     */     }
/*     */     
/* 679 */     if (newSize > this.readLimit) {
/* 680 */       newSize = this.readLimit;
/*     */     }
/*     */     
/* 683 */     CharBuffer tmp = CharBuffer.allocate(newSize);
/* 684 */     int oldPosition = this.cb.position();
/* 685 */     this.cb.position(0);
/* 686 */     tmp.put(this.cb);
/* 687 */     tmp.flip();
/* 688 */     tmp.position(oldPosition);
/* 689 */     this.cb = tmp;
/* 690 */     tmp = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\InputBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */