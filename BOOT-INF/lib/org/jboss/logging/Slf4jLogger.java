/*    */ package org.jboss.logging;
/*    */ 
/*    */ import java.text.MessageFormat;
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
/*    */ final class Slf4jLogger
/*    */   extends Logger
/*    */ {
/*    */   private static final long serialVersionUID = 8685757928087758380L;
/*    */   private final org.slf4j.Logger logger;
/*    */   
/*    */   Slf4jLogger(String name, org.slf4j.Logger logger)
/*    */   {
/* 30 */     super(name);
/* 31 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public boolean isEnabled(Logger.Level level) {
/* 35 */     if (level == Logger.Level.TRACE)
/* 36 */       return this.logger.isTraceEnabled();
/* 37 */     if (level == Logger.Level.DEBUG) {
/* 38 */       return this.logger.isDebugEnabled();
/*    */     }
/* 40 */     return infoOrHigherEnabled(level);
/*    */   }
/*    */   
/*    */   private boolean infoOrHigherEnabled(Logger.Level level) {
/* 44 */     if (level == Logger.Level.INFO)
/* 45 */       return this.logger.isInfoEnabled();
/* 46 */     if (level == Logger.Level.WARN)
/* 47 */       return this.logger.isWarnEnabled();
/* 48 */     if ((level == Logger.Level.ERROR) || (level == Logger.Level.FATAL)) {
/* 49 */       return this.logger.isErrorEnabled();
/*    */     }
/* 51 */     return true;
/*    */   }
/*    */   
/*    */   protected void doLog(Logger.Level level, String loggerClassName, Object message, Object[] parameters, Throwable thrown) {
/* 55 */     if (isEnabled(level)) {
/* 56 */       try { String text = (parameters == null) || (parameters.length == 0) ? String.valueOf(message) : MessageFormat.format(String.valueOf(message), parameters);
/* 57 */         if (level == Logger.Level.INFO) {
/* 58 */           this.logger.info(text, thrown);
/* 59 */         } else if (level == Logger.Level.WARN) {
/* 60 */           this.logger.warn(text, thrown);
/* 61 */         } else if ((level == Logger.Level.ERROR) || (level == Logger.Level.FATAL)) {
/* 62 */           this.logger.error(text, thrown);
/* 63 */         } else if (level == Logger.Level.DEBUG) {
/* 64 */           this.logger.debug(text, thrown);
/* 65 */         } else if (level == Logger.Level.TRACE)
/* 66 */           this.logger.debug(text, thrown);
/*    */       } catch (Throwable localThrowable) {}
/*    */     }
/*    */   }
/*    */   
/*    */   protected void doLogf(Logger.Level level, String loggerClassName, String format, Object[] parameters, Throwable thrown) {
/* 72 */     if (isEnabled(level)) {
/* 73 */       try { String text = parameters == null ? String.format(format, new Object[0]) : String.format(format, parameters);
/* 74 */         if (level == Logger.Level.INFO) {
/* 75 */           this.logger.info(text, thrown);
/* 76 */         } else if (level == Logger.Level.WARN) {
/* 77 */           this.logger.warn(text, thrown);
/* 78 */         } else if ((level == Logger.Level.ERROR) || (level == Logger.Level.FATAL)) {
/* 79 */           this.logger.error(text, thrown);
/* 80 */         } else if (level == Logger.Level.DEBUG) {
/* 81 */           this.logger.debug(text, thrown);
/* 82 */         } else if (level == Logger.Level.TRACE) {
/* 83 */           this.logger.debug(text, thrown);
/*    */         }
/*    */       }
/*    */       catch (Throwable localThrowable) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\Slf4jLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */