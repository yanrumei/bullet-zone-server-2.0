/*     */ package org.jboss.logging;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.text.MessageFormat;
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
/*     */ final class Slf4jLocationAwareLogger
/*     */   extends Logger
/*     */ {
/*     */   private static final long serialVersionUID = 8685757928087758380L;
/*  31 */   private static final Object[] EMPTY = new Object[0];
/*     */   private static final boolean POST_1_6;
/*     */   private static final Method LOG_METHOD;
/*     */   private final LocationAwareLogger logger;
/*     */   
/*  36 */   static { Method[] methods = LocationAwareLogger.class.getDeclaredMethods();
/*  37 */     Method logMethod = null;
/*  38 */     boolean post16 = false;
/*  39 */     for (Method method : methods) {
/*  40 */       if (method.getName().equals("log")) {
/*  41 */         logMethod = method;
/*  42 */         Class<?>[] parameterTypes = method.getParameterTypes();
/*  43 */         post16 = parameterTypes.length == 6;
/*     */       }
/*     */     }
/*  46 */     if (logMethod == null) {
/*  47 */       throw new NoSuchMethodError("Cannot find LocationAwareLogger.log() method");
/*     */     }
/*  49 */     POST_1_6 = post16;
/*  50 */     LOG_METHOD = logMethod;
/*     */   }
/*     */   
/*     */ 
/*     */   Slf4jLocationAwareLogger(String name, LocationAwareLogger logger)
/*     */   {
/*  56 */     super(name);
/*  57 */     this.logger = logger;
/*     */   }
/*     */   
/*     */   public boolean isEnabled(Logger.Level level) {
/*  61 */     if (level != null) switch (level) {
/*  62 */       case FATAL:  return this.logger.isErrorEnabled();
/*  63 */       case ERROR:  return this.logger.isErrorEnabled();
/*  64 */       case WARN:  return this.logger.isWarnEnabled();
/*  65 */       case INFO:  return this.logger.isInfoEnabled();
/*  66 */       case DEBUG:  return this.logger.isDebugEnabled();
/*  67 */       case TRACE:  return this.logger.isTraceEnabled();
/*     */       }
/*  69 */     return true;
/*     */   }
/*     */   
/*     */   protected void doLog(Logger.Level level, String loggerClassName, Object message, Object[] parameters, Throwable thrown) {
/*  73 */     if (isEnabled(level)) {
/*  74 */       String text = (parameters == null) || (parameters.length == 0) ? String.valueOf(message) : MessageFormat.format(String.valueOf(message), parameters);
/*  75 */       doLog(this.logger, loggerClassName, translate(level), text, thrown);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doLogf(Logger.Level level, String loggerClassName, String format, Object[] parameters, Throwable thrown) {
/*  80 */     if (isEnabled(level)) {
/*  81 */       String text = parameters == null ? String.format(format, new Object[0]) : String.format(format, parameters);
/*  82 */       doLog(this.logger, loggerClassName, translate(level), text, thrown);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void doLog(LocationAwareLogger logger, String className, int level, String text, Throwable thrown) {
/*     */     try {
/*  88 */       if (POST_1_6) {
/*  89 */         LOG_METHOD.invoke(logger, new Object[] { null, className, Integer.valueOf(level), text, EMPTY, thrown });
/*     */       } else {
/*  91 */         LOG_METHOD.invoke(logger, new Object[] { null, className, Integer.valueOf(level), text, thrown });
/*     */       }
/*     */     } catch (InvocationTargetException e) {
/*     */       try {
/*  95 */         throw e.getCause();
/*     */       } catch (RuntimeException ex) {
/*  97 */         throw ex;
/*     */       } catch (Error er) {
/*  99 */         throw er;
/*     */       } catch (Throwable throwable) {
/* 101 */         throw new UndeclaredThrowableException(throwable);
/*     */       }
/*     */     } catch (IllegalAccessException e) {
/* 104 */       throw new IllegalAccessError(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private static int translate(Logger.Level level) {
/* 109 */     if (level != null) switch (level) {
/*     */       case FATAL: case ERROR: 
/* 111 */         return 40;
/* 112 */       case WARN:  return 30;
/* 113 */       case INFO:  return 20;
/* 114 */       case DEBUG:  return 10;
/* 115 */       case TRACE:  return 0;
/*     */       }
/* 117 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\Slf4jLocationAwareLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */