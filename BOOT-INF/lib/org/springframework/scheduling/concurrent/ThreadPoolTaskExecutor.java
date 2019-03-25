/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskDecorator;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ThreadPoolTaskExecutor
/*     */   extends ExecutorConfigurationSupport
/*     */   implements AsyncListenableTaskExecutor, SchedulingTaskExecutor
/*     */ {
/*  74 */   private final Object poolSizeMonitor = new Object();
/*     */   
/*  76 */   private int corePoolSize = 1;
/*     */   
/*  78 */   private int maxPoolSize = Integer.MAX_VALUE;
/*     */   
/*  80 */   private int keepAliveSeconds = 60;
/*     */   
/*  82 */   private int queueCapacity = Integer.MAX_VALUE;
/*     */   
/*  84 */   private boolean allowCoreThreadTimeOut = false;
/*     */   
/*     */ 
/*     */ 
/*     */   private TaskDecorator taskDecorator;
/*     */   
/*     */ 
/*     */   private ThreadPoolExecutor threadPoolExecutor;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCorePoolSize(int corePoolSize)
/*     */   {
/*  97 */     synchronized (this.poolSizeMonitor) {
/*  98 */       this.corePoolSize = corePoolSize;
/*  99 */       if (this.threadPoolExecutor != null) {
/* 100 */         this.threadPoolExecutor.setCorePoolSize(corePoolSize);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int getCorePoolSize()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 5	org/springframework/scheduling/concurrent/ThreadPoolTaskExecutor:poolSizeMonitor	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 6	org/springframework/scheduling/concurrent/ThreadPoolTaskExecutor:corePoolSize	I
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: ireturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #109	-> byte code offset #0
/*     */     //   Java source line #110	-> byte code offset #7
/*     */     //   Java source line #111	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	ThreadPoolTaskExecutor
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   public void setMaxPoolSize(int maxPoolSize)
/*     */   {
/* 120 */     synchronized (this.poolSizeMonitor) {
/* 121 */       this.maxPoolSize = maxPoolSize;
/* 122 */       if (this.threadPoolExecutor != null) {
/* 123 */         this.threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int getMaxPoolSize()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 5	org/springframework/scheduling/concurrent/ThreadPoolTaskExecutor:poolSizeMonitor	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 9	org/springframework/scheduling/concurrent/ThreadPoolTaskExecutor:maxPoolSize	I
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: ireturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #132	-> byte code offset #0
/*     */     //   Java source line #133	-> byte code offset #7
/*     */     //   Java source line #134	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	ThreadPoolTaskExecutor
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   public void setKeepAliveSeconds(int keepAliveSeconds)
/*     */   {
/* 143 */     synchronized (this.poolSizeMonitor) {
/* 144 */       this.keepAliveSeconds = keepAliveSeconds;
/* 145 */       if (this.threadPoolExecutor != null) {
/* 146 */         this.threadPoolExecutor.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int getKeepAliveSeconds()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 5	org/springframework/scheduling/concurrent/ThreadPoolTaskExecutor:poolSizeMonitor	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 10	org/springframework/scheduling/concurrent/ThreadPoolTaskExecutor:keepAliveSeconds	I
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: ireturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #155	-> byte code offset #0
/*     */     //   Java source line #156	-> byte code offset #7
/*     */     //   Java source line #157	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	ThreadPoolTaskExecutor
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   public void setQueueCapacity(int queueCapacity)
/*     */   {
/* 169 */     this.queueCapacity = queueCapacity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut)
/*     */   {
/* 180 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
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
/*     */   public void setTaskDecorator(TaskDecorator taskDecorator)
/*     */   {
/* 194 */     this.taskDecorator = taskDecorator;
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
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*     */   {
/* 208 */     BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
/*     */     ThreadPoolExecutor executor;
/*     */     ThreadPoolExecutor executor;
/* 211 */     if (this.taskDecorator != null) {
/* 212 */       executor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler)
/*     */       {
/*     */ 
/*     */         public void execute(Runnable command)
/*     */         {
/* 217 */           super.execute(ThreadPoolTaskExecutor.this.taskDecorator.decorate(command));
/*     */         }
/*     */         
/*     */       };
/*     */     } else {
/* 222 */       executor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 228 */     if (this.allowCoreThreadTimeOut) {
/* 229 */       executor.allowCoreThreadTimeOut(true);
/*     */     }
/*     */     
/* 232 */     this.threadPoolExecutor = executor;
/* 233 */     return executor;
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
/*     */   protected BlockingQueue<Runnable> createQueue(int queueCapacity)
/*     */   {
/* 246 */     if (queueCapacity > 0) {
/* 247 */       return new LinkedBlockingQueue(queueCapacity);
/*     */     }
/*     */     
/* 250 */     return new SynchronousQueue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ThreadPoolExecutor getThreadPoolExecutor()
/*     */     throws IllegalStateException
/*     */   {
/* 260 */     Assert.state(this.threadPoolExecutor != null, "ThreadPoolTaskExecutor not initialized");
/* 261 */     return this.threadPoolExecutor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPoolSize()
/*     */   {
/* 269 */     if (this.threadPoolExecutor == null)
/*     */     {
/* 271 */       return this.corePoolSize;
/*     */     }
/* 273 */     return this.threadPoolExecutor.getPoolSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getActiveCount()
/*     */   {
/* 281 */     if (this.threadPoolExecutor == null)
/*     */     {
/* 283 */       return 0;
/*     */     }
/* 285 */     return this.threadPoolExecutor.getActiveCount();
/*     */   }
/*     */   
/*     */ 
/*     */   public void execute(Runnable task)
/*     */   {
/* 291 */     Executor executor = getThreadPoolExecutor();
/*     */     try {
/* 293 */       executor.execute(task);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 296 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public void execute(Runnable task, long startTimeout)
/*     */   {
/* 302 */     execute(task);
/*     */   }
/*     */   
/*     */   public Future<?> submit(Runnable task)
/*     */   {
/* 307 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 309 */       return executor.submit(task);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 312 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task)
/*     */   {
/* 318 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 320 */       return executor.submit(task);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 323 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task)
/*     */   {
/* 329 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 331 */       ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 332 */       executor.execute(future);
/* 333 */       return future;
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 336 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task)
/*     */   {
/* 342 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 344 */       ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 345 */       executor.execute(future);
/* 346 */       return future;
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 349 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean prefersShortLivedTasks()
/*     */   {
/* 358 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\concurrent\ThreadPoolTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */