/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
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
/*    */ public abstract class AbstractMappingJacksonResponseBodyAdvice
/*    */   implements ResponseBodyAdvice<Object>
/*    */ {
/*    */   public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
/*    */   {
/* 40 */     return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response)
/*    */   {
/* 48 */     MappingJacksonValue container = getOrCreateContainer(body);
/* 49 */     beforeBodyWriteInternal(container, contentType, returnType, request, response);
/* 50 */     return container;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected MappingJacksonValue getOrCreateContainer(Object body)
/*    */   {
/* 58 */     return (body instanceof MappingJacksonValue) ? (MappingJacksonValue)body : new MappingJacksonValue(body);
/*    */   }
/*    */   
/*    */   protected abstract void beforeBodyWriteInternal(MappingJacksonValue paramMappingJacksonValue, MediaType paramMediaType, MethodParameter paramMethodParameter, ServerHttpRequest paramServerHttpRequest, ServerHttpResponse paramServerHttpResponse);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\AbstractMappingJacksonResponseBodyAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */