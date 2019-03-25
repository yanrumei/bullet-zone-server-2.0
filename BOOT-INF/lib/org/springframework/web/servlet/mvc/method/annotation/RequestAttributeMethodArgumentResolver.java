/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.bind.ServletRequestBindingException;
/*    */ import org.springframework.web.bind.annotation.RequestAttribute;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
/*    */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.NamedValueInfo;
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
/*    */ public class RequestAttributeMethodArgumentResolver
/*    */   extends AbstractNamedValueMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 39 */     return parameter.hasParameterAnnotation(RequestAttribute.class);
/*    */   }
/*    */   
/*    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/*    */   {
/* 44 */     RequestAttribute ann = (RequestAttribute)parameter.getParameterAnnotation(RequestAttribute.class);
/* 45 */     return new AbstractNamedValueMethodArgumentResolver.NamedValueInfo(ann.name(), ann.required(), "\n\t\t\n\t\t\n\n\t\t\t\t\n");
/*    */   }
/*    */   
/*    */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request)
/*    */   {
/* 50 */     return request.getAttribute(name, 0);
/*    */   }
/*    */   
/*    */   protected void handleMissingValue(String name, MethodParameter parameter)
/*    */     throws ServletException
/*    */   {
/* 56 */     throw new ServletRequestBindingException("Missing request attribute '" + name + "' of type " + parameter.getNestedParameterType().getSimpleName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\RequestAttributeMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */