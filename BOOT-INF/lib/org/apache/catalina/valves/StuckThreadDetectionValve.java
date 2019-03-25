/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class StuckThreadDetectionValve
/*     */   extends ValveBase
/*     */ {
/*  49 */   private static final Log log = LogFactory.getLog(StuckThreadDetectionValve.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.valves");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   private final AtomicInteger stuckCount = new AtomicInteger(0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  65 */   private AtomicLong interruptedThreadsCount = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  70 */   private int threshold = 600;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int interruptThreadThreshold;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */   private final Map<Long, MonitoredThread> activeThreads = new ConcurrentHashMap();
/*     */   
/*  85 */   private final Queue<CompletedStuckThread> completedStuckThreadsQueue = new ConcurrentLinkedQueue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThreshold(int threshold)
/*     */   {
/*  96 */     this.threshold = threshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getThreshold()
/*     */   {
/* 104 */     return this.threshold;
/*     */   }
/*     */   
/*     */   public int getInterruptThreadThreshold()
/*     */   {
/* 109 */     return this.interruptThreadThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInterruptThreadThreshold(int interruptThreadThreshold)
/*     */   {
/* 121 */     this.interruptThreadThreshold = interruptThreadThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StuckThreadDetectionValve()
/*     */   {
/* 128 */     super(true);
/*     */   }
/*     */   
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 134 */     super.initInternal();
/*     */     
/* 136 */     if (log.isDebugEnabled()) {
/* 137 */       log.debug("Monitoring stuck threads with threshold = " + this.threshold + " sec");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void notifyStuckThreadDetected(MonitoredThread monitoredThread, long activeTime, int numStuckThreads)
/*     */   {
/* 145 */     if (log.isWarnEnabled()) {
/* 146 */       String msg = sm.getString("stuckThreadDetectionValve.notifyStuckThreadDetected", new Object[] {monitoredThread
/*     */       
/* 148 */         .getThread().getName(), 
/* 149 */         Long.valueOf(activeTime), monitoredThread
/* 150 */         .getStartTime(), 
/* 151 */         Integer.valueOf(numStuckThreads), monitoredThread
/* 152 */         .getRequestUri(), 
/* 153 */         Integer.valueOf(this.threshold), 
/* 154 */         String.valueOf(monitoredThread.getThread().getId()) });
/*     */       
/*     */ 
/* 157 */       Throwable th = new Throwable();
/* 158 */       th.setStackTrace(monitoredThread.getThread().getStackTrace());
/* 159 */       log.warn(msg, th);
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyStuckThreadCompleted(CompletedStuckThread thread, int numStuckThreads)
/*     */   {
/* 165 */     if (log.isWarnEnabled()) {
/* 166 */       String msg = sm.getString("stuckThreadDetectionValve.notifyStuckThreadCompleted", new Object[] {thread
/*     */       
/* 168 */         .getName(), 
/* 169 */         Long.valueOf(thread.getTotalActiveTime()), 
/* 170 */         Integer.valueOf(numStuckThreads), 
/* 171 */         String.valueOf(thread.getId()) });
/*     */       
/*     */ 
/* 174 */       log.warn(msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 185 */     if (this.threshold <= 0)
/*     */     {
/* 187 */       getNext().invoke(request, response);
/* 188 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */     Long key = Long.valueOf(Thread.currentThread().getId());
/* 196 */     StringBuffer requestUrl = request.getRequestURL();
/* 197 */     if (request.getQueryString() != null) {
/* 198 */       requestUrl.append("?");
/* 199 */       requestUrl.append(request.getQueryString());
/*     */     }
/*     */     
/* 202 */     MonitoredThread monitoredThread = new MonitoredThread(Thread.currentThread(), requestUrl.toString(), this.interruptThreadThreshold > 0);
/* 203 */     this.activeThreads.put(key, monitoredThread);
/*     */     try
/*     */     {
/* 206 */       getNext().invoke(request, response);
/*     */     } finally {
/* 208 */       this.activeThreads.remove(key);
/* 209 */       if (monitoredThread.markAsDone() == MonitoredThreadState.STUCK) {
/* 210 */         if (monitoredThread.wasInterrupted()) {
/* 211 */           this.interruptedThreadsCount.incrementAndGet();
/*     */         }
/* 213 */         this.completedStuckThreadsQueue.add(new CompletedStuckThread(monitoredThread
/* 214 */           .getThread(), monitoredThread
/* 215 */           .getActiveTimeInMillis()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void backgroundProcess()
/*     */   {
/* 222 */     super.backgroundProcess();
/*     */     
/* 224 */     long thresholdInMillis = this.threshold * 1000L;
/*     */     
/*     */ 
/*     */ 
/* 228 */     for (MonitoredThread monitoredThread : this.activeThreads.values()) {
/* 229 */       long activeTime = monitoredThread.getActiveTimeInMillis();
/*     */       
/* 231 */       if ((activeTime >= thresholdInMillis) && (monitoredThread.markAsStuckIfStillRunning())) {
/* 232 */         int numStuckThreads = this.stuckCount.incrementAndGet();
/* 233 */         notifyStuckThreadDetected(monitoredThread, activeTime, numStuckThreads);
/*     */       }
/* 235 */       if ((this.interruptThreadThreshold > 0) && (activeTime >= this.interruptThreadThreshold * 1000L)) {
/* 236 */         monitoredThread.interruptIfStuck(this.interruptThreadThreshold);
/*     */       }
/*     */     }
/*     */     
/* 240 */     for (CompletedStuckThread completedStuckThread = (CompletedStuckThread)this.completedStuckThreadsQueue.poll(); 
/* 241 */         completedStuckThread != null; completedStuckThread = (CompletedStuckThread)this.completedStuckThreadsQueue.poll())
/*     */     {
/* 243 */       int numStuckThreads = this.stuckCount.decrementAndGet();
/* 244 */       notifyStuckThreadCompleted(completedStuckThread, numStuckThreads);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getStuckThreadCount() {
/* 249 */     return this.stuckCount.get();
/*     */   }
/*     */   
/*     */   public long[] getStuckThreadIds() {
/* 253 */     List<Long> idList = new ArrayList();
/* 254 */     for (MonitoredThread monitoredThread : this.activeThreads.values()) {
/* 255 */       if (monitoredThread.isMarkedAsStuck()) {
/* 256 */         idList.add(Long.valueOf(monitoredThread.getThread().getId()));
/*     */       }
/*     */     }
/*     */     
/* 260 */     long[] result = new long[idList.size()];
/* 261 */     for (int i = 0; i < result.length; i++) {
/* 262 */       result[i] = ((Long)idList.get(i)).longValue();
/*     */     }
/* 264 */     return result;
/*     */   }
/*     */   
/*     */   public String[] getStuckThreadNames() {
/* 268 */     List<String> nameList = new ArrayList();
/* 269 */     for (MonitoredThread monitoredThread : this.activeThreads.values()) {
/* 270 */       if (monitoredThread.isMarkedAsStuck()) {
/* 271 */         nameList.add(monitoredThread.getThread().getName());
/*     */       }
/*     */     }
/* 274 */     return (String[])nameList.toArray(new String[nameList.size()]);
/*     */   }
/*     */   
/*     */   public long getInterruptedThreadsCount() {
/* 278 */     return this.interruptedThreadsCount.get();
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MonitoredThread
/*     */   {
/*     */     private final Thread thread;
/*     */     
/*     */     private final String requestUri;
/*     */     
/*     */     private final long start;
/*     */     
/* 290 */     private final AtomicInteger state = new AtomicInteger(StuckThreadDetectionValve.MonitoredThreadState.RUNNING
/* 291 */       .ordinal());
/*     */     
/*     */ 
/*     */ 
/*     */     private final Semaphore interruptionSemaphore;
/*     */     
/*     */ 
/*     */ 
/*     */     private boolean interrupted;
/*     */     
/*     */ 
/*     */ 
/*     */     public MonitoredThread(Thread thread, String requestUri, boolean interruptible)
/*     */     {
/* 305 */       this.thread = thread;
/* 306 */       this.requestUri = requestUri;
/* 307 */       this.start = System.currentTimeMillis();
/* 308 */       if (interruptible) {
/* 309 */         this.interruptionSemaphore = new Semaphore(1);
/*     */       } else {
/* 311 */         this.interruptionSemaphore = null;
/*     */       }
/*     */     }
/*     */     
/*     */     public Thread getThread() {
/* 316 */       return this.thread;
/*     */     }
/*     */     
/*     */     public String getRequestUri() {
/* 320 */       return this.requestUri;
/*     */     }
/*     */     
/*     */     public long getActiveTimeInMillis() {
/* 324 */       return System.currentTimeMillis() - this.start;
/*     */     }
/*     */     
/*     */     public Date getStartTime() {
/* 328 */       return new Date(this.start);
/*     */     }
/*     */     
/*     */     public boolean markAsStuckIfStillRunning() {
/* 332 */       return this.state.compareAndSet(StuckThreadDetectionValve.MonitoredThreadState.RUNNING.ordinal(), StuckThreadDetectionValve.MonitoredThreadState.STUCK
/* 333 */         .ordinal());
/*     */     }
/*     */     
/*     */     public StuckThreadDetectionValve.MonitoredThreadState markAsDone() {
/* 337 */       int val = this.state.getAndSet(StuckThreadDetectionValve.MonitoredThreadState.DONE.ordinal());
/* 338 */       StuckThreadDetectionValve.MonitoredThreadState threadState = StuckThreadDetectionValve.MonitoredThreadState.values()[val];
/*     */       
/* 340 */       if ((threadState == StuckThreadDetectionValve.MonitoredThreadState.STUCK) && (this.interruptionSemaphore != null))
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/*     */ 
/* 347 */           this.interruptionSemaphore.acquire();
/*     */         } catch (InterruptedException e) {
/* 349 */           StuckThreadDetectionValve.log.debug("thread interrupted after the request is finished, ignoring", e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 357 */       return threadState;
/*     */     }
/*     */     
/*     */     boolean isMarkedAsStuck() {
/* 361 */       return this.state.get() == StuckThreadDetectionValve.MonitoredThreadState.STUCK.ordinal();
/*     */     }
/*     */     
/*     */     public boolean interruptIfStuck(long interruptThreadThreshold) {
/* 365 */       if ((!isMarkedAsStuck()) || (this.interruptionSemaphore == null) || 
/* 366 */         (!this.interruptionSemaphore.tryAcquire()))
/*     */       {
/*     */ 
/* 369 */         return false;
/*     */       }
/*     */       try {
/* 372 */         if (StuckThreadDetectionValve.log.isWarnEnabled()) {
/* 373 */           String msg = StuckThreadDetectionValve.sm.getString("stuckThreadDetectionValve.notifyStuckThreadInterrupted", new Object[] {
/*     */           
/* 375 */             getThread().getName(), 
/* 376 */             Long.valueOf(getActiveTimeInMillis()), 
/* 377 */             getStartTime(), getRequestUri(), 
/* 378 */             Long.valueOf(interruptThreadThreshold), 
/* 379 */             String.valueOf(getThread().getId()) });
/* 380 */           Throwable th = new Throwable();
/* 381 */           th.setStackTrace(getThread().getStackTrace());
/* 382 */           StuckThreadDetectionValve.log.warn(msg, th);
/*     */         }
/* 384 */         this.thread.interrupt();
/*     */       } finally {
/* 386 */         this.interrupted = true;
/* 387 */         this.interruptionSemaphore.release();
/*     */       }
/* 389 */       return true;
/*     */     }
/*     */     
/*     */     public boolean wasInterrupted() {
/* 393 */       return this.interrupted;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompletedStuckThread
/*     */   {
/*     */     private final String threadName;
/*     */     private final long threadId;
/*     */     private final long totalActiveTime;
/*     */     
/*     */     public CompletedStuckThread(Thread thread, long totalActiveTime) {
/* 404 */       this.threadName = thread.getName();
/* 405 */       this.threadId = thread.getId();
/* 406 */       this.totalActiveTime = totalActiveTime;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 410 */       return this.threadName;
/*     */     }
/*     */     
/*     */     public long getId() {
/* 414 */       return this.threadId;
/*     */     }
/*     */     
/*     */     public long getTotalActiveTime() {
/* 418 */       return this.totalActiveTime;
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum MonitoredThreadState {
/* 423 */     RUNNING,  STUCK,  DONE;
/*     */     
/*     */     private MonitoredThreadState() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\StuckThreadDetectionValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */