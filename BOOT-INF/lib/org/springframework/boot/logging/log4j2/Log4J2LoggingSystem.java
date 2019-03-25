/*     */ package org.springframework.boot.logging.log4j2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Filter.Result;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilter;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.springframework.boot.logging.AbstractLoggingSystem.LogLevels;
/*     */ import org.springframework.boot.logging.LogFile;
/*     */ import org.springframework.boot.logging.LogLevel;
/*     */ import org.springframework.boot.logging.LoggerConfiguration;
/*     */ import org.springframework.boot.logging.LoggingInitializationContext;
/*     */ import org.springframework.boot.logging.LoggingSystem;
/*     */ import org.springframework.boot.logging.Slf4JLoggingSystem;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ public class Log4J2LoggingSystem
/*     */   extends Slf4JLoggingSystem
/*     */ {
/*     */   private static final String FILE_PROTOCOL = "file";
/*  65 */   private static final AbstractLoggingSystem.LogLevels<Level> LEVELS = new AbstractLoggingSystem.LogLevels();
/*     */   
/*     */   static {
/*  68 */     LEVELS.map(LogLevel.TRACE, Level.TRACE);
/*  69 */     LEVELS.map(LogLevel.DEBUG, Level.DEBUG);
/*  70 */     LEVELS.map(LogLevel.INFO, Level.INFO);
/*  71 */     LEVELS.map(LogLevel.WARN, Level.WARN);
/*  72 */     LEVELS.map(LogLevel.ERROR, Level.ERROR);
/*  73 */     LEVELS.map(LogLevel.FATAL, Level.FATAL);
/*  74 */     LEVELS.map(LogLevel.OFF, Level.OFF);
/*     */   }
/*     */   
/*  77 */   private static final Filter FILTER = new AbstractFilter()
/*     */   {
/*     */     public Filter.Result filter(LogEvent event)
/*     */     {
/*  81 */       return Filter.Result.DENY;
/*     */     }
/*     */     
/*     */ 
/*     */     public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t)
/*     */     {
/*  87 */       return Filter.Result.DENY;
/*     */     }
/*     */     
/*     */ 
/*     */     public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t)
/*     */     {
/*  93 */       return Filter.Result.DENY;
/*     */     }
/*     */     
/*     */ 
/*     */     public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params)
/*     */     {
/*  99 */       return Filter.Result.DENY;
/*     */     }
/*     */   };
/*     */   
/*     */   public Log4J2LoggingSystem(ClassLoader classLoader)
/*     */   {
/* 105 */     super(classLoader);
/*     */   }
/*     */   
/*     */   protected String[] getStandardConfigLocations()
/*     */   {
/* 110 */     return getCurrentlySupportedConfigLocations();
/*     */   }
/*     */   
/*     */   private String[] getCurrentlySupportedConfigLocations() {
/* 114 */     List<String> supportedConfigLocations = new ArrayList();
/* 115 */     if (isClassAvailable("com.fasterxml.jackson.dataformat.yaml.YAMLParser")) {
/* 116 */       Collections.addAll(supportedConfigLocations, new String[] { "log4j2.yaml", "log4j2.yml" });
/*     */     }
/* 118 */     if (isClassAvailable("com.fasterxml.jackson.databind.ObjectMapper")) {
/* 119 */       Collections.addAll(supportedConfigLocations, new String[] { "log4j2.json", "log4j2.jsn" });
/*     */     }
/* 121 */     supportedConfigLocations.add("log4j2.xml");
/* 122 */     return 
/* 123 */       (String[])supportedConfigLocations.toArray(new String[supportedConfigLocations.size()]);
/*     */   }
/*     */   
/*     */   protected boolean isClassAvailable(String className) {
/* 127 */     return ClassUtils.isPresent(className, getClassLoader());
/*     */   }
/*     */   
/*     */   public void beforeInitialize()
/*     */   {
/* 132 */     LoggerContext loggerContext = getLoggerContext();
/* 133 */     if (isAlreadyInitialized(loggerContext)) {
/* 134 */       return;
/*     */     }
/* 136 */     super.beforeInitialize();
/* 137 */     loggerContext.getConfiguration().addFilter(FILTER);
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialize(LoggingInitializationContext initializationContext, String configLocation, LogFile logFile)
/*     */   {
/* 143 */     LoggerContext loggerContext = getLoggerContext();
/* 144 */     if (isAlreadyInitialized(loggerContext)) {
/* 145 */       return;
/*     */     }
/* 147 */     loggerContext.getConfiguration().removeFilter(FILTER);
/* 148 */     super.initialize(initializationContext, configLocation, logFile);
/* 149 */     markAsInitialized(loggerContext);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile)
/*     */   {
/* 155 */     if (logFile != null) {
/* 156 */       loadConfiguration(getPackagedConfigFile("log4j2-file.xml"), logFile);
/*     */     }
/*     */     else {
/* 159 */       loadConfiguration(getPackagedConfigFile("log4j2.xml"), logFile);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void loadConfiguration(LoggingInitializationContext initializationContext, String location, LogFile logFile)
/*     */   {
/* 166 */     super.loadConfiguration(initializationContext, location, logFile);
/* 167 */     loadConfiguration(location, logFile);
/*     */   }
/*     */   
/*     */   protected void loadConfiguration(String location, LogFile logFile) {
/* 171 */     Assert.notNull(location, "Location must not be null");
/*     */     try {
/* 173 */       LoggerContext ctx = getLoggerContext();
/* 174 */       URL url = ResourceUtils.getURL(location);
/* 175 */       ConfigurationSource source = getConfigurationSource(url);
/* 176 */       ctx.start(ConfigurationFactory.getInstance().getConfiguration(ctx, source));
/*     */     }
/*     */     catch (Exception ex) {
/* 179 */       throw new IllegalStateException("Could not initialize Log4J2 logging from " + location, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private ConfigurationSource getConfigurationSource(URL url) throws IOException
/*     */   {
/* 185 */     InputStream stream = url.openStream();
/* 186 */     if ("file".equals(url.getProtocol())) {
/* 187 */       return new ConfigurationSource(stream, ResourceUtils.getFile(url));
/*     */     }
/* 189 */     return new ConfigurationSource(stream, url);
/*     */   }
/*     */   
/*     */   protected void reinitialize(LoggingInitializationContext initializationContext)
/*     */   {
/* 194 */     getLoggerContext().reconfigure();
/*     */   }
/*     */   
/*     */   public Set<LogLevel> getSupportedLogLevels()
/*     */   {
/* 199 */     return LEVELS.getSupported();
/*     */   }
/*     */   
/*     */   public void setLogLevel(String loggerName, LogLevel logLevel)
/*     */   {
/* 204 */     Level level = (Level)LEVELS.convertSystemToNative(logLevel);
/* 205 */     LoggerConfig loggerConfig = getLoggerConfig(loggerName);
/* 206 */     if (loggerConfig == null) {
/* 207 */       loggerConfig = new LoggerConfig(loggerName, level, true);
/* 208 */       getLoggerContext().getConfiguration().addLogger(loggerName, loggerConfig);
/*     */     }
/*     */     else {
/* 211 */       loggerConfig.setLevel(level);
/*     */     }
/* 213 */     getLoggerContext().updateLoggers();
/*     */   }
/*     */   
/*     */   public List<LoggerConfiguration> getLoggerConfigurations()
/*     */   {
/* 218 */     List<LoggerConfiguration> result = new ArrayList();
/* 219 */     Configuration configuration = getLoggerContext().getConfiguration();
/* 220 */     for (LoggerConfig loggerConfig : configuration.getLoggers().values()) {
/* 221 */       result.add(convertLoggerConfiguration(loggerConfig));
/*     */     }
/* 223 */     Collections.sort(result, CONFIGURATION_COMPARATOR);
/* 224 */     return result;
/*     */   }
/*     */   
/*     */   public LoggerConfiguration getLoggerConfiguration(String loggerName)
/*     */   {
/* 229 */     return convertLoggerConfiguration(getLoggerConfig(loggerName));
/*     */   }
/*     */   
/*     */   private LoggerConfiguration convertLoggerConfiguration(LoggerConfig loggerConfig) {
/* 233 */     if (loggerConfig == null) {
/* 234 */       return null;
/*     */     }
/* 236 */     LogLevel level = LEVELS.convertNativeToSystem(loggerConfig.getLevel());
/* 237 */     String name = loggerConfig.getName();
/* 238 */     if ((!StringUtils.hasLength(name)) || ("".equals(name))) {
/* 239 */       name = "ROOT";
/*     */     }
/* 241 */     return new LoggerConfiguration(name, level, level);
/*     */   }
/*     */   
/*     */   public Runnable getShutdownHandler()
/*     */   {
/* 246 */     return new ShutdownHandler(null);
/*     */   }
/*     */   
/*     */   public void cleanUp()
/*     */   {
/* 251 */     super.cleanUp();
/* 252 */     LoggerContext loggerContext = getLoggerContext();
/* 253 */     markAsUninitialized(loggerContext);
/* 254 */     loggerContext.getConfiguration().removeFilter(FILTER);
/*     */   }
/*     */   
/*     */   private LoggerConfig getLoggerConfig(String name) {
/* 258 */     if ((!StringUtils.hasLength(name)) || ("ROOT".equals(name))) {
/* 259 */       name = "";
/*     */     }
/* 261 */     return (LoggerConfig)getLoggerContext().getConfiguration().getLoggers().get(name);
/*     */   }
/*     */   
/*     */   private LoggerContext getLoggerContext() {
/* 265 */     return (LoggerContext)LogManager.getContext(false);
/*     */   }
/*     */   
/*     */   private boolean isAlreadyInitialized(LoggerContext loggerContext) {
/* 269 */     return LoggingSystem.class.getName().equals(loggerContext.getExternalContext());
/*     */   }
/*     */   
/*     */   private void markAsInitialized(LoggerContext loggerContext) {
/* 273 */     loggerContext.setExternalContext(LoggingSystem.class.getName());
/*     */   }
/*     */   
/*     */   private void markAsUninitialized(LoggerContext loggerContext) {
/* 277 */     loggerContext.setExternalContext(null);
/*     */   }
/*     */   
/*     */   private final class ShutdownHandler implements Runnable {
/*     */     private ShutdownHandler() {}
/*     */     
/*     */     public void run() {
/* 284 */       Log4J2LoggingSystem.this.getLoggerContext().stop();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\log4j2\Log4J2LoggingSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */