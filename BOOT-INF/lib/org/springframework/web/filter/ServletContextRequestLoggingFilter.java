/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.http.HttpServletRequest;
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
/*    */ public class ServletContextRequestLoggingFilter
/*    */   extends AbstractRequestLoggingFilter
/*    */ {
/*    */   protected void beforeRequest(HttpServletRequest request, String message)
/*    */   {
/* 41 */     getServletContext().log(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void afterRequest(HttpServletRequest request, String message)
/*    */   {
/* 49 */     getServletContext().log(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\ServletContextRequestLoggingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */