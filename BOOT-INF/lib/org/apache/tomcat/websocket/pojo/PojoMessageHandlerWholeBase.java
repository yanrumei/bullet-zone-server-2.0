/*    */ package org.apache.tomcat.websocket.pojo;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.websocket.DecodeException;
/*    */ import javax.websocket.Endpoint;
/*    */ import javax.websocket.MessageHandler.Whole;
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
/*    */ public abstract class PojoMessageHandlerWholeBase<T>
/*    */   extends PojoMessageHandlerBase<T>
/*    */   implements MessageHandler.Whole<T>
/*    */ {
/*    */   public PojoMessageHandlerWholeBase(Object pojo, Method method, Session session, Object[] params, int indexPayload, boolean convert, int indexSession, long maxMessageSize)
/*    */   {
/* 40 */     super(pojo, method, session, params, indexPayload, convert, indexSession, maxMessageSize);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void onMessage(T message)
/*    */   {
/* 48 */     if ((this.params.length == 1) && ((this.params[0] instanceof DecodeException))) {
/* 49 */       ((WsSession)this.session).getLocal().onError(this.session, (DecodeException)this.params[0]);
/*    */       
/* 51 */       return;
/*    */     }
/*    */     
/*    */ 
/*    */     try
/*    */     {
/* 57 */       payload = decode(message);
/*    */     } catch (DecodeException de) { Object payload;
/* 59 */       ((WsSession)this.session).getLocal().onError(this.session, de); return;
/*    */     }
/*    */     
/*    */     Object payload;
/* 63 */     if (payload == null)
/*    */     {
/* 65 */       if (this.convert) {
/* 66 */         payload = convert(message);
/*    */       } else {
/* 68 */         payload = message;
/*    */       }
/*    */     }
/*    */     
/* 72 */     Object[] parameters = (Object[])this.params.clone();
/* 73 */     if (this.indexSession != -1) {
/* 74 */       parameters[this.indexSession] = this.session;
/*    */     }
/* 76 */     parameters[this.indexPayload] = payload;
/*    */     
/* 78 */     Object result = null;
/*    */     try {
/* 80 */       result = this.method.invoke(this.pojo, parameters);
/*    */     } catch (IllegalAccessException|InvocationTargetException e) {
/* 82 */       handlePojoMethodException(e);
/*    */     }
/* 84 */     processResult(result);
/*    */   }
/*    */   
/*    */   protected Object convert(T message) {
/* 88 */     return message;
/*    */   }
/*    */   
/*    */   protected abstract Object decode(T paramT)
/*    */     throws DecodeException;
/*    */   
/*    */   protected abstract void onClose();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMessageHandlerWholeBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */