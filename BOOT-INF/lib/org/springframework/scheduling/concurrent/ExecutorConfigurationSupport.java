/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ public abstract class ExecutorConfigurationSupport
/*     */   extends CustomizableThreadFactory
/*     */   implements BeanNameAware, InitializingBean, DisposableBean
/*     */ {
/*  48 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  50 */   private ThreadFactory threadFactory = this;
/*     */   
/*  52 */   private boolean threadNamePrefixSet = false;
/*     */   
/*  54 */   private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
/*     */   
/*  56 */   private boolean waitForTasksToCompleteOnShutdown = false;
/*     */   
/*  58 */   private int awaitTerminationSeconds = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String beanName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ExecutorService executor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThreadFactory(ThreadFactory threadFactory)
/*     */   {
/*  80 */     this.threadFactory = (threadFactory != null ? threadFactory : this);
/*     */   }
/*     */   
/*     */   public void setThreadNamePrefix(String threadNamePrefix)
/*     */   {
/*  85 */     super.setThreadNamePrefix(threadNamePrefix);
/*  86 */     this.threadNamePrefixSet = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler)
/*     */   {
/*  95 */     this.rejectedExecutionHandler = (rejectedExecutionHandler != null ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy());
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
/*     */   public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown)
/*     */   {
/* 115 */     this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAwaitTerminationSeconds(int awaitTerminationSeconds)
/*     */   {
/* 142 */     this.awaitTerminationSeconds = awaitTerminationSeconds;
/*     */   }
/*     */   
/*     */   public void setBeanName(String name)
/*     */   {
/* 147 */     this.beanName = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 157 */     initialize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize()
/*     */   {
/* 164 */     if (this.logger.isInfoEnabled()) {
/* 165 */       this.logger.info("Initializing ExecutorService " + (this.beanName != null ? " '" + this.beanName + "'" : ""));
/*     */     }
/* 167 */     if ((!this.threadNamePrefixSet) && (this.beanName != null)) {
/* 168 */       setThreadNamePrefix(this.beanName + "-");
/*     */     }
/* 170 */     this.executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
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
/*     */   protected abstract ExecutorService initializeExecutor(ThreadFactory paramThreadFactory, RejectedExecutionHandler paramRejectedExecutionHandler);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 192 */     shutdown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 201 */     if (this.logger.isInfoEnabled()) {
/* 202 */       this.logger.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
/*     */     }
/* 204 */     if (this.executor != null) {
/* 205 */       if (this.waitForTasksToCompleteOnShutdown) {
/* 206 */         this.executor.shutdown();
/*     */       }
/*     */       else {
/* 209 */         this.executor.shutdownNow();
/*     */       }
/* 211 */       awaitTerminationIfNecessary(this.executor);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void awaitTerminationIfNecessary(ExecutorService executor)
/*     */   {
/* 220 */     if (this.awaitTerminationSeconds > 0) {
/*     */       try {
/* 222 */         if ((!executor.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS)) && 
/* 223 */           (this.logger.isWarnEnabled())) {
/* 224 */           this.logger.warn("Timed out while waiting for executor" + (this.beanName != null ? " '" + this.beanName + "'" : "") + " to terminate");
/*     */         }
/*     */         
/*     */       }
/*     */       catch (InterruptedException ex)
/*     */       {
/* 230 */         if (this.logger.isWarnEnabled()) {
/* 231 */           this.logger.warn("Interrupted while waiting for executor" + (this.beanName != null ? " '" + this.beanName + "'" : "") + " to terminate");
/*     */         }
/*     */         
/* 234 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\concurrent\ExecutorConfigurationSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */