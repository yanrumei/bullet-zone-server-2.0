/*    */ package org.springframework.web.servlet.mvc;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.HttpRequestHandler;
/*    */ import org.springframework.web.servlet.HandlerAdapter;
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
/*    */ 
/*    */ public class HttpRequestHandlerAdapter
/*    */   implements HandlerAdapter
/*    */ {
/*    */   public boolean supports(Object handler)
/*    */   {
/* 44 */     return handler instanceof HttpRequestHandler;
/*    */   }
/*    */   
/*    */ 
/*    */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws Exception
/*    */   {
/* 51 */     ((HttpRequestHandler)handler).handleRequest(request, response);
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public long getLastModified(HttpServletRequest request, Object handler)
/*    */   {
/* 57 */     if ((handler instanceof LastModified)) {
/* 58 */       return ((LastModified)handler).getLastModified(request);
/*    */     }
/* 60 */     return -1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\HttpRequestHandlerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */