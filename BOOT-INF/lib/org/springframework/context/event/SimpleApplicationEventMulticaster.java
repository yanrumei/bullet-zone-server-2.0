/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.util.ErrorHandler;
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
/*     */ public class SimpleApplicationEventMulticaster
/*     */   extends AbstractApplicationEventMulticaster
/*     */ {
/*     */   private Executor taskExecutor;
/*     */   private ErrorHandler errorHandler;
/*     */   
/*     */   public SimpleApplicationEventMulticaster() {}
/*     */   
/*     */   public SimpleApplicationEventMulticaster(BeanFactory beanFactory)
/*     */   {
/*  65 */     setBeanFactory(beanFactory);
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
/*     */   public void setTaskExecutor(Executor taskExecutor)
/*     */   {
/*  82 */     this.taskExecutor = taskExecutor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Executor getTaskExecutor()
/*     */   {
/*  89 */     return this.taskExecutor;
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
/*     */   public void setErrorHandler(ErrorHandler errorHandler)
/*     */   {
/* 108 */     this.errorHandler = errorHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ErrorHandler getErrorHandler()
/*     */   {
/* 116 */     return this.errorHandler;
/*     */   }
/*     */   
/*     */ 
/*     */   public void multicastEvent(ApplicationEvent event)
/*     */   {
/* 122 */     multicastEvent(event, resolveDefaultEventType(event));
/*     */   }
/*     */   
/*     */   public void multicastEvent(final ApplicationEvent event, ResolvableType eventType)
/*     */   {
/* 127 */     ResolvableType type = eventType != null ? eventType : resolveDefaultEventType(event);
/* 128 */     for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) {
/* 129 */       Executor executor = getTaskExecutor();
/* 130 */       if (executor != null) {
/* 131 */         executor.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 134 */             SimpleApplicationEventMulticaster.this.invokeListener(listener, event);
/*     */           }
/*     */           
/*     */         });
/*     */       } else {
/* 139 */         invokeListener(listener, event);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private ResolvableType resolveDefaultEventType(ApplicationEvent event) {
/* 145 */     return ResolvableType.forInstance(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event)
/*     */   {
/* 155 */     ErrorHandler errorHandler = getErrorHandler();
/* 156 */     if (errorHandler != null) {
/*     */       try {
/* 158 */         doInvokeListener(listener, event);
/*     */       }
/*     */       catch (Throwable err) {
/* 161 */         errorHandler.handleError(err);
/*     */       }
/*     */       
/*     */     } else {
/* 165 */       doInvokeListener(listener, event);
/*     */     }
/*     */   }
/*     */   
/*     */   private void doInvokeListener(ApplicationListener listener, ApplicationEvent event)
/*     */   {
/*     */     try {
/* 172 */       listener.onApplicationEvent(event);
/*     */     }
/*     */     catch (ClassCastException ex) {
/* 175 */       String msg = ex.getMessage();
/* 176 */       if ((msg == null) || (msg.startsWith(event.getClass().getName())))
/*     */       {
/* 178 */         Log logger = LogFactory.getLog(getClass());
/* 179 */         if (logger.isDebugEnabled()) {
/* 180 */           logger.debug("Non-matching event type for listener: " + listener, ex);
/*     */         }
/*     */       }
/*     */       else {
/* 184 */         throw ex;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\SimpleApplicationEventMulticaster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */