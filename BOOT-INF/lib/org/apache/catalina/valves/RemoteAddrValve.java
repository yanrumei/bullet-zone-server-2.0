/*    */ package org.apache.catalina.valves;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.catalina.connector.Connector;
/*    */ import org.apache.catalina.connector.Request;
/*    */ import org.apache.catalina.connector.Response;
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
/*    */ public final class RemoteAddrValve
/*    */   extends RequestFilterValve
/*    */ {
/* 39 */   private static final Log log = LogFactory.getLog(RemoteAddrValve.class);
/*    */   
/*    */ 
/*    */   public void invoke(Request request, Response response)
/*    */     throws IOException, ServletException
/*    */   {
/*    */     String property;
/*    */     String property;
/* 47 */     if (getAddConnectorPort()) {
/* 48 */       property = request.getRequest().getRemoteAddr() + ";" + request.getConnector().getPort();
/*    */     } else {
/* 50 */       property = request.getRequest().getRemoteAddr();
/*    */     }
/* 52 */     process(property, request, response);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected Log getLog()
/*    */   {
/* 59 */     return log;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\RemoteAddrValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */