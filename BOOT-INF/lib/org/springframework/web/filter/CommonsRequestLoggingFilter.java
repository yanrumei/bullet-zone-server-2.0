/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.logging.Log;
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
/*    */ public class CommonsRequestLoggingFilter
/*    */   extends AbstractRequestLoggingFilter
/*    */ {
/*    */   protected boolean shouldLog(HttpServletRequest request)
/*    */   {
/* 39 */     return this.logger.isDebugEnabled();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void beforeRequest(HttpServletRequest request, String message)
/*    */   {
/* 47 */     this.logger.debug(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void afterRequest(HttpServletRequest request, String message)
/*    */   {
/* 55 */     this.logger.debug(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\CommonsRequestLoggingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */