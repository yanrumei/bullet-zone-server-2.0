/*     */ package org.slf4j.bridge;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogManager;
/*     */ import java.util.logging.LogRecord;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.slf4j.spi.LocationAwareLogger;
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
/*     */ public class SLF4JBridgeHandler
/*     */   extends Handler
/*     */ {
/* 102 */   private static final String FQCN = java.util.logging.Logger.class.getName();
/*     */   
/*     */   private static final String UNKNOWN_LOGGER_NAME = "unknown.jul.logger";
/* 105 */   private static final int TRACE_LEVEL_THRESHOLD = Level.FINEST.intValue();
/* 106 */   private static final int DEBUG_LEVEL_THRESHOLD = Level.FINE.intValue();
/* 107 */   private static final int INFO_LEVEL_THRESHOLD = Level.INFO.intValue();
/* 108 */   private static final int WARN_LEVEL_THRESHOLD = Level.WARNING.intValue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void install()
/*     */   {
/* 120 */     LogManager.getLogManager().getLogger("").addHandler(new SLF4JBridgeHandler());
/*     */   }
/*     */   
/*     */   private static java.util.logging.Logger getRootLogger() {
/* 124 */     return LogManager.getLogManager().getLogger("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void uninstall()
/*     */     throws SecurityException
/*     */   {
/* 136 */     java.util.logging.Logger rootLogger = getRootLogger();
/* 137 */     Handler[] handlers = rootLogger.getHandlers();
/* 138 */     for (int i = 0; i < handlers.length; i++) {
/* 139 */       if ((handlers[i] instanceof SLF4JBridgeHandler)) {
/* 140 */         rootLogger.removeHandler(handlers[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isInstalled()
/*     */     throws SecurityException
/*     */   {
/* 152 */     java.util.logging.Logger rootLogger = getRootLogger();
/* 153 */     Handler[] handlers = rootLogger.getHandlers();
/* 154 */     for (int i = 0; i < handlers.length; i++) {
/* 155 */       if ((handlers[i] instanceof SLF4JBridgeHandler)) {
/* 156 */         return true;
/*     */       }
/*     */     }
/* 159 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void removeHandlersForRootLogger()
/*     */   {
/* 167 */     java.util.logging.Logger rootLogger = getRootLogger();
/* 168 */     Handler[] handlers = rootLogger.getHandlers();
/* 169 */     for (int i = 0; i < handlers.length; i++) {
/* 170 */       rootLogger.removeHandler(handlers[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flush() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected org.slf4j.Logger getSLF4JLogger(LogRecord record)
/*     */   {
/* 198 */     String name = record.getLoggerName();
/* 199 */     if (name == null) {
/* 200 */       name = "unknown.jul.logger";
/*     */     }
/* 202 */     return LoggerFactory.getLogger(name);
/*     */   }
/*     */   
/*     */   protected void callLocationAwareLogger(LocationAwareLogger lal, LogRecord record) {
/* 206 */     int julLevelValue = record.getLevel().intValue();
/*     */     int slf4jLevel;
/*     */     int slf4jLevel;
/* 209 */     if (julLevelValue <= TRACE_LEVEL_THRESHOLD) {
/* 210 */       slf4jLevel = 0; } else { int slf4jLevel;
/* 211 */       if (julLevelValue <= DEBUG_LEVEL_THRESHOLD) {
/* 212 */         slf4jLevel = 10; } else { int slf4jLevel;
/* 213 */         if (julLevelValue <= INFO_LEVEL_THRESHOLD) {
/* 214 */           slf4jLevel = 20; } else { int slf4jLevel;
/* 215 */           if (julLevelValue <= WARN_LEVEL_THRESHOLD) {
/* 216 */             slf4jLevel = 30;
/*     */           } else
/* 218 */             slf4jLevel = 40;
/*     */         } } }
/* 220 */     String i18nMessage = getMessageI18N(record);
/* 221 */     lal.log(null, FQCN, slf4jLevel, i18nMessage, null, record.getThrown());
/*     */   }
/*     */   
/*     */   protected void callPlainSLF4JLogger(org.slf4j.Logger slf4jLogger, LogRecord record) {
/* 225 */     String i18nMessage = getMessageI18N(record);
/* 226 */     int julLevelValue = record.getLevel().intValue();
/* 227 */     if (julLevelValue <= TRACE_LEVEL_THRESHOLD) {
/* 228 */       slf4jLogger.trace(i18nMessage, record.getThrown());
/* 229 */     } else if (julLevelValue <= DEBUG_LEVEL_THRESHOLD) {
/* 230 */       slf4jLogger.debug(i18nMessage, record.getThrown());
/* 231 */     } else if (julLevelValue <= INFO_LEVEL_THRESHOLD) {
/* 232 */       slf4jLogger.info(i18nMessage, record.getThrown());
/* 233 */     } else if (julLevelValue <= WARN_LEVEL_THRESHOLD) {
/* 234 */       slf4jLogger.warn(i18nMessage, record.getThrown());
/*     */     } else {
/* 236 */       slf4jLogger.error(i18nMessage, record.getThrown());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getMessageI18N(LogRecord record)
/*     */   {
/* 247 */     String message = record.getMessage();
/*     */     
/* 249 */     if (message == null) {
/* 250 */       return null;
/*     */     }
/*     */     
/* 253 */     ResourceBundle bundle = record.getResourceBundle();
/* 254 */     if (bundle != null) {
/*     */       try {
/* 256 */         message = bundle.getString(message);
/*     */       }
/*     */       catch (MissingResourceException e) {}
/*     */     }
/* 260 */     Object[] params = record.getParameters();
/*     */     
/*     */ 
/* 263 */     if ((params != null) && (params.length > 0)) {
/*     */       try {
/* 265 */         message = MessageFormat.format(message, params);
/*     */       }
/*     */       catch (IllegalArgumentException e)
/*     */       {
/* 269 */         return message;
/*     */       }
/*     */     }
/* 272 */     return message;
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
/*     */   public void publish(LogRecord record)
/*     */   {
/* 289 */     if (record == null) {
/* 290 */       return;
/*     */     }
/*     */     
/* 293 */     org.slf4j.Logger slf4jLogger = getSLF4JLogger(record);
/* 294 */     String message = record.getMessage();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 299 */     if (message == null) {
/* 300 */       message = "";
/*     */     }
/* 302 */     if ((slf4jLogger instanceof LocationAwareLogger)) {
/* 303 */       callLocationAwareLogger((LocationAwareLogger)slf4jLogger, record);
/*     */     } else {
/* 305 */       callPlainSLF4JLogger(slf4jLogger, record);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jul-to-slf4j-1.7.25.jar!\org\slf4j\bridge\SLF4JBridgeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */