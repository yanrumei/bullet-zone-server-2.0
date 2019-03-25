/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.ui.Model;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
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
/*    */ 
/*    */ 
/*    */ public class ModelMethodProcessor
/*    */   implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 42 */     return Model.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */   
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*    */     throws Exception
/*    */   {
/* 49 */     return mavContainer.getModel();
/*    */   }
/*    */   
/*    */   public boolean supportsReturnType(MethodParameter returnType)
/*    */   {
/* 54 */     return Model.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */   
/*    */ 
/*    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 61 */     if (returnValue == null) {
/* 62 */       return;
/*    */     }
/* 64 */     if ((returnValue instanceof Model)) {
/* 65 */       mavContainer.addAllAttributes(((Model)returnValue).asMap());
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/* 70 */       throw new UnsupportedOperationException("Unexpected return type: " + returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\ModelMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */