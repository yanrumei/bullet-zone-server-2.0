/*    */ package org.apache.catalina.filters;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
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
/*    */ public final class RemoteAddrFilter
/*    */   extends RequestFilter
/*    */ {
/* 40 */   private static final Log log = LogFactory.getLog(RemoteAddrFilter.class);
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
/*    */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*    */     throws IOException, ServletException
/*    */   {
/* 67 */     process(request.getRemoteAddr(), request, response, chain);
/*    */   }
/*    */   
/*    */ 
/*    */   protected Log getLogger()
/*    */   {
/* 73 */     return log;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\RemoteAddrFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */