/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.web.context.request.async.CallableProcessingInterceptor;
/*     */ import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
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
/*     */ public class AsyncSupportConfigurer
/*     */ {
/*     */   private AsyncTaskExecutor taskExecutor;
/*     */   private Long timeout;
/*  43 */   private final List<CallableProcessingInterceptor> callableInterceptors = new ArrayList();
/*     */   
/*     */ 
/*  46 */   private final List<DeferredResultProcessingInterceptor> deferredResultInterceptors = new ArrayList();
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
/*     */   public AsyncSupportConfigurer setTaskExecutor(AsyncTaskExecutor taskExecutor)
/*     */   {
/*  60 */     this.taskExecutor = taskExecutor;
/*  61 */     return this;
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
/*     */   public AsyncSupportConfigurer setDefaultTimeout(long timeout)
/*     */   {
/*  74 */     this.timeout = Long.valueOf(timeout);
/*  75 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AsyncSupportConfigurer registerCallableInterceptors(CallableProcessingInterceptor... interceptors)
/*     */   {
/*  85 */     this.callableInterceptors.addAll(Arrays.asList(interceptors));
/*  86 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AsyncSupportConfigurer registerDeferredResultInterceptors(DeferredResultProcessingInterceptor... interceptors)
/*     */   {
/*  95 */     this.deferredResultInterceptors.addAll(Arrays.asList(interceptors));
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   protected AsyncTaskExecutor getTaskExecutor()
/*     */   {
/* 101 */     return this.taskExecutor;
/*     */   }
/*     */   
/*     */   protected Long getTimeout() {
/* 105 */     return this.timeout;
/*     */   }
/*     */   
/*     */   protected List<CallableProcessingInterceptor> getCallableInterceptors() {
/* 109 */     return this.callableInterceptors;
/*     */   }
/*     */   
/*     */   protected List<DeferredResultProcessingInterceptor> getDeferredResultInterceptors() {
/* 113 */     return this.deferredResultInterceptors;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\AsyncSupportConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */