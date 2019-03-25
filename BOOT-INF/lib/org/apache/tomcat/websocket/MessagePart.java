/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ 
/*    */ 
/*    */ class MessagePart
/*    */ {
/*    */   private final boolean fin;
/*    */   private final int rsv;
/*    */   private final byte opCode;
/*    */   private final ByteBuffer payload;
/*    */   private final SendHandler intermediateHandler;
/*    */   private volatile SendHandler endHandler;
/*    */   private final long blockingWriteTimeoutExpiry;
/*    */   
/*    */   public MessagePart(boolean fin, int rsv, byte opCode, ByteBuffer payload, SendHandler intermediateHandler, SendHandler endHandler, long blockingWriteTimeoutExpiry)
/*    */   {
/* 35 */     this.fin = fin;
/* 36 */     this.rsv = rsv;
/* 37 */     this.opCode = opCode;
/* 38 */     this.payload = payload;
/* 39 */     this.intermediateHandler = intermediateHandler;
/* 40 */     this.endHandler = endHandler;
/* 41 */     this.blockingWriteTimeoutExpiry = blockingWriteTimeoutExpiry;
/*    */   }
/*    */   
/*    */   public boolean isFin()
/*    */   {
/* 46 */     return this.fin;
/*    */   }
/*    */   
/*    */   public int getRsv()
/*    */   {
/* 51 */     return this.rsv;
/*    */   }
/*    */   
/*    */   public byte getOpCode()
/*    */   {
/* 56 */     return this.opCode;
/*    */   }
/*    */   
/*    */   public ByteBuffer getPayload()
/*    */   {
/* 61 */     return this.payload;
/*    */   }
/*    */   
/*    */   public SendHandler getIntermediateHandler()
/*    */   {
/* 66 */     return this.intermediateHandler;
/*    */   }
/*    */   
/*    */   public SendHandler getEndHandler()
/*    */   {
/* 71 */     return this.endHandler;
/*    */   }
/*    */   
/*    */   public void setEndHandler(SendHandler endHandler) {
/* 75 */     this.endHandler = endHandler;
/*    */   }
/*    */   
/*    */   public long getBlockingWriteTimeoutExpiry() {
/* 79 */     return this.blockingWriteTimeoutExpiry;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\MessagePart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */