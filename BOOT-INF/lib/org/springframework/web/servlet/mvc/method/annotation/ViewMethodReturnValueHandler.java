/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*    */ import org.springframework.web.servlet.SmartView;
/*    */ import org.springframework.web.servlet.View;
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
/*    */ public class ViewMethodReturnValueHandler
/*    */   implements HandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsReturnType(MethodParameter returnType)
/*    */   {
/* 45 */     return View.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */   
/*    */ 
/*    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 52 */     if (returnValue == null) {
/* 53 */       return;
/*    */     }
/* 55 */     if ((returnValue instanceof View)) {
/* 56 */       View view = (View)returnValue;
/* 57 */       mavContainer.setView(view);
/* 58 */       if (((view instanceof SmartView)) && 
/* 59 */         (((SmartView)view).isRedirectView())) {
/* 60 */         mavContainer.setRedirectModelScenario(true);
/*    */       }
/*    */       
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/* 67 */       throw new UnsupportedOperationException("Unexpected return type: " + returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ViewMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */