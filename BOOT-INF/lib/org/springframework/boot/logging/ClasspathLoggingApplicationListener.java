/*    */ package org.springframework.boot.logging;
/*    */ 
/*    */ import java.net.URLClassLoader;
/*    */ import java.util.Arrays;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
/*    */ import org.springframework.boot.context.event.ApplicationFailedEvent;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.event.GenericApplicationListener;
/*    */ import org.springframework.core.ResolvableType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClasspathLoggingApplicationListener
/*    */   implements GenericApplicationListener
/*    */ {
/*    */   private static final int ORDER = -2147483627;
/* 46 */   private static final Log logger = LogFactory.getLog(ClasspathLoggingApplicationListener.class);
/*    */   
/*    */   public void onApplicationEvent(ApplicationEvent event)
/*    */   {
/* 50 */     if (logger.isDebugEnabled()) {
/* 51 */       if ((event instanceof ApplicationEnvironmentPreparedEvent)) {
/* 52 */         logger.debug("Application started with classpath: " + getClasspath());
/*    */       }
/* 54 */       else if ((event instanceof ApplicationFailedEvent)) {
/* 55 */         logger.debug("Application failed to start with classpath: " + 
/* 56 */           getClasspath());
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 63 */     return -2147483627;
/*    */   }
/*    */   
/*    */   public boolean supportsEventType(ResolvableType resolvableType)
/*    */   {
/* 68 */     Class<?> type = resolvableType.getRawClass();
/* 69 */     if (type == null) {
/* 70 */       return false;
/*    */     }
/* 72 */     return (ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(type)) || 
/* 73 */       (ApplicationFailedEvent.class.isAssignableFrom(type));
/*    */   }
/*    */   
/*    */   public boolean supportsSourceType(Class<?> sourceType)
/*    */   {
/* 78 */     return true;
/*    */   }
/*    */   
/*    */   private String getClasspath() {
/* 82 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 83 */     if ((classLoader instanceof URLClassLoader)) {
/* 84 */       return Arrays.toString(((URLClassLoader)classLoader).getURLs());
/*    */     }
/* 86 */     return "unknown";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\ClasspathLoggingApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */