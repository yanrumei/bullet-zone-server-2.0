/*    */ package org.jboss.logging;
/*    */ 
/*    */ import org.jboss.logmanager.ExtLogRecord.FormatStyle;
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
/*    */ final class JBossLogManagerLogger
/*    */   extends Logger
/*    */ {
/*    */   private static final long serialVersionUID = 7429618317727584742L;
/*    */   private final org.jboss.logmanager.Logger logger;
/*    */   
/*    */   JBossLogManagerLogger(String name, org.jboss.logmanager.Logger logger)
/*    */   {
/* 30 */     super(name);
/* 31 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public boolean isEnabled(Logger.Level level) {
/* 35 */     return this.logger.isLoggable(translate(level));
/*    */   }
/*    */   
/*    */   protected void doLog(Logger.Level level, String loggerClassName, Object message, Object[] parameters, Throwable thrown) {
/* 39 */     java.util.logging.Level translatedLevel = translate(level);
/* 40 */     if (this.logger.isLoggable(translatedLevel)) {
/* 41 */       if (parameters == null) {
/* 42 */         this.logger.log(loggerClassName, translatedLevel, String.valueOf(message), thrown);
/*    */       } else {
/* 44 */         this.logger.log(loggerClassName, translatedLevel, String.valueOf(message), ExtLogRecord.FormatStyle.MESSAGE_FORMAT, parameters, thrown);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected void doLogf(Logger.Level level, String loggerClassName, String format, Object[] parameters, Throwable thrown) {
/* 50 */     if (parameters == null) {
/* 51 */       this.logger.log(loggerClassName, translate(level), format, thrown);
/*    */     } else {
/* 53 */       this.logger.log(loggerClassName, translate(level), format, ExtLogRecord.FormatStyle.PRINTF, parameters, thrown);
/*    */     }
/*    */   }
/*    */   
/*    */   private static java.util.logging.Level translate(Logger.Level level) {
/* 58 */     if (level == Logger.Level.TRACE)
/* 59 */       return org.jboss.logmanager.Level.TRACE;
/* 60 */     if (level == Logger.Level.DEBUG) {
/* 61 */       return org.jboss.logmanager.Level.DEBUG;
/*    */     }
/* 63 */     return infoOrHigher(level);
/*    */   }
/*    */   
/*    */   private static java.util.logging.Level infoOrHigher(Logger.Level level) {
/* 67 */     if (level == Logger.Level.INFO)
/* 68 */       return org.jboss.logmanager.Level.INFO;
/* 69 */     if (level == Logger.Level.WARN)
/* 70 */       return org.jboss.logmanager.Level.WARN;
/* 71 */     if (level == Logger.Level.ERROR)
/* 72 */       return org.jboss.logmanager.Level.ERROR;
/* 73 */     if (level == Logger.Level.FATAL) {
/* 74 */       return org.jboss.logmanager.Level.FATAL;
/*    */     }
/* 76 */     return org.jboss.logmanager.Level.ALL;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\JBossLogManagerLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */