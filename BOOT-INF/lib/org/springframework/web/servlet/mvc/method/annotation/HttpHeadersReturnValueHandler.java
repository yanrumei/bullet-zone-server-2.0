/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.server.ServletServerHttpResponse;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*    */ public class HttpHeadersReturnValueHandler
/*    */   implements HandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsReturnType(MethodParameter returnType)
/*    */   {
/* 39 */     return HttpHeaders.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 47 */     mavContainer.setRequestHandled(true);
/*    */     
/* 49 */     Assert.state(returnValue instanceof HttpHeaders, "HttpHeaders expected");
/* 50 */     HttpHeaders headers = (HttpHeaders)returnValue;
/*    */     
/* 52 */     if (!headers.isEmpty()) {
/* 53 */       HttpServletResponse servletResponse = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 54 */       ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(servletResponse);
/* 55 */       outputMessage.getHeaders().putAll(headers);
/* 56 */       outputMessage.getBody();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\HttpHeadersReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */