/*     */ package org.apache.tomcat.websocket.pojo;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.websocket.CloseReason;
/*     */ import javax.websocket.Endpoint;
/*     */ import javax.websocket.EndpointConfig;
/*     */ import javax.websocket.MessageHandler;
/*     */ import javax.websocket.Session;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public abstract class PojoEndpointBase
/*     */   extends Endpoint
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(PojoEndpointBase.class);
/*  43 */   private static final StringManager sm = StringManager.getManager(PojoEndpointBase.class);
/*     */   
/*     */   private Object pojo;
/*     */   private Map<String, String> pathParameters;
/*     */   private PojoMethodMapping methodMapping;
/*     */   
/*     */   protected final void doOnOpen(Session session, EndpointConfig config)
/*     */   {
/*  51 */     PojoMethodMapping methodMapping = getMethodMapping();
/*  52 */     Object pojo = getPojo();
/*  53 */     Map<String, String> pathParameters = getPathParameters();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  58 */     for (MessageHandler mh : methodMapping.getMessageHandlers(pojo, pathParameters, session, config))
/*     */     {
/*  60 */       session.addMessageHandler(mh);
/*     */     }
/*     */     
/*  63 */     if (methodMapping.getOnOpen() != null) {
/*     */       try {
/*  65 */         methodMapping.getOnOpen().invoke(pojo, methodMapping
/*  66 */           .getOnOpenArgs(pathParameters, session, config));
/*     */ 
/*     */       }
/*     */       catch (IllegalAccessException e)
/*     */       {
/*  71 */         log.error(sm.getString("pojoEndpointBase.onOpenFail", new Object[] {pojo
/*     */         
/*  73 */           .getClass().getName() }), e);
/*  74 */         handleOnOpenOrCloseError(session, e);
/*  75 */         return;
/*     */       } catch (InvocationTargetException e) {
/*  77 */         Throwable cause = e.getCause();
/*  78 */         handleOnOpenOrCloseError(session, cause);
/*  79 */         return;
/*     */       } catch (Throwable t) {
/*  81 */         handleOnOpenOrCloseError(session, t);
/*  82 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void handleOnOpenOrCloseError(Session session, Throwable t)
/*     */   {
/*  90 */     ExceptionUtils.handleThrowable(t);
/*     */     
/*     */ 
/*  93 */     onError(session, t);
/*     */     try {
/*  95 */       session.close();
/*     */     } catch (IOException ioe) {
/*  97 */       log.warn(sm.getString("pojoEndpointBase.closeSessionFail"), ioe);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public final void onClose(Session session, CloseReason closeReason)
/*     */   {
/* 104 */     if (this.methodMapping.getOnClose() != null) {
/*     */       try {
/* 106 */         this.methodMapping.getOnClose().invoke(this.pojo, this.methodMapping
/* 107 */           .getOnCloseArgs(this.pathParameters, session, closeReason));
/*     */       } catch (Throwable t) {
/* 109 */         log.error(sm.getString("pojoEndpointBase.onCloseFail", new Object[] {this.pojo
/* 110 */           .getClass().getName() }), t);
/* 111 */         handleOnOpenOrCloseError(session, t);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 116 */     Set<MessageHandler> messageHandlers = session.getMessageHandlers();
/* 117 */     for (MessageHandler messageHandler : messageHandlers) {
/* 118 */       if ((messageHandler instanceof PojoMessageHandlerWholeBase)) {
/* 119 */         ((PojoMessageHandlerWholeBase)messageHandler).onClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void onError(Session session, Throwable throwable)
/*     */   {
/* 128 */     if (this.methodMapping.getOnError() == null) {
/* 129 */       log.error(sm.getString("pojoEndpointBase.onError", new Object[] {this.pojo
/* 130 */         .getClass().getName() }), throwable);
/*     */     } else {
/*     */       try {
/* 133 */         this.methodMapping.getOnError().invoke(this.pojo, this.methodMapping
/*     */         
/* 135 */           .getOnErrorArgs(this.pathParameters, session, throwable));
/*     */       }
/*     */       catch (Throwable t) {
/* 138 */         ExceptionUtils.handleThrowable(t);
/* 139 */         log.error(sm.getString("pojoEndpointBase.onErrorFail", new Object[] {this.pojo
/* 140 */           .getClass().getName() }), t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 145 */   protected Object getPojo() { return this.pojo; }
/* 146 */   protected void setPojo(Object pojo) { this.pojo = pojo; }
/*     */   
/*     */ 
/* 149 */   protected Map<String, String> getPathParameters() { return this.pathParameters; }
/*     */   
/* 151 */   protected void setPathParameters(Map<String, String> pathParameters) { this.pathParameters = pathParameters; }
/*     */   
/*     */ 
/*     */ 
/* 155 */   protected PojoMethodMapping getMethodMapping() { return this.methodMapping; }
/*     */   
/* 157 */   protected void setMethodMapping(PojoMethodMapping methodMapping) { this.methodMapping = methodMapping; }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoEndpointBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */