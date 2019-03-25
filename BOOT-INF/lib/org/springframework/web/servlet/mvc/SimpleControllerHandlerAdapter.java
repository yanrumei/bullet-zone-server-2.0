/*    */ package org.springframework.web.servlet.mvc;
/*    */ 
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
/*    */ public class SimpleControllerHandlerAdapter
/*    */   implements HandlerAdapter
/*    */ {
/*    */   public boolean supports(Object handler)
/*    */   {
/* 43 */     return handler instanceof Controller;
/*    */   }
/*    */   
/*    */ 
/*    */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws Exception
/*    */   {
/* 50 */     return ((Controller)handler).handleRequest(request, response);
/*    */   }
/*    */   
/*    */   public long getLastModified(HttpServletRequest request, Object handler)
/*    */   {
/* 55 */     if ((handler instanceof LastModified)) {
/* 56 */       return ((LastModified)handler).getLastModified(request);
/*    */     }
/* 58 */     return -1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\SimpleControllerHandlerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */