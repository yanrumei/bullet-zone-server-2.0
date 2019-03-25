/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ 
/*     */ public final class WebAsyncManager
/*     */ {
/*  61 */   private static final Object RESULT_NONE = new Object();
/*     */   
/*  63 */   private static final Log logger = LogFactory.getLog(WebAsyncManager.class);
/*     */   
/*  65 */   private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*  67 */   private static final CallableProcessingInterceptor timeoutCallableInterceptor = new TimeoutCallableProcessingInterceptor();
/*     */   
/*     */ 
/*  70 */   private static final DeferredResultProcessingInterceptor timeoutDeferredResultInterceptor = new TimeoutDeferredResultProcessingInterceptor();
/*     */   
/*     */ 
/*     */   private AsyncWebRequest asyncWebRequest;
/*     */   
/*     */ 
/*  76 */   private AsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(getClass().getSimpleName());
/*     */   
/*  78 */   private Object concurrentResult = RESULT_NONE;
/*     */   
/*     */   private Object[] concurrentResultContext;
/*     */   
/*  82 */   private final Map<Object, CallableProcessingInterceptor> callableInterceptors = new LinkedHashMap();
/*     */   
/*     */ 
/*  85 */   private final Map<Object, DeferredResultProcessingInterceptor> deferredResultInterceptors = new LinkedHashMap();
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
/*     */   public void setAsyncWebRequest(final AsyncWebRequest asyncWebRequest)
/*     */   {
/* 108 */     Assert.notNull(asyncWebRequest, "AsyncWebRequest must not be null");
/* 109 */     this.asyncWebRequest = asyncWebRequest;
/* 110 */     this.asyncWebRequest.addCompletionHandler(new Runnable()
/*     */     {
/*     */       public void run() {
/* 113 */         asyncWebRequest.removeAttribute(WebAsyncUtils.WEB_ASYNC_MANAGER_ATTRIBUTE, 0);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTaskExecutor(AsyncTaskExecutor taskExecutor)
/*     */   {
/* 124 */     this.taskExecutor = taskExecutor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConcurrentHandlingStarted()
/*     */   {
/* 136 */     return (this.asyncWebRequest != null) && (this.asyncWebRequest.isAsyncStarted());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasConcurrentResult()
/*     */   {
/* 143 */     return this.concurrentResult != RESULT_NONE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getConcurrentResult()
/*     */   {
/* 153 */     return this.concurrentResult;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] getConcurrentResultContext()
/*     */   {
/* 162 */     return this.concurrentResultContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallableProcessingInterceptor getCallableInterceptor(Object key)
/*     */   {
/* 171 */     return (CallableProcessingInterceptor)this.callableInterceptors.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeferredResultProcessingInterceptor getDeferredResultInterceptor(Object key)
/*     */   {
/* 180 */     return (DeferredResultProcessingInterceptor)this.deferredResultInterceptors.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerCallableInterceptor(Object key, CallableProcessingInterceptor interceptor)
/*     */   {
/* 189 */     Assert.notNull(key, "Key is required");
/* 190 */     Assert.notNull(interceptor, "CallableProcessingInterceptor  is required");
/* 191 */     this.callableInterceptors.put(key, interceptor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerCallableInterceptors(CallableProcessingInterceptor... interceptors)
/*     */   {
/* 200 */     Assert.notNull(interceptors, "A CallableProcessingInterceptor is required");
/* 201 */     for (CallableProcessingInterceptor interceptor : interceptors) {
/* 202 */       String key = interceptor.getClass().getName() + ":" + interceptor.hashCode();
/* 203 */       this.callableInterceptors.put(key, interceptor);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerDeferredResultInterceptor(Object key, DeferredResultProcessingInterceptor interceptor)
/*     */   {
/* 213 */     Assert.notNull(key, "Key is required");
/* 214 */     Assert.notNull(interceptor, "DeferredResultProcessingInterceptor is required");
/* 215 */     this.deferredResultInterceptors.put(key, interceptor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerDeferredResultInterceptors(DeferredResultProcessingInterceptor... interceptors)
/*     */   {
/* 224 */     Assert.notNull(interceptors, "A DeferredResultProcessingInterceptor is required");
/* 225 */     for (DeferredResultProcessingInterceptor interceptor : interceptors) {
/* 226 */       String key = interceptor.getClass().getName() + ":" + interceptor.hashCode();
/* 227 */       this.deferredResultInterceptors.put(key, interceptor);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearConcurrentResult()
/*     */   {
/* 236 */     this.concurrentResult = RESULT_NONE;
/* 237 */     this.concurrentResultContext = null;
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
/*     */   public void startCallableProcessing(Callable<?> callable, Object... processingContext)
/*     */     throws Exception
/*     */   {
/* 255 */     Assert.notNull(callable, "Callable must not be null");
/* 256 */     startCallableProcessing(new WebAsyncTask(callable), processingContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startCallableProcessing(WebAsyncTask<?> webAsyncTask, Object... processingContext)
/*     */     throws Exception
/*     */   {
/* 269 */     Assert.notNull(webAsyncTask, "WebAsyncTask must not be null");
/* 270 */     Assert.state(this.asyncWebRequest != null, "AsyncWebRequest must not be null");
/*     */     
/* 272 */     Long timeout = webAsyncTask.getTimeout();
/* 273 */     if (timeout != null) {
/* 274 */       this.asyncWebRequest.setTimeout(timeout);
/*     */     }
/*     */     
/* 277 */     AsyncTaskExecutor executor = webAsyncTask.getExecutor();
/* 278 */     if (executor != null) {
/* 279 */       this.taskExecutor = executor;
/*     */     }
/*     */     
/* 282 */     List<CallableProcessingInterceptor> interceptors = new ArrayList();
/* 283 */     interceptors.add(webAsyncTask.getInterceptor());
/* 284 */     interceptors.addAll(this.callableInterceptors.values());
/* 285 */     interceptors.add(timeoutCallableInterceptor);
/*     */     
/* 287 */     final Callable<?> callable = webAsyncTask.getCallable();
/* 288 */     final CallableInterceptorChain interceptorChain = new CallableInterceptorChain(interceptors);
/*     */     
/* 290 */     this.asyncWebRequest.addTimeoutHandler(new Runnable()
/*     */     {
/*     */       public void run() {
/* 293 */         WebAsyncManager.logger.debug("Processing timeout");
/* 294 */         Object result = interceptorChain.triggerAfterTimeout(WebAsyncManager.this.asyncWebRequest, callable);
/* 295 */         if (result != CallableProcessingInterceptor.RESULT_NONE) {
/* 296 */           WebAsyncManager.this.setConcurrentResultAndDispatch(result);
/*     */         }
/*     */       }
/*     */     });
/*     */     
/* 301 */     if ((this.asyncWebRequest instanceof StandardServletAsyncWebRequest)) {
/* 302 */       ((StandardServletAsyncWebRequest)this.asyncWebRequest).setErrorHandler(new StandardServletAsyncWebRequest.ErrorHandler()
/*     */       {
/*     */         public void handle(Throwable ex)
/*     */         {
/* 306 */           WebAsyncManager.this.setConcurrentResultAndDispatch(ex);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 311 */     this.asyncWebRequest.addCompletionHandler(new Runnable()
/*     */     {
/*     */       public void run() {
/* 314 */         interceptorChain.triggerAfterCompletion(WebAsyncManager.this.asyncWebRequest, callable);
/*     */       }
/*     */       
/* 317 */     });
/* 318 */     interceptorChain.applyBeforeConcurrentHandling(this.asyncWebRequest, callable);
/* 319 */     startAsyncProcessing(processingContext);
/*     */     try {
/* 321 */       Future<?> future = this.taskExecutor.submit(new Runnable()
/*     */       {
/*     */         public void run() {
/* 324 */           Object result = null;
/*     */           try {
/* 326 */             interceptorChain.applyPreProcess(WebAsyncManager.this.asyncWebRequest, callable);
/* 327 */             result = callable.call();
/*     */           }
/*     */           catch (Throwable ex) {
/* 330 */             result = ex;
/*     */           }
/*     */           finally {
/* 333 */             result = interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, callable, result);
/*     */           }
/* 335 */           WebAsyncManager.this.setConcurrentResultAndDispatch(result);
/*     */         }
/* 337 */       });
/* 338 */       interceptorChain.setTaskFuture(future);
/*     */     }
/*     */     catch (RejectedExecutionException ex) {
/* 341 */       Object result = interceptorChain.applyPostProcess(this.asyncWebRequest, callable, ex);
/* 342 */       setConcurrentResultAndDispatch(result);
/* 343 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */   private void setConcurrentResultAndDispatch(Object result) {
/* 348 */     synchronized (this) {
/* 349 */       if (hasConcurrentResult()) {
/* 350 */         return;
/*     */       }
/* 352 */       this.concurrentResult = result;
/*     */     }
/*     */     
/* 355 */     if (this.asyncWebRequest.isAsyncComplete()) {
/* 356 */       logger.error("Could not complete async processing due to timeout or network error");
/* 357 */       return;
/*     */     }
/*     */     
/* 360 */     if (logger.isDebugEnabled()) {
/* 361 */       logger.debug("Concurrent result value [" + this.concurrentResult + "] - dispatching request to resume processing");
/*     */     }
/*     */     
/*     */ 
/* 365 */     this.asyncWebRequest.dispatch();
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
/*     */   public void startDeferredResultProcessing(final DeferredResult<?> deferredResult, Object... processingContext)
/*     */     throws Exception
/*     */   {
/* 385 */     Assert.notNull(deferredResult, "DeferredResult must not be null");
/* 386 */     Assert.state(this.asyncWebRequest != null, "AsyncWebRequest must not be null");
/*     */     
/* 388 */     Long timeout = deferredResult.getTimeoutValue();
/* 389 */     if (timeout != null) {
/* 390 */       this.asyncWebRequest.setTimeout(timeout);
/*     */     }
/*     */     
/* 393 */     List<DeferredResultProcessingInterceptor> interceptors = new ArrayList();
/* 394 */     interceptors.add(deferredResult.getInterceptor());
/* 395 */     interceptors.addAll(this.deferredResultInterceptors.values());
/* 396 */     interceptors.add(timeoutDeferredResultInterceptor);
/*     */     
/* 398 */     final DeferredResultInterceptorChain interceptorChain = new DeferredResultInterceptorChain(interceptors);
/*     */     
/* 400 */     this.asyncWebRequest.addTimeoutHandler(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/* 404 */           interceptorChain.triggerAfterTimeout(WebAsyncManager.this.asyncWebRequest, deferredResult);
/*     */         }
/*     */         catch (Throwable ex) {
/* 407 */           WebAsyncManager.this.setConcurrentResultAndDispatch(ex);
/*     */         }
/*     */       }
/*     */     });
/*     */     
/* 412 */     if ((this.asyncWebRequest instanceof StandardServletAsyncWebRequest)) {
/* 413 */       ((StandardServletAsyncWebRequest)this.asyncWebRequest).setErrorHandler(new StandardServletAsyncWebRequest.ErrorHandler()
/*     */       {
/*     */         public void handle(Throwable ex)
/*     */         {
/* 417 */           deferredResult.setErrorResult(ex);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 422 */     this.asyncWebRequest.addCompletionHandler(new Runnable()
/*     */     {
/*     */       public void run() {
/* 425 */         interceptorChain.triggerAfterCompletion(WebAsyncManager.this.asyncWebRequest, deferredResult);
/*     */       }
/*     */       
/* 428 */     });
/* 429 */     interceptorChain.applyBeforeConcurrentHandling(this.asyncWebRequest, deferredResult);
/* 430 */     startAsyncProcessing(processingContext);
/*     */     try
/*     */     {
/* 433 */       interceptorChain.applyPreProcess(this.asyncWebRequest, deferredResult);
/* 434 */       deferredResult.setResultHandler(new DeferredResult.DeferredResultHandler()
/*     */       {
/*     */         public void handleResult(Object result) {
/* 437 */           result = interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, deferredResult, result);
/* 438 */           WebAsyncManager.this.setConcurrentResultAndDispatch(result);
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (Throwable ex) {
/* 443 */       setConcurrentResultAndDispatch(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void startAsyncProcessing(Object[] processingContext) {
/* 448 */     clearConcurrentResult();
/* 449 */     this.concurrentResultContext = processingContext;
/* 450 */     this.asyncWebRequest.startAsync();
/*     */     
/* 452 */     if (logger.isDebugEnabled()) {
/* 453 */       HttpServletRequest request = (HttpServletRequest)this.asyncWebRequest.getNativeRequest(HttpServletRequest.class);
/* 454 */       String requestUri = urlPathHelper.getRequestUri(request);
/* 455 */       logger.debug("Concurrent handling starting for " + request.getMethod() + " [" + requestUri + "]");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\async\WebAsyncManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */