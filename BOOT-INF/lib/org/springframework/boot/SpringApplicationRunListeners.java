/*    */ package org.springframework.boot;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ class SpringApplicationRunListeners
/*    */ {
/*    */   private final Log log;
/*    */   private final List<SpringApplicationRunListener> listeners;
/*    */   
/*    */   SpringApplicationRunListeners(Log log, Collection<? extends SpringApplicationRunListener> listeners)
/*    */   {
/* 42 */     this.log = log;
/* 43 */     this.listeners = new ArrayList(listeners);
/*    */   }
/*    */   
/*    */   public void starting() {
/* 47 */     for (SpringApplicationRunListener listener : this.listeners) {
/* 48 */       listener.starting();
/*    */     }
/*    */   }
/*    */   
/*    */   public void environmentPrepared(ConfigurableEnvironment environment) {
/* 53 */     for (SpringApplicationRunListener listener : this.listeners) {
/* 54 */       listener.environmentPrepared(environment);
/*    */     }
/*    */   }
/*    */   
/*    */   public void contextPrepared(ConfigurableApplicationContext context) {
/* 59 */     for (SpringApplicationRunListener listener : this.listeners) {
/* 60 */       listener.contextPrepared(context);
/*    */     }
/*    */   }
/*    */   
/*    */   public void contextLoaded(ConfigurableApplicationContext context) {
/* 65 */     for (SpringApplicationRunListener listener : this.listeners) {
/* 66 */       listener.contextLoaded(context);
/*    */     }
/*    */   }
/*    */   
/*    */   public void finished(ConfigurableApplicationContext context, Throwable exception) {
/* 71 */     for (SpringApplicationRunListener listener : this.listeners) {
/* 72 */       callFinishedListener(listener, context, exception);
/*    */     }
/*    */   }
/*    */   
/*    */   private void callFinishedListener(SpringApplicationRunListener listener, ConfigurableApplicationContext context, Throwable exception)
/*    */   {
/*    */     try {
/* 79 */       listener.finished(context, exception);
/*    */     }
/*    */     catch (Throwable ex) {
/* 82 */       if (exception == null) {
/* 83 */         ReflectionUtils.rethrowRuntimeException(ex);
/*    */       }
/* 85 */       if (this.log.isDebugEnabled()) {
/* 86 */         this.log.error("Error handling failed", ex);
/*    */       }
/*    */       else {
/* 89 */         String message = ex.getMessage();
/* 90 */         message = message == null ? "no error message" : message;
/* 91 */         this.log.warn("Error handling failed (" + message + ")");
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\SpringApplicationRunListeners.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */