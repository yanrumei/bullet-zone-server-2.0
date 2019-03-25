/*    */ package org.apache.tomcat.websocket.pojo;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class PojoMessageHandlerPartialText
/*    */   extends PojoMessageHandlerPartialBase<String>
/*    */ {
/*    */   public PojoMessageHandlerPartialText(Object pojo, Method method, Session session, Object[] params, int indexPayload, boolean convert, int indexBoolean, int indexSession, long maxMessageSize)
/*    */   {
/* 32 */     super(pojo, method, session, params, indexPayload, convert, indexBoolean, indexSession, maxMessageSize);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMessageHandlerPartialText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */