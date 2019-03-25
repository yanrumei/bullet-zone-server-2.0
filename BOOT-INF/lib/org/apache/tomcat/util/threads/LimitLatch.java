/*     */ package org.apache.tomcat.util.threads;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class LimitLatch
/*     */ {
/*  33 */   private static final Log log = LogFactory.getLog(LimitLatch.class);
/*     */   private final Sync sync;
/*     */   private final AtomicLong count;
/*     */   private volatile long limit;
/*     */   
/*     */   private class Sync extends AbstractQueuedSynchronizer {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public Sync() {}
/*     */     
/*  43 */     protected int tryAcquireShared(int ignored) { long newCount = LimitLatch.this.count.incrementAndGet();
/*  44 */       if ((!LimitLatch.this.released) && (newCount > LimitLatch.this.limit))
/*     */       {
/*  46 */         LimitLatch.this.count.decrementAndGet();
/*  47 */         return -1;
/*     */       }
/*  49 */       return 1;
/*     */     }
/*     */     
/*     */ 
/*     */     protected boolean tryReleaseShared(int arg)
/*     */     {
/*  55 */       LimitLatch.this.count.decrementAndGet();
/*  56 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private volatile boolean released = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LimitLatch(long limit)
/*     */   {
/*  70 */     this.limit = limit;
/*  71 */     this.count = new AtomicLong(0L);
/*  72 */     this.sync = new Sync();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getCount()
/*     */   {
/*  80 */     return this.count.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLimit()
/*     */   {
/*  88 */     return this.limit;
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
/*     */   public void setLimit(long limit)
/*     */   {
/* 104 */     this.limit = limit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void countUpOrAwait()
/*     */     throws InterruptedException
/*     */   {
/* 114 */     if (log.isDebugEnabled()) {
/* 115 */       log.debug("Counting up[" + Thread.currentThread().getName() + "] latch=" + getCount());
/*     */     }
/* 117 */     this.sync.acquireSharedInterruptibly(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long countDown()
/*     */   {
/* 125 */     this.sync.releaseShared(0);
/* 126 */     long result = getCount();
/* 127 */     if (log.isDebugEnabled()) {
/* 128 */       log.debug("Counting down[" + Thread.currentThread().getName() + "] latch=" + result);
/*     */     }
/* 130 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean releaseAll()
/*     */   {
/* 139 */     this.released = true;
/* 140 */     return this.sync.releaseShared(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 148 */     this.count.set(0L);
/* 149 */     this.released = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasQueuedThreads()
/*     */   {
/* 158 */     return this.sync.hasQueuedThreads();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Thread> getQueuedThreads()
/*     */   {
/* 167 */     return this.sync.getQueuedThreads();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\threads\LimitLatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */