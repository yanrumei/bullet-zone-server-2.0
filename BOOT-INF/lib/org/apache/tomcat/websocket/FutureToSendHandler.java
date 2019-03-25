/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.websocket.SendHandler;
/*     */ import javax.websocket.SendResult;
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
/*     */ class FutureToSendHandler
/*     */   implements Future<Void>, SendHandler
/*     */ {
/*  37 */   private static final StringManager sm = StringManager.getManager(FutureToSendHandler.class);
/*     */   
/*  39 */   private final CountDownLatch latch = new CountDownLatch(1);
/*     */   private final WsSession wsSession;
/*  41 */   private volatile AtomicReference<SendResult> result = new AtomicReference(null);
/*     */   
/*     */   public FutureToSendHandler(WsSession wsSession) {
/*  44 */     this.wsSession = wsSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onResult(SendResult result)
/*     */   {
/*  52 */     this.result.compareAndSet(null, result);
/*  53 */     this.latch.countDown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean cancel(boolean mayInterruptIfRunning)
/*     */   {
/*  62 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isCancelled()
/*     */   {
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDone()
/*     */   {
/*  73 */     return this.latch.getCount() == 0L;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Void get()
/*     */     throws InterruptedException, ExecutionException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 8	org/apache/tomcat/websocket/FutureToSendHandler:wsSession	Lorg/apache/tomcat/websocket/WsSession;
/*     */     //   4: aload_0
/*     */     //   5: invokevirtual 12	org/apache/tomcat/websocket/WsSession:registerFuture	(Lorg/apache/tomcat/websocket/FutureToSendHandler;)V
/*     */     //   8: aload_0
/*     */     //   9: getfield 4	org/apache/tomcat/websocket/FutureToSendHandler:latch	Ljava/util/concurrent/CountDownLatch;
/*     */     //   12: invokevirtual 13	java/util/concurrent/CountDownLatch:await	()V
/*     */     //   15: aload_0
/*     */     //   16: getfield 8	org/apache/tomcat/websocket/FutureToSendHandler:wsSession	Lorg/apache/tomcat/websocket/WsSession;
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual 14	org/apache/tomcat/websocket/WsSession:unregisterFuture	(Lorg/apache/tomcat/websocket/FutureToSendHandler;)V
/*     */     //   23: goto +14 -> 37
/*     */     //   26: astore_1
/*     */     //   27: aload_0
/*     */     //   28: getfield 8	org/apache/tomcat/websocket/FutureToSendHandler:wsSession	Lorg/apache/tomcat/websocket/WsSession;
/*     */     //   31: aload_0
/*     */     //   32: invokevirtual 14	org/apache/tomcat/websocket/WsSession:unregisterFuture	(Lorg/apache/tomcat/websocket/FutureToSendHandler;)V
/*     */     //   35: aload_1
/*     */     //   36: athrow
/*     */     //   37: aload_0
/*     */     //   38: getfield 7	org/apache/tomcat/websocket/FutureToSendHandler:result	Ljava/util/concurrent/atomic/AtomicReference;
/*     */     //   41: invokevirtual 15	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
/*     */     //   44: checkcast 16	javax/websocket/SendResult
/*     */     //   47: invokevirtual 17	javax/websocket/SendResult:getException	()Ljava/lang/Throwable;
/*     */     //   50: ifnull +24 -> 74
/*     */     //   53: new 18	java/util/concurrent/ExecutionException
/*     */     //   56: dup
/*     */     //   57: aload_0
/*     */     //   58: getfield 7	org/apache/tomcat/websocket/FutureToSendHandler:result	Ljava/util/concurrent/atomic/AtomicReference;
/*     */     //   61: invokevirtual 15	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
/*     */     //   64: checkcast 16	javax/websocket/SendResult
/*     */     //   67: invokevirtual 17	javax/websocket/SendResult:getException	()Ljava/lang/Throwable;
/*     */     //   70: invokespecial 19	java/util/concurrent/ExecutionException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   73: athrow
/*     */     //   74: aconst_null
/*     */     //   75: areturn
/*     */     // Line number table:
/*     */     //   Java source line #80	-> byte code offset #0
/*     */     //   Java source line #81	-> byte code offset #8
/*     */     //   Java source line #83	-> byte code offset #15
/*     */     //   Java source line #84	-> byte code offset #23
/*     */     //   Java source line #83	-> byte code offset #26
/*     */     //   Java source line #85	-> byte code offset #37
/*     */     //   Java source line #86	-> byte code offset #53
/*     */     //   Java source line #88	-> byte code offset #74
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	76	0	this	FutureToSendHandler
/*     */     //   26	10	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	15	26	finally
/*     */   }
/*     */   
/*     */   public Void get(long timeout, TimeUnit unit)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/*  95 */     boolean retval = false;
/*     */     try {
/*  97 */       this.wsSession.registerFuture(this);
/*  98 */       retval = this.latch.await(timeout, unit);
/*     */     } finally {
/* 100 */       this.wsSession.unregisterFuture(this);
/*     */     }
/*     */     
/* 103 */     if (!retval) {
/* 104 */       throw new TimeoutException(sm.getString("futureToSendHandler.timeout", new Object[] {
/* 105 */         Long.valueOf(timeout), unit.toString().toLowerCase() }));
/*     */     }
/* 107 */     if (((SendResult)this.result.get()).getException() != null) {
/* 108 */       throw new ExecutionException(((SendResult)this.result.get()).getException());
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\FutureToSendHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */