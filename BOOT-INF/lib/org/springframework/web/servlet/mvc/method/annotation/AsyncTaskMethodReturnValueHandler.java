/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.context.request.async.WebAsyncManager;
/*    */ import org.springframework.web.context.request.async.WebAsyncTask;
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
/*    */ public class AsyncTaskMethodReturnValueHandler
/*    */   implements AsyncHandlerMethodReturnValueHandler
/*    */ {
/*    */   private final BeanFactory beanFactory;
/*    */   
/*    */   public AsyncTaskMethodReturnValueHandler(BeanFactory beanFactory)
/*    */   {
/* 39 */     this.beanFactory = beanFactory;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean supportsReturnType(MethodParameter returnType)
/*    */   {
/* 45 */     return WebAsyncTask.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */   
/*    */   public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType)
/*    */   {
/* 50 */     return (returnValue != null) && ((returnValue instanceof WebAsyncTask));
/*    */   }
/*    */   
/*    */ 
/*    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 57 */     if (returnValue == null) {
/* 58 */       mavContainer.setRequestHandled(true);
/* 59 */       return;
/*    */     }
/*    */     
/* 62 */     WebAsyncTask<?> webAsyncTask = (WebAsyncTask)returnValue;
/* 63 */     webAsyncTask.setBeanFactory(this.beanFactory);
/* 64 */     WebAsyncUtils.getAsyncManager(webRequest).startCallableProcessing(webAsyncTask, new Object[] { mavContainer });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\AsyncTaskMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */