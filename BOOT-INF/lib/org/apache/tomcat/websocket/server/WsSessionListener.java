/*    */ package org.apache.tomcat.websocket.server;
/*    */ 
/*    */ import javax.servlet.http.HttpSession;
/*    */ import javax.servlet.http.HttpSessionEvent;
/*    */ import javax.servlet.http.HttpSessionListener;
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
/*    */ public class WsSessionListener
/*    */   implements HttpSessionListener
/*    */ {
/*    */   private final WsServerContainer wsServerContainer;
/*    */   
/*    */   public WsSessionListener(WsServerContainer wsServerContainer)
/*    */   {
/* 28 */     this.wsServerContainer = wsServerContainer;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void sessionCreated(HttpSessionEvent se) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void sessionDestroyed(HttpSessionEvent se)
/*    */   {
/* 40 */     this.wsServerContainer.closeAuthenticatedSession(se.getSession().getId());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsSessionListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */