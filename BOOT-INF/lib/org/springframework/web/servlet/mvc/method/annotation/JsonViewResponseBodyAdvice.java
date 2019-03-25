/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonView;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.MappingJacksonValue;
/*    */ import org.springframework.http.server.ServerHttpRequest;
/*    */ import org.springframework.http.server.ServerHttpResponse;
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
/*    */ public class JsonViewResponseBodyAdvice
/*    */   extends AbstractMappingJacksonResponseBodyAdvice
/*    */ {
/*    */   public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
/*    */   {
/* 50 */     return (super.supports(returnType, converterType)) && (returnType.hasMethodAnnotation(JsonView.class));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response)
/*    */   {
/* 57 */     JsonView annotation = (JsonView)returnType.getMethodAnnotation(JsonView.class);
/* 58 */     Class<?>[] classes = annotation.value();
/* 59 */     if (classes.length != 1) {
/* 60 */       throw new IllegalArgumentException("@JsonView only supported for response body advice with exactly 1 class argument: " + returnType);
/*    */     }
/*    */     
/* 63 */     bodyContainer.setSerializationView(classes[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\JsonViewResponseBodyAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */