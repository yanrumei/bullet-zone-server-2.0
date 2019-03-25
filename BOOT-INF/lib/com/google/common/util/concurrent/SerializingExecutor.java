/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
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
/*     */ @GwtIncompatible
/*     */ final class SerializingExecutor
/*     */   implements Executor
/*     */ {
/*  48 */   private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
/*     */   
/*     */   private final Executor executor;
/*     */   
/*     */   @GuardedBy("queue")
/*  53 */   private final Deque<Runnable> queue = new ArrayDeque();
/*     */   
/*     */   @GuardedBy("queue")
/*  56 */   private boolean isWorkerRunning = false;
/*     */   
/*     */   @GuardedBy("queue")
/*  59 */   private int suspensions = 0;
/*     */   
/*     */ 
/*  62 */   private final QueueWorker worker = new QueueWorker(null);
/*     */   
/*     */   public SerializingExecutor(Executor executor) {
/*  65 */     this.executor = ((Executor)Preconditions.checkNotNull(executor));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void execute(Runnable task)
/*     */   {
/*  77 */     synchronized (this.queue) {
/*  78 */       this.queue.addLast(task);
/*  79 */       if ((this.isWorkerRunning) || (this.suspensions > 0)) {
/*  80 */         return;
/*     */       }
/*  82 */       this.isWorkerRunning = true;
/*     */     }
/*  84 */     startQueueWorker();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void executeFirst(Runnable task)
/*     */   {
/*  92 */     synchronized (this.queue) {
/*  93 */       this.queue.addFirst(task);
/*  94 */       if ((this.isWorkerRunning) || (this.suspensions > 0)) {
/*  95 */         return;
/*     */       }
/*  97 */       this.isWorkerRunning = true;
/*     */     }
/*  99 */     startQueueWorker();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void suspend()
/*     */   {
/* 111 */     synchronized (this.queue) {
/* 112 */       this.suspensions += 1;
/*     */     }
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
/*     */   public void resume()
/*     */   {
/* 128 */     synchronized (this.queue) {
/* 129 */       Preconditions.checkState(this.suspensions > 0);
/* 130 */       this.suspensions -= 1;
/* 131 */       if ((this.isWorkerRunning) || (this.suspensions > 0) || (this.queue.isEmpty())) {
/* 132 */         return;
/*     */       }
/* 134 */       this.isWorkerRunning = true;
/*     */     }
/* 136 */     startQueueWorker();
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
/*     */   private void startQueueWorker()
/*     */   {
/* 150 */     boolean executionRejected = true;
/*     */     try {
/* 152 */       this.executor.execute(this.worker);
/* 153 */       executionRejected = false;
/*     */     } finally {
/* 155 */       if (executionRejected)
/*     */       {
/*     */ 
/* 158 */         synchronized (this.queue) {
/* 159 */           this.isWorkerRunning = false;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class QueueWorker implements Runnable
/*     */   {
/*     */     private QueueWorker() {}
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/* 173 */         workOnQueue();
/*     */       } catch (Error e) {
/* 175 */         synchronized (SerializingExecutor.this.queue) {
/* 176 */           SerializingExecutor.this.isWorkerRunning = false;
/*     */         }
/* 178 */         throw e;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private void workOnQueue()
/*     */     {
/*     */       for (;;)
/*     */       {
/* 187 */         Runnable task = null;
/* 188 */         synchronized (SerializingExecutor.this.queue)
/*     */         {
/* 190 */           if (SerializingExecutor.this.suspensions == 0) {
/* 191 */             task = (Runnable)SerializingExecutor.this.queue.pollFirst();
/*     */           }
/* 193 */           if (task == null) {
/* 194 */             SerializingExecutor.this.isWorkerRunning = false;
/* 195 */             return;
/*     */           }
/*     */         }
/*     */         try {
/* 199 */           task.run();
/*     */         } catch (RuntimeException e) {
/* 201 */           SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + task, e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\SerializingExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */