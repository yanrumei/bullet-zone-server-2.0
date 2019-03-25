/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
/*    */ import java.nio.ByteBuffer;
/*    */ import javax.websocket.EncodeException;
/*    */ import javax.websocket.RemoteEndpoint.Basic;
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
/*    */ public class WsRemoteEndpointBasic
/*    */   extends WsRemoteEndpointBase
/*    */   implements RemoteEndpoint.Basic
/*    */ {
/*    */   WsRemoteEndpointBasic(WsRemoteEndpointImplBase base)
/*    */   {
/* 31 */     super(base);
/*    */   }
/*    */   
/*    */   public void sendText(String text)
/*    */     throws IOException
/*    */   {
/* 37 */     this.base.sendString(text);
/*    */   }
/*    */   
/*    */   public void sendBinary(ByteBuffer data)
/*    */     throws IOException
/*    */   {
/* 43 */     this.base.sendBytes(data);
/*    */   }
/*    */   
/*    */   public void sendText(String fragment, boolean isLast)
/*    */     throws IOException
/*    */   {
/* 49 */     this.base.sendPartialString(fragment, isLast);
/*    */   }
/*    */   
/*    */ 
/*    */   public void sendBinary(ByteBuffer partialByte, boolean isLast)
/*    */     throws IOException
/*    */   {
/* 56 */     this.base.sendPartialBytes(partialByte, isLast);
/*    */   }
/*    */   
/*    */   public OutputStream getSendStream()
/*    */     throws IOException
/*    */   {
/* 62 */     return this.base.getSendStream();
/*    */   }
/*    */   
/*    */   public Writer getSendWriter()
/*    */     throws IOException
/*    */   {
/* 68 */     return this.base.getSendWriter();
/*    */   }
/*    */   
/*    */   public void sendObject(Object o)
/*    */     throws IOException, EncodeException
/*    */   {
/* 74 */     this.base.sendObject(o);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsRemoteEndpointBasic.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */