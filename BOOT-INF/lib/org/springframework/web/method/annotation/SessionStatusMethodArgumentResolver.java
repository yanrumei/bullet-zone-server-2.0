/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.bind.support.SessionStatus;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
/*    */ public class SessionStatusMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 37 */     return SessionStatus.class == parameter.getParameterType();
/*    */   }
/*    */   
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*    */     throws Exception
/*    */   {
/* 44 */     return mavContainer.getSessionStatus();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\SessionStatusMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */