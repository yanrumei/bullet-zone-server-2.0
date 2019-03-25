/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ 
/*    */ @Deprecated
/*    */ @UsesJava8
/*    */ public class CompletionStageReturnValueHandler
/*    */   implements AsyncHandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsReturnType(MethodParameter returnType)
/*    */   {
/* 46 */     return CompletionStage.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */   
/*    */   public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType)
/*    */   {
/* 51 */     return (returnValue != null) && ((returnValue instanceof CompletionStage));
/*    */   }
/*    */   
/*    */ 
/*    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 58 */     if (returnValue == null) {
/* 59 */       mavContainer.setRequestHandled(true);
/* 60 */       return;
/*    */     }
/*    */     
/* 63 */     final DeferredResult<Object> deferredResult = new DeferredResult();
/* 64 */     WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(deferredResult, new Object[] { mavContainer });
/*    */     
/*    */ 
/* 67 */     CompletionStage<Object> future = (CompletionStage)returnValue;
/* 68 */     future.thenAccept(new Consumer()
/*    */     {
/*    */       public void accept(Object result) {
/* 71 */         deferredResult.setResult(result);
/*    */       }
/* 73 */     });
/* 74 */     future.exceptionally(new Function()
/*    */     {
/*    */       public Object apply(Throwable ex) {
/* 77 */         deferredResult.setErrorResult(ex);
/* 78 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\CompletionStageReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */