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
/*    */ public final class RemoteHostValve
/*    */   extends RequestFilterValve
/*    */ {
/* 37 */   private static final Log log = LogFactory.getLog(RemoteHostValve.class);
/*    */   
/*    */ 
/*    */   public void invoke(Request request, Response response)
/*    */     throws IOException, ServletException
/*    */   {
/*    */     String property;
/*    */     String property;
/* 45 */     if (getAddConnectorPort()) {
/* 46 */       property = request.getRequest().getRemoteHost() + ";" + request.getConnector().getPort();
/*    */     } else {
/* 48 */       property = request.getRequest().getRemoteHost();
/*    */     }
/* 50 */     process(property, request, response);
/*    */   }
/*    */   
/*    */ 
/*    */   protected Log getLog()
/*    */   {
/* 56 */     return log;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\RemoteHostValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */