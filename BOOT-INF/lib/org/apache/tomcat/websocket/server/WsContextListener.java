/*    */ package org.apache.tomcat.websocket.server;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
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
/*    */ public class WsContextListener
/*    */   implements ServletContextListener
/*    */ {
/*    */   public void contextInitialized(ServletContextEvent sce)
/*    */   {
/* 35 */     ServletContext sc = sce.getServletContext();
/*    */     
/*    */ 
/* 38 */     if (sc.getAttribute("javax.websocket.server.ServerContainer") == null) {
/* 39 */       WsSci.init(sce.getServletContext(), false);
/*    */     }
/*    */   }
/*    */   
/*    */   public void contextDestroyed(ServletContextEvent sce)
/*    */   {
/* 45 */     ServletContext sc = sce.getServletContext();
/* 46 */     Object obj = sc.getAttribute("javax.websocket.server.ServerContainer");
/* 47 */     if ((obj instanceof WsServerContainer)) {
/* 48 */       ((WsServerContainer)obj).destroy();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsContextListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */