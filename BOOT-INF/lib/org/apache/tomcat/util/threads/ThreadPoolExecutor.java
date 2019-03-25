/*     */ package org.apache.tomcat.util.threads;
/*     */ 
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public class ThreadPoolExecutor
/*     */   extends java.util.concurrent.ThreadPoolExecutor
/*     */ {
/*  41 */   protected static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.threads.res");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private final AtomicInteger submittedCount = new AtomicInteger(0);
/*  50 */   private final AtomicLong lastContextStoppedTime = new AtomicLong(0L);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private final AtomicLong lastTimeThreadKilledItself = new AtomicLong(0L);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   private long threadRenewalDelay = 1000L;
/*     */   
/*     */   public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
/*  65 */     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
/*  66 */     prestartAllCoreThreads();
/*     */   }
/*     */   
/*     */   public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler)
/*     */   {
/*  71 */     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
/*  72 */     prestartAllCoreThreads();
/*     */   }
/*     */   
/*     */   public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
/*  76 */     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, new RejectHandler(null));
/*  77 */     prestartAllCoreThreads();
/*     */   }
/*     */   
/*     */   public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
/*  81 */     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new RejectHandler(null));
/*  82 */     prestartAllCoreThreads();
/*     */   }
/*     */   
/*     */   public long getThreadRenewalDelay() {
/*  86 */     return this.threadRenewalDelay;
/*     */   }
/*     */   
/*     */   public void setThreadRenewalDelay(long threadRenewalDelay) {
/*  90 */     this.threadRenewalDelay = threadRenewalDelay;
/*     */   }
/*     */   
/*     */   protected void afterExecute(Runnable r, Throwable t)
/*     */   {
/*  95 */     this.submittedCount.decrementAndGet();
/*     */     
/*  97 */     if (t == null) {
/*  98 */       stopCurrentThreadIfNeeded();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void stopCurrentThreadIfNeeded()
/*     */   {
/* 107 */     if (currentThreadShouldBeStopped()) {
/* 108 */       long lastTime = this.lastTimeThreadKilledItself.longValue();
/* 109 */       if ((lastTime + this.threadRenewalDelay < System.currentTimeMillis()) && 
/* 110 */         (this.lastTimeThreadKilledItself.compareAndSet(lastTime, 
/* 111 */         System.currentTimeMillis() + 1L)))
/*     */       {
/*     */ 
/* 114 */         String msg = sm.getString("threadPoolExecutor.threadStoppedToAvoidPotentialLeak", new Object[] {
/*     */         
/* 116 */           Thread.currentThread().getName() });
/*     */         
/* 118 */         throw new StopPooledThreadException(msg);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean currentThreadShouldBeStopped()
/*     */   {
/* 125 */     if ((this.threadRenewalDelay >= 0L) && 
/* 126 */       ((Thread.currentThread() instanceof TaskThread))) {
/* 127 */       TaskThread currentTaskThread = (TaskThread)Thread.currentThread();
/*     */       
/* 129 */       if (currentTaskThread.getCreationTime() < this.lastContextStoppedTime.longValue()) {
/* 130 */         return true;
/*     */       }
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   public int getSubmittedCount() {
/* 137 */     return this.submittedCount.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void execute(Runnable command)
/*     */   {
/* 145 */     execute(command, 0L, TimeUnit.MILLISECONDS);
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
/*     */   public void execute(Runnable command, long timeout, TimeUnit unit)
/*     */   {
/* 165 */     this.submittedCount.incrementAndGet();
/*     */     try {
/* 167 */       super.execute(command);
/*     */     } catch (RejectedExecutionException rx) {
/* 169 */       if ((super.getQueue() instanceof TaskQueue)) {
/* 170 */         TaskQueue queue = (TaskQueue)super.getQueue();
/*     */         try {
/* 172 */           if (!queue.force(command, timeout, unit)) {
/* 173 */             this.submittedCount.decrementAndGet();
/* 174 */             throw new RejectedExecutionException("Queue capacity is full.");
/*     */           }
/*     */         } catch (InterruptedException x) {
/* 177 */           this.submittedCount.decrementAndGet();
/* 178 */           throw new RejectedExecutionException(x);
/*     */         }
/*     */       } else {
/* 181 */         this.submittedCount.decrementAndGet();
/* 182 */         throw rx;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void contextStopping()
/*     */   {
/* 189 */     this.lastContextStoppedTime.set(System.currentTimeMillis());
/*     */     
/*     */ 
/* 192 */     int savedCorePoolSize = getCorePoolSize();
/*     */     
/* 194 */     TaskQueue taskQueue = (getQueue() instanceof TaskQueue) ? (TaskQueue)getQueue() : null;
/* 195 */     if (taskQueue != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 200 */       taskQueue.setForcedRemainingCapacity(Integer.valueOf(0));
/*     */     }
/*     */     
/*     */ 
/* 204 */     setCorePoolSize(0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */     if (taskQueue != null)
/*     */     {
/* 212 */       taskQueue.setForcedRemainingCapacity(null);
/*     */     }
/* 214 */     setCorePoolSize(savedCorePoolSize);
/*     */   }
/*     */   
/*     */   private static class RejectHandler implements RejectedExecutionHandler
/*     */   {
/*     */     public void rejectedExecution(Runnable r, java.util.concurrent.ThreadPoolExecutor executor)
/*     */     {
/* 221 */       throw new RejectedExecutionException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\threads\ThreadPoolExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */