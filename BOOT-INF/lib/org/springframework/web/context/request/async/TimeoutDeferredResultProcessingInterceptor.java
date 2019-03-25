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
/*    */ public class TimeoutDeferredResultProcessingInterceptor
/*    */   extends DeferredResultProcessingInterceptorAdapter
/*    */ {
/*    */   public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> result)
/*    */     throws Exception
/*    */   {
/* 42 */     result.setErrorResult(new AsyncRequestTimeoutException());
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\async\TimeoutDeferredResultProcessingInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */