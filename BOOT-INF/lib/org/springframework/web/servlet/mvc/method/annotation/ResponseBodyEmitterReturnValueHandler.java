/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.async.DeferredResult;
/*     */ import org.springframework.web.context.request.async.WebAsyncManager;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ import org.springframework.web.filter.ShallowEtagHeaderFilter;
/*     */ import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
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
/*     */ public class ResponseBodyEmitterReturnValueHandler
/*     */   implements AsyncHandlerMethodReturnValueHandler
/*     */ {
/*  58 */   private static final Log logger = LogFactory.getLog(ResponseBodyEmitterReturnValueHandler.class);
/*     */   
/*     */   private final List<HttpMessageConverter<?>> messageConverters;
/*     */   
/*     */   private final Map<Class<?>, ResponseBodyEmitterAdapter> adapterMap;
/*     */   
/*     */ 
/*     */   public ResponseBodyEmitterReturnValueHandler(List<HttpMessageConverter<?>> messageConverters)
/*     */   {
/*  67 */     Assert.notEmpty(messageConverters, "HttpMessageConverter List must not be empty");
/*  68 */     this.messageConverters = messageConverters;
/*  69 */     this.adapterMap = new HashMap(4);
/*  70 */     this.adapterMap.put(ResponseBodyEmitter.class, new SimpleResponseBodyEmitterAdapter(null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Map<Class<?>, ResponseBodyEmitterAdapter> getAdapterMap()
/*     */   {
/*  83 */     return this.adapterMap;
/*     */   }
/*     */   
/*     */   private ResponseBodyEmitterAdapter getAdapterFor(Class<?> type) {
/*  87 */     if (type != null) {
/*  88 */       for (Class<?> adapteeType : getAdapterMap().keySet()) {
/*  89 */         if (adapteeType.isAssignableFrom(type)) {
/*  90 */           return (ResponseBodyEmitterAdapter)getAdapterMap().get(adapteeType);
/*     */         }
/*     */       }
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType)
/*     */   {
/*     */     Class<?> bodyType;
/*     */     Class<?> bodyType;
/* 101 */     if (ResponseEntity.class.isAssignableFrom(returnType.getParameterType())) {
/* 102 */       bodyType = ResolvableType.forMethodParameter(returnType).getGeneric(new int[] { 0 }).resolve();
/*     */     }
/*     */     else {
/* 105 */       bodyType = returnType.getParameterType();
/*     */     }
/* 107 */     return getAdapterFor(bodyType) != null;
/*     */   }
/*     */   
/*     */   public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType)
/*     */   {
/* 112 */     if (returnValue != null) {
/* 113 */       Object adaptFrom = returnValue;
/* 114 */       if ((returnValue instanceof ResponseEntity)) {
/* 115 */         adaptFrom = ((ResponseEntity)returnValue).getBody();
/*     */       }
/* 117 */       if (adaptFrom != null) {
/* 118 */         return getAdapterFor(adaptFrom.getClass()) != null;
/*     */       }
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 128 */     if (returnValue == null) {
/* 129 */       mavContainer.setRequestHandled(true);
/* 130 */       return;
/*     */     }
/*     */     
/* 133 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 134 */     ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
/*     */     
/* 136 */     if ((returnValue instanceof ResponseEntity)) {
/* 137 */       ResponseEntity<?> responseEntity = (ResponseEntity)returnValue;
/* 138 */       response.setStatus(responseEntity.getStatusCodeValue());
/* 139 */       outputMessage.getHeaders().putAll(responseEntity.getHeaders());
/* 140 */       returnValue = responseEntity.getBody();
/* 141 */       if (returnValue == null) {
/* 142 */         mavContainer.setRequestHandled(true);
/* 143 */         outputMessage.flush();
/* 144 */         return;
/*     */       }
/*     */     }
/*     */     
/* 148 */     ServletRequest request = (ServletRequest)webRequest.getNativeRequest(ServletRequest.class);
/* 149 */     ShallowEtagHeaderFilter.disableContentCaching(request);
/*     */     
/* 151 */     ResponseBodyEmitterAdapter adapter = getAdapterFor(returnValue.getClass());
/* 152 */     if (adapter == null)
/*     */     {
/* 154 */       throw new IllegalStateException("Could not find ResponseBodyEmitterAdapter for return value type: " + returnValue.getClass());
/*     */     }
/* 156 */     ResponseBodyEmitter emitter = adapter.adaptToEmitter(returnValue, outputMessage);
/* 157 */     emitter.extendResponse(outputMessage);
/*     */     
/*     */ 
/* 160 */     outputMessage.getBody();
/* 161 */     outputMessage.flush();
/* 162 */     outputMessage = new StreamingServletServerHttpResponse(outputMessage);
/*     */     
/* 164 */     DeferredResult<?> deferredResult = new DeferredResult(emitter.getTimeout());
/* 165 */     WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(deferredResult, new Object[] { mavContainer });
/*     */     
/* 167 */     HttpMessageConvertingHandler handler = new HttpMessageConvertingHandler(outputMessage, deferredResult);
/* 168 */     emitter.initialize(handler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class SimpleResponseBodyEmitterAdapter
/*     */     implements ResponseBodyEmitterAdapter
/*     */   {
/*     */     public ResponseBodyEmitter adaptToEmitter(Object returnValue, ServerHttpResponse response)
/*     */     {
/* 179 */       Assert.isInstanceOf(ResponseBodyEmitter.class, returnValue, "ResponseBodyEmitter expected");
/* 180 */       return (ResponseBodyEmitter)returnValue;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class HttpMessageConvertingHandler
/*     */     implements ResponseBodyEmitter.Handler
/*     */   {
/*     */     private final ServerHttpResponse outputMessage;
/*     */     
/*     */     private final DeferredResult<?> deferredResult;
/*     */     
/*     */     public HttpMessageConvertingHandler(DeferredResult<?> outputMessage)
/*     */     {
/* 194 */       this.outputMessage = outputMessage;
/* 195 */       this.deferredResult = deferredResult;
/*     */     }
/*     */     
/*     */     public void send(Object data, MediaType mediaType) throws IOException
/*     */     {
/* 200 */       sendInternal(data, mediaType);
/*     */     }
/*     */     
/*     */     private <T> void sendInternal(T data, MediaType mediaType) throws IOException
/*     */     {
/* 205 */       for (HttpMessageConverter<?> converter : ResponseBodyEmitterReturnValueHandler.this.messageConverters) {
/* 206 */         if (converter.canWrite(data.getClass(), mediaType)) {
/* 207 */           converter.write(data, mediaType, this.outputMessage);
/* 208 */           this.outputMessage.flush();
/* 209 */           if (ResponseBodyEmitterReturnValueHandler.logger.isDebugEnabled()) {
/* 210 */             ResponseBodyEmitterReturnValueHandler.logger.debug("Written [" + data + "] using [" + converter + "]");
/*     */           }
/* 212 */           return;
/*     */         }
/*     */       }
/* 215 */       throw new IllegalArgumentException("No suitable converter for " + data.getClass());
/*     */     }
/*     */     
/*     */     public void complete()
/*     */     {
/* 220 */       this.deferredResult.setResult(null);
/*     */     }
/*     */     
/*     */     public void completeWithError(Throwable failure)
/*     */     {
/* 225 */       this.deferredResult.setErrorResult(failure);
/*     */     }
/*     */     
/*     */     public void onTimeout(Runnable callback)
/*     */     {
/* 230 */       this.deferredResult.onTimeout(callback);
/*     */     }
/*     */     
/*     */     public void onCompletion(Runnable callback)
/*     */     {
/* 235 */       this.deferredResult.onCompletion(callback);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class StreamingServletServerHttpResponse
/*     */     implements ServerHttpResponse
/*     */   {
/*     */     private final ServerHttpResponse delegate;
/*     */     
/*     */ 
/* 248 */     private final HttpHeaders mutableHeaders = new HttpHeaders();
/*     */     
/*     */     public StreamingServletServerHttpResponse(ServerHttpResponse delegate) {
/* 251 */       this.delegate = delegate;
/* 252 */       this.mutableHeaders.putAll(delegate.getHeaders());
/*     */     }
/*     */     
/*     */     public void setStatusCode(HttpStatus status)
/*     */     {
/* 257 */       this.delegate.setStatusCode(status);
/*     */     }
/*     */     
/*     */     public HttpHeaders getHeaders()
/*     */     {
/* 262 */       return this.mutableHeaders;
/*     */     }
/*     */     
/*     */     public OutputStream getBody() throws IOException
/*     */     {
/* 267 */       return this.delegate.getBody();
/*     */     }
/*     */     
/*     */     public void flush() throws IOException
/*     */     {
/* 272 */       this.delegate.flush();
/*     */     }
/*     */     
/*     */     public void close()
/*     */     {
/* 277 */       this.delegate.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ResponseBodyEmitterReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */