/*    */ package org.apache.tomcat.websocket.pojo;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.nio.ByteBuffer;
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
/*    */ public class PojoMessageHandlerPartialBinary
/*    */   extends PojoMessageHandlerPartialBase<ByteBuffer>
/*    */ {
/*    */   public PojoMessageHandlerPartialBinary(Object pojo, Method method, Session session, Object[] params, int indexPayload, boolean convert, int indexBoolean, int indexSession, long maxMessageSize)
/*    */   {
/* 33 */     super(pojo, method, session, params, indexPayload, convert, indexBoolean, indexSession, maxMessageSize);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMessageHandlerPartialBinary.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */