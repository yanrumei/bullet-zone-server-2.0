/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import javax.websocket.RemoteEndpoint;
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
/*    */ public abstract class WsRemoteEndpointBase
/*    */   implements RemoteEndpoint
/*    */ {
/*    */   protected final WsRemoteEndpointImplBase base;
/*    */   
/*    */   WsRemoteEndpointBase(WsRemoteEndpointImplBase base)
/*    */   {
/* 30 */     this.base = base;
/*    */   }
/*    */   
/*    */   public final void setBatchingAllowed(boolean batchingAllowed)
/*    */     throws IOException
/*    */   {
/* 36 */     this.base.setBatchingAllowed(batchingAllowed);
/*    */   }
/*    */   
/*    */ 
/*    */   public final boolean getBatchingAllowed()
/*    */   {
/* 42 */     return this.base.getBatchingAllowed();
/*    */   }
/*    */   
/*    */   public final void flushBatch()
/*    */     throws IOException
/*    */   {
/* 48 */     this.base.flushBatch();
/*    */   }
/*    */   
/*    */ 
/*    */   public final void sendPing(ByteBuffer applicationData)
/*    */     throws IOException, IllegalArgumentException
/*    */   {
/* 55 */     this.base.sendPing(applicationData);
/*    */   }
/*    */   
/*    */ 
/*    */   public final void sendPong(ByteBuffer applicationData)
/*    */     throws IOException, IllegalArgumentException
/*    */   {
/* 62 */     this.base.sendPong(applicationData);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsRemoteEndpointBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */