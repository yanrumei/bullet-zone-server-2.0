/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.apache.commons.logging.Log;
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
/*     */ 
/*     */ 
/*     */ public abstract class RemoteInvocationBasedExporter
/*     */   extends RemoteExporter
/*     */ {
/*  35 */   private RemoteInvocationExecutor remoteInvocationExecutor = new DefaultRemoteInvocationExecutor();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoteInvocationExecutor(RemoteInvocationExecutor remoteInvocationExecutor)
/*     */   {
/*  45 */     this.remoteInvocationExecutor = remoteInvocationExecutor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RemoteInvocationExecutor getRemoteInvocationExecutor()
/*     */   {
/*  52 */     return this.remoteInvocationExecutor;
/*     */   }
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
/*     */   protected Object invoke(RemoteInvocation invocation, Object targetObject)
/*     */     throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
/*     */   {
/*  74 */     if (this.logger.isTraceEnabled()) {
/*  75 */       this.logger.trace("Executing " + invocation);
/*     */     }
/*     */     try {
/*  78 */       return getRemoteInvocationExecutor().invoke(invocation, targetObject);
/*     */     }
/*     */     catch (NoSuchMethodException ex) {
/*  81 */       if (this.logger.isDebugEnabled()) {
/*  82 */         this.logger.warn("Could not find target method for " + invocation, ex);
/*     */       }
/*  84 */       throw ex;
/*     */     }
/*     */     catch (IllegalAccessException ex) {
/*  87 */       if (this.logger.isDebugEnabled()) {
/*  88 */         this.logger.warn("Could not access target method for " + invocation, ex);
/*     */       }
/*  90 */       throw ex;
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/*  93 */       if (this.logger.isDebugEnabled()) {
/*  94 */         this.logger.debug("Target method failed for " + invocation, ex.getTargetException());
/*     */       }
/*  96 */       throw ex;
/*     */     }
/*     */   }
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
/*     */   protected RemoteInvocationResult invokeAndCreateResult(RemoteInvocation invocation, Object targetObject)
/*     */   {
/*     */     try
/*     */     {
/* 114 */       Object value = invoke(invocation, targetObject);
/* 115 */       return new RemoteInvocationResult(value);
/*     */     }
/*     */     catch (Throwable ex) {
/* 118 */       return new RemoteInvocationResult(ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\remoting\support\RemoteInvocationBasedExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */