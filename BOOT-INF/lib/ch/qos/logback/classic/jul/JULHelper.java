/*    */ package ch.qos.logback.classic.jul;
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
/*    */ public class JULHelper
/*    */ {
/*    */   public static final boolean isRegularNonRootLogger(java.util.logging.Logger julLogger)
/*    */   {
/* 22 */     if (julLogger == null)
/* 23 */       return false;
/* 24 */     return !julLogger.getName().equals("");
/*    */   }
/*    */   
/*    */   public static final boolean isRoot(java.util.logging.Logger julLogger) {
/* 28 */     if (julLogger == null)
/* 29 */       return false;
/* 30 */     return julLogger.getName().equals("");
/*    */   }
/*    */   
/*    */   public static java.util.logging.Level asJULLevel(ch.qos.logback.classic.Level lbLevel) {
/* 34 */     if (lbLevel == null) {
/* 35 */       throw new IllegalArgumentException("Unexpected level [null]");
/*    */     }
/* 37 */     switch (lbLevel.levelInt) {
/*    */     case -2147483648: 
/* 39 */       return java.util.logging.Level.ALL;
/*    */     case 5000: 
/* 41 */       return java.util.logging.Level.FINEST;
/*    */     case 10000: 
/* 43 */       return java.util.logging.Level.FINE;
/*    */     case 20000: 
/* 45 */       return java.util.logging.Level.INFO;
/*    */     case 30000: 
/* 47 */       return java.util.logging.Level.WARNING;
/*    */     case 40000: 
/* 49 */       return java.util.logging.Level.SEVERE;
/*    */     case 2147483647: 
/* 51 */       return java.util.logging.Level.OFF;
/*    */     }
/* 53 */     throw new IllegalArgumentException("Unexpected level [" + lbLevel + "]");
/*    */   }
/*    */   
/*    */   public static String asJULLoggerName(String loggerName)
/*    */   {
/* 58 */     if ("ROOT".equals(loggerName)) {
/* 59 */       return "";
/*    */     }
/* 61 */     return loggerName;
/*    */   }
/*    */   
/*    */   public static java.util.logging.Logger asJULLogger(String loggerName) {
/* 65 */     String julLoggerName = asJULLoggerName(loggerName);
/* 66 */     return java.util.logging.Logger.getLogger(julLoggerName);
/*    */   }
/*    */   
/*    */   public static java.util.logging.Logger asJULLogger(ch.qos.logback.classic.Logger logger) {
/* 70 */     return asJULLogger(logger.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\jul\JULHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */