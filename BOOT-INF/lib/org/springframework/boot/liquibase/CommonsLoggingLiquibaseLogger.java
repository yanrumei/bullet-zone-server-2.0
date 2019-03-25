/*     */ package org.springframework.boot.liquibase;
/*     */ 
/*     */ import liquibase.logging.LogLevel;
/*     */ import liquibase.logging.core.AbstractLogger;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class CommonsLoggingLiquibaseLogger
/*     */   extends AbstractLogger
/*     */ {
/*     */   public static final int PRIORITY = 10;
/*     */   private Log logger;
/*     */   
/*     */   public void setName(String name)
/*     */   {
/*  44 */     this.logger = createLogger(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Log createLogger(String name)
/*     */   {
/*  53 */     return LogFactory.getLog(name);
/*     */   }
/*     */   
/*     */   public void setLogLevel(String logLevel, String logFile)
/*     */   {
/*  58 */     super.setLogLevel(logLevel);
/*     */   }
/*     */   
/*     */   public void severe(String message)
/*     */   {
/*  63 */     if (isEnabled(LogLevel.SEVERE)) {
/*  64 */       this.logger.error(buildMessage(message));
/*     */     }
/*     */   }
/*     */   
/*     */   public void severe(String message, Throwable e)
/*     */   {
/*  70 */     if (isEnabled(LogLevel.SEVERE)) {
/*  71 */       this.logger.error(buildMessage(message), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warning(String message)
/*     */   {
/*  77 */     if (isEnabled(LogLevel.WARNING)) {
/*  78 */       this.logger.warn(buildMessage(message));
/*     */     }
/*     */   }
/*     */   
/*     */   public void warning(String message, Throwable e)
/*     */   {
/*  84 */     if (isEnabled(LogLevel.WARNING)) {
/*  85 */       this.logger.warn(buildMessage(message), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String message)
/*     */   {
/*  91 */     if (isEnabled(LogLevel.INFO)) {
/*  92 */       this.logger.info(buildMessage(message));
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String message, Throwable e)
/*     */   {
/*  98 */     if (isEnabled(LogLevel.INFO)) {
/*  99 */       this.logger.info(buildMessage(message), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String message)
/*     */   {
/* 105 */     if (isEnabled(LogLevel.DEBUG)) {
/* 106 */       this.logger.debug(buildMessage(message));
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String message, Throwable e)
/*     */   {
/* 112 */     if (isEnabled(LogLevel.DEBUG)) {
/* 113 */       this.logger.debug(buildMessage(message), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getPriority()
/*     */   {
/* 119 */     return 10;
/*     */   }
/*     */   
/*     */   private boolean isEnabled(LogLevel level) {
/* 123 */     if (this.logger != null) {
/* 124 */       switch (level) {
/*     */       case DEBUG: 
/* 126 */         return this.logger.isDebugEnabled();
/*     */       case INFO: 
/* 128 */         return this.logger.isInfoEnabled();
/*     */       case WARNING: 
/* 130 */         return this.logger.isWarnEnabled();
/*     */       case SEVERE: 
/* 132 */         return this.logger.isErrorEnabled();
/*     */       }
/*     */     }
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\liquibase\CommonsLoggingLiquibaseLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */