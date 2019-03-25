/*    */ package org.apache.tomcat.websocket.pojo;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import javax.websocket.PongMessage;
/*    */ import javax.websocket.Session;
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
/*    */ public class PojoMessageHandlerWholePong
/*    */   extends PojoMessageHandlerWholeBase<PongMessage>
/*    */ {
/*    */   public PojoMessageHandlerWholePong(Object pojo, Method method, Session session, Object[] params, int indexPayload, boolean convert, int indexSession)
/*    */   {
/* 33 */     super(pojo, method, session, params, indexPayload, convert, indexSession, -1L);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected Object decode(PongMessage message)
/*    */   {
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   protected void onClose() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMessageHandlerWholePong.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */