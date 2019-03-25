/*     */ package org.apache.tomcat.websocket.pojo;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.websocket.EncodeException;
/*     */ import javax.websocket.MessageHandler;
/*     */ import javax.websocket.RemoteEndpoint.Basic;
/*     */ import javax.websocket.Session;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.websocket.WrappedMessageHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PojoMessageHandlerBase<T>
/*     */   implements WrappedMessageHandler
/*     */ {
/*     */   protected final Object pojo;
/*     */   protected final Method method;
/*     */   protected final Session session;
/*     */   protected final Object[] params;
/*     */   protected final int indexPayload;
/*     */   protected final boolean convert;
/*     */   protected final int indexSession;
/*     */   protected final long maxMessageSize;
/*     */   
/*     */   public PojoMessageHandlerBase(Object pojo, Method method, Session session, Object[] params, int indexPayload, boolean convert, int indexSession, long maxMessageSize)
/*     */   {
/*  51 */     this.pojo = pojo;
/*  52 */     this.method = method;
/*     */     
/*     */     try
/*     */     {
/*  56 */       this.method.setAccessible(true);
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/*     */ 
/*  61 */     this.session = session;
/*  62 */     this.params = params;
/*  63 */     this.indexPayload = indexPayload;
/*  64 */     this.convert = convert;
/*  65 */     this.indexSession = indexSession;
/*  66 */     this.maxMessageSize = maxMessageSize;
/*     */   }
/*     */   
/*     */   protected final void processResult(Object result)
/*     */   {
/*  71 */     if (result == null) {
/*  72 */       return;
/*     */     }
/*     */     
/*  75 */     RemoteEndpoint.Basic remoteEndpoint = this.session.getBasicRemote();
/*     */     try {
/*  77 */       if ((result instanceof String)) {
/*  78 */         remoteEndpoint.sendText((String)result);
/*  79 */       } else if ((result instanceof ByteBuffer)) {
/*  80 */         remoteEndpoint.sendBinary((ByteBuffer)result);
/*  81 */       } else if ((result instanceof byte[])) {
/*  82 */         remoteEndpoint.sendBinary(ByteBuffer.wrap((byte[])result));
/*     */       } else {
/*  84 */         remoteEndpoint.sendObject(result);
/*     */       }
/*     */     } catch (IOException|EncodeException ioe) {
/*  87 */       throw new IllegalStateException(ioe);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final MessageHandler getWrappedHandler()
/*     */   {
/*  99 */     if ((this.pojo instanceof MessageHandler)) {
/* 100 */       return (MessageHandler)this.pojo;
/*     */     }
/* 102 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final long getMaxMessageSize()
/*     */   {
/* 109 */     return this.maxMessageSize;
/*     */   }
/*     */   
/*     */   protected final void handlePojoMethodException(Throwable t)
/*     */   {
/* 114 */     t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 115 */     ExceptionUtils.handleThrowable(t);
/* 116 */     if ((t instanceof RuntimeException)) {
/* 117 */       throw ((RuntimeException)t);
/*     */     }
/* 119 */     throw new RuntimeException(t.getMessage(), t);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoMessageHandlerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */