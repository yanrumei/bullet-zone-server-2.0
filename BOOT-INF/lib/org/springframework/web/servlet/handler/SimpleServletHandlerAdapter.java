/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import javax.servlet.Servlet;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public class SimpleServletHandlerAdapter
/*    */   implements HandlerAdapter
/*    */ {
/*    */   public boolean supports(Object handler)
/*    */   {
/* 58 */     return handler instanceof Servlet;
/*    */   }
/*    */   
/*    */ 
/*    */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws Exception
/*    */   {
/* 65 */     ((Servlet)handler).service(request, response);
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   public long getLastModified(HttpServletRequest request, Object handler)
/*    */   {
/* 71 */     return -1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\SimpleServletHandlerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */