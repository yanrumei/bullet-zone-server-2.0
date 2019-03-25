/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonView;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.MappingJacksonInputMessage;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonViewRequestBodyAdvice
/*    */   extends RequestBodyAdviceAdapter
/*    */ {
/*    */   public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
/*    */   {
/* 56 */     return (AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType)) && 
/* 57 */       (methodParameter.getParameterAnnotation(JsonView.class) != null);
/*    */   }
/*    */   
/*    */ 
/*    */   public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType)
/*    */     throws IOException
/*    */   {
/* 64 */     JsonView annotation = (JsonView)methodParameter.getParameterAnnotation(JsonView.class);
/* 65 */     Class<?>[] classes = annotation.value();
/* 66 */     if (classes.length != 1) {
/* 67 */       throw new IllegalArgumentException("@JsonView only supported for request body advice with exactly 1 class argument: " + methodParameter);
/*    */     }
/*    */     
/* 70 */     return new MappingJacksonInputMessage(inputMessage.getBody(), inputMessage.getHeaders(), classes[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\JsonViewRequestBodyAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */