/*     */ package org.apache.tomcat.util.threads;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class TaskQueue
/*     */   extends LinkedBlockingQueue<Runnable>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  35 */   private volatile ThreadPoolExecutor parent = null;
/*     */   
/*     */ 
/*     */ 
/*  39 */   private Integer forcedRemainingCapacity = null;
/*     */   
/*     */ 
/*     */   public TaskQueue() {}
/*     */   
/*     */   public TaskQueue(int capacity)
/*     */   {
/*  46 */     super(capacity);
/*     */   }
/*     */   
/*     */   public TaskQueue(Collection<? extends Runnable> c) {
/*  50 */     super(c);
/*     */   }
/*     */   
/*     */   public void setParent(ThreadPoolExecutor tp) {
/*  54 */     this.parent = tp;
/*     */   }
/*     */   
/*     */   public boolean force(Runnable o) {
/*  58 */     if ((this.parent == null) || (this.parent.isShutdown())) throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
/*  59 */     return super.offer(o);
/*     */   }
/*     */   
/*     */   public boolean force(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
/*  63 */     if ((this.parent == null) || (this.parent.isShutdown())) throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
/*  64 */     return super.offer(o, timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean offer(Runnable o)
/*     */   {
/*  70 */     if (this.parent == null) { return super.offer(o);
/*     */     }
/*  72 */     if (this.parent.getPoolSize() == this.parent.getMaximumPoolSize()) { return super.offer(o);
/*     */     }
/*  74 */     if (this.parent.getSubmittedCount() < this.parent.getPoolSize()) { return super.offer(o);
/*     */     }
/*  76 */     if (this.parent.getPoolSize() < this.parent.getMaximumPoolSize()) { return false;
/*     */     }
/*  78 */     return super.offer(o);
/*     */   }
/*     */   
/*     */ 
/*     */   public Runnable poll(long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/*  85 */     Runnable runnable = (Runnable)super.poll(timeout, unit);
/*  86 */     if ((runnable == null) && (this.parent != null))
/*     */     {
/*     */ 
/*  89 */       this.parent.stopCurrentThreadIfNeeded();
/*     */     }
/*  91 */     return runnable;
/*     */   }
/*     */   
/*     */   public Runnable take() throws InterruptedException
/*     */   {
/*  96 */     if ((this.parent != null) && (this.parent.currentThreadShouldBeStopped())) {
/*  97 */       return poll(this.parent.getKeepAliveTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 103 */     return (Runnable)super.take();
/*     */   }
/*     */   
/*     */   public int remainingCapacity()
/*     */   {
/* 108 */     if (this.forcedRemainingCapacity != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 113 */       return this.forcedRemainingCapacity.intValue();
/*     */     }
/* 115 */     return super.remainingCapacity();
/*     */   }
/*     */   
/*     */   public void setForcedRemainingCapacity(Integer forcedRemainingCapacity) {
/* 119 */     this.forcedRemainingCapacity = forcedRemainingCapacity;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\threads\TaskQueue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */