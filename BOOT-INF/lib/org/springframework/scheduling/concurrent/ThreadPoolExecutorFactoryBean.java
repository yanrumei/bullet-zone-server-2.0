/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadPoolExecutorFactoryBean
/*     */   extends ExecutorConfigurationSupport
/*     */   implements FactoryBean<ExecutorService>, InitializingBean, DisposableBean
/*     */ {
/*  59 */   private int corePoolSize = 1;
/*     */   
/*  61 */   private int maxPoolSize = Integer.MAX_VALUE;
/*     */   
/*  63 */   private int keepAliveSeconds = 60;
/*     */   
/*  65 */   private boolean allowCoreThreadTimeOut = false;
/*     */   
/*  67 */   private int queueCapacity = Integer.MAX_VALUE;
/*     */   
/*  69 */   private boolean exposeUnconfigurableExecutor = false;
/*     */   
/*     */ 
/*     */ 
/*     */   private ExecutorService exposedExecutor;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCorePoolSize(int corePoolSize)
/*     */   {
/*  79 */     this.corePoolSize = corePoolSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxPoolSize(int maxPoolSize)
/*     */   {
/*  87 */     this.maxPoolSize = maxPoolSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setKeepAliveSeconds(int keepAliveSeconds)
/*     */   {
/*  95 */     this.keepAliveSeconds = keepAliveSeconds;
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
/* 106 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setQueueCapacity(int queueCapacity)
/*     */   {
/* 118 */     this.queueCapacity = queueCapacity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposeUnconfigurableExecutor(boolean exposeUnconfigurableExecutor)
/*     */   {
/* 130 */     this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*     */   {
/* 138 */     BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
/* 139 */     ThreadPoolExecutor executor = createExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, queue, threadFactory, rejectedExecutionHandler);
/*     */     
/* 141 */     if (this.allowCoreThreadTimeOut) {
/* 142 */       executor.allowCoreThreadTimeOut(true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 147 */     this.exposedExecutor = (this.exposeUnconfigurableExecutor ? Executors.unconfigurableExecutorService(executor) : executor);
/*     */     
/* 149 */     return executor;
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
/*     */   protected ThreadPoolExecutor createExecutor(int corePoolSize, int maxPoolSize, int keepAliveSeconds, BlockingQueue<Runnable> queue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*     */   {
/* 169 */     return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);
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
/*     */   protected BlockingQueue<Runnable> createQueue(int queueCapacity)
/*     */   {
/* 183 */     if (queueCapacity > 0) {
/* 184 */       return new LinkedBlockingQueue(queueCapacity);
/*     */     }
/*     */     
/* 187 */     return new SynchronousQueue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ExecutorService getObject()
/*     */   {
/* 194 */     return this.exposedExecutor;
/*     */   }
/*     */   
/*     */   public Class<? extends ExecutorService> getObjectType()
/*     */   {
/* 199 */     return this.exposedExecutor != null ? this.exposedExecutor.getClass() : ExecutorService.class;
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/* 204 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\concurrent\ThreadPoolExecutorFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */