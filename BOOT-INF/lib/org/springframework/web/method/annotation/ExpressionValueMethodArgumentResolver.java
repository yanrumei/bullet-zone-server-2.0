/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public class ExpressionValueMethodArgumentResolver
/*    */   extends AbstractNamedValueMethodArgumentResolver
/*    */ {
/*    */   public ExpressionValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory)
/*    */   {
/* 48 */     super(beanFactory);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 54 */     return parameter.hasParameterAnnotation(Value.class);
/*    */   }
/*    */   
/*    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/*    */   {
/* 59 */     Value annotation = (Value)parameter.getParameterAnnotation(Value.class);
/* 60 */     return new ExpressionValueNamedValueInfo(annotation, null);
/*    */   }
/*    */   
/*    */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException
/*    */   {
/* 71 */     throw new UnsupportedOperationException("@Value is never required: " + parameter.getMethod());
/*    */   }
/*    */   
/*    */   private static class ExpressionValueNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*    */   {
/*    */     private ExpressionValueNamedValueInfo(Value annotation)
/*    */     {
/* 78 */       super(false, annotation.value());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\ExpressionValueMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */