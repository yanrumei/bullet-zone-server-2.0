/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousSocketChannel;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ public class AsyncChannelWrapperNonSecure
/*     */   implements AsyncChannelWrapper
/*     */ {
/*  35 */   private static final Future<Void> NOOP_FUTURE = new NoOpFuture(null);
/*     */   
/*     */   private final AsynchronousSocketChannel socketChannel;
/*     */   
/*     */   public AsyncChannelWrapperNonSecure(AsynchronousSocketChannel socketChannel)
/*     */   {
/*  41 */     this.socketChannel = socketChannel;
/*     */   }
/*     */   
/*     */   public Future<Integer> read(ByteBuffer dst)
/*     */   {
/*  46 */     return this.socketChannel.read(dst);
/*     */   }
/*     */   
/*     */ 
/*     */   public <B, A extends B> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, B> handler)
/*     */   {
/*  52 */     this.socketChannel.read(dst, attachment, handler);
/*     */   }
/*     */   
/*     */   public Future<Integer> write(ByteBuffer src)
/*     */   {
/*  57 */     return this.socketChannel.write(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <B, A extends B> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, B> handler)
/*     */   {
/*  64 */     this.socketChannel.write(srcs, offset, length, timeout, unit, attachment, handler);
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/*     */     try
/*     */     {
/*  71 */       this.socketChannel.close();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */   }
/*     */   
/*     */ 
/*     */   public Future<Void> handshake()
/*     */   {
/*  79 */     return NOOP_FUTURE;
/*     */   }
/*     */   
/*     */   private static final class NoOpFuture
/*     */     implements Future<Void>
/*     */   {
/*     */     public boolean cancel(boolean mayInterruptIfRunning)
/*     */     {
/*  87 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isCancelled()
/*     */     {
/*  92 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isDone()
/*     */     {
/*  97 */       return true;
/*     */     }
/*     */     
/*     */     public Void get() throws InterruptedException, ExecutionException
/*     */     {
/* 102 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     public Void get(long timeout, TimeUnit unit)
/*     */       throws InterruptedException, ExecutionException, TimeoutException
/*     */     {
/* 109 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\AsyncChannelWrapperNonSecure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */