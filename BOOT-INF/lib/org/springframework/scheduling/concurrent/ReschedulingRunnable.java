/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
/*     */ import org.springframework.scheduling.support.SimpleTriggerContext;
/*     */ import org.springframework.util.ErrorHandler;
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
/*     */ class ReschedulingRunnable
/*     */   extends DelegatingErrorHandlingRunnable
/*     */   implements ScheduledFuture<Object>
/*     */ {
/*     */   private final Trigger trigger;
/*  48 */   private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();
/*     */   
/*     */   private final ScheduledExecutorService executor;
/*     */   
/*     */   private ScheduledFuture<?> currentFuture;
/*     */   
/*     */   private Date scheduledExecutionTime;
/*     */   
/*  56 */   private final Object triggerContextMonitor = new Object();
/*     */   
/*     */   public ReschedulingRunnable(Runnable delegate, Trigger trigger, ScheduledExecutorService executor, ErrorHandler errorHandler)
/*     */   {
/*  60 */     super(delegate, errorHandler);
/*  61 */     this.trigger = trigger;
/*  62 */     this.executor = executor;
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> schedule()
/*     */   {
/*  67 */     synchronized (this.triggerContextMonitor) {
/*  68 */       this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
/*  69 */       if (this.scheduledExecutionTime == null) {
/*  70 */         return null;
/*     */       }
/*  72 */       long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
/*  73 */       this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
/*  74 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  80 */     Date actualExecutionTime = new Date();
/*  81 */     super.run();
/*  82 */     Date completionTime = new Date();
/*  83 */     synchronized (this.triggerContextMonitor) {
/*  84 */       this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
/*  85 */       if (!this.currentFuture.isCancelled()) {
/*  86 */         schedule();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean cancel(boolean mayInterruptIfRunning)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	org/springframework/scheduling/concurrent/ReschedulingRunnable:triggerContextMonitor	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 16	org/springframework/scheduling/concurrent/ReschedulingRunnable:currentFuture	Ljava/util/concurrent/ScheduledFuture;
/*     */     //   11: iload_1
/*     */     //   12: invokeinterface 23 2 0
/*     */     //   17: aload_2
/*     */     //   18: monitorexit
/*     */     //   19: ireturn
/*     */     //   20: astore_3
/*     */     //   21: aload_2
/*     */     //   22: monitorexit
/*     */     //   23: aload_3
/*     */     //   24: athrow
/*     */     // Line number table:
/*     */     //   Java source line #94	-> byte code offset #0
/*     */     //   Java source line #95	-> byte code offset #7
/*     */     //   Java source line #96	-> byte code offset #20
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	25	0	this	ReschedulingRunnable
/*     */     //   0	25	1	mayInterruptIfRunning	boolean
/*     */     //   5	17	2	Ljava/lang/Object;	Object
/*     */     //   20	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	19	20	finally
/*     */     //   20	23	20	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean isCancelled()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	org/springframework/scheduling/concurrent/ReschedulingRunnable:triggerContextMonitor	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 16	org/springframework/scheduling/concurrent/ReschedulingRunnable:currentFuture	Ljava/util/concurrent/ScheduledFuture;
/*     */     //   11: invokeinterface 21 1 0
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: ireturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #101	-> byte code offset #0
/*     */     //   Java source line #102	-> byte code offset #7
/*     */     //   Java source line #103	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	this	ReschedulingRunnable
/*     */     //   5	16	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	18	19	finally
/*     */     //   19	22	19	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean isDone()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	org/springframework/scheduling/concurrent/ReschedulingRunnable:triggerContextMonitor	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 16	org/springframework/scheduling/concurrent/ReschedulingRunnable:currentFuture	Ljava/util/concurrent/ScheduledFuture;
/*     */     //   11: invokeinterface 24 1 0
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: ireturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #108	-> byte code offset #0
/*     */     //   Java source line #109	-> byte code offset #7
/*     */     //   Java source line #110	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	this	ReschedulingRunnable
/*     */     //   5	16	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	18	19	finally
/*     */     //   19	22	19	finally
/*     */   }
/*     */   
/*     */   public Object get()
/*     */     throws InterruptedException, ExecutionException
/*     */   {
/*     */     ScheduledFuture<?> curr;
/* 116 */     synchronized (this.triggerContextMonitor) {
/* 117 */       curr = this.currentFuture; }
/*     */     ScheduledFuture<?> curr;
/* 119 */     return curr.get();
/*     */   }
/*     */   
/*     */   public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/*     */     ScheduledFuture<?> curr;
/* 125 */     synchronized (this.triggerContextMonitor) {
/* 126 */       curr = this.currentFuture; }
/*     */     ScheduledFuture<?> curr;
/* 128 */     return curr.get(timeout, unit);
/*     */   }
/*     */   
/*     */   public long getDelay(TimeUnit unit)
/*     */   {
/*     */     ScheduledFuture<?> curr;
/* 134 */     synchronized (this.triggerContextMonitor) {
/* 135 */       curr = this.currentFuture; }
/*     */     ScheduledFuture<?> curr;
/* 137 */     return curr.getDelay(unit);
/*     */   }
/*     */   
/*     */   public int compareTo(Delayed other)
/*     */   {
/* 142 */     if (this == other) {
/* 143 */       return 0;
/*     */     }
/* 145 */     long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
/* 146 */     return diff < 0L ? -1 : diff == 0L ? 0 : 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\concurrent\ReschedulingRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */