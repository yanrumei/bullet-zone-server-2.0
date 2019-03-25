/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
/*    */ import javax.websocket.SendHandler;
/*    */ import javax.websocket.SendResult;
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
/*    */ public class WsRemoteEndpointImplClient
/*    */   extends WsRemoteEndpointImplBase
/*    */ {
/*    */   private final AsyncChannelWrapper channel;
/*    */   
/*    */   public WsRemoteEndpointImplClient(AsyncChannelWrapper channel)
/*    */   {
/* 33 */     this.channel = channel;
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean isMasked()
/*    */   {
/* 39 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void doWrite(SendHandler handler, long blockingWriteTimeoutExpiry, ByteBuffer... data)
/*    */   {
/* 47 */     for (ByteBuffer byteBuffer : data) { long timeout;
/* 48 */       if (blockingWriteTimeoutExpiry == -1L) {
/* 49 */         long timeout = getSendTimeout();
/* 50 */         if (timeout < 1L) {
/* 51 */           timeout = Long.MAX_VALUE;
/*    */         }
/*    */       } else {
/* 54 */         timeout = blockingWriteTimeoutExpiry - System.currentTimeMillis();
/* 55 */         if (timeout < 0L) {
/* 56 */           SendResult sr = new SendResult(new IOException("Blocking write timeout"));
/* 57 */           handler.onResult(sr);
/*    */         }
/*    */       }
/*    */       try
/*    */       {
/* 62 */         this.channel.write(byteBuffer).get(timeout, TimeUnit.MILLISECONDS);
/*    */       } catch (InterruptedException|ExecutionException|TimeoutException e) {
/* 64 */         handler.onResult(new SendResult(e));
/* 65 */         return;
/*    */       }
/*    */     }
/* 68 */     handler.onResult(SENDRESULT_OK);
/*    */   }
/*    */   
/*    */   protected void doClose()
/*    */   {
/* 73 */     this.channel.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsRemoteEndpointImplClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */