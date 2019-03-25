/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.RequestEntity;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpEntityMethodProcessor
/*     */   extends AbstractMessageConverterMethodProcessor
/*     */ {
/*     */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> converters)
/*     */   {
/*  68 */     super(converters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager)
/*     */   {
/*  79 */     super(converters, manager);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice)
/*     */   {
/*  91 */     super(converters, null, requestResponseBodyAdvice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice)
/*     */   {
/* 101 */     super(converters, manager, requestResponseBodyAdvice);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/* 107 */     return (HttpEntity.class == parameter.getParameterType()) || 
/* 108 */       (RequestEntity.class == parameter.getParameterType());
/*     */   }
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType)
/*     */   {
/* 113 */     return (HttpEntity.class.isAssignableFrom(returnType.getParameterType())) && 
/* 114 */       (!RequestEntity.class.isAssignableFrom(returnType.getParameterType()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*     */     throws IOException, HttpMediaTypeNotSupportedException
/*     */   {
/* 122 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/* 123 */     Type paramType = getHttpEntityType(parameter);
/* 124 */     if (paramType == null)
/*     */     {
/* 126 */       throw new IllegalArgumentException("HttpEntity parameter '" + parameter.getParameterName() + "' in method " + parameter.getMethod() + " is not parameterized");
/*     */     }
/*     */     
/* 129 */     Object body = readWithMessageConverters(webRequest, parameter, paramType);
/* 130 */     if (RequestEntity.class == parameter.getParameterType()) {
/* 131 */       return new RequestEntity(body, inputMessage.getHeaders(), inputMessage
/* 132 */         .getMethod(), inputMessage.getURI());
/*     */     }
/*     */     
/* 135 */     return new HttpEntity(body, inputMessage.getHeaders());
/*     */   }
/*     */   
/*     */   private Type getHttpEntityType(MethodParameter parameter)
/*     */   {
/* 140 */     Assert.isAssignable(HttpEntity.class, parameter.getParameterType());
/* 141 */     Type parameterType = parameter.getGenericParameterType();
/* 142 */     if ((parameterType instanceof ParameterizedType)) {
/* 143 */       ParameterizedType type = (ParameterizedType)parameterType;
/* 144 */       if (type.getActualTypeArguments().length != 1)
/*     */       {
/* 146 */         throw new IllegalArgumentException("Expected single generic parameter on '" + parameter.getParameterName() + "' in method " + parameter.getMethod());
/*     */       }
/* 148 */       return type.getActualTypeArguments()[0];
/*     */     }
/* 150 */     if ((parameterType instanceof Class)) {
/* 151 */       return Object.class;
/*     */     }
/*     */     
/* 154 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 162 */     mavContainer.setRequestHandled(true);
/* 163 */     if (returnValue == null) {
/* 164 */       return;
/*     */     }
/*     */     
/* 167 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/* 168 */     ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
/*     */     
/* 170 */     Assert.isInstanceOf(HttpEntity.class, returnValue);
/* 171 */     HttpEntity<?> responseEntity = (HttpEntity)returnValue;
/*     */     
/* 173 */     HttpHeaders outputHeaders = outputMessage.getHeaders();
/* 174 */     HttpHeaders entityHeaders = responseEntity.getHeaders();
/* 175 */     if (!entityHeaders.isEmpty()) {
/* 176 */       for (Map.Entry<String, List<String>> entry : entityHeaders.entrySet()) {
/* 177 */         if (("Vary".equals(entry.getKey())) && (outputHeaders.containsKey("Vary"))) {
/* 178 */           List<String> values = getVaryRequestHeadersToAdd(outputHeaders, entityHeaders);
/* 179 */           if (!values.isEmpty()) {
/* 180 */             outputHeaders.setVary(values);
/*     */           }
/*     */         }
/*     */         else {
/* 184 */           outputHeaders.put((String)entry.getKey(), (List)entry.getValue());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 189 */     if ((responseEntity instanceof ResponseEntity)) {
/* 190 */       int returnStatus = ((ResponseEntity)responseEntity).getStatusCodeValue();
/* 191 */       outputMessage.getServletResponse().setStatus(returnStatus);
/* 192 */       if ((returnStatus == 200) && 
/* 193 */         (isResourceNotModified(inputMessage, outputMessage)))
/*     */       {
/* 195 */         outputMessage.flush();
/*     */         
/* 197 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 203 */     writeWithMessageConverters(responseEntity.getBody(), returnType, inputMessage, outputMessage);
/*     */     
/*     */ 
/* 206 */     outputMessage.flush();
/*     */   }
/*     */   
/*     */   private List<String> getVaryRequestHeadersToAdd(HttpHeaders responseHeaders, HttpHeaders entityHeaders) {
/* 210 */     List<String> entityHeadersVary = entityHeaders.getVary();
/* 211 */     List<String> vary = responseHeaders.get("Vary");
/* 212 */     if (vary != null) {
/* 213 */       List<String> result = new ArrayList(entityHeadersVary);
/* 214 */       for (String header : vary) { String existing;
/* 215 */         for (existing : StringUtils.tokenizeToStringArray(header, ",")) {
/* 216 */           if ("*".equals(existing)) {
/* 217 */             return Collections.emptyList();
/*     */           }
/* 219 */           for (String value : entityHeadersVary) {
/* 220 */             if (value.equalsIgnoreCase(existing)) {
/* 221 */               result.remove(value);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 226 */       return result;
/*     */     }
/* 228 */     return entityHeadersVary;
/*     */   }
/*     */   
/*     */   private boolean isResourceNotModified(ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
/*     */   {
/* 233 */     ServletWebRequest servletWebRequest = new ServletWebRequest(inputMessage.getServletRequest(), outputMessage.getServletResponse());
/* 234 */     HttpHeaders responseHeaders = outputMessage.getHeaders();
/* 235 */     String etag = responseHeaders.getETag();
/* 236 */     long lastModifiedTimestamp = responseHeaders.getLastModified();
/* 237 */     if ((inputMessage.getMethod() == HttpMethod.GET) || (inputMessage.getMethod() == HttpMethod.HEAD)) {
/* 238 */       responseHeaders.remove("ETag");
/* 239 */       responseHeaders.remove("Last-Modified");
/*     */     }
/*     */     
/* 242 */     return servletWebRequest.checkNotModified(etag, lastModifiedTimestamp);
/*     */   }
/*     */   
/*     */   protected Class<?> getReturnValueType(Object returnValue, MethodParameter returnType)
/*     */   {
/* 247 */     if (returnValue != null) {
/* 248 */       return returnValue.getClass();
/*     */     }
/*     */     
/* 251 */     Type type = getHttpEntityType(returnType);
/* 252 */     type = type != null ? type : Object.class;
/* 253 */     return ResolvableType.forMethodParameter(returnType, type).resolve(Object.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\HttpEntityMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */