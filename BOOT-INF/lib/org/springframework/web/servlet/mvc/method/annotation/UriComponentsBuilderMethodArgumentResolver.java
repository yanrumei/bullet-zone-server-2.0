/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*    */ import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
/*    */ import org.springframework.web.util.UriComponentsBuilder;
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
/*    */ public class UriComponentsBuilderMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 43 */     Class<?> type = parameter.getParameterType();
/* 44 */     return (UriComponentsBuilder.class == type) || (ServletUriComponentsBuilder.class == type);
/*    */   }
/*    */   
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*    */     throws Exception
/*    */   {
/* 51 */     HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 52 */     return ServletUriComponentsBuilder.fromServletMapping(request);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\UriComponentsBuilderMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */