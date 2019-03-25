/*     */ package org.apache.coyote.http11;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.coyote.ActionCode;
/*     */ import org.apache.coyote.Response;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.HttpMessages;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
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
/*     */ public class Http11OutputBuffer
/*     */   implements HttpOutputBuffer
/*     */ {
/*  43 */   protected static final StringManager sm = StringManager.getManager(Http11OutputBuffer.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Response response;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean responseFinished;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ByteBuffer headerBuffer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected OutputFilter[] filterLibrary;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected OutputFilter[] activeFilters;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int lastActiveFilter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpOutputBuffer outputStreamOutputBuffer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SocketWrapperBase<?> socketWrapper;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   protected long byteCount = 0L;
/*     */   
/*     */   @Deprecated
/* 102 */   private boolean sendReasonPhrase = false;
/*     */   
/*     */ 
/*     */ 
/*     */   protected Http11OutputBuffer(Response response, int headerBufferSize, boolean sendReasonPhrase)
/*     */   {
/* 108 */     this.response = response;
/* 109 */     this.sendReasonPhrase = sendReasonPhrase;
/*     */     
/* 111 */     this.headerBuffer = ByteBuffer.allocate(headerBufferSize);
/*     */     
/* 113 */     this.filterLibrary = new OutputFilter[0];
/* 114 */     this.activeFilters = new OutputFilter[0];
/* 115 */     this.lastActiveFilter = -1;
/*     */     
/* 117 */     this.responseFinished = false;
/*     */     
/* 119 */     this.outputStreamOutputBuffer = new SocketOutputBuffer();
/*     */     
/* 121 */     if (sendReasonPhrase)
/*     */     {
/* 123 */       HttpMessages.getInstance(response.getLocale()).getMessage(200);
/*     */     }
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
/*     */   public void addFilter(OutputFilter filter)
/*     */   {
/* 138 */     OutputFilter[] newFilterLibrary = new OutputFilter[this.filterLibrary.length + 1];
/* 139 */     for (int i = 0; i < this.filterLibrary.length; i++) {
/* 140 */       newFilterLibrary[i] = this.filterLibrary[i];
/*     */     }
/* 142 */     newFilterLibrary[this.filterLibrary.length] = filter;
/* 143 */     this.filterLibrary = newFilterLibrary;
/*     */     
/* 145 */     this.activeFilters = new OutputFilter[this.filterLibrary.length];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputFilter[] getFilters()
/*     */   {
/* 155 */     return this.filterLibrary;
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
/*     */   public void addActiveFilter(OutputFilter filter)
/*     */   {
/* 171 */     if (this.lastActiveFilter == -1) {
/* 172 */       filter.setBuffer(this.outputStreamOutputBuffer);
/*     */     } else {
/* 174 */       for (int i = 0; i <= this.lastActiveFilter; i++) {
/* 175 */         if (this.activeFilters[i] == filter)
/* 176 */           return;
/*     */       }
/* 178 */       filter.setBuffer(this.activeFilters[this.lastActiveFilter]);
/*     */     }
/*     */     
/* 181 */     this.activeFilters[(++this.lastActiveFilter)] = filter;
/*     */     
/* 183 */     filter.setResponse(this.response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int doWrite(ByteChunk chunk)
/*     */     throws IOException
/*     */   {
/* 197 */     if (!this.response.isCommitted())
/*     */     {
/*     */ 
/*     */ 
/* 201 */       this.response.action(ActionCode.COMMIT, null);
/*     */     }
/*     */     
/* 204 */     if (this.lastActiveFilter == -1) {
/* 205 */       return this.outputStreamOutputBuffer.doWrite(chunk);
/*     */     }
/* 207 */     return this.activeFilters[this.lastActiveFilter].doWrite(chunk);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int doWrite(ByteBuffer chunk)
/*     */     throws IOException
/*     */   {
/* 215 */     if (!this.response.isCommitted())
/*     */     {
/*     */ 
/*     */ 
/* 219 */       this.response.action(ActionCode.COMMIT, null);
/*     */     }
/*     */     
/* 222 */     if (this.lastActiveFilter == -1) {
/* 223 */       return this.outputStreamOutputBuffer.doWrite(chunk);
/*     */     }
/* 225 */     return this.activeFilters[this.lastActiveFilter].doWrite(chunk);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getBytesWritten()
/*     */   {
/* 232 */     if (this.lastActiveFilter == -1) {
/* 233 */       return this.outputStreamOutputBuffer.getBytesWritten();
/*     */     }
/* 235 */     return this.activeFilters[this.lastActiveFilter].getBytesWritten();
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
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 249 */     if (this.lastActiveFilter == -1) {
/* 250 */       this.outputStreamOutputBuffer.flush();
/*     */     } else {
/* 252 */       this.activeFilters[this.lastActiveFilter].flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public void end()
/*     */     throws IOException
/*     */   {
/* 259 */     if (this.responseFinished) {
/* 260 */       return;
/*     */     }
/*     */     
/* 263 */     if (this.lastActiveFilter == -1) {
/* 264 */       this.outputStreamOutputBuffer.end();
/*     */     } else {
/* 266 */       this.activeFilters[this.lastActiveFilter].end();
/*     */     }
/*     */     
/* 269 */     this.responseFinished = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void resetHeaderBuffer()
/*     */   {
/* 280 */     this.headerBuffer.position(0).limit(this.headerBuffer.capacity());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 289 */     nextRequest();
/* 290 */     this.socketWrapper = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void nextRequest()
/*     */   {
/* 302 */     for (int i = 0; i <= this.lastActiveFilter; i++) {
/* 303 */       this.activeFilters[i].recycle();
/*     */     }
/*     */     
/* 306 */     this.response.recycle();
/*     */     
/* 308 */     this.headerBuffer.position(0).limit(this.headerBuffer.capacity());
/* 309 */     this.lastActiveFilter = -1;
/* 310 */     this.responseFinished = false;
/* 311 */     this.byteCount = 0L;
/*     */   }
/*     */   
/*     */   public void init(SocketWrapperBase<?> socketWrapper)
/*     */   {
/* 316 */     this.socketWrapper = socketWrapper;
/*     */   }
/*     */   
/*     */   public void sendAck()
/*     */     throws IOException
/*     */   {
/* 322 */     if (!this.response.isCommitted()) {
/* 323 */       if (this.sendReasonPhrase) {
/* 324 */         this.socketWrapper.write(isBlocking(), Constants.ACK_BYTES_REASON, 0, Constants.ACK_BYTES_REASON.length);
/*     */       } else {
/* 326 */         this.socketWrapper.write(isBlocking(), Constants.ACK_BYTES, 0, Constants.ACK_BYTES.length);
/*     */       }
/* 328 */       if (flushBuffer(true)) {
/* 329 */         throw new IOException(sm.getString("iob.failedwrite.ack"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void commit()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/coyote/http11/Http11OutputBuffer:response	Lorg/apache/coyote/Response;
/*     */     //   4: iconst_1
/*     */     //   5: invokevirtual 50	org/apache/coyote/Response:setCommitted	(Z)V
/*     */     //   8: aload_0
/*     */     //   9: getfield 6	org/apache/coyote/http11/Http11OutputBuffer:headerBuffer	Ljava/nio/ByteBuffer;
/*     */     //   12: invokevirtual 51	java/nio/ByteBuffer:position	()I
/*     */     //   15: ifle +70 -> 85
/*     */     //   18: aload_0
/*     */     //   19: getfield 6	org/apache/coyote/http11/Http11OutputBuffer:headerBuffer	Ljava/nio/ByteBuffer;
/*     */     //   22: invokevirtual 52	java/nio/ByteBuffer:flip	()Ljava/nio/Buffer;
/*     */     //   25: pop
/*     */     //   26: aload_0
/*     */     //   27: getfield 37	org/apache/coyote/http11/Http11OutputBuffer:socketWrapper	Lorg/apache/tomcat/util/net/SocketWrapperBase;
/*     */     //   30: aload_0
/*     */     //   31: invokevirtual 40	org/apache/coyote/http11/Http11OutputBuffer:isBlocking	()Z
/*     */     //   34: aload_0
/*     */     //   35: getfield 6	org/apache/coyote/http11/Http11OutputBuffer:headerBuffer	Ljava/nio/ByteBuffer;
/*     */     //   38: invokevirtual 53	org/apache/tomcat/util/net/SocketWrapperBase:write	(ZLjava/nio/ByteBuffer;)V
/*     */     //   41: aload_0
/*     */     //   42: getfield 6	org/apache/coyote/http11/Http11OutputBuffer:headerBuffer	Ljava/nio/ByteBuffer;
/*     */     //   45: iconst_0
/*     */     //   46: invokevirtual 33	java/nio/ByteBuffer:position	(I)Ljava/nio/Buffer;
/*     */     //   49: aload_0
/*     */     //   50: getfield 6	org/apache/coyote/http11/Http11OutputBuffer:headerBuffer	Ljava/nio/ByteBuffer;
/*     */     //   53: invokevirtual 34	java/nio/ByteBuffer:capacity	()I
/*     */     //   56: invokevirtual 35	java/nio/Buffer:limit	(I)Ljava/nio/Buffer;
/*     */     //   59: pop
/*     */     //   60: goto +25 -> 85
/*     */     //   63: astore_1
/*     */     //   64: aload_0
/*     */     //   65: getfield 6	org/apache/coyote/http11/Http11OutputBuffer:headerBuffer	Ljava/nio/ByteBuffer;
/*     */     //   68: iconst_0
/*     */     //   69: invokevirtual 33	java/nio/ByteBuffer:position	(I)Ljava/nio/Buffer;
/*     */     //   72: aload_0
/*     */     //   73: getfield 6	org/apache/coyote/http11/Http11OutputBuffer:headerBuffer	Ljava/nio/ByteBuffer;
/*     */     //   76: invokevirtual 34	java/nio/ByteBuffer:capacity	()I
/*     */     //   79: invokevirtual 35	java/nio/Buffer:limit	(I)Ljava/nio/Buffer;
/*     */     //   82: pop
/*     */     //   83: aload_1
/*     */     //   84: athrow
/*     */     //   85: return
/*     */     // Line number table:
/*     */     //   Java source line #341	-> byte code offset #0
/*     */     //   Java source line #343	-> byte code offset #8
/*     */     //   Java source line #345	-> byte code offset #18
/*     */     //   Java source line #347	-> byte code offset #26
/*     */     //   Java source line #349	-> byte code offset #41
/*     */     //   Java source line #350	-> byte code offset #60
/*     */     //   Java source line #349	-> byte code offset #63
/*     */     //   Java source line #352	-> byte code offset #85
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	86	0	this	Http11OutputBuffer
/*     */     //   63	21	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   26	41	63	finally
/*     */   }
/*     */   
/*     */   public void sendStatus()
/*     */   {
/* 361 */     write(Constants.HTTP_11_BYTES);
/* 362 */     this.headerBuffer.put((byte)32);
/*     */     
/*     */ 
/* 365 */     int status = this.response.getStatus();
/* 366 */     switch (status) {
/*     */     case 200: 
/* 368 */       write(Constants._200_BYTES);
/* 369 */       break;
/*     */     case 400: 
/* 371 */       write(Constants._400_BYTES);
/* 372 */       break;
/*     */     case 404: 
/* 374 */       write(Constants._404_BYTES);
/* 375 */       break;
/*     */     default: 
/* 377 */       write(status);
/*     */     }
/*     */     
/* 380 */     this.headerBuffer.put((byte)32);
/*     */     
/* 382 */     if (this.sendReasonPhrase)
/*     */     {
/* 384 */       String message = null;
/* 385 */       if ((org.apache.coyote.Constants.USE_CUSTOM_STATUS_MSG_IN_HEADER) && 
/* 386 */         (HttpMessages.isSafeInHttpHeader(this.response.getMessage()))) {
/* 387 */         message = this.response.getMessage();
/*     */       }
/* 389 */       if (message == null) {
/* 390 */         write(HttpMessages.getInstance(this.response
/* 391 */           .getLocale()).getMessage(status));
/*     */       } else {
/* 393 */         write(message);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 401 */     this.headerBuffer.put((byte)13).put((byte)10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendHeader(MessageBytes name, MessageBytes value)
/*     */   {
/* 412 */     write(name);
/* 413 */     this.headerBuffer.put((byte)58).put((byte)32);
/* 414 */     write(value);
/* 415 */     this.headerBuffer.put((byte)13).put((byte)10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void endHeaders()
/*     */   {
/* 423 */     this.headerBuffer.put((byte)13).put((byte)10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void write(MessageBytes mb)
/*     */   {
/* 435 */     if (mb.getType() != 2) {
/* 436 */       mb.toBytes();
/* 437 */       ByteChunk bc = mb.getByteChunk();
/*     */       
/*     */ 
/*     */ 
/* 441 */       byte[] buffer = bc.getBuffer();
/* 442 */       for (int i = bc.getOffset(); i < bc.getLength(); i++)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 448 */         if (((buffer[i] > -1) && (buffer[i] <= 31) && (buffer[i] != 9)) || (buffer[i] == Byte.MAX_VALUE))
/*     */         {
/* 450 */           buffer[i] = 32;
/*     */         }
/*     */       }
/*     */     }
/* 454 */     write(mb.getByteChunk());
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
/*     */   private void write(ByteChunk bc)
/*     */   {
/* 467 */     int length = bc.getLength();
/* 468 */     checkLengthBeforeWrite(length);
/* 469 */     this.headerBuffer.put(bc.getBytes(), bc.getStart(), length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(byte[] b)
/*     */   {
/* 481 */     checkLengthBeforeWrite(b.length);
/*     */     
/*     */ 
/* 484 */     this.headerBuffer.put(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void write(String s)
/*     */   {
/* 496 */     if (s == null) {
/* 497 */       return;
/*     */     }
/*     */     
/*     */ 
/* 501 */     int len = s.length();
/* 502 */     checkLengthBeforeWrite(len);
/* 503 */     for (int i = 0; i < len; i++) {
/* 504 */       char c = s.charAt(i);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 509 */       if (((c <= '\037') && (c != '\t')) || (c == '') || (c > 'ÿ')) {
/* 510 */         c = ' ';
/*     */       }
/* 512 */       this.headerBuffer.put((byte)c);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void write(int value)
/*     */   {
/* 525 */     String s = Integer.toString(value);
/* 526 */     int len = s.length();
/* 527 */     checkLengthBeforeWrite(len);
/* 528 */     for (int i = 0; i < len; i++) {
/* 529 */       char c = s.charAt(i);
/* 530 */       this.headerBuffer.put((byte)c);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkLengthBeforeWrite(int length)
/*     */   {
/* 542 */     if (this.headerBuffer.position() + length + 4 > this.headerBuffer.capacity())
/*     */     {
/* 544 */       throw new HeadersTooLargeException(sm.getString("iob.responseheadertoolarge.error"));
/*     */     }
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
/*     */   protected boolean flushBuffer(boolean block)
/*     */     throws IOException
/*     */   {
/* 560 */     return this.socketWrapper.flush(block);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean isBlocking()
/*     */   {
/* 569 */     return this.response.getWriteListener() == null;
/*     */   }
/*     */   
/*     */   protected final boolean isReady()
/*     */   {
/* 574 */     boolean result = !hasDataToWrite();
/* 575 */     if (!result) {
/* 576 */       this.socketWrapper.registerWriteInterest();
/*     */     }
/* 578 */     return result;
/*     */   }
/*     */   
/*     */   public boolean hasDataToWrite()
/*     */   {
/* 583 */     return this.socketWrapper.hasDataToWrite();
/*     */   }
/*     */   
/*     */   public void registerWriteInterest()
/*     */   {
/* 588 */     this.socketWrapper.registerWriteInterest();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected class SocketOutputBuffer
/*     */     implements HttpOutputBuffer
/*     */   {
/*     */     protected SocketOutputBuffer() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Deprecated
/*     */     public int doWrite(ByteChunk chunk)
/*     */       throws IOException
/*     */     {
/* 608 */       int len = chunk.getLength();
/* 609 */       int start = chunk.getStart();
/* 610 */       byte[] b = chunk.getBuffer();
/* 611 */       Http11OutputBuffer.this.socketWrapper.write(Http11OutputBuffer.this.isBlocking(), b, start, len);
/* 612 */       Http11OutputBuffer.this.byteCount += len;
/* 613 */       return len;
/*     */     }
/*     */     
/*     */ 
/*     */     public int doWrite(ByteBuffer chunk)
/*     */       throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 622 */         int len = chunk.remaining();
/* 623 */         Http11OutputBuffer.this.socketWrapper.write(Http11OutputBuffer.this.isBlocking(), chunk);
/* 624 */         len -= chunk.remaining();
/* 625 */         Http11OutputBuffer.this.byteCount += len;
/* 626 */         return len;
/*     */       } catch (IOException ioe) {
/* 628 */         Http11OutputBuffer.this.response.action(ActionCode.CLOSE_NOW, ioe);
/*     */         
/* 630 */         throw ioe;
/*     */       }
/*     */     }
/*     */     
/*     */     public long getBytesWritten()
/*     */     {
/* 636 */       return Http11OutputBuffer.this.byteCount;
/*     */     }
/*     */     
/*     */     public void end() throws IOException
/*     */     {
/* 641 */       Http11OutputBuffer.this.socketWrapper.flush(true);
/*     */     }
/*     */     
/*     */     public void flush() throws IOException
/*     */     {
/* 646 */       Http11OutputBuffer.this.socketWrapper.flush(Http11OutputBuffer.this.isBlocking());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\Http11OutputBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */