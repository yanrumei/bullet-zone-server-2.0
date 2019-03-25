/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.enterprise.concurrent.LastExecution;
/*     */ import javax.enterprise.concurrent.ManagedScheduledExecutorService;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.support.SimpleTriggerContext;
/*     */ import org.springframework.scheduling.support.TaskUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class ConcurrentTaskScheduler
/*     */   extends ConcurrentTaskExecutor
/*     */   implements TaskScheduler
/*     */ {
/*     */   private static Class<?> managedScheduledExecutorServiceClass;
/*     */   private ScheduledExecutorService scheduledExecutor;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  71 */       managedScheduledExecutorServiceClass = ClassUtils.forName("javax.enterprise.concurrent.ManagedScheduledExecutorService", ConcurrentTaskScheduler.class
/*     */       
/*  73 */         .getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException ex)
/*     */     {
/*  77 */       managedScheduledExecutorServiceClass = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  83 */   private boolean enterpriseConcurrentScheduler = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ErrorHandler errorHandler;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConcurrentTaskScheduler()
/*     */   {
/*  95 */     setScheduledExecutor(null);
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
/*     */   public ConcurrentTaskScheduler(ScheduledExecutorService scheduledExecutor)
/*     */   {
/* 109 */     super(scheduledExecutor);
/* 110 */     setScheduledExecutor(scheduledExecutor);
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
/*     */   public ConcurrentTaskScheduler(Executor concurrentExecutor, ScheduledExecutorService scheduledExecutor)
/*     */   {
/* 125 */     super(concurrentExecutor);
/* 126 */     setScheduledExecutor(scheduledExecutor);
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
/*     */   public final void setScheduledExecutor(ScheduledExecutorService scheduledExecutor)
/*     */   {
/* 142 */     if (scheduledExecutor != null) {
/* 143 */       this.scheduledExecutor = scheduledExecutor;
/*     */       
/* 145 */       this.enterpriseConcurrentScheduler = ((managedScheduledExecutorServiceClass != null) && (managedScheduledExecutorServiceClass.isInstance(scheduledExecutor)));
/*     */     }
/*     */     else {
/* 148 */       this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
/* 149 */       this.enterpriseConcurrentScheduler = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler errorHandler)
/*     */   {
/* 157 */     Assert.notNull(errorHandler, "ErrorHandler must not be null");
/* 158 */     this.errorHandler = errorHandler;
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable task, org.springframework.scheduling.Trigger trigger)
/*     */   {
/*     */     try
/*     */     {
/* 165 */       if (this.enterpriseConcurrentScheduler) {
/* 166 */         return new EnterpriseConcurrentTriggerScheduler(null).schedule(decorateTask(task, true), trigger);
/*     */       }
/*     */       
/*     */ 
/* 170 */       ErrorHandler errorHandler = this.errorHandler != null ? this.errorHandler : TaskUtils.getDefaultErrorHandler(true);
/* 171 */       return new ReschedulingRunnable(task, trigger, this.scheduledExecutor, errorHandler).schedule();
/*     */     }
/*     */     catch (RejectedExecutionException ex)
/*     */     {
/* 175 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable task, Date startTime)
/*     */   {
/* 181 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 183 */       return this.scheduledExecutor.schedule(decorateTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 186 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period)
/*     */   {
/* 192 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 194 */       return this.scheduledExecutor.scheduleAtFixedRate(decorateTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 197 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period)
/*     */   {
/*     */     try {
/* 204 */       return this.scheduledExecutor.scheduleAtFixedRate(decorateTask(task, true), 0L, period, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 207 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay)
/*     */   {
/* 213 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 215 */       return this.scheduledExecutor.scheduleWithFixedDelay(decorateTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 218 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay)
/*     */   {
/*     */     try {
/* 225 */       return this.scheduledExecutor.scheduleWithFixedDelay(decorateTask(task, true), 0L, delay, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 228 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private Runnable decorateTask(Runnable task, boolean isRepeatingTask) {
/* 233 */     Runnable result = TaskUtils.decorateTaskWithErrorHandler(task, this.errorHandler, isRepeatingTask);
/* 234 */     if (this.enterpriseConcurrentScheduler) {
/* 235 */       result = ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(result, task.toString());
/*     */     }
/* 237 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private class EnterpriseConcurrentTriggerScheduler
/*     */   {
/*     */     private EnterpriseConcurrentTriggerScheduler() {}
/*     */     
/*     */ 
/*     */     public ScheduledFuture<?> schedule(Runnable task, final org.springframework.scheduling.Trigger trigger)
/*     */     {
/* 248 */       ManagedScheduledExecutorService executor = (ManagedScheduledExecutorService)ConcurrentTaskScheduler.this.scheduledExecutor;
/* 249 */       executor.schedule(task, new javax.enterprise.concurrent.Trigger()
/*     */       {
/*     */         public Date getNextRunTime(LastExecution le, Date taskScheduledTime) {
/* 252 */           return trigger.nextExecutionTime(le != null ? new SimpleTriggerContext(le
/* 253 */             .getScheduledStart(), le.getRunStart(), le.getRunEnd()) : new SimpleTriggerContext());
/*     */         }
/*     */         
/*     */         public boolean skipRun(LastExecution lastExecution, Date scheduledRunTime)
/*     */         {
/* 258 */           return false;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\concurrent\ConcurrentTaskScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */