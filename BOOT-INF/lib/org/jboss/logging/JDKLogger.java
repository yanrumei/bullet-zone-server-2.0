/*    */ package org.jboss.logging;
/*    */ 
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ import java.util.logging.Level;
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
/*    */ final class JDKLogger
/*    */   extends Logger
/*    */ {
/*    */   private static final long serialVersionUID = 2563174097983721393L;
/*    */   private final transient java.util.logging.Logger logger;
/*    */   
/*    */   public JDKLogger(String name)
/*    */   {
/* 32 */     super(name);
/* 33 */     this.logger = java.util.logging.Logger.getLogger(name);
/*    */   }
/*    */   
/*    */   protected void doLog(Logger.Level level, String loggerClassName, Object message, Object[] parameters, Throwable thrown) {
/* 37 */     if (isEnabled(level))
/* 38 */       try { JBossLogRecord rec = new JBossLogRecord(translate(level), String.valueOf(message), loggerClassName);
/* 39 */         if (thrown != null) rec.setThrown(thrown);
/* 40 */         rec.setLoggerName(getName());
/* 41 */         rec.setParameters(parameters);
/* 42 */         rec.setResourceBundleName(this.logger.getResourceBundleName());
/* 43 */         rec.setResourceBundle(this.logger.getResourceBundle());
/* 44 */         this.logger.log(rec);
/*    */       } catch (Throwable localThrowable) {}
/*    */   }
/*    */   
/*    */   protected void doLogf(Logger.Level level, String loggerClassName, String format, Object[] parameters, Throwable thrown) {
/* 49 */     if (isEnabled(level))
/* 50 */       try { ResourceBundle resourceBundle = this.logger.getResourceBundle();
/* 51 */         if (resourceBundle != null) {
/* 52 */           try { format = resourceBundle.getString(format);
/*    */           }
/*    */           catch (MissingResourceException localMissingResourceException) {}
/*    */         }
/* 56 */         String msg = parameters == null ? String.format(format, new Object[0]) : String.format(format, parameters);
/* 57 */         JBossLogRecord rec = new JBossLogRecord(translate(level), msg, loggerClassName);
/* 58 */         if (thrown != null) rec.setThrown(thrown);
/* 59 */         rec.setLoggerName(getName());
/* 60 */         rec.setResourceBundleName(this.logger.getResourceBundleName());
/*    */         
/* 62 */         rec.setResourceBundle(null);
/* 63 */         rec.setParameters(null);
/* 64 */         this.logger.log(rec);
/*    */       } catch (Throwable localThrowable) {}
/*    */   }
/*    */   
/*    */   private static Level translate(Logger.Level level) {
/* 69 */     if (level == Logger.Level.TRACE)
/* 70 */       return JDKLevel.TRACE;
/* 71 */     if (level == Logger.Level.DEBUG) {
/* 72 */       return JDKLevel.DEBUG;
/*    */     }
/* 74 */     return infoOrHigher(level);
/*    */   }
/*    */   
/*    */   private static Level infoOrHigher(Logger.Level level) {
/* 78 */     if (level == Logger.Level.INFO)
/* 79 */       return JDKLevel.INFO;
/* 80 */     if (level == Logger.Level.WARN)
/* 81 */       return JDKLevel.WARN;
/* 82 */     if (level == Logger.Level.ERROR)
/* 83 */       return JDKLevel.ERROR;
/* 84 */     if (level == Logger.Level.FATAL) {
/* 85 */       return JDKLevel.FATAL;
/*    */     }
/* 87 */     return JDKLevel.ALL;
/*    */   }
/*    */   
/*    */   public boolean isEnabled(Logger.Level level) {
/* 91 */     return this.logger.isLoggable(translate(level));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\JDKLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */