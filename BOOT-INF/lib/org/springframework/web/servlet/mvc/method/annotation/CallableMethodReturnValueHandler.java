/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public class CallableMethodReturnValueHandler
/*    */   implements AsyncHandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsReturnType(MethodParameter returnType)
/*    */   {
/* 37 */     return Callable.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */   
/*    */   public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType)
/*    */   {
/* 42 */     return (returnValue != null) && ((returnValue instanceof Callable));
/*    */   }
/*    */   
/*    */ 
/*    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 49 */     if (returnValue == null) {
/* 50 */       mavContainer.setRequestHandled(true);
/* 51 */       return;
/*    */     }
/*    */     
/* 54 */     Callable<?> callable = (Callable)returnValue;
/* 55 */     WebAsyncUtils.getAsyncManager(webRequest).startCallableProcessing(callable, new Object[] { mavContainer });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\CallableMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */