/*    */ package org.springframework.boot.logging;
/*    */ 
/*    */ import org.slf4j.bridge.SLF4JBridgeHandler;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public abstract class Slf4JLoggingSystem
/*    */   extends AbstractLoggingSystem
/*    */ {
/*    */   private static final String BRIDGE_HANDLER = "org.slf4j.bridge.SLF4JBridgeHandler";
/*    */   
/*    */   public Slf4JLoggingSystem(ClassLoader classLoader)
/*    */   {
/* 35 */     super(classLoader);
/*    */   }
/*    */   
/*    */   public void beforeInitialize()
/*    */   {
/* 40 */     super.beforeInitialize();
/* 41 */     configureJdkLoggingBridgeHandler();
/*    */   }
/*    */   
/*    */   public void cleanUp()
/*    */   {
/* 46 */     removeJdkLoggingBridgeHandler();
/*    */   }
/*    */   
/*    */ 
/*    */   protected void loadConfiguration(LoggingInitializationContext initializationContext, String location, LogFile logFile)
/*    */   {
/* 52 */     Assert.notNull(location, "Location must not be null");
/* 53 */     if (initializationContext != null) {
/* 54 */       applySystemProperties(initializationContext.getEnvironment(), logFile);
/*    */     }
/*    */   }
/*    */   
/*    */   private void configureJdkLoggingBridgeHandler() {
/*    */     try {
/* 60 */       if (isBridgeHandlerAvailable()) {
/* 61 */         removeJdkLoggingBridgeHandler();
/* 62 */         SLF4JBridgeHandler.install();
/*    */       }
/*    */     }
/*    */     catch (Throwable localThrowable) {}
/*    */   }
/*    */   
/*    */ 
/*    */   protected final boolean isBridgeHandlerAvailable()
/*    */   {
/* 71 */     return ClassUtils.isPresent("org.slf4j.bridge.SLF4JBridgeHandler", getClassLoader());
/*    */   }
/*    */   
/*    */   private void removeJdkLoggingBridgeHandler() {
/*    */     try {
/* 76 */       if (isBridgeHandlerAvailable()) {
/*    */         try {
/* 78 */           SLF4JBridgeHandler.removeHandlersForRootLogger();
/*    */         }
/*    */         catch (NoSuchMethodError ex)
/*    */         {
/* 82 */           SLF4JBridgeHandler.uninstall();
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (Throwable localThrowable) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\Slf4JLoggingSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */