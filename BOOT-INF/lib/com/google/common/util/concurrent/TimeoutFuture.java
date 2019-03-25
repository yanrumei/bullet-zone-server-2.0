/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtIncompatible
/*     */ final class TimeoutFuture<V>
/*     */   extends AbstractFuture.TrustedFuture<V>
/*     */ {
/*     */   @Nullable
/*     */   private ListenableFuture<V> delegateRef;
/*     */   @Nullable
/*     */   private Future<?> timer;
/*     */   
/*     */   static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor)
/*     */   {
/*  42 */     TimeoutFuture<V> result = new TimeoutFuture(delegate);
/*  43 */     Fire<V> fire = new Fire(result);
/*  44 */     result.timer = scheduledExecutor.schedule(fire, time, unit);
/*  45 */     delegate.addListener(fire, MoreExecutors.directExecutor());
/*  46 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TimeoutFuture(ListenableFuture<V> delegate)
/*     */   {
/*  77 */     this.delegateRef = ((ListenableFuture)Preconditions.checkNotNull(delegate));
/*     */   }
/*     */   
/*     */   private static final class Fire<V> implements Runnable {
/*     */     @Nullable
/*     */     TimeoutFuture<V> timeoutFutureRef;
/*     */     
/*     */     Fire(TimeoutFuture<V> timeoutFuture) {
/*  85 */       this.timeoutFutureRef = timeoutFuture;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void run()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	com/google/common/util/concurrent/TimeoutFuture$Fire:timeoutFutureRef	Lcom/google/common/util/concurrent/TimeoutFuture;
/*     */       //   4: astore_1
/*     */       //   5: aload_1
/*     */       //   6: ifnonnull +4 -> 10
/*     */       //   9: return
/*     */       //   10: aload_1
/*     */       //   11: invokestatic 3	com/google/common/util/concurrent/TimeoutFuture:access$000	(Lcom/google/common/util/concurrent/TimeoutFuture;)Lcom/google/common/util/concurrent/ListenableFuture;
/*     */       //   14: astore_2
/*     */       //   15: aload_2
/*     */       //   16: ifnonnull +4 -> 20
/*     */       //   19: return
/*     */       //   20: aload_0
/*     */       //   21: aconst_null
/*     */       //   22: putfield 2	com/google/common/util/concurrent/TimeoutFuture$Fire:timeoutFutureRef	Lcom/google/common/util/concurrent/TimeoutFuture;
/*     */       //   25: aload_2
/*     */       //   26: invokeinterface 4 1 0
/*     */       //   31: ifeq +12 -> 43
/*     */       //   34: aload_1
/*     */       //   35: aload_2
/*     */       //   36: invokevirtual 5	com/google/common/util/concurrent/TimeoutFuture:setFuture	(Lcom/google/common/util/concurrent/ListenableFuture;)Z
/*     */       //   39: pop
/*     */       //   40: goto +56 -> 96
/*     */       //   43: aload_1
/*     */       //   44: new 6	java/util/concurrent/TimeoutException
/*     */       //   47: dup
/*     */       //   48: new 7	java/lang/StringBuilder
/*     */       //   51: dup
/*     */       //   52: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*     */       //   55: ldc 9
/*     */       //   57: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   60: aload_2
/*     */       //   61: invokevirtual 11	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   64: invokevirtual 12	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */       //   67: invokespecial 13	java/util/concurrent/TimeoutException:<init>	(Ljava/lang/String;)V
/*     */       //   70: invokevirtual 14	com/google/common/util/concurrent/TimeoutFuture:setException	(Ljava/lang/Throwable;)Z
/*     */       //   73: pop
/*     */       //   74: aload_2
/*     */       //   75: iconst_1
/*     */       //   76: invokeinterface 15 2 0
/*     */       //   81: pop
/*     */       //   82: goto +14 -> 96
/*     */       //   85: astore_3
/*     */       //   86: aload_2
/*     */       //   87: iconst_1
/*     */       //   88: invokeinterface 15 2 0
/*     */       //   93: pop
/*     */       //   94: aload_3
/*     */       //   95: athrow
/*     */       //   96: return
/*     */       // Line number table:
/*     */       //   Java source line #92	-> byte code offset #0
/*     */       //   Java source line #93	-> byte code offset #5
/*     */       //   Java source line #94	-> byte code offset #9
/*     */       //   Java source line #96	-> byte code offset #10
/*     */       //   Java source line #97	-> byte code offset #15
/*     */       //   Java source line #98	-> byte code offset #19
/*     */       //   Java source line #113	-> byte code offset #20
/*     */       //   Java source line #114	-> byte code offset #25
/*     */       //   Java source line #115	-> byte code offset #34
/*     */       //   Java source line #120	-> byte code offset #43
/*     */       //   Java source line #122	-> byte code offset #74
/*     */       //   Java source line #123	-> byte code offset #82
/*     */       //   Java source line #122	-> byte code offset #85
/*     */       //   Java source line #125	-> byte code offset #96
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	97	0	this	Fire<V>
/*     */       //   4	40	1	timeoutFuture	TimeoutFuture<V>
/*     */       //   14	73	2	delegate	ListenableFuture<V>
/*     */       //   85	10	3	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   43	74	85	finally
/*     */     }
/*     */   }
/*     */   
/*     */   protected void afterDone()
/*     */   {
/* 130 */     maybePropagateCancellation(this.delegateRef);
/*     */     
/* 132 */     Future<?> localTimer = this.timer;
/*     */     
/*     */ 
/*     */ 
/* 136 */     if (localTimer != null) {
/* 137 */       localTimer.cancel(false);
/*     */     }
/*     */     
/* 140 */     this.delegateRef = null;
/* 141 */     this.timer = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\TimeoutFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */