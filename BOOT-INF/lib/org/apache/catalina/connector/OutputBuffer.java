/*     */ package org.apache.catalina.connector;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.WriteListener;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.coyote.ActionCode;
/*     */ import org.apache.coyote.Constants;
/*     */ import org.apache.coyote.Response;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
/*     */ import org.apache.tomcat.util.buf.C2BConverter;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
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
/*     */ public class OutputBuffer
/*     */   extends Writer
/*     */ {
/*  51 */   private static final StringManager sm = StringManager.getManager(OutputBuffer.class);
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   
/*     */ 
/*  58 */   private final Map<Charset, C2BConverter> encoders = new HashMap();
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
/*     */ 
/*     */   private final CharBuffer cb;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   private boolean initial = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private long bytesWritten = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */   private long charsWritten = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   private volatile boolean closed = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private boolean doFlush = false;
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
/*     */   protected C2BConverter conv;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Response coyoteResponse;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */   private volatile boolean suspended = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputBuffer()
/*     */   {
/* 137 */     this(8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputBuffer(int size)
/*     */   {
/* 149 */     this.bb = ByteBuffer.allocate(size);
/* 150 */     clear(this.bb);
/* 151 */     this.cb = CharBuffer.allocate(size);
/* 152 */     clear(this.cb);
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
/*     */   public void setResponse(Response coyoteResponse)
/*     */   {
/* 166 */     this.coyoteResponse = coyoteResponse;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSuspended()
/*     */   {
/* 176 */     return this.suspended;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSuspended(boolean suspended)
/*     */   {
/* 186 */     this.suspended = suspended;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 196 */     return this.closed;
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
/* 207 */     this.initial = true;
/* 208 */     this.bytesWritten = 0L;
/* 209 */     this.charsWritten = 0L;
/*     */     
/* 211 */     clear(this.bb);
/* 212 */     clear(this.cb);
/* 213 */     this.closed = false;
/* 214 */     this.suspended = false;
/* 215 */     this.doFlush = false;
/*     */     
/* 217 */     if (this.conv != null) {
/* 218 */       this.conv.recycle();
/* 219 */       this.conv = null;
/*     */     }
/*     */     
/* 222 */     this.enc = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 235 */     if (this.closed) {
/* 236 */       return;
/*     */     }
/* 238 */     if (this.suspended) {
/* 239 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 244 */     if (this.cb.remaining() > 0) {
/* 245 */       flushCharBuffer();
/*     */     }
/*     */     
/* 248 */     if ((!this.coyoteResponse.isCommitted()) && (this.coyoteResponse.getContentLengthLong() == -1L) && 
/* 249 */       (!this.coyoteResponse.getRequest().method().equals("HEAD")))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 255 */       if (!this.coyoteResponse.isCommitted()) {
/* 256 */         this.coyoteResponse.setContentLength(this.bb.remaining());
/*     */       }
/*     */     }
/*     */     
/* 260 */     if (this.coyoteResponse.getStatus() == 101) {
/* 261 */       doFlush(true);
/*     */     } else {
/* 263 */       doFlush(false);
/*     */     }
/* 265 */     this.closed = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 270 */     Request req = (Request)this.coyoteResponse.getRequest().getNote(1);
/* 271 */     req.inputBuffer.close();
/*     */     
/* 273 */     this.coyoteResponse.action(ActionCode.CLOSE, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 284 */     doFlush(true);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void doFlush(boolean realFlush)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 12	org/apache/catalina/connector/OutputBuffer:suspended	Z
/*     */     //   4: ifeq +4 -> 8
/*     */     //   7: return
/*     */     //   8: aload_0
/*     */     //   9: iconst_1
/*     */     //   10: putfield 11	org/apache/catalina/connector/OutputBuffer:doFlush	Z
/*     */     //   13: aload_0
/*     */     //   14: getfield 7	org/apache/catalina/connector/OutputBuffer:initial	Z
/*     */     //   17: ifeq +15 -> 32
/*     */     //   20: aload_0
/*     */     //   21: getfield 18	org/apache/catalina/connector/OutputBuffer:coyoteResponse	Lorg/apache/coyote/Response;
/*     */     //   24: invokevirtual 44	org/apache/coyote/Response:sendHeaders	()V
/*     */     //   27: aload_0
/*     */     //   28: iconst_0
/*     */     //   29: putfield 7	org/apache/catalina/connector/OutputBuffer:initial	Z
/*     */     //   32: aload_0
/*     */     //   33: getfield 17	org/apache/catalina/connector/OutputBuffer:cb	Ljava/nio/CharBuffer;
/*     */     //   36: invokevirtual 22	java/nio/CharBuffer:remaining	()I
/*     */     //   39: ifle +7 -> 46
/*     */     //   42: aload_0
/*     */     //   43: invokespecial 23	org/apache/catalina/connector/OutputBuffer:flushCharBuffer	()V
/*     */     //   46: aload_0
/*     */     //   47: getfield 14	org/apache/catalina/connector/OutputBuffer:bb	Ljava/nio/ByteBuffer;
/*     */     //   50: invokevirtual 32	java/nio/ByteBuffer:remaining	()I
/*     */     //   53: ifle +7 -> 60
/*     */     //   56: aload_0
/*     */     //   57: invokespecial 45	org/apache/catalina/connector/OutputBuffer:flushByteBuffer	()V
/*     */     //   60: aload_0
/*     */     //   61: iconst_0
/*     */     //   62: putfield 11	org/apache/catalina/connector/OutputBuffer:doFlush	Z
/*     */     //   65: goto +11 -> 76
/*     */     //   68: astore_2
/*     */     //   69: aload_0
/*     */     //   70: iconst_0
/*     */     //   71: putfield 11	org/apache/catalina/connector/OutputBuffer:doFlush	Z
/*     */     //   74: aload_2
/*     */     //   75: athrow
/*     */     //   76: iload_1
/*     */     //   77: ifeq +39 -> 116
/*     */     //   80: aload_0
/*     */     //   81: getfield 18	org/apache/catalina/connector/OutputBuffer:coyoteResponse	Lorg/apache/coyote/Response;
/*     */     //   84: getstatic 46	org/apache/coyote/ActionCode:CLIENT_FLUSH	Lorg/apache/coyote/ActionCode;
/*     */     //   87: aconst_null
/*     */     //   88: invokevirtual 43	org/apache/coyote/Response:action	(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
/*     */     //   91: aload_0
/*     */     //   92: getfield 18	org/apache/catalina/connector/OutputBuffer:coyoteResponse	Lorg/apache/coyote/Response;
/*     */     //   95: invokevirtual 47	org/apache/coyote/Response:isExceptionPresent	()Z
/*     */     //   98: ifeq +18 -> 116
/*     */     //   101: new 48	org/apache/catalina/connector/ClientAbortException
/*     */     //   104: dup
/*     */     //   105: aload_0
/*     */     //   106: getfield 18	org/apache/catalina/connector/OutputBuffer:coyoteResponse	Lorg/apache/coyote/Response;
/*     */     //   109: invokevirtual 49	org/apache/coyote/Response:getErrorException	()Ljava/lang/Exception;
/*     */     //   112: invokespecial 50	org/apache/catalina/connector/ClientAbortException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   115: athrow
/*     */     //   116: return
/*     */     // Line number table:
/*     */     //   Java source line #296	-> byte code offset #0
/*     */     //   Java source line #297	-> byte code offset #7
/*     */     //   Java source line #301	-> byte code offset #8
/*     */     //   Java source line #302	-> byte code offset #13
/*     */     //   Java source line #303	-> byte code offset #20
/*     */     //   Java source line #304	-> byte code offset #27
/*     */     //   Java source line #306	-> byte code offset #32
/*     */     //   Java source line #307	-> byte code offset #42
/*     */     //   Java source line #309	-> byte code offset #46
/*     */     //   Java source line #310	-> byte code offset #56
/*     */     //   Java source line #313	-> byte code offset #60
/*     */     //   Java source line #314	-> byte code offset #65
/*     */     //   Java source line #313	-> byte code offset #68
/*     */     //   Java source line #316	-> byte code offset #76
/*     */     //   Java source line #317	-> byte code offset #80
/*     */     //   Java source line #320	-> byte code offset #91
/*     */     //   Java source line #321	-> byte code offset #101
/*     */     //   Java source line #325	-> byte code offset #116
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	117	0	this	OutputBuffer
/*     */     //   0	117	1	realFlush	boolean
/*     */     //   68	7	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   8	60	68	finally
/*     */   }
/*     */   
/*     */   public void realWriteBytes(ByteBuffer buf)
/*     */     throws IOException
/*     */   {
/* 340 */     if (this.closed) {
/* 341 */       return;
/*     */     }
/* 343 */     if (this.coyoteResponse == null) {
/* 344 */       return;
/*     */     }
/*     */     
/*     */ 
/* 348 */     if (buf.remaining() > 0) {
/*     */       try
/*     */       {
/* 351 */         this.coyoteResponse.doWrite(buf);
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 356 */         throw new ClientAbortException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 365 */     if (this.suspended) {
/* 366 */       return;
/*     */     }
/*     */     
/* 369 */     writeBytes(b, off, len);
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(ByteBuffer from)
/*     */     throws IOException
/*     */   {
/* 376 */     if (this.suspended) {
/* 377 */       return;
/*     */     }
/*     */     
/* 380 */     writeBytes(from);
/*     */   }
/*     */   
/*     */ 
/*     */   private void writeBytes(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 387 */     if (this.closed) {
/* 388 */       return;
/*     */     }
/*     */     
/* 391 */     append(b, off, len);
/* 392 */     this.bytesWritten += len;
/*     */     
/*     */ 
/*     */ 
/* 396 */     if (this.doFlush) {
/* 397 */       flushByteBuffer();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void writeBytes(ByteBuffer from)
/*     */     throws IOException
/*     */   {
/* 405 */     if (this.closed) {
/* 406 */       return;
/*     */     }
/*     */     
/* 409 */     append(from);
/* 410 */     this.bytesWritten += from.remaining();
/*     */     
/*     */ 
/*     */ 
/* 414 */     if (this.doFlush) {
/* 415 */       flushByteBuffer();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeByte(int b)
/*     */     throws IOException
/*     */   {
/* 423 */     if (this.suspended) {
/* 424 */       return;
/*     */     }
/*     */     
/* 427 */     if (isFull(this.bb)) {
/* 428 */       flushByteBuffer();
/*     */     }
/*     */     
/* 431 */     transfer((byte)b, this.bb);
/* 432 */     this.bytesWritten += 1L;
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
/*     */   public void realWriteChars(CharBuffer from)
/*     */     throws IOException
/*     */   {
/* 449 */     while (from.remaining() > 0) {
/* 450 */       this.conv.convert(from, this.bb);
/* 451 */       if (this.bb.remaining() == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 455 */       if (from.remaining() > 0) {
/* 456 */         flushByteBuffer();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/* 465 */     if (this.suspended) {
/* 466 */       return;
/*     */     }
/*     */     
/* 469 */     if (isFull(this.cb)) {
/* 470 */       flushCharBuffer();
/*     */     }
/*     */     
/* 473 */     transfer((char)c, this.cb);
/* 474 */     this.charsWritten += 1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(char[] c)
/*     */     throws IOException
/*     */   {
/* 482 */     if (this.suspended) {
/* 483 */       return;
/*     */     }
/*     */     
/* 486 */     write(c, 0, c.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(char[] c, int off, int len)
/*     */     throws IOException
/*     */   {
/* 494 */     if (this.suspended) {
/* 495 */       return;
/*     */     }
/*     */     
/* 498 */     append(c, off, len);
/* 499 */     this.charsWritten += len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(String s, int off, int len)
/*     */     throws IOException
/*     */   {
/* 510 */     if (this.suspended) {
/* 511 */       return;
/*     */     }
/*     */     
/* 514 */     if (s == null) {
/* 515 */       throw new NullPointerException(sm.getString("outputBuffer.writeNull"));
/*     */     }
/*     */     
/* 518 */     int sOff = off;
/* 519 */     int sEnd = off + len;
/* 520 */     while (sOff < sEnd) {
/* 521 */       int n = transfer(s, sOff, sEnd - sOff, this.cb);
/* 522 */       sOff += n;
/* 523 */       if (isFull(this.cb)) {
/* 524 */         flushCharBuffer();
/*     */       }
/*     */     }
/*     */     
/* 528 */     this.charsWritten += len;
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(String s)
/*     */     throws IOException
/*     */   {
/* 535 */     if (this.suspended) {
/* 536 */       return;
/*     */     }
/*     */     
/* 539 */     if (s == null) {
/* 540 */       s = "null";
/*     */     }
/* 542 */     write(s, 0, s.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setEncoding(String s)
/*     */   {
/* 553 */     this.enc = s;
/*     */   }
/*     */   
/*     */   public void checkConverter() throws IOException
/*     */   {
/* 558 */     if (this.conv != null) {
/* 559 */       return;
/*     */     }
/*     */     
/* 562 */     Charset charset = null;
/*     */     
/* 564 */     if (this.coyoteResponse != null) {
/* 565 */       charset = this.coyoteResponse.getCharset();
/*     */     }
/*     */     
/* 568 */     if (charset == null) {
/* 569 */       if (this.enc == null) {
/* 570 */         charset = Constants.DEFAULT_BODY_CHARSET;
/*     */       } else {
/* 572 */         charset = getCharset(this.enc);
/*     */       }
/*     */     }
/*     */     
/* 576 */     this.conv = ((C2BConverter)this.encoders.get(charset));
/*     */     
/* 578 */     if (this.conv == null) {
/* 579 */       this.conv = createConverter(charset);
/* 580 */       this.encoders.put(charset, this.conv);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Charset getCharset(String encoding) throws IOException
/*     */   {
/* 586 */     if (Globals.IS_SECURITY_ENABLED) {
/*     */       try {
/* 588 */         (Charset)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Charset run() throws IOException {
/* 591 */             return B2CConverter.getCharset(this.val$encoding);
/*     */           }
/*     */         });
/*     */       } catch (PrivilegedActionException ex) {
/* 595 */         Exception e = ex.getException();
/* 596 */         if ((e instanceof IOException)) {
/* 597 */           throw ((IOException)e);
/*     */         }
/* 599 */         throw new IOException(ex);
/*     */       }
/*     */     }
/*     */     
/* 603 */     return B2CConverter.getCharset(encoding);
/*     */   }
/*     */   
/*     */   private static C2BConverter createConverter(Charset charset)
/*     */     throws IOException
/*     */   {
/* 609 */     if (Globals.IS_SECURITY_ENABLED) {
/*     */       try {
/* 611 */         (C2BConverter)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public C2BConverter run() throws IOException {
/* 614 */             return new C2BConverter(this.val$charset);
/*     */           }
/*     */         });
/*     */       } catch (PrivilegedActionException ex) {
/* 618 */         Exception e = ex.getException();
/* 619 */         if ((e instanceof IOException)) {
/* 620 */           throw ((IOException)e);
/*     */         }
/* 622 */         throw new IOException(ex);
/*     */       }
/*     */     }
/*     */     
/* 626 */     return new C2BConverter(charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getContentWritten()
/*     */   {
/* 634 */     return this.bytesWritten + this.charsWritten;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNew()
/*     */   {
/* 644 */     return (this.bytesWritten == 0L) && (this.charsWritten == 0L);
/*     */   }
/*     */   
/*     */   public void setBufferSize(int size)
/*     */   {
/* 649 */     if (size > this.bb.capacity()) {
/* 650 */       this.bb = ByteBuffer.allocate(size);
/* 651 */       clear(this.bb);
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset()
/*     */   {
/* 657 */     reset(false);
/*     */   }
/*     */   
/*     */   public void reset(boolean resetWriterStreamFlags) {
/* 661 */     clear(this.bb);
/* 662 */     clear(this.cb);
/* 663 */     this.bytesWritten = 0L;
/* 664 */     this.charsWritten = 0L;
/* 665 */     if (resetWriterStreamFlags) {
/* 666 */       if (this.conv != null) {
/* 667 */         this.conv.recycle();
/*     */       }
/* 669 */       this.conv = null;
/* 670 */       this.enc = null;
/*     */     }
/* 672 */     this.initial = true;
/*     */   }
/*     */   
/*     */   public int getBufferSize()
/*     */   {
/* 677 */     return this.bb.capacity();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReady()
/*     */   {
/* 687 */     return this.coyoteResponse.isReady();
/*     */   }
/*     */   
/*     */   public void setWriteListener(WriteListener listener)
/*     */   {
/* 692 */     this.coyoteResponse.setWriteListener(listener);
/*     */   }
/*     */   
/*     */   public boolean isBlocking()
/*     */   {
/* 697 */     return this.coyoteResponse.getWriteListener() == null;
/*     */   }
/*     */   
/*     */   public void checkRegisterForWrite() {
/* 701 */     this.coyoteResponse.checkRegisterForWrite();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void append(byte[] src, int off, int len)
/*     */     throws IOException
/*     */   {
/* 713 */     if (this.bb.remaining() == 0) {
/* 714 */       appendByteArray(src, off, len);
/*     */     } else {
/* 716 */       int n = transfer(src, off, len, this.bb);
/* 717 */       len -= n;
/* 718 */       off += n;
/* 719 */       if (isFull(this.bb)) {
/* 720 */         flushByteBuffer();
/* 721 */         appendByteArray(src, off, len);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void append(char[] src, int off, int len)
/*     */     throws IOException
/*     */   {
/* 735 */     if (len <= this.cb.capacity() - this.cb.limit()) {
/* 736 */       transfer(src, off, len, this.cb);
/* 737 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 746 */     if (len + this.cb.limit() < 2 * this.cb.capacity())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 751 */       int n = transfer(src, off, len, this.cb);
/*     */       
/* 753 */       flushCharBuffer();
/*     */       
/* 755 */       transfer(src, off + n, len - n, this.cb);
/*     */     }
/*     */     else
/*     */     {
/* 759 */       flushCharBuffer();
/*     */       
/* 761 */       realWriteChars(CharBuffer.wrap(src, off, len));
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(ByteBuffer from) throws IOException
/*     */   {
/* 767 */     if (this.bb.remaining() == 0) {
/* 768 */       appendByteBuffer(from);
/*     */     } else {
/* 770 */       transfer(from, this.bb);
/* 771 */       if (isFull(this.bb)) {
/* 772 */         flushByteBuffer();
/* 773 */         appendByteBuffer(from);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void appendByteArray(byte[] src, int off, int len) throws IOException {
/* 779 */     if (len == 0) {
/* 780 */       return;
/*     */     }
/*     */     
/* 783 */     int limit = this.bb.capacity();
/* 784 */     while (len >= limit) {
/* 785 */       realWriteBytes(ByteBuffer.wrap(src, off, limit));
/* 786 */       len -= limit;
/* 787 */       off += limit;
/*     */     }
/*     */     
/* 790 */     if (len > 0) {
/* 791 */       transfer(src, off, len, this.bb);
/*     */     }
/*     */   }
/*     */   
/*     */   private void appendByteBuffer(ByteBuffer from) throws IOException {
/* 796 */     if (from.remaining() == 0) {
/* 797 */       return;
/*     */     }
/*     */     
/* 800 */     int limit = this.bb.capacity();
/* 801 */     int fromLimit = from.limit();
/* 802 */     while (from.remaining() >= limit) {
/* 803 */       from.limit(from.position() + limit);
/* 804 */       realWriteBytes(from.slice());
/* 805 */       from.position(from.limit());
/* 806 */       from.limit(fromLimit);
/*     */     }
/*     */     
/* 809 */     if (from.remaining() > 0) {
/* 810 */       transfer(from, this.bb);
/*     */     }
/*     */   }
/*     */   
/*     */   private void flushByteBuffer() throws IOException {
/* 815 */     realWriteBytes(this.bb.slice());
/* 816 */     clear(this.bb);
/*     */   }
/*     */   
/*     */   private void flushCharBuffer() throws IOException {
/* 820 */     realWriteChars(this.cb.slice());
/* 821 */     clear(this.cb);
/*     */   }
/*     */   
/*     */   private void transfer(byte b, ByteBuffer to) {
/* 825 */     toWriteMode(to);
/* 826 */     to.put(b);
/* 827 */     toReadMode(to);
/*     */   }
/*     */   
/*     */   private void transfer(char b, CharBuffer to) {
/* 831 */     toWriteMode(to);
/* 832 */     to.put(b);
/* 833 */     toReadMode(to);
/*     */   }
/*     */   
/*     */   private int transfer(byte[] buf, int off, int len, ByteBuffer to) {
/* 837 */     toWriteMode(to);
/* 838 */     int max = Math.min(len, to.remaining());
/* 839 */     if (max > 0) {
/* 840 */       to.put(buf, off, max);
/*     */     }
/* 842 */     toReadMode(to);
/* 843 */     return max;
/*     */   }
/*     */   
/*     */   private int transfer(char[] buf, int off, int len, CharBuffer to) {
/* 847 */     toWriteMode(to);
/* 848 */     int max = Math.min(len, to.remaining());
/* 849 */     if (max > 0) {
/* 850 */       to.put(buf, off, max);
/*     */     }
/* 852 */     toReadMode(to);
/* 853 */     return max;
/*     */   }
/*     */   
/*     */   private int transfer(String s, int off, int len, CharBuffer to) {
/* 857 */     toWriteMode(to);
/* 858 */     int max = Math.min(len, to.remaining());
/* 859 */     if (max > 0) {
/* 860 */       to.put(s, off, off + max);
/*     */     }
/* 862 */     toReadMode(to);
/* 863 */     return max;
/*     */   }
/*     */   
/*     */   private void transfer(ByteBuffer from, ByteBuffer to) {
/* 867 */     toWriteMode(to);
/* 868 */     int max = Math.min(from.remaining(), to.remaining());
/* 869 */     if (max > 0) {
/* 870 */       int fromLimit = from.limit();
/* 871 */       from.limit(from.position() + max);
/* 872 */       to.put(from);
/* 873 */       from.limit(fromLimit);
/*     */     }
/* 875 */     toReadMode(to);
/*     */   }
/*     */   
/*     */   private void clear(Buffer buffer) {
/* 879 */     buffer.rewind().limit(0);
/*     */   }
/*     */   
/*     */   private boolean isFull(Buffer buffer) {
/* 883 */     return buffer.limit() == buffer.capacity();
/*     */   }
/*     */   
/*     */   private void toReadMode(Buffer buffer)
/*     */   {
/* 888 */     buffer.limit(buffer.position()).reset();
/*     */   }
/*     */   
/*     */ 
/*     */   private void toWriteMode(Buffer buffer)
/*     */   {
/* 894 */     buffer.mark().position(buffer.limit()).limit(buffer.capacity());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\OutputBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */