/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.concurrent.Future;
/*    */ import javax.websocket.RemoteEndpoint.Async;
/*    */ import javax.websocket.SendHandler;
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
/*    */ public class WsRemoteEndpointAsync
/*    */   extends WsRemoteEndpointBase
/*    */   implements RemoteEndpoint.Async
/*    */ {
/*    */   WsRemoteEndpointAsync(WsRemoteEndpointImplBase base)
/*    */   {
/* 29 */     super(base);
/*    */   }
/*    */   
/*    */ 
/*    */   public long getSendTimeout()
/*    */   {
/* 35 */     return this.base.getSendTimeout();
/*    */   }
/*    */   
/*    */ 
/*    */   public void setSendTimeout(long timeout)
/*    */   {
/* 41 */     this.base.setSendTimeout(timeout);
/*    */   }
/*    */   
/*    */ 
/*    */   public void sendText(String text, SendHandler completion)
/*    */   {
/* 47 */     this.base.sendStringByCompletion(text, completion);
/*    */   }
/*    */   
/*    */ 
/*    */   public Future<Void> sendText(String text)
/*    */   {
/* 53 */     return this.base.sendStringByFuture(text);
/*    */   }
/*    */   
/*    */ 
/*    */   public Future<Void> sendBinary(ByteBuffer data)
/*    */   {
/* 59 */     return this.base.sendBytesByFuture(data);
/*    */   }
/*    */   
/*    */ 
/*    */   public void sendBinary(ByteBuffer data, SendHandler completion)
/*    */   {
/* 65 */     this.base.sendBytesByCompletion(data, completion);
/*    */   }
/*    */   
/*    */ 
/*    */   public Future<Void> sendObject(Object obj)
/*    */   {
/* 71 */     return this.base.sendObjectByFuture(obj);
/*    */   }
/*    */   
/*    */ 
/*    */   public void sendObject(Object obj, SendHandler completion)
/*    */   {
/* 77 */     this.base.sendObjectByCompletion(obj, completion);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsRemoteEndpointAsync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */