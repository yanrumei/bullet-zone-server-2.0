/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousByteChannel;
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
/*     */ public class Nio2Channel
/*     */   implements AsynchronousByteChannel
/*     */ {
/*  36 */   protected static final ByteBuffer emptyBuf = ByteBuffer.allocate(0);
/*     */   
/*  38 */   protected AsynchronousSocketChannel sc = null;
/*  39 */   protected SocketWrapperBase<Nio2Channel> socket = null;
/*     */   protected final SocketBufferHandler bufHandler;
/*     */   
/*     */   public Nio2Channel(SocketBufferHandler bufHandler) {
/*  43 */     this.bufHandler = bufHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset(AsynchronousSocketChannel channel, SocketWrapperBase<Nio2Channel> socket)
/*     */     throws IOException
/*     */   {
/*  56 */     this.sc = channel;
/*  57 */     this.socket = socket;
/*  58 */     this.bufHandler.reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void free()
/*     */   {
/*  65 */     this.bufHandler.free();
/*     */   }
/*     */   
/*     */   public SocketWrapperBase<Nio2Channel> getSocket() {
/*  69 */     return this.socket;
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
/*  80 */     this.sc.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close(boolean force)
/*     */     throws IOException
/*     */   {
/*  92 */     if ((isOpen()) || (force)) {
/*  93 */       close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 105 */     return this.sc.isOpen();
/*     */   }
/*     */   
/*     */   public SocketBufferHandler getBufHandler() {
/* 109 */     return this.bufHandler;
/*     */   }
/*     */   
/*     */   public AsynchronousSocketChannel getIOChannel() {
/* 113 */     return this.sc;
/*     */   }
/*     */   
/*     */   public boolean isClosing() {
/* 117 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isHandshakeComplete() {
/* 121 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int handshake()
/*     */     throws IOException
/*     */   {
/* 133 */     return 0;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 138 */     return super.toString() + ":" + this.sc.toString();
/*     */   }
/*     */   
/*     */   public Future<Integer> read(ByteBuffer dst)
/*     */   {
/* 143 */     return this.sc.read(dst);
/*     */   }
/*     */   
/*     */ 
/*     */   public <A> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler)
/*     */   {
/* 149 */     read(dst, 2147483647L, TimeUnit.MILLISECONDS, attachment, handler);
/*     */   }
/*     */   
/*     */ 
/*     */   public <A> void read(ByteBuffer dst, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler)
/*     */   {
/* 155 */     this.sc.read(dst, timeout, unit, attachment, handler);
/*     */   }
/*     */   
/*     */ 
/*     */   public <A> void read(ByteBuffer[] dsts, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler)
/*     */   {
/* 161 */     this.sc.read(dsts, offset, length, timeout, unit, attachment, handler);
/*     */   }
/*     */   
/*     */   public Future<Integer> write(ByteBuffer src)
/*     */   {
/* 166 */     return this.sc.write(src);
/*     */   }
/*     */   
/*     */ 
/*     */   public <A> void write(ByteBuffer src, A attachment, CompletionHandler<Integer, ? super A> handler)
/*     */   {
/* 172 */     write(src, 2147483647L, TimeUnit.MILLISECONDS, attachment, handler);
/*     */   }
/*     */   
/*     */   public <A> void write(ByteBuffer src, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler)
/*     */   {
/* 177 */     this.sc.write(src, timeout, unit, attachment, handler);
/*     */   }
/*     */   
/*     */ 
/*     */   public <A> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler)
/*     */   {
/* 183 */     this.sc.write(srcs, offset, length, timeout, unit, attachment, handler);
/*     */   }
/*     */   
/* 186 */   private static final Future<Boolean> DONE = new Future()
/*     */   {
/*     */     public boolean cancel(boolean mayInterruptIfRunning) {
/* 189 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isCancelled() {
/* 193 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isDone() {
/* 197 */       return true;
/*     */     }
/*     */     
/*     */     public Boolean get() throws InterruptedException, ExecutionException
/*     */     {
/* 202 */       return Boolean.TRUE;
/*     */     }
/*     */     
/*     */     public Boolean get(long timeout, TimeUnit unit)
/*     */       throws InterruptedException, ExecutionException, TimeoutException
/*     */     {
/* 208 */       return Boolean.TRUE;
/*     */     }
/*     */   };
/*     */   private ApplicationBufferHandler appReadBufHandler;
/*     */   
/* 213 */   public Future<Boolean> flush() { return DONE; }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAppReadBufHandler(ApplicationBufferHandler handler)
/*     */   {
/* 219 */     this.appReadBufHandler = handler;
/*     */   }
/*     */   
/* 222 */   protected ApplicationBufferHandler getAppReadBufHandler() { return this.appReadBufHandler; }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\Nio2Channel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */