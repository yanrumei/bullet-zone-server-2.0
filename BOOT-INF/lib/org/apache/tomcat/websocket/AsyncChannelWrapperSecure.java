/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousSocketChannel;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*     */ import javax.net.ssl.SSLEngineResult.Status;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class AsyncChannelWrapperSecure
/*     */   implements AsyncChannelWrapper
/*     */ {
/*  52 */   private static final Log log = LogFactory.getLog(AsyncChannelWrapperSecure.class);
/*     */   
/*  54 */   private static final StringManager sm = StringManager.getManager(AsyncChannelWrapperSecure.class);
/*     */   
/*  56 */   private static final ByteBuffer DUMMY = ByteBuffer.allocate(16921);
/*     */   
/*     */   private final AsynchronousSocketChannel socketChannel;
/*     */   
/*     */   private final SSLEngine sslEngine;
/*     */   private final ByteBuffer socketReadBuffer;
/*     */   private final ByteBuffer socketWriteBuffer;
/*  63 */   private final ExecutorService executor = Executors.newFixedThreadPool(2, new SecureIOThreadFactory(null));
/*  64 */   private AtomicBoolean writing = new AtomicBoolean(false);
/*  65 */   private AtomicBoolean reading = new AtomicBoolean(false);
/*     */   
/*     */   public AsyncChannelWrapperSecure(AsynchronousSocketChannel socketChannel, SSLEngine sslEngine)
/*     */   {
/*  69 */     this.socketChannel = socketChannel;
/*  70 */     this.sslEngine = sslEngine;
/*     */     
/*  72 */     int socketBufferSize = sslEngine.getSession().getPacketBufferSize();
/*  73 */     this.socketReadBuffer = ByteBuffer.allocateDirect(socketBufferSize);
/*  74 */     this.socketWriteBuffer = ByteBuffer.allocateDirect(socketBufferSize);
/*     */   }
/*     */   
/*     */   public Future<Integer> read(ByteBuffer dst)
/*     */   {
/*  79 */     WrapperFuture<Integer, Void> future = new WrapperFuture();
/*     */     
/*  81 */     if (!this.reading.compareAndSet(false, true)) {
/*  82 */       throw new IllegalStateException(sm.getString("asyncChannelWrapperSecure.concurrentRead"));
/*     */     }
/*     */     
/*     */ 
/*  86 */     ReadTask readTask = new ReadTask(dst, future);
/*     */     
/*  88 */     this.executor.execute(readTask);
/*     */     
/*  90 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <B, A extends B> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, B> handler)
/*     */   {
/*  97 */     WrapperFuture<Integer, B> future = new WrapperFuture(handler, attachment);
/*     */     
/*     */ 
/* 100 */     if (!this.reading.compareAndSet(false, true)) {
/* 101 */       throw new IllegalStateException(sm.getString("asyncChannelWrapperSecure.concurrentRead"));
/*     */     }
/*     */     
/*     */ 
/* 105 */     ReadTask readTask = new ReadTask(dst, future);
/*     */     
/* 107 */     this.executor.execute(readTask);
/*     */   }
/*     */   
/*     */ 
/*     */   public Future<Integer> write(ByteBuffer src)
/*     */   {
/* 113 */     WrapperFuture<Long, Void> inner = new WrapperFuture();
/*     */     
/* 115 */     if (!this.writing.compareAndSet(false, true)) {
/* 116 */       throw new IllegalStateException(sm.getString("asyncChannelWrapperSecure.concurrentWrite"));
/*     */     }
/*     */     
/*     */ 
/* 120 */     WriteTask writeTask = new WriteTask(new ByteBuffer[] { src }, 0, 1, inner);
/*     */     
/*     */ 
/* 123 */     this.executor.execute(writeTask);
/*     */     
/* 125 */     Future<Integer> future = new LongToIntegerFuture(inner);
/* 126 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <B, A extends B> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, B> handler)
/*     */   {
/* 134 */     WrapperFuture<Long, B> future = new WrapperFuture(handler, attachment);
/*     */     
/*     */ 
/* 137 */     if (!this.writing.compareAndSet(false, true)) {
/* 138 */       throw new IllegalStateException(sm.getString("asyncChannelWrapperSecure.concurrentWrite"));
/*     */     }
/*     */     
/*     */ 
/* 142 */     WriteTask writeTask = new WriteTask(srcs, offset, length, future);
/*     */     
/* 144 */     this.executor.execute(writeTask);
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/*     */     try {
/* 150 */       this.socketChannel.close();
/*     */     } catch (IOException e) {
/* 152 */       log.info(sm.getString("asyncChannelWrapperSecure.closeFail"));
/*     */     }
/* 154 */     this.executor.shutdownNow();
/*     */   }
/*     */   
/*     */   public Future<Void> handshake()
/*     */     throws SSLException
/*     */   {
/* 160 */     WrapperFuture<Void, Void> wFuture = new WrapperFuture();
/*     */     
/* 162 */     Thread t = new WebSocketSslHandshakeThread(wFuture);
/* 163 */     t.start();
/*     */     
/* 165 */     return wFuture;
/*     */   }
/*     */   
/*     */   private class WriteTask
/*     */     implements Runnable
/*     */   {
/*     */     private final ByteBuffer[] srcs;
/*     */     private final int offset;
/*     */     private final int length;
/*     */     private final AsyncChannelWrapperSecure.WrapperFuture<Long, ?> future;
/*     */     
/*     */     public WriteTask(int srcs, int offset, AsyncChannelWrapperSecure.WrapperFuture<Long, ?> length)
/*     */     {
/* 178 */       this.srcs = srcs;
/* 179 */       this.future = future;
/* 180 */       this.offset = offset;
/* 181 */       this.length = length;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 186 */       long written = 0L;
/*     */       try
/*     */       {
/* 189 */         for (int i = this.offset; i < this.offset + this.length; i++) {
/* 190 */           ByteBuffer src = this.srcs[i];
/* 191 */           while (src.hasRemaining()) {
/* 192 */             AsyncChannelWrapperSecure.this.socketWriteBuffer.clear();
/*     */             
/*     */ 
/* 195 */             SSLEngineResult r = AsyncChannelWrapperSecure.this.sslEngine.wrap(src, AsyncChannelWrapperSecure.this.socketWriteBuffer);
/* 196 */             written += r.bytesConsumed();
/* 197 */             SSLEngineResult.Status s = r.getStatus();
/*     */             
/* 199 */             if ((s != SSLEngineResult.Status.OK) && (s != SSLEngineResult.Status.BUFFER_OVERFLOW))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 205 */               throw new IllegalStateException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.statusWrap"));
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 210 */             if (r.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/* 211 */               Runnable runnable = AsyncChannelWrapperSecure.this.sslEngine.getDelegatedTask();
/* 212 */               while (runnable != null) {
/* 213 */                 runnable.run();
/* 214 */                 runnable = AsyncChannelWrapperSecure.this.sslEngine.getDelegatedTask();
/*     */               }
/*     */             }
/*     */             
/* 218 */             AsyncChannelWrapperSecure.this.socketWriteBuffer.flip();
/*     */             
/*     */ 
/* 221 */             int toWrite = r.bytesProduced();
/* 222 */             while (toWrite > 0)
/*     */             {
/* 224 */               Future<Integer> f = AsyncChannelWrapperSecure.this.socketChannel.write(AsyncChannelWrapperSecure.this.socketWriteBuffer);
/* 225 */               Integer socketWrite = (Integer)f.get();
/* 226 */               toWrite -= socketWrite.intValue();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 232 */         if (AsyncChannelWrapperSecure.this.writing.compareAndSet(true, false)) {
/* 233 */           this.future.complete(Long.valueOf(written));
/*     */         } else {
/* 235 */           this.future.fail(new IllegalStateException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.wrongStateWrite")));
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 239 */         AsyncChannelWrapperSecure.this.writing.set(false);
/* 240 */         this.future.fail(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class ReadTask implements Runnable
/*     */   {
/*     */     private final ByteBuffer dest;
/*     */     private final AsyncChannelWrapperSecure.WrapperFuture<Integer, ?> future;
/*     */     
/*     */     public ReadTask(AsyncChannelWrapperSecure.WrapperFuture<Integer, ?> dest)
/*     */     {
/* 252 */       this.dest = dest;
/* 253 */       this.future = future;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 258 */       int read = 0;
/*     */       
/* 260 */       boolean forceRead = false;
/*     */       try
/*     */       {
/* 263 */         while (read == 0) {
/* 264 */           AsyncChannelWrapperSecure.this.socketReadBuffer.compact();
/*     */           
/* 266 */           if (forceRead) {
/* 267 */             forceRead = false;
/* 268 */             Future<Integer> f = AsyncChannelWrapperSecure.this.socketChannel.read(AsyncChannelWrapperSecure.this.socketReadBuffer);
/* 269 */             Integer socketRead = (Integer)f.get();
/* 270 */             if (socketRead.intValue() == -1) {
/* 271 */               throw new EOFException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.eof"));
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 276 */           AsyncChannelWrapperSecure.this.socketReadBuffer.flip();
/*     */           
/* 278 */           if (AsyncChannelWrapperSecure.this.socketReadBuffer.hasRemaining())
/*     */           {
/*     */ 
/* 281 */             SSLEngineResult r = AsyncChannelWrapperSecure.this.sslEngine.unwrap(AsyncChannelWrapperSecure.this.socketReadBuffer, this.dest);
/* 282 */             read += r.bytesProduced();
/* 283 */             SSLEngineResult.Status s = r.getStatus();
/*     */             
/* 285 */             if (s != SSLEngineResult.Status.OK)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 290 */               if (s == SSLEngineResult.Status.BUFFER_UNDERFLOW)
/*     */               {
/* 292 */                 if (read == 0)
/*     */                 {
/*     */ 
/* 295 */                   forceRead = true;
/*     */                 }
/*     */                 
/*     */               }
/* 299 */               else if (s == SSLEngineResult.Status.BUFFER_OVERFLOW)
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 305 */                 if (AsyncChannelWrapperSecure.this.reading.compareAndSet(true, false))
/*     */                 {
/* 307 */                   throw new ReadBufferOverflowException(AsyncChannelWrapperSecure.this.sslEngine.getSession().getApplicationBufferSize());
/*     */                 }
/* 309 */                 this.future.fail(new IllegalStateException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.wrongStateRead")));
/*     */ 
/*     */               }
/*     */               else
/*     */               {
/* 314 */                 throw new IllegalStateException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.statusUnwrap"));
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 319 */             if (r.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/* 320 */               Runnable runnable = AsyncChannelWrapperSecure.this.sslEngine.getDelegatedTask();
/* 321 */               while (runnable != null) {
/* 322 */                 runnable.run();
/* 323 */                 runnable = AsyncChannelWrapperSecure.this.sslEngine.getDelegatedTask();
/*     */               }
/*     */             }
/*     */           } else {
/* 327 */             forceRead = true;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 332 */         if (AsyncChannelWrapperSecure.this.reading.compareAndSet(true, false)) {
/* 333 */           this.future.complete(Integer.valueOf(read));
/*     */         } else {
/* 335 */           this.future.fail(new IllegalStateException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.wrongStateRead")));
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 339 */         AsyncChannelWrapperSecure.this.reading.set(false);
/* 340 */         this.future.fail(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class WebSocketSslHandshakeThread
/*     */     extends Thread
/*     */   {
/*     */     private final AsyncChannelWrapperSecure.WrapperFuture<Void, Void> hFuture;
/*     */     private SSLEngineResult.HandshakeStatus handshakeStatus;
/*     */     private SSLEngineResult.Status resultStatus;
/*     */     
/*     */     public WebSocketSslHandshakeThread()
/*     */     {
/* 354 */       this.hFuture = hFuture;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try {
/* 360 */         AsyncChannelWrapperSecure.this.sslEngine.beginHandshake();
/*     */         
/* 362 */         AsyncChannelWrapperSecure.this.socketReadBuffer.position(AsyncChannelWrapperSecure.this.socketReadBuffer.limit());
/*     */         
/* 364 */         this.handshakeStatus = AsyncChannelWrapperSecure.this.sslEngine.getHandshakeStatus();
/* 365 */         this.resultStatus = SSLEngineResult.Status.OK;
/*     */         
/* 367 */         boolean handshaking = true;
/*     */         
/* 369 */         while (handshaking) {
/* 370 */           switch (AsyncChannelWrapperSecure.1.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[this.handshakeStatus.ordinal()]) {
/*     */           case 1: 
/* 372 */             AsyncChannelWrapperSecure.this.socketWriteBuffer.clear();
/*     */             
/* 374 */             SSLEngineResult r = AsyncChannelWrapperSecure.this.sslEngine.wrap(AsyncChannelWrapperSecure.DUMMY, AsyncChannelWrapperSecure.this.socketWriteBuffer);
/* 375 */             checkResult(r, true);
/* 376 */             AsyncChannelWrapperSecure.this.socketWriteBuffer.flip();
/*     */             
/* 378 */             Future<Integer> fWrite = AsyncChannelWrapperSecure.this.socketChannel.write(AsyncChannelWrapperSecure.this.socketWriteBuffer);
/* 379 */             fWrite.get();
/* 380 */             break;
/*     */           
/*     */           case 2: 
/* 383 */             AsyncChannelWrapperSecure.this.socketReadBuffer.compact();
/* 384 */             if ((AsyncChannelWrapperSecure.this.socketReadBuffer.position() == 0) || (this.resultStatus == SSLEngineResult.Status.BUFFER_UNDERFLOW))
/*     */             {
/*     */ 
/* 387 */               Future<Integer> fRead = AsyncChannelWrapperSecure.this.socketChannel.read(AsyncChannelWrapperSecure.this.socketReadBuffer);
/* 388 */               fRead.get();
/*     */             }
/* 390 */             AsyncChannelWrapperSecure.this.socketReadBuffer.flip();
/*     */             
/* 392 */             SSLEngineResult r = AsyncChannelWrapperSecure.this.sslEngine.unwrap(AsyncChannelWrapperSecure.this.socketReadBuffer, AsyncChannelWrapperSecure.DUMMY);
/* 393 */             checkResult(r, false);
/* 394 */             break;
/*     */           
/*     */           case 3: 
/* 397 */             Runnable r = null;
/* 398 */             while ((r = AsyncChannelWrapperSecure.this.sslEngine.getDelegatedTask()) != null) {
/* 399 */               r.run();
/*     */             }
/* 401 */             this.handshakeStatus = AsyncChannelWrapperSecure.this.sslEngine.getHandshakeStatus();
/* 402 */             break;
/*     */           
/*     */           case 4: 
/* 405 */             handshaking = false;
/* 406 */             break;
/*     */           
/*     */ 
/*     */           case 5: 
/* 410 */             throw new SSLException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.notHandshaking"));
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (SSLException|InterruptedException|ExecutionException e)
/*     */       {
/* 416 */         this.hFuture.fail(e);
/*     */       }
/*     */       
/* 419 */       this.hFuture.complete(null);
/*     */     }
/*     */     
/*     */     private void checkResult(SSLEngineResult result, boolean wrap)
/*     */       throws SSLException
/*     */     {
/* 425 */       this.handshakeStatus = result.getHandshakeStatus();
/* 426 */       this.resultStatus = result.getStatus();
/*     */       
/* 428 */       if ((this.resultStatus != SSLEngineResult.Status.OK) && ((wrap) || (this.resultStatus != SSLEngineResult.Status.BUFFER_UNDERFLOW)))
/*     */       {
/*     */ 
/* 431 */         throw new SSLException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.check.notOk", new Object[] { this.resultStatus }));
/*     */       }
/* 433 */       if ((wrap) && (result.bytesConsumed() != 0)) {
/* 434 */         throw new SSLException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.check.wrap"));
/*     */       }
/* 436 */       if ((!wrap) && (result.bytesProduced() != 0)) {
/* 437 */         throw new SSLException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.check.unwrap"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class WrapperFuture<T, A>
/*     */     implements Future<T>
/*     */   {
/*     */     private final CompletionHandler<T, A> handler;
/*     */     private final A attachment;
/* 448 */     private volatile T result = null;
/* 449 */     private volatile Throwable throwable = null;
/* 450 */     private CountDownLatch completionLatch = new CountDownLatch(1);
/*     */     
/*     */     public WrapperFuture() {
/* 453 */       this(null, null);
/*     */     }
/*     */     
/*     */     public WrapperFuture(CompletionHandler<T, A> handler, A attachment) {
/* 457 */       this.handler = handler;
/* 458 */       this.attachment = attachment;
/*     */     }
/*     */     
/*     */     public void complete(T result) {
/* 462 */       this.result = result;
/* 463 */       this.completionLatch.countDown();
/* 464 */       if (this.handler != null) {
/* 465 */         this.handler.completed(result, this.attachment);
/*     */       }
/*     */     }
/*     */     
/*     */     public void fail(Throwable t) {
/* 470 */       this.throwable = t;
/* 471 */       this.completionLatch.countDown();
/* 472 */       if (this.handler != null) {
/* 473 */         this.handler.failed(this.throwable, this.attachment);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public final boolean cancel(boolean mayInterruptIfRunning)
/*     */     {
/* 480 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public final boolean isCancelled()
/*     */     {
/* 486 */       return false;
/*     */     }
/*     */     
/*     */     public final boolean isDone()
/*     */     {
/* 491 */       return this.completionLatch.getCount() > 0L;
/*     */     }
/*     */     
/*     */     public T get() throws InterruptedException, ExecutionException
/*     */     {
/* 496 */       this.completionLatch.await();
/* 497 */       if (this.throwable != null) {
/* 498 */         throw new ExecutionException(this.throwable);
/*     */       }
/* 500 */       return (T)this.result;
/*     */     }
/*     */     
/*     */ 
/*     */     public T get(long timeout, TimeUnit unit)
/*     */       throws InterruptedException, ExecutionException, TimeoutException
/*     */     {
/* 507 */       boolean latchResult = this.completionLatch.await(timeout, unit);
/* 508 */       if (!latchResult) {
/* 509 */         throw new TimeoutException();
/*     */       }
/* 511 */       if (this.throwable != null) {
/* 512 */         throw new ExecutionException(this.throwable);
/*     */       }
/* 514 */       return (T)this.result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LongToIntegerFuture implements Future<Integer>
/*     */   {
/*     */     private final Future<Long> wrapped;
/*     */     
/*     */     public LongToIntegerFuture(Future<Long> wrapped) {
/* 523 */       this.wrapped = wrapped;
/*     */     }
/*     */     
/*     */     public boolean cancel(boolean mayInterruptIfRunning)
/*     */     {
/* 528 */       return this.wrapped.cancel(mayInterruptIfRunning);
/*     */     }
/*     */     
/*     */     public boolean isCancelled()
/*     */     {
/* 533 */       return this.wrapped.isCancelled();
/*     */     }
/*     */     
/*     */     public boolean isDone()
/*     */     {
/* 538 */       return this.wrapped.isDone();
/*     */     }
/*     */     
/*     */     public Integer get() throws InterruptedException, ExecutionException
/*     */     {
/* 543 */       Long result = (Long)this.wrapped.get();
/* 544 */       if (result.longValue() > 2147483647L) {
/* 545 */         throw new ExecutionException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.tooBig", new Object[] { result }), null);
/*     */       }
/*     */       
/* 548 */       return Integer.valueOf(result.intValue());
/*     */     }
/*     */     
/*     */ 
/*     */     public Integer get(long timeout, TimeUnit unit)
/*     */       throws InterruptedException, ExecutionException, TimeoutException
/*     */     {
/* 555 */       Long result = (Long)this.wrapped.get(timeout, unit);
/* 556 */       if (result.longValue() > 2147483647L) {
/* 557 */         throw new ExecutionException(AsyncChannelWrapperSecure.sm.getString("asyncChannelWrapperSecure.tooBig", new Object[] { result }), null);
/*     */       }
/*     */       
/* 560 */       return Integer.valueOf(result.intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SecureIOThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/* 567 */     private AtomicInteger count = new AtomicInteger(0);
/*     */     
/*     */     public Thread newThread(Runnable r)
/*     */     {
/* 571 */       Thread t = new Thread(r);
/* 572 */       t.setName("WebSocketClient-SecureIO-" + this.count.incrementAndGet());
/*     */       
/*     */ 
/* 575 */       t.setDaemon(true);
/* 576 */       return t;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\AsyncChannelWrapperSecure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */