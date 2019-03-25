/*     */ package org.springframework.core.task;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrencyThrottleSupport;
/*     */ import org.springframework.util.CustomizableThreadCreator;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureTask;
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
/*     */ public class SimpleAsyncTaskExecutor
/*     */   extends CustomizableThreadCreator
/*     */   implements AsyncListenableTaskExecutor, Serializable
/*     */ {
/*     */   public static final int UNBOUNDED_CONCURRENCY = -1;
/*     */   public static final int NO_CONCURRENCY = 0;
/*  67 */   private final ConcurrencyThrottleAdapter concurrencyThrottle = new ConcurrencyThrottleAdapter(null);
/*     */   
/*     */ 
/*     */ 
/*     */   private ThreadFactory threadFactory;
/*     */   
/*     */ 
/*     */ 
/*     */   private TaskDecorator taskDecorator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleAsyncTaskExecutor() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public SimpleAsyncTaskExecutor(String threadNamePrefix)
/*     */   {
/*  86 */     super(threadNamePrefix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleAsyncTaskExecutor(ThreadFactory threadFactory)
/*     */   {
/*  94 */     this.threadFactory = threadFactory;
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
/*     */   public void setThreadFactory(ThreadFactory threadFactory)
/*     */   {
/* 107 */     this.threadFactory = threadFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final ThreadFactory getThreadFactory()
/*     */   {
/* 114 */     return this.threadFactory;
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
/*     */   public final void setTaskDecorator(TaskDecorator taskDecorator)
/*     */   {
/* 128 */     this.taskDecorator = taskDecorator;
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
/*     */   public void setConcurrencyLimit(int concurrencyLimit)
/*     */   {
/* 142 */     this.concurrencyThrottle.setConcurrencyLimit(concurrencyLimit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int getConcurrencyLimit()
/*     */   {
/* 149 */     return this.concurrencyThrottle.getConcurrencyLimit();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isThrottleActive()
/*     */   {
/* 159 */     return this.concurrencyThrottle.isThrottleActive();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void execute(Runnable task)
/*     */   {
/* 170 */     execute(task, Long.MAX_VALUE);
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
/*     */   public void execute(Runnable task, long startTimeout)
/*     */   {
/* 184 */     Assert.notNull(task, "Runnable must not be null");
/* 185 */     Runnable taskToUse = this.taskDecorator != null ? this.taskDecorator.decorate(task) : task;
/* 186 */     if ((isThrottleActive()) && (startTimeout > 0L)) {
/* 187 */       this.concurrencyThrottle.beforeAccess();
/* 188 */       doExecute(new ConcurrencyThrottlingRunnable(taskToUse));
/*     */     }
/*     */     else {
/* 191 */       doExecute(taskToUse);
/*     */     }
/*     */   }
/*     */   
/*     */   public Future<?> submit(Runnable task)
/*     */   {
/* 197 */     FutureTask<Object> future = new FutureTask(task, null);
/* 198 */     execute(future, Long.MAX_VALUE);
/* 199 */     return future;
/*     */   }
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task)
/*     */   {
/* 204 */     FutureTask<T> future = new FutureTask(task);
/* 205 */     execute(future, Long.MAX_VALUE);
/* 206 */     return future;
/*     */   }
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task)
/*     */   {
/* 211 */     ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 212 */     execute(future, Long.MAX_VALUE);
/* 213 */     return future;
/*     */   }
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task)
/*     */   {
/* 218 */     ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 219 */     execute(future, Long.MAX_VALUE);
/* 220 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doExecute(Runnable task)
/*     */   {
/* 232 */     Thread thread = this.threadFactory != null ? this.threadFactory.newThread(task) : createThread(task);
/* 233 */     thread.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ConcurrencyThrottleAdapter
/*     */     extends ConcurrencyThrottleSupport
/*     */   {
/*     */     protected void beforeAccess()
/*     */     {
/* 246 */       super.beforeAccess();
/*     */     }
/*     */     
/*     */     protected void afterAccess()
/*     */     {
/* 251 */       super.afterAccess();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class ConcurrencyThrottlingRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private final Runnable target;
/*     */     
/*     */ 
/*     */     public ConcurrencyThrottlingRunnable(Runnable target)
/*     */     {
/* 265 */       this.target = target;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void run()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 3	org/springframework/core/task/SimpleAsyncTaskExecutor$ConcurrencyThrottlingRunnable:target	Ljava/lang/Runnable;
/*     */       //   4: invokeinterface 4 1 0
/*     */       //   9: aload_0
/*     */       //   10: getfield 1	org/springframework/core/task/SimpleAsyncTaskExecutor$ConcurrencyThrottlingRunnable:this$0	Lorg/springframework/core/task/SimpleAsyncTaskExecutor;
/*     */       //   13: invokestatic 5	org/springframework/core/task/SimpleAsyncTaskExecutor:access$100	(Lorg/springframework/core/task/SimpleAsyncTaskExecutor;)Lorg/springframework/core/task/SimpleAsyncTaskExecutor$ConcurrencyThrottleAdapter;
/*     */       //   16: invokevirtual 6	org/springframework/core/task/SimpleAsyncTaskExecutor$ConcurrencyThrottleAdapter:afterAccess	()V
/*     */       //   19: goto +16 -> 35
/*     */       //   22: astore_1
/*     */       //   23: aload_0
/*     */       //   24: getfield 1	org/springframework/core/task/SimpleAsyncTaskExecutor$ConcurrencyThrottlingRunnable:this$0	Lorg/springframework/core/task/SimpleAsyncTaskExecutor;
/*     */       //   27: invokestatic 5	org/springframework/core/task/SimpleAsyncTaskExecutor:access$100	(Lorg/springframework/core/task/SimpleAsyncTaskExecutor;)Lorg/springframework/core/task/SimpleAsyncTaskExecutor$ConcurrencyThrottleAdapter;
/*     */       //   30: invokevirtual 6	org/springframework/core/task/SimpleAsyncTaskExecutor$ConcurrencyThrottleAdapter:afterAccess	()V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #271	-> byte code offset #0
/*     */       //   Java source line #274	-> byte code offset #9
/*     */       //   Java source line #275	-> byte code offset #19
/*     */       //   Java source line #274	-> byte code offset #22
/*     */       //   Java source line #276	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	ConcurrencyThrottlingRunnable
/*     */       //   22	12	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	9	22	finally
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\task\SimpleAsyncTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */