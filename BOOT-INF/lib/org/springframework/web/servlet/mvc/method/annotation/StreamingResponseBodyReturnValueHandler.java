/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.async.WebAsyncManager;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ import org.springframework.web.filter.ShallowEtagHeaderFilter;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
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
/*     */ public class StreamingResponseBodyReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   public boolean supportsReturnType(MethodParameter returnType)
/*     */   {
/*  48 */     if (StreamingResponseBody.class.isAssignableFrom(returnType.getParameterType())) {
/*  49 */       return true;
/*     */     }
/*  51 */     if (ResponseEntity.class.isAssignableFrom(returnType.getParameterType())) {
/*  52 */       Class<?> bodyType = ResolvableType.forMethodParameter(returnType).getGeneric(new int[] { 0 }).resolve();
/*  53 */       return (bodyType != null) && (StreamingResponseBody.class.isAssignableFrom(bodyType));
/*     */     }
/*  55 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/*  62 */     if (returnValue == null) {
/*  63 */       mavContainer.setRequestHandled(true);
/*  64 */       return;
/*     */     }
/*     */     
/*  67 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/*  68 */     ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
/*     */     
/*  70 */     if ((returnValue instanceof ResponseEntity)) {
/*  71 */       ResponseEntity<?> responseEntity = (ResponseEntity)returnValue;
/*  72 */       response.setStatus(responseEntity.getStatusCodeValue());
/*  73 */       outputMessage.getHeaders().putAll(responseEntity.getHeaders());
/*  74 */       returnValue = responseEntity.getBody();
/*  75 */       if (returnValue == null) {
/*  76 */         mavContainer.setRequestHandled(true);
/*  77 */         outputMessage.flush();
/*  78 */         return;
/*     */       }
/*     */     }
/*     */     
/*  82 */     ServletRequest request = (ServletRequest)webRequest.getNativeRequest(ServletRequest.class);
/*  83 */     ShallowEtagHeaderFilter.disableContentCaching(request);
/*     */     
/*  85 */     Assert.isInstanceOf(StreamingResponseBody.class, returnValue, "StreamingResponseBody expected");
/*  86 */     StreamingResponseBody streamingBody = (StreamingResponseBody)returnValue;
/*     */     
/*  88 */     Callable<Void> callable = new StreamingResponseBodyTask(outputMessage.getBody(), streamingBody);
/*  89 */     WebAsyncUtils.getAsyncManager(webRequest).startCallableProcessing(callable, new Object[] { mavContainer });
/*     */   }
/*     */   
/*     */   private static class StreamingResponseBodyTask
/*     */     implements Callable<Void>
/*     */   {
/*     */     private final OutputStream outputStream;
/*     */     private final StreamingResponseBody streamingBody;
/*     */     
/*     */     public StreamingResponseBodyTask(OutputStream outputStream, StreamingResponseBody streamingBody)
/*     */     {
/* 100 */       this.outputStream = outputStream;
/* 101 */       this.streamingBody = streamingBody;
/*     */     }
/*     */     
/*     */     public Void call() throws Exception
/*     */     {
/* 106 */       this.streamingBody.writeTo(this.outputStream);
/* 107 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\StreamingResponseBodyReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */