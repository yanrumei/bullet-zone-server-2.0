/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.catalina.Executor;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.util.LifecycleMBeanBase;
/*     */ import org.apache.tomcat.util.threads.ResizableExecutor;
/*     */ import org.apache.tomcat.util.threads.TaskQueue;
/*     */ import org.apache.tomcat.util.threads.TaskThreadFactory;
/*     */ import org.apache.tomcat.util.threads.ThreadPoolExecutor;
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
/*     */ public class StandardThreadExecutor
/*     */   extends LifecycleMBeanBase
/*     */   implements Executor, ResizableExecutor
/*     */ {
/*  39 */   protected int threadPriority = 5;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  44 */   protected boolean daemon = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   protected String namePrefix = "tomcat-exec-";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   protected int maxThreads = 200;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   protected int minSpareThreads = 25;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   protected int maxIdleTime = 60000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   protected ThreadPoolExecutor executor = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  79 */   protected boolean prestartminSpareThreads = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  84 */   protected int maxQueueSize = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */   protected long threadRenewalDelay = 1000L;
/*     */   
/*     */ 
/*  94 */   private TaskQueue taskqueue = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 105 */     super.initInternal();
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
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 119 */     this.taskqueue = new TaskQueue(this.maxQueueSize);
/* 120 */     TaskThreadFactory tf = new TaskThreadFactory(this.namePrefix, this.daemon, getThreadPriority());
/* 121 */     this.executor = new ThreadPoolExecutor(getMinSpareThreads(), getMaxThreads(), this.maxIdleTime, TimeUnit.MILLISECONDS, this.taskqueue, tf);
/* 122 */     this.executor.setThreadRenewalDelay(this.threadRenewalDelay);
/* 123 */     if (this.prestartminSpareThreads) {
/* 124 */       this.executor.prestartAllCoreThreads();
/*     */     }
/* 126 */     this.taskqueue.setParent(this.executor);
/*     */     
/* 128 */     setState(LifecycleState.STARTING);
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
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 142 */     setState(LifecycleState.STOPPING);
/* 143 */     if (this.executor != null) this.executor.shutdownNow();
/* 144 */     this.executor = null;
/* 145 */     this.taskqueue = null;
/*     */   }
/*     */   
/*     */   protected void destroyInternal()
/*     */     throws LifecycleException
/*     */   {
/* 151 */     super.destroyInternal();
/*     */   }
/*     */   
/*     */ 
/*     */   public void execute(Runnable command, long timeout, TimeUnit unit)
/*     */   {
/* 157 */     if (this.executor != null) {
/* 158 */       this.executor.execute(command, timeout, unit);
/*     */     } else {
/* 160 */       throw new IllegalStateException("StandardThreadExecutor not started.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void execute(Runnable command)
/*     */   {
/* 167 */     if (this.executor != null)
/*     */       try {
/* 169 */         this.executor.execute(command);
/*     */       }
/*     */       catch (RejectedExecutionException rx) {
/* 172 */         if (!((TaskQueue)this.executor.getQueue()).force(command)) throw new RejectedExecutionException("Work queue full.");
/*     */       } else
/* 174 */       throw new IllegalStateException("StandardThreadPool not started.");
/*     */   }
/*     */   
/*     */   public void contextStopping() {
/* 178 */     if (this.executor != null) {
/* 179 */       this.executor.contextStopping();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getThreadPriority() {
/* 184 */     return this.threadPriority;
/*     */   }
/*     */   
/*     */   public boolean isDaemon()
/*     */   {
/* 189 */     return this.daemon;
/*     */   }
/*     */   
/*     */   public String getNamePrefix() {
/* 193 */     return this.namePrefix;
/*     */   }
/*     */   
/*     */   public int getMaxIdleTime() {
/* 197 */     return this.maxIdleTime;
/*     */   }
/*     */   
/*     */   public int getMaxThreads()
/*     */   {
/* 202 */     return this.maxThreads;
/*     */   }
/*     */   
/*     */   public int getMinSpareThreads() {
/* 206 */     return this.minSpareThreads;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 211 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isPrestartminSpareThreads()
/*     */   {
/* 216 */     return this.prestartminSpareThreads;
/*     */   }
/*     */   
/* 219 */   public void setThreadPriority(int threadPriority) { this.threadPriority = threadPriority; }
/*     */   
/*     */   public void setDaemon(boolean daemon)
/*     */   {
/* 223 */     this.daemon = daemon;
/*     */   }
/*     */   
/*     */   public void setNamePrefix(String namePrefix) {
/* 227 */     this.namePrefix = namePrefix;
/*     */   }
/*     */   
/*     */   public void setMaxIdleTime(int maxIdleTime) {
/* 231 */     this.maxIdleTime = maxIdleTime;
/* 232 */     if (this.executor != null) {
/* 233 */       this.executor.setKeepAliveTime(maxIdleTime, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setMaxThreads(int maxThreads) {
/* 238 */     this.maxThreads = maxThreads;
/* 239 */     if (this.executor != null) {
/* 240 */       this.executor.setMaximumPoolSize(maxThreads);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setMinSpareThreads(int minSpareThreads) {
/* 245 */     this.minSpareThreads = minSpareThreads;
/* 246 */     if (this.executor != null) {
/* 247 */       this.executor.setCorePoolSize(minSpareThreads);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPrestartminSpareThreads(boolean prestartminSpareThreads) {
/* 252 */     this.prestartminSpareThreads = prestartminSpareThreads;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 256 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void setMaxQueueSize(int size) {
/* 260 */     this.maxQueueSize = size;
/*     */   }
/*     */   
/*     */   public int getMaxQueueSize() {
/* 264 */     return this.maxQueueSize;
/*     */   }
/*     */   
/*     */   public long getThreadRenewalDelay() {
/* 268 */     return this.threadRenewalDelay;
/*     */   }
/*     */   
/*     */   public void setThreadRenewalDelay(long threadRenewalDelay) {
/* 272 */     this.threadRenewalDelay = threadRenewalDelay;
/* 273 */     if (this.executor != null) {
/* 274 */       this.executor.setThreadRenewalDelay(threadRenewalDelay);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getActiveCount()
/*     */   {
/* 281 */     return this.executor != null ? this.executor.getActiveCount() : 0;
/*     */   }
/*     */   
/*     */   public long getCompletedTaskCount() {
/* 285 */     return this.executor != null ? this.executor.getCompletedTaskCount() : 0L;
/*     */   }
/*     */   
/*     */   public int getCorePoolSize() {
/* 289 */     return this.executor != null ? this.executor.getCorePoolSize() : 0;
/*     */   }
/*     */   
/*     */   public int getLargestPoolSize() {
/* 293 */     return this.executor != null ? this.executor.getLargestPoolSize() : 0;
/*     */   }
/*     */   
/*     */   public int getPoolSize()
/*     */   {
/* 298 */     return this.executor != null ? this.executor.getPoolSize() : 0;
/*     */   }
/*     */   
/*     */   public int getQueueSize() {
/* 302 */     return this.executor != null ? this.executor.getQueue().size() : -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean resizePool(int corePoolSize, int maximumPoolSize)
/*     */   {
/* 308 */     if (this.executor == null) {
/* 309 */       return false;
/*     */     }
/* 311 */     this.executor.setCorePoolSize(corePoolSize);
/* 312 */     this.executor.setMaximumPoolSize(maximumPoolSize);
/* 313 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean resizeQueue(int capacity)
/*     */   {
/* 319 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getDomainInternal()
/*     */   {
/* 326 */     return null;
/*     */   }
/*     */   
/*     */   protected String getObjectNameKeyProperties()
/*     */   {
/* 331 */     StringBuilder name = new StringBuilder("type=Executor,name=");
/* 332 */     name.append(getName());
/* 333 */     return name.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardThreadExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */