/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.util.concurrent.ListenableFuture;
/*    */ import org.springframework.util.concurrent.ListenableFutureCallback;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.context.request.async.DeferredResult;
/*    */ import org.springframework.web.context.request.async.WebAsyncManager;
/*    */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*    */ import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*    */ @Deprecated
/*    */ public class ListenableFutureReturnValueHandler
/*    */   implements AsyncHandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsReturnType(MethodParameter returnType)
/*    */   {
/* 42 */     return ListenableFuture.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */   
/*    */   public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType)
/*    */   {
/* 47 */     return (returnValue != null) && ((returnValue instanceof ListenableFuture));
/*    */   }
/*    */   
/*    */ 
/*    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 54 */     if (returnValue == null) {
/* 55 */       mavContainer.setRequestHandled(true);
/* 56 */       return;
/*    */     }
/*    */     
/* 59 */     final DeferredResult<Object> deferredResult = new DeferredResult();
/* 60 */     WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(deferredResult, new Object[] { mavContainer });
/*    */     
/* 62 */     ListenableFuture<?> future = (ListenableFuture)returnValue;
/* 63 */     future.addCallback(new ListenableFutureCallback()
/*    */     {
/*    */       public void onSuccess(Object result) {
/* 66 */         deferredResult.setResult(result);
/*    */       }
/*    */       
/*    */       public void onFailure(Throwable ex) {
/* 70 */         deferredResult.setErrorResult(ex);
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ListenableFutureReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */