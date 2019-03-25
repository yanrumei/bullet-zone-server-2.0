/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.server.ServerHttpRequest;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.method.ControllerAdviceBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RequestResponseBodyAdviceChain
/*     */   implements RequestBodyAdvice, ResponseBodyAdvice<Object>
/*     */ {
/*  44 */   private final List<Object> requestBodyAdvice = new ArrayList(4);
/*     */   
/*  46 */   private final List<Object> responseBodyAdvice = new ArrayList(4);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestResponseBodyAdviceChain(List<Object> requestResponseBodyAdvice)
/*     */   {
/*  54 */     initAdvice(requestResponseBodyAdvice);
/*     */   }
/*     */   
/*     */   private void initAdvice(List<Object> requestResponseBodyAdvice) {
/*  58 */     if (requestResponseBodyAdvice == null) {
/*  59 */       return;
/*     */     }
/*  61 */     for (Object advice : requestResponseBodyAdvice)
/*     */     {
/*  63 */       Class<?> beanType = (advice instanceof ControllerAdviceBean) ? ((ControllerAdviceBean)advice).getBeanType() : advice.getClass();
/*  64 */       if (RequestBodyAdvice.class.isAssignableFrom(beanType)) {
/*  65 */         this.requestBodyAdvice.add(advice);
/*     */       }
/*  67 */       else if (ResponseBodyAdvice.class.isAssignableFrom(beanType)) {
/*  68 */         this.responseBodyAdvice.add(advice);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private List<Object> getAdvice(Class<?> adviceType) {
/*  74 */     if (RequestBodyAdvice.class == adviceType) {
/*  75 */       return this.requestBodyAdvice;
/*     */     }
/*  77 */     if (ResponseBodyAdvice.class == adviceType) {
/*  78 */       return this.responseBodyAdvice;
/*     */     }
/*     */     
/*  81 */     throw new IllegalArgumentException("Unexpected adviceType: " + adviceType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean supports(MethodParameter param, Type type, Class<? extends HttpMessageConverter<?>> converterType)
/*     */   {
/*  88 */     throw new UnsupportedOperationException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
/*     */   {
/*  93 */     throw new UnsupportedOperationException("Not implemented");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
/*     */   {
/* 100 */     for (RequestBodyAdvice advice : getMatchingAdvice(parameter, RequestBodyAdvice.class)) {
/* 101 */       if (advice.supports(parameter, targetType, converterType)) {
/* 102 */         body = advice.handleEmptyBody(body, inputMessage, parameter, targetType, converterType);
/*     */       }
/*     */     }
/* 105 */     return body;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpInputMessage beforeBodyRead(HttpInputMessage request, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
/*     */     throws IOException
/*     */   {
/* 112 */     for (RequestBodyAdvice advice : getMatchingAdvice(parameter, RequestBodyAdvice.class)) {
/* 113 */       if (advice.supports(parameter, targetType, converterType)) {
/* 114 */         request = advice.beforeBodyRead(request, parameter, targetType, converterType);
/*     */       }
/*     */     }
/* 117 */     return request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
/*     */   {
/* 124 */     for (RequestBodyAdvice advice : getMatchingAdvice(parameter, RequestBodyAdvice.class)) {
/* 125 */       if (advice.supports(parameter, targetType, converterType)) {
/* 126 */         body = advice.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
/*     */       }
/*     */     }
/* 129 */     return body;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response)
/*     */   {
/* 137 */     return processBody(body, returnType, contentType, converterType, request, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> Object processBody(Object body, MethodParameter returnType, MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response)
/*     */   {
/* 145 */     for (ResponseBodyAdvice<?> advice : getMatchingAdvice(returnType, ResponseBodyAdvice.class)) {
/* 146 */       if (advice.supports(returnType, converterType)) {
/* 147 */         body = advice.beforeBodyWrite(body, returnType, contentType, converterType, request, response);
/*     */       }
/*     */     }
/*     */     
/* 151 */     return body;
/*     */   }
/*     */   
/*     */   private <A> List<A> getMatchingAdvice(MethodParameter parameter, Class<? extends A> adviceType)
/*     */   {
/* 156 */     List<Object> availableAdvice = getAdvice(adviceType);
/* 157 */     if (CollectionUtils.isEmpty(availableAdvice)) {
/* 158 */       return Collections.emptyList();
/*     */     }
/* 160 */     List<A> result = new ArrayList(availableAdvice.size());
/* 161 */     for (Object advice : availableAdvice) {
/* 162 */       if ((advice instanceof ControllerAdviceBean)) {
/* 163 */         ControllerAdviceBean adviceBean = (ControllerAdviceBean)advice;
/* 164 */         if (adviceBean.isApplicableToBeanType(parameter.getContainingClass()))
/*     */         {
/*     */ 
/* 167 */           advice = adviceBean.resolveBean();
/*     */         }
/* 169 */       } else if (adviceType.isAssignableFrom(advice.getClass())) {
/* 170 */         result.add(advice);
/*     */       }
/*     */     }
/* 173 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\RequestResponseBodyAdviceChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */