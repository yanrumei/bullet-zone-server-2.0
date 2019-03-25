/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.bind.ServletRequestBindingException;
/*    */ import org.springframework.web.bind.annotation.CookieValue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCookieValueMethodArgumentResolver
/*    */   extends AbstractNamedValueMethodArgumentResolver
/*    */ {
/*    */   public AbstractCookieValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory)
/*    */   {
/* 48 */     super(beanFactory);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 54 */     return parameter.hasParameterAnnotation(CookieValue.class);
/*    */   }
/*    */   
/*    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/*    */   {
/* 59 */     CookieValue annotation = (CookieValue)parameter.getParameterAnnotation(CookieValue.class);
/* 60 */     return new CookieValueNamedValueInfo(annotation, null);
/*    */   }
/*    */   
/*    */   protected void handleMissingValue(String name, MethodParameter parameter)
/*    */     throws ServletRequestBindingException
/*    */   {
/* 66 */     throw new ServletRequestBindingException("Missing cookie '" + name + "' for method parameter of type " + parameter.getNestedParameterType().getSimpleName());
/*    */   }
/*    */   
/*    */   private static class CookieValueNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*    */   {
/*    */     private CookieValueNamedValueInfo(CookieValue annotation)
/*    */     {
/* 73 */       super(annotation.required(), annotation.defaultValue());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\AbstractCookieValueMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */