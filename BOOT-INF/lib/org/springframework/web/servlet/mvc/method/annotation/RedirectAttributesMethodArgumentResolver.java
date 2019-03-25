/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.ui.ModelMap;
/*    */ import org.springframework.validation.DataBinder;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*    */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*    */ import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
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
/*    */ public class RedirectAttributesMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 49 */     return RedirectAttributes.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */   
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*    */     throws Exception
/*    */   {
/*    */     ModelMap redirectAttributes;
/*    */     ModelMap redirectAttributes;
/* 57 */     if (binderFactory != null) {
/* 58 */       DataBinder dataBinder = binderFactory.createBinder(webRequest, null, null);
/* 59 */       redirectAttributes = new RedirectAttributesModelMap(dataBinder);
/*    */     }
/*    */     else {
/* 62 */       redirectAttributes = new RedirectAttributesModelMap();
/*    */     }
/* 64 */     mavContainer.setRedirectModel(redirectAttributes);
/* 65 */     return redirectAttributes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\RedirectAttributesMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */