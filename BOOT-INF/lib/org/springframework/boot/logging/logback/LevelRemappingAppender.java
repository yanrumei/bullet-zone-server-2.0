/*     */ package org.springframework.boot.logging.logback;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*     */ import ch.qos.logback.classic.spi.LoggerContextVO;
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Marker;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class LevelRemappingAppender
/*     */   extends AppenderBase<ILoggingEvent>
/*     */ {
/*  47 */   private static final Map<Level, Level> DEFAULT_REMAPS = Collections.singletonMap(Level.INFO, Level.DEBUG);
/*     */   
/*  49 */   private String destinationLogger = "ROOT";
/*     */   
/*  51 */   private Map<Level, Level> remapLevels = DEFAULT_REMAPS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LevelRemappingAppender() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LevelRemappingAppender(String destinationLogger)
/*     */   {
/*  64 */     this.destinationLogger = destinationLogger;
/*     */   }
/*     */   
/*     */   protected void append(ILoggingEvent event)
/*     */   {
/*  69 */     AppendableLogger logger = getLogger(this.destinationLogger);
/*  70 */     Level remapped = (Level)this.remapLevels.get(event.getLevel());
/*  71 */     logger.callAppenders(remapped == null ? event : new RemappedLoggingEvent(event));
/*     */   }
/*     */   
/*     */   protected AppendableLogger getLogger(String name) {
/*  75 */     LoggerContext loggerContext = (LoggerContext)this.context;
/*  76 */     return new AppendableLogger(loggerContext.getLogger(name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDestinationLogger(String destinationLogger)
/*     */   {
/*  85 */     Assert.hasLength(destinationLogger, "DestinationLogger must not be empty");
/*  86 */     this.destinationLogger = destinationLogger;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemapLevels(String remapLevels)
/*     */   {
/*  95 */     Assert.hasLength(remapLevels, "RemapLevels must not be empty");
/*  96 */     this.remapLevels = new HashMap();
/*  97 */     for (String remap : StringUtils.commaDelimitedListToStringArray(remapLevels)) {
/*  98 */       String[] split = StringUtils.split(remap, "->");
/*  99 */       Assert.notNull(split, "Remap element '" + remap + "' must contain '->'");
/* 100 */       this.remapLevels.put(Level.toLevel(split[0]), Level.toLevel(split[1]));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected static class AppendableLogger
/*     */   {
/*     */     private Logger logger;
/*     */     
/*     */ 
/*     */     public AppendableLogger(Logger logger)
/*     */     {
/* 112 */       this.logger = logger;
/*     */     }
/*     */     
/*     */     public void callAppenders(ILoggingEvent event) {
/* 116 */       if (this.logger.isEnabledFor(event.getLevel())) {
/* 117 */         this.logger.callAppenders(event);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class RemappedLoggingEvent
/*     */     implements ILoggingEvent
/*     */   {
/*     */     private final ILoggingEvent event;
/*     */     
/*     */ 
/*     */     RemappedLoggingEvent(ILoggingEvent event)
/*     */     {
/* 131 */       this.event = event;
/*     */     }
/*     */     
/*     */     public String getThreadName()
/*     */     {
/* 136 */       return this.event.getThreadName();
/*     */     }
/*     */     
/*     */ 
/*     */     public Level getLevel()
/*     */     {
/* 142 */       Level remappedLevel = (Level)LevelRemappingAppender.this.remapLevels.get(this.event.getLevel());
/* 143 */       return remappedLevel == null ? this.event.getLevel() : remappedLevel;
/*     */     }
/*     */     
/*     */     public String getMessage()
/*     */     {
/* 148 */       return this.event.getMessage();
/*     */     }
/*     */     
/*     */     public Object[] getArgumentArray()
/*     */     {
/* 153 */       return this.event.getArgumentArray();
/*     */     }
/*     */     
/*     */     public String getFormattedMessage()
/*     */     {
/* 158 */       return this.event.getFormattedMessage();
/*     */     }
/*     */     
/*     */     public String getLoggerName()
/*     */     {
/* 163 */       return this.event.getLoggerName();
/*     */     }
/*     */     
/*     */     public LoggerContextVO getLoggerContextVO()
/*     */     {
/* 168 */       return this.event.getLoggerContextVO();
/*     */     }
/*     */     
/*     */     public IThrowableProxy getThrowableProxy()
/*     */     {
/* 173 */       return this.event.getThrowableProxy();
/*     */     }
/*     */     
/*     */     public StackTraceElement[] getCallerData()
/*     */     {
/* 178 */       return this.event.getCallerData();
/*     */     }
/*     */     
/*     */     public boolean hasCallerData()
/*     */     {
/* 183 */       return this.event.hasCallerData();
/*     */     }
/*     */     
/*     */     public Marker getMarker()
/*     */     {
/* 188 */       return this.event.getMarker();
/*     */     }
/*     */     
/*     */     public Map<String, String> getMDCPropertyMap()
/*     */     {
/* 193 */       return this.event.getMDCPropertyMap();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Map<String, String> getMdc()
/*     */     {
/* 199 */       return this.event.getMDCPropertyMap();
/*     */     }
/*     */     
/*     */     public long getTimeStamp()
/*     */     {
/* 204 */       return this.event.getTimeStamp();
/*     */     }
/*     */     
/*     */     public void prepareForDeferredProcessing()
/*     */     {
/* 209 */       this.event.prepareForDeferredProcessing();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\LevelRemappingAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */