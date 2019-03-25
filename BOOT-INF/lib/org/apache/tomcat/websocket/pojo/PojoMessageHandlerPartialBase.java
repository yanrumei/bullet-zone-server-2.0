/*    */ package org.apache.tomcat.websocket.pojo;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.nio.ByteBuffer;
/*    */ import javax.websocket.DecodeException;
/*    */ import javax.websocket.Endpoint;
/*    */ import javax.websocket.MessageHandler.Partial;
/*    */ import javax.websocket.Session;
/*    */ import org.apache.tomcat.websocket.WsSession;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PojoMessageHandlerPartialBase<T>
/*    */   extends PojoMessageHandlerBase<T>
/*    */   implements MessageHandler.Partial<T>
/*    */ {
/*    */   private final int indexBoolean;
/*    */   
/*    */   public PojoMessageHandlerPartialBase(Object pojo, Method method, Session session, Object[] params, int indexPayload, boolean convert, int indexBoolean, int indexSession, long maxMessageSize)
/*    */   {
/* 44 */     super(pojo, method, session, params, indexPayload, convert, indexSession, maxMessageSize);
/*    */     
/* 46 */     this.indexBoolean = indexBoolean;
/*    */   }
/*    */   
/*    */ 
/*    */   public final void onMessage(T message, boolean last)
/*    */   {
/* 52 */     if ((this.params.length == 1) && ((this.params[0] instanceof DecodeException))) {
/* 53 */       ((WsSession)this.session).getLocal().onError(this.session, (DecodeException)this.params[0]);
/*    */       
/* 55 */       return;
/*    */     }
/* 57 */     Object[] parameters = (Object[])this.params.clone();
/* 58 */     if (this.indexBoolean != -1) {
/* 59 */       parameters[this.indexBoolean] = Boolean.valueOf(last);
/*    */     }
/* 61 */     if (this.indexSession != -1) {
/* 62 */       parameters[this.indexSession] = this.session;
/*    */     }
/* 64 */     if (this.convert) {
/* 65 */       parameters[this.indexPayload] = ((ByteBuffer)message).array();
/*    */     } else {
/* 67 */       parameters[this.indexPayload] = message;
/*    */     }
/* 69 */     Object result = null;
/*    */     try {
/* 71 */       result = this.method.invoke(this.pojo, parameters);
/*    */     } catch (IllegalAccessException|InvocationTargetException e) {
/* 73 */       handlePojoMethodException(e);
/*    */     }
/* 75 */     processResult(result);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMessageHandlerPartialBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */