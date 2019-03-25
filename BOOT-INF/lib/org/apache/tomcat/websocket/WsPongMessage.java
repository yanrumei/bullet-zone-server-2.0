/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import javax.websocket.PongMessage;
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
/*    */ public class WsPongMessage
/*    */   implements PongMessage
/*    */ {
/*    */   private final ByteBuffer applicationData;
/*    */   
/*    */   public WsPongMessage(ByteBuffer applicationData)
/*    */   {
/* 29 */     byte[] dst = new byte[applicationData.limit()];
/* 30 */     applicationData.get(dst);
/* 31 */     this.applicationData = ByteBuffer.wrap(dst);
/*    */   }
/*    */   
/*    */ 
/*    */   public ByteBuffer getApplicationData()
/*    */   {
/* 37 */     return this.applicationData;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsPongMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */