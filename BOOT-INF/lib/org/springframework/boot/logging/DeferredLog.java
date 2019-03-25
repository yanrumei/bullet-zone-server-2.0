/*     */ package org.springframework.boot.logging;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class DeferredLog
/*     */   implements Log
/*     */ {
/*  34 */   private List<Line> lines = new ArrayList();
/*     */   
/*     */   public boolean isTraceEnabled()
/*     */   {
/*  38 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled()
/*     */   {
/*  43 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled()
/*     */   {
/*  48 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled()
/*     */   {
/*  53 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled()
/*     */   {
/*  58 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isFatalEnabled()
/*     */   {
/*  63 */     return true;
/*     */   }
/*     */   
/*     */   public void trace(Object message)
/*     */   {
/*  68 */     log(LogLevel.TRACE, message, null);
/*     */   }
/*     */   
/*     */   public void trace(Object message, Throwable t)
/*     */   {
/*  73 */     log(LogLevel.TRACE, message, t);
/*     */   }
/*     */   
/*     */   public void debug(Object message)
/*     */   {
/*  78 */     log(LogLevel.DEBUG, message, null);
/*     */   }
/*     */   
/*     */   public void debug(Object message, Throwable t)
/*     */   {
/*  83 */     log(LogLevel.DEBUG, message, t);
/*     */   }
/*     */   
/*     */   public void info(Object message)
/*     */   {
/*  88 */     log(LogLevel.INFO, message, null);
/*     */   }
/*     */   
/*     */   public void info(Object message, Throwable t)
/*     */   {
/*  93 */     log(LogLevel.INFO, message, t);
/*     */   }
/*     */   
/*     */   public void warn(Object message)
/*     */   {
/*  98 */     log(LogLevel.WARN, message, null);
/*     */   }
/*     */   
/*     */   public void warn(Object message, Throwable t)
/*     */   {
/* 103 */     log(LogLevel.WARN, message, t);
/*     */   }
/*     */   
/*     */   public void error(Object message)
/*     */   {
/* 108 */     log(LogLevel.ERROR, message, null);
/*     */   }
/*     */   
/*     */   public void error(Object message, Throwable t)
/*     */   {
/* 113 */     log(LogLevel.ERROR, message, t);
/*     */   }
/*     */   
/*     */   public void fatal(Object message)
/*     */   {
/* 118 */     log(LogLevel.FATAL, message, null);
/*     */   }
/*     */   
/*     */   public void fatal(Object message, Throwable t)
/*     */   {
/* 123 */     log(LogLevel.FATAL, message, t);
/*     */   }
/*     */   
/*     */   private void log(LogLevel level, Object message, Throwable t) {
/* 127 */     this.lines.add(new Line(level, message, t));
/*     */   }
/*     */   
/*     */   public void replayTo(Class<?> destination) {
/* 131 */     replayTo(LogFactory.getLog(destination));
/*     */   }
/*     */   
/*     */   public void replayTo(Log destination) {
/* 135 */     for (Line line : this.lines) {
/* 136 */       line.replayTo(destination);
/*     */     }
/* 138 */     this.lines.clear();
/*     */   }
/*     */   
/*     */   public static Log replay(Log source, Class<?> destination) {
/* 142 */     return replay(source, LogFactory.getLog(destination));
/*     */   }
/*     */   
/*     */   public static Log replay(Log source, Log destination) {
/* 146 */     if ((source instanceof DeferredLog)) {
/* 147 */       ((DeferredLog)source).replayTo(destination);
/*     */     }
/* 149 */     return destination;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Line
/*     */   {
/*     */     private final LogLevel level;
/*     */     private final Object message;
/*     */     private final Throwable throwable;
/*     */     
/*     */     Line(LogLevel level, Object message, Throwable throwable)
/*     */     {
/* 161 */       this.level = level;
/* 162 */       this.message = message;
/* 163 */       this.throwable = throwable;
/*     */     }
/*     */     
/*     */     public void replayTo(Log log) {
/* 167 */       switch (DeferredLog.1.$SwitchMap$org$springframework$boot$logging$LogLevel[this.level.ordinal()]) {
/*     */       case 1: 
/* 169 */         log.trace(this.message, this.throwable);
/* 170 */         return;
/*     */       case 2: 
/* 172 */         log.debug(this.message, this.throwable);
/* 173 */         return;
/*     */       case 3: 
/* 175 */         log.info(this.message, this.throwable);
/* 176 */         return;
/*     */       case 4: 
/* 178 */         log.warn(this.message, this.throwable);
/* 179 */         return;
/*     */       case 5: 
/* 181 */         log.error(this.message, this.throwable);
/* 182 */         return;
/*     */       case 6: 
/* 184 */         log.fatal(this.message, this.throwable);
/* 185 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\DeferredLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */