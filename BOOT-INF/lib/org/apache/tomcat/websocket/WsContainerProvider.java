/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import javax.websocket.ContainerProvider;
/*    */ import javax.websocket.WebSocketContainer;
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
/*    */ public class WsContainerProvider
/*    */   extends ContainerProvider
/*    */ {
/*    */   protected WebSocketContainer getContainer()
/*    */   {
/* 26 */     return new WsWebSocketContainer();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsContainerProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */