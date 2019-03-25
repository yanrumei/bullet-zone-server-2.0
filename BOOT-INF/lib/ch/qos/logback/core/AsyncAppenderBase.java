/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.spi.AppenderAttachable;
/*     */ import ch.qos.logback.core.spi.AppenderAttachableImpl;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
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
/*     */ public class AsyncAppenderBase<E>
/*     */   extends UnsynchronizedAppenderBase<E>
/*     */   implements AppenderAttachable<E>
/*     */ {
/*     */   AppenderAttachableImpl<E> aai;
/*     */   BlockingQueue<E> blockingQueue;
/*     */   public static final int DEFAULT_QUEUE_SIZE = 256;
/*     */   int queueSize;
/*     */   int appenderCount;
/*     */   static final int UNDEFINED = -1;
/*     */   int discardingThreshold;
/*     */   boolean neverBlock;
/*     */   AsyncAppenderBase<E>.Worker worker;
/*     */   public static final int DEFAULT_MAX_FLUSH_TIME = 1000;
/*     */   int maxFlushTime;
/*     */   
/*     */   public AsyncAppenderBase()
/*     */   {
/*  41 */     this.aai = new AppenderAttachableImpl();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */     this.queueSize = 256;
/*     */     
/*  50 */     this.appenderCount = 0;
/*     */     
/*     */ 
/*  53 */     this.discardingThreshold = -1;
/*  54 */     this.neverBlock = false;
/*     */     
/*  56 */     this.worker = new Worker();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */     this.maxFlushTime = 1000;
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
/*     */   protected boolean isDiscardable(E eventObject)
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void preprocess(E eventObject) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  91 */     if (isStarted())
/*  92 */       return;
/*  93 */     if (this.appenderCount == 0) {
/*  94 */       addError("No attached appenders found.");
/*  95 */       return;
/*     */     }
/*  97 */     if (this.queueSize < 1) {
/*  98 */       addError("Invalid queue size [" + this.queueSize + "]");
/*  99 */       return;
/*     */     }
/* 101 */     this.blockingQueue = new ArrayBlockingQueue(this.queueSize);
/*     */     
/* 103 */     if (this.discardingThreshold == -1)
/* 104 */       this.discardingThreshold = (this.queueSize / 5);
/* 105 */     addInfo("Setting discardingThreshold to " + this.discardingThreshold);
/* 106 */     this.worker.setDaemon(true);
/* 107 */     this.worker.setName("AsyncAppender-Worker-" + getName());
/*     */     
/* 109 */     super.start();
/* 110 */     this.worker.start();
/*     */   }
/*     */   
/*     */   public void stop()
/*     */   {
/* 115 */     if (!isStarted()) {
/* 116 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 121 */     super.stop();
/*     */     
/*     */ 
/*     */ 
/* 125 */     this.worker.interrupt();
/*     */     try {
/* 127 */       this.worker.join(this.maxFlushTime);
/*     */       
/*     */ 
/* 130 */       if (this.worker.isAlive()) {
/* 131 */         addWarn("Max queue flush timeout (" + this.maxFlushTime + " ms) exceeded. Approximately " + this.blockingQueue.size() + " queued events were possibly discarded.");
/*     */       }
/*     */       else {
/* 134 */         addInfo("Queue flush finished successfully within timeout.");
/*     */       }
/*     */     }
/*     */     catch (InterruptedException e) {
/* 138 */       addError("Failed to join worker thread. " + this.blockingQueue.size() + " queued events may be discarded.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void append(E eventObject)
/*     */   {
/* 144 */     if ((isQueueBelowDiscardingThreshold()) && (isDiscardable(eventObject))) {
/* 145 */       return;
/*     */     }
/* 147 */     preprocess(eventObject);
/* 148 */     put(eventObject);
/*     */   }
/*     */   
/*     */   private boolean isQueueBelowDiscardingThreshold() {
/* 152 */     return this.blockingQueue.remainingCapacity() < this.discardingThreshold;
/*     */   }
/*     */   
/*     */   private void put(E eventObject) {
/* 156 */     if (this.neverBlock) {
/* 157 */       this.blockingQueue.offer(eventObject);
/*     */     } else {
/*     */       try {
/* 160 */         this.blockingQueue.put(eventObject);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 164 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getQueueSize() {
/* 170 */     return this.queueSize;
/*     */   }
/*     */   
/*     */   public void setQueueSize(int queueSize) {
/* 174 */     this.queueSize = queueSize;
/*     */   }
/*     */   
/*     */   public int getDiscardingThreshold() {
/* 178 */     return this.discardingThreshold;
/*     */   }
/*     */   
/*     */   public void setDiscardingThreshold(int discardingThreshold) {
/* 182 */     this.discardingThreshold = discardingThreshold;
/*     */   }
/*     */   
/*     */   public int getMaxFlushTime() {
/* 186 */     return this.maxFlushTime;
/*     */   }
/*     */   
/*     */   public void setMaxFlushTime(int maxFlushTime) {
/* 190 */     this.maxFlushTime = maxFlushTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getNumberOfElementsInQueue()
/*     */   {
/* 199 */     return this.blockingQueue.size();
/*     */   }
/*     */   
/*     */   public void setNeverBlock(boolean neverBlock) {
/* 203 */     this.neverBlock = neverBlock;
/*     */   }
/*     */   
/*     */   public boolean isNeverBlock() {
/* 207 */     return this.neverBlock;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRemainingCapacity()
/*     */   {
/* 217 */     return this.blockingQueue.remainingCapacity();
/*     */   }
/*     */   
/*     */   public void addAppender(Appender<E> newAppender) {
/* 221 */     if (this.appenderCount == 0) {
/* 222 */       this.appenderCount += 1;
/* 223 */       addInfo("Attaching appender named [" + newAppender.getName() + "] to AsyncAppender.");
/* 224 */       this.aai.addAppender(newAppender);
/*     */     } else {
/* 226 */       addWarn("One and only one appender may be attached to AsyncAppender.");
/* 227 */       addWarn("Ignoring additional appender named [" + newAppender.getName() + "]");
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator<Appender<E>> iteratorForAppenders() {
/* 232 */     return this.aai.iteratorForAppenders();
/*     */   }
/*     */   
/*     */   public Appender<E> getAppender(String name) {
/* 236 */     return this.aai.getAppender(name);
/*     */   }
/*     */   
/*     */   public boolean isAttached(Appender<E> eAppender) {
/* 240 */     return this.aai.isAttached(eAppender);
/*     */   }
/*     */   
/*     */   public void detachAndStopAllAppenders() {
/* 244 */     this.aai.detachAndStopAllAppenders();
/*     */   }
/*     */   
/*     */   public boolean detachAppender(Appender<E> eAppender) {
/* 248 */     return this.aai.detachAppender(eAppender);
/*     */   }
/*     */   
/*     */ 
/* 252 */   public boolean detachAppender(String name) { return this.aai.detachAppender(name); }
/*     */   
/*     */   class Worker extends Thread {
/*     */     Worker() {}
/*     */     
/*     */     public void run() {
/* 258 */       AsyncAppenderBase<E> parent = AsyncAppenderBase.this;
/* 259 */       AppenderAttachableImpl<E> aai = parent.aai;
/*     */       for (;;)
/*     */       {
/* 262 */         if (parent.isStarted()) {
/*     */           try {
/* 264 */             E e = parent.blockingQueue.take();
/* 265 */             aai.appendLoopOnAppenders(e);
/*     */           }
/*     */           catch (InterruptedException ie) {}
/*     */         }
/*     */       }
/*     */       
/* 271 */       AsyncAppenderBase.this.addInfo("Worker thread will flush remaining events before exiting. ");
/*     */       
/* 273 */       for (E e : parent.blockingQueue) {
/* 274 */         aai.appendLoopOnAppenders(e);
/* 275 */         parent.blockingQueue.remove(e);
/*     */       }
/*     */       
/* 278 */       aai.detachAndStopAllAppenders();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\AsyncAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */