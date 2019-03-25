/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.request.AsyncWebRequestInterceptor;
/*    */ import org.springframework.web.context.request.WebRequestInterceptor;
/*    */ import org.springframework.web.servlet.AsyncHandlerInterceptor;
/*    */ import org.springframework.web.servlet.ModelAndView;
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
/*    */ public class WebRequestHandlerInterceptorAdapter
/*    */   implements AsyncHandlerInterceptor
/*    */ {
/*    */   private final WebRequestInterceptor requestInterceptor;
/*    */   
/*    */   public WebRequestHandlerInterceptorAdapter(WebRequestInterceptor requestInterceptor)
/*    */   {
/* 47 */     Assert.notNull(requestInterceptor, "WebRequestInterceptor must not be null");
/* 48 */     this.requestInterceptor = requestInterceptor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws Exception
/*    */   {
/* 56 */     this.requestInterceptor.preHandle(new DispatcherServletWebRequest(request, response));
/* 57 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
/*    */     throws Exception
/*    */   {
/* 64 */     this.requestInterceptor.postHandle(new DispatcherServletWebRequest(request, response), (modelAndView != null) && 
/* 65 */       (!modelAndView.wasCleared()) ? modelAndView.getModelMap() : null);
/*    */   }
/*    */   
/*    */ 
/*    */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*    */     throws Exception
/*    */   {
/* 72 */     this.requestInterceptor.afterCompletion(new DispatcherServletWebRequest(request, response), ex);
/*    */   }
/*    */   
/*    */   public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */   {
/* 77 */     if ((this.requestInterceptor instanceof AsyncWebRequestInterceptor)) {
/* 78 */       AsyncWebRequestInterceptor asyncInterceptor = (AsyncWebRequestInterceptor)this.requestInterceptor;
/* 79 */       DispatcherServletWebRequest webRequest = new DispatcherServletWebRequest(request, response);
/* 80 */       asyncInterceptor.afterConcurrentHandlingStarted(webRequest);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\WebRequestHandlerInterceptorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */