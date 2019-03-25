/*     */ package org.apache.juli.logging;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ class DirectJDKLog
/*     */   implements Log
/*     */ {
/*     */   public final Logger logger;
/*     */   private static final String SIMPLE_FMT = "java.util.logging.SimpleFormatter";
/*     */   private static final String SIMPLE_CFG = "org.apache.juli.JdkLoggerConfig";
/*     */   private static final String FORMATTER = "org.apache.juli.formatter";
/*     */   
/*     */   static
/*     */   {
/*  40 */     if ((System.getProperty("java.util.logging.config.class") == null) && 
/*  41 */       (System.getProperty("java.util.logging.config.file") == null))
/*     */     {
/*     */       try
/*     */       {
/*  45 */         Class.forName("org.apache.juli.JdkLoggerConfig").getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */       }
/*     */       catch (Throwable localThrowable) {}
/*     */       try
/*     */       {
/*  50 */         Formatter fmt = (Formatter)Class.forName(System.getProperty("org.apache.juli.formatter", "java.util.logging.SimpleFormatter")).getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */         
/*     */ 
/*  53 */         Logger root = Logger.getLogger("");
/*  54 */         for (Handler handler : root.getHandlers())
/*     */         {
/*  56 */           if ((handler instanceof ConsoleHandler)) {
/*  57 */             handler.setFormatter(fmt);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public DirectJDKLog(String name)
/*     */   {
/*  68 */     this.logger = Logger.getLogger(name);
/*     */   }
/*     */   
/*     */   public final boolean isErrorEnabled()
/*     */   {
/*  73 */     return this.logger.isLoggable(Level.SEVERE);
/*     */   }
/*     */   
/*     */   public final boolean isWarnEnabled()
/*     */   {
/*  78 */     return this.logger.isLoggable(Level.WARNING);
/*     */   }
/*     */   
/*     */   public final boolean isInfoEnabled()
/*     */   {
/*  83 */     return this.logger.isLoggable(Level.INFO);
/*     */   }
/*     */   
/*     */   public final boolean isDebugEnabled()
/*     */   {
/*  88 */     return this.logger.isLoggable(Level.FINE);
/*     */   }
/*     */   
/*     */   public final boolean isFatalEnabled()
/*     */   {
/*  93 */     return this.logger.isLoggable(Level.SEVERE);
/*     */   }
/*     */   
/*     */   public final boolean isTraceEnabled()
/*     */   {
/*  98 */     return this.logger.isLoggable(Level.FINER);
/*     */   }
/*     */   
/*     */   public final void debug(Object message)
/*     */   {
/* 103 */     log(Level.FINE, String.valueOf(message), null);
/*     */   }
/*     */   
/*     */   public final void debug(Object message, Throwable t)
/*     */   {
/* 108 */     log(Level.FINE, String.valueOf(message), t);
/*     */   }
/*     */   
/*     */   public final void trace(Object message)
/*     */   {
/* 113 */     log(Level.FINER, String.valueOf(message), null);
/*     */   }
/*     */   
/*     */   public final void trace(Object message, Throwable t)
/*     */   {
/* 118 */     log(Level.FINER, String.valueOf(message), t);
/*     */   }
/*     */   
/*     */   public final void info(Object message)
/*     */   {
/* 123 */     log(Level.INFO, String.valueOf(message), null);
/*     */   }
/*     */   
/*     */   public final void info(Object message, Throwable t)
/*     */   {
/* 128 */     log(Level.INFO, String.valueOf(message), t);
/*     */   }
/*     */   
/*     */   public final void warn(Object message)
/*     */   {
/* 133 */     log(Level.WARNING, String.valueOf(message), null);
/*     */   }
/*     */   
/*     */   public final void warn(Object message, Throwable t)
/*     */   {
/* 138 */     log(Level.WARNING, String.valueOf(message), t);
/*     */   }
/*     */   
/*     */   public final void error(Object message)
/*     */   {
/* 143 */     log(Level.SEVERE, String.valueOf(message), null);
/*     */   }
/*     */   
/*     */   public final void error(Object message, Throwable t)
/*     */   {
/* 148 */     log(Level.SEVERE, String.valueOf(message), t);
/*     */   }
/*     */   
/*     */   public final void fatal(Object message)
/*     */   {
/* 153 */     log(Level.SEVERE, String.valueOf(message), null);
/*     */   }
/*     */   
/*     */   public final void fatal(Object message, Throwable t)
/*     */   {
/* 158 */     log(Level.SEVERE, String.valueOf(message), t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void log(Level level, String msg, Throwable ex)
/*     */   {
/* 167 */     if (this.logger.isLoggable(level))
/*     */     {
/* 169 */       Throwable dummyException = new Throwable();
/* 170 */       StackTraceElement[] locations = dummyException.getStackTrace();
/*     */       
/* 172 */       String cname = "unknown";
/* 173 */       String method = "unknown";
/* 174 */       if ((locations != null) && (locations.length > 2)) {
/* 175 */         StackTraceElement caller = locations[2];
/* 176 */         cname = caller.getClassName();
/* 177 */         method = caller.getMethodName();
/*     */       }
/* 179 */       if (ex == null) {
/* 180 */         this.logger.logp(level, cname, method, msg);
/*     */       } else {
/* 182 */         this.logger.logp(level, cname, method, msg, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static Log getInstance(String name) {
/* 188 */     return new DirectJDKLog(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\logging\DirectJDKLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */