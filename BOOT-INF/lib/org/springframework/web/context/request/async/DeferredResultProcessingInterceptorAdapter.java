/*    */ package org.springframework.web.context.request.async;
/*    */ 
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DeferredResultProcessingInterceptorAdapter
/*    */   implements DeferredResultProcessingInterceptor
/*    */ {
/*    */   public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult, Object concurrentResult)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult)
/*    */     throws Exception
/*    */   {
/* 60 */     return true;
/*    */   }
/*    */   
/*    */   public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult)
/*    */     throws Exception
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\async\DeferredResultProcessingInterceptorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */