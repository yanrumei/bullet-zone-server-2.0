/*     */ package org.springframework.boot.logging.java;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogManager;
/*     */ import java.util.logging.Logger;
/*     */ import org.springframework.boot.logging.AbstractLoggingSystem;
/*     */ import org.springframework.boot.logging.AbstractLoggingSystem.LogLevels;
/*     */ import org.springframework.boot.logging.LogFile;
/*     */ import org.springframework.boot.logging.LogLevel;
/*     */ import org.springframework.boot.logging.LoggerConfiguration;
/*     */ import org.springframework.boot.logging.LoggingInitializationContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ public class JavaLoggingSystem
/*     */   extends AbstractLoggingSystem
/*     */ {
/*  51 */   private static final AbstractLoggingSystem.LogLevels<Level> LEVELS = new AbstractLoggingSystem.LogLevels();
/*     */   
/*     */   static {
/*  54 */     LEVELS.map(LogLevel.TRACE, Level.FINEST);
/*  55 */     LEVELS.map(LogLevel.DEBUG, Level.FINE);
/*  56 */     LEVELS.map(LogLevel.INFO, Level.INFO);
/*  57 */     LEVELS.map(LogLevel.WARN, Level.WARNING);
/*  58 */     LEVELS.map(LogLevel.ERROR, Level.SEVERE);
/*  59 */     LEVELS.map(LogLevel.FATAL, Level.SEVERE);
/*  60 */     LEVELS.map(LogLevel.OFF, Level.OFF);
/*     */   }
/*     */   
/*     */   public JavaLoggingSystem(ClassLoader classLoader) {
/*  64 */     super(classLoader);
/*     */   }
/*     */   
/*     */   protected String[] getStandardConfigLocations()
/*     */   {
/*  69 */     return new String[] { "logging.properties" };
/*     */   }
/*     */   
/*     */   public void beforeInitialize()
/*     */   {
/*  74 */     super.beforeInitialize();
/*  75 */     Logger.getLogger("").setLevel(Level.SEVERE);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile)
/*     */   {
/*  81 */     if (logFile != null) {
/*  82 */       loadConfiguration(getPackagedConfigFile("logging-file.properties"), logFile);
/*     */     }
/*     */     else {
/*  85 */       loadConfiguration(getPackagedConfigFile("logging.properties"), logFile);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void loadConfiguration(LoggingInitializationContext initializationContext, String location, LogFile logFile)
/*     */   {
/*  92 */     loadConfiguration(location, logFile);
/*     */   }
/*     */   
/*     */   protected void loadConfiguration(String location, LogFile logFile) {
/*  96 */     Assert.notNull(location, "Location must not be null");
/*     */     try {
/*  98 */       String configuration = FileCopyUtils.copyToString(new InputStreamReader(
/*  99 */         ResourceUtils.getURL(location).openStream()));
/* 100 */       if (logFile != null) {
/* 101 */         configuration = configuration.replace("${LOG_FILE}", 
/* 102 */           StringUtils.cleanPath(logFile.toString()));
/*     */       }
/* 104 */       LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(configuration
/* 105 */         .getBytes()));
/*     */     }
/*     */     catch (Exception ex) {
/* 108 */       throw new IllegalStateException("Could not initialize Java logging from " + location, ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<LogLevel> getSupportedLogLevels()
/*     */   {
/* 115 */     return LEVELS.getSupported();
/*     */   }
/*     */   
/*     */   public void setLogLevel(String loggerName, LogLevel level)
/*     */   {
/* 120 */     Assert.notNull(level, "Level must not be null");
/* 121 */     if ((loggerName == null) || ("ROOT".equals(loggerName))) {
/* 122 */       loggerName = "";
/*     */     }
/* 124 */     Logger logger = Logger.getLogger(loggerName);
/* 125 */     if (logger != null) {
/* 126 */       logger.setLevel((Level)LEVELS.convertSystemToNative(level));
/*     */     }
/*     */   }
/*     */   
/*     */   public List<LoggerConfiguration> getLoggerConfigurations()
/*     */   {
/* 132 */     List<LoggerConfiguration> result = new ArrayList();
/* 133 */     Enumeration<String> names = LogManager.getLogManager().getLoggerNames();
/* 134 */     while (names.hasMoreElements()) {
/* 135 */       result.add(getLoggerConfiguration((String)names.nextElement()));
/*     */     }
/* 137 */     Collections.sort(result, CONFIGURATION_COMPARATOR);
/* 138 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */   
/*     */   public LoggerConfiguration getLoggerConfiguration(String loggerName)
/*     */   {
/* 143 */     Logger logger = Logger.getLogger(loggerName);
/* 144 */     if (logger == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     LogLevel level = LEVELS.convertNativeToSystem(logger.getLevel());
/* 148 */     LogLevel effectiveLevel = LEVELS.convertNativeToSystem(getEffectiveLevel(logger));
/* 149 */     String name = StringUtils.hasLength(logger.getName()) ? logger.getName() : "ROOT";
/*     */     
/* 151 */     return new LoggerConfiguration(name, level, effectiveLevel);
/*     */   }
/*     */   
/*     */   private Level getEffectiveLevel(Logger root) {
/* 155 */     Logger logger = root;
/* 156 */     while (logger.getLevel() == null) {
/* 157 */       logger = logger.getParent();
/*     */     }
/* 159 */     return logger.getLevel();
/*     */   }
/*     */   
/*     */   public Runnable getShutdownHandler()
/*     */   {
/* 164 */     return new ShutdownHandler(null);
/*     */   }
/*     */   
/*     */   private final class ShutdownHandler implements Runnable {
/*     */     private ShutdownHandler() {}
/*     */     
/*     */     public void run() {
/* 171 */       LogManager.getLogManager().reset();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\java\JavaLoggingSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */