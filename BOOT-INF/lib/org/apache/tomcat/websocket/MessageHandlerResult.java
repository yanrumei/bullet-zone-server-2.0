/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import javax.websocket.MessageHandler;
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
/*    */ public class MessageHandlerResult
/*    */ {
/*    */   private final MessageHandler handler;
/*    */   private final MessageHandlerResultType type;
/*    */   
/*    */   public MessageHandlerResult(MessageHandler handler, MessageHandlerResultType type)
/*    */   {
/* 29 */     this.handler = handler;
/* 30 */     this.type = type;
/*    */   }
/*    */   
/*    */   public MessageHandler getHandler()
/*    */   {
/* 35 */     return this.handler;
/*    */   }
/*    */   
/*    */   public MessageHandlerResultType getType()
/*    */   {
/* 40 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\MessageHandlerResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */