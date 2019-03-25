/*     */ package org.springframework.boot.logging.logback;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.classic.jul.LevelChangePropagator;
/*     */ import ch.qos.logback.classic.spi.TurboFilterList;
/*     */ import ch.qos.logback.classic.turbo.TurboFilter;
/*     */ import ch.qos.logback.classic.util.ContextInitializer;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.spi.FilterReply;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.slf4j.ILoggerFactory;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.impl.StaticLoggerBinder;
/*     */ import org.springframework.boot.logging.AbstractLoggingSystem.LogLevels;
/*     */ import org.springframework.boot.logging.LogFile;
/*     */ import org.springframework.boot.logging.LogLevel;
/*     */ import org.springframework.boot.logging.LoggerConfiguration;
/*     */ import org.springframework.boot.logging.LoggingInitializationContext;
/*     */ import org.springframework.boot.logging.LoggingSystem;
/*     */ import org.springframework.boot.logging.Slf4JLoggingSystem;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class LogbackLoggingSystem
/*     */   extends Slf4JLoggingSystem
/*     */ {
/*     */   private static final String CONFIGURATION_FILE_PROPERTY = "logback.configurationFile";
/*  63 */   private static final AbstractLoggingSystem.LogLevels<Level> LEVELS = new AbstractLoggingSystem.LogLevels();
/*     */   
/*     */   static {
/*  66 */     LEVELS.map(LogLevel.TRACE, Level.TRACE);
/*  67 */     LEVELS.map(LogLevel.TRACE, Level.ALL);
/*  68 */     LEVELS.map(LogLevel.DEBUG, Level.DEBUG);
/*  69 */     LEVELS.map(LogLevel.INFO, Level.INFO);
/*  70 */     LEVELS.map(LogLevel.WARN, Level.WARN);
/*  71 */     LEVELS.map(LogLevel.ERROR, Level.ERROR);
/*  72 */     LEVELS.map(LogLevel.FATAL, Level.ERROR);
/*  73 */     LEVELS.map(LogLevel.OFF, Level.OFF);
/*     */   }
/*     */   
/*  76 */   private static final TurboFilter FILTER = new TurboFilter()
/*     */   {
/*     */ 
/*     */     public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
/*     */     {
/*  81 */       return FilterReply.DENY;
/*     */     }
/*     */   };
/*     */   
/*     */   public LogbackLoggingSystem(ClassLoader classLoader)
/*     */   {
/*  87 */     super(classLoader);
/*     */   }
/*     */   
/*     */   protected String[] getStandardConfigLocations()
/*     */   {
/*  92 */     return new String[] { "logback-test.groovy", "logback-test.xml", "logback.groovy", "logback.xml" };
/*     */   }
/*     */   
/*     */ 
/*     */   public void beforeInitialize()
/*     */   {
/*  98 */     LoggerContext loggerContext = getLoggerContext();
/*  99 */     if (isAlreadyInitialized(loggerContext)) {
/* 100 */       return;
/*     */     }
/* 102 */     super.beforeInitialize();
/* 103 */     loggerContext.getTurboFilterList().add(FILTER);
/* 104 */     configureJBossLoggingToUseSlf4j();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialize(LoggingInitializationContext initializationContext, String configLocation, LogFile logFile)
/*     */   {
/* 110 */     LoggerContext loggerContext = getLoggerContext();
/* 111 */     if (isAlreadyInitialized(loggerContext)) {
/* 112 */       return;
/*     */     }
/* 114 */     loggerContext.getTurboFilterList().remove(FILTER);
/* 115 */     super.initialize(initializationContext, configLocation, logFile);
/* 116 */     markAsInitialized(loggerContext);
/* 117 */     if (StringUtils.hasText(System.getProperty("logback.configurationFile"))) {
/* 118 */       getLogger(LogbackLoggingSystem.class.getName()).warn("Ignoring 'logback.configurationFile' system property. Please use 'logging.config' instead.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile)
/*     */   {
/* 127 */     LoggerContext context = getLoggerContext();
/* 128 */     stopAndReset(context);
/* 129 */     LogbackConfigurator configurator = new LogbackConfigurator(context);
/* 130 */     context.putProperty("LOG_LEVEL_PATTERN", initializationContext
/* 131 */       .getEnvironment().resolvePlaceholders("${logging.pattern.level:${LOG_LEVEL_PATTERN:%5p}}"));
/*     */     
/* 133 */     new DefaultLogbackConfiguration(initializationContext, logFile)
/* 134 */       .apply(configurator);
/* 135 */     context.setPackagingDataEnabled(true);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void loadConfiguration(LoggingInitializationContext initializationContext, String location, LogFile logFile)
/*     */   {
/* 141 */     super.loadConfiguration(initializationContext, location, logFile);
/* 142 */     LoggerContext loggerContext = getLoggerContext();
/* 143 */     stopAndReset(loggerContext);
/*     */     try {
/* 145 */       configureByResourceUrl(initializationContext, loggerContext, 
/* 146 */         ResourceUtils.getURL(location));
/*     */     }
/*     */     catch (Exception ex) {
/* 149 */       throw new IllegalStateException("Could not initialize Logback logging from " + location, ex);
/*     */     }
/*     */     
/* 152 */     List<Status> statuses = loggerContext.getStatusManager().getCopyOfStatusList();
/* 153 */     StringBuilder errors = new StringBuilder();
/* 154 */     for (Status status : statuses) {
/* 155 */       if (status.getLevel() == 2) {
/* 156 */         errors.append(errors.length() > 0 ? String.format("%n", new Object[0]) : "");
/* 157 */         errors.append(status.toString());
/*     */       }
/*     */     }
/* 160 */     if (errors.length() > 0)
/*     */     {
/* 162 */       throw new IllegalStateException(String.format("Logback configuration error detected: %n%s", new Object[] { errors }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureByResourceUrl(LoggingInitializationContext initializationContext, LoggerContext loggerContext, URL url)
/*     */     throws JoranException
/*     */   {
/* 169 */     if (url.toString().endsWith("xml")) {
/* 170 */       JoranConfigurator configurator = new SpringBootJoranConfigurator(initializationContext);
/*     */       
/* 172 */       configurator.setContext(loggerContext);
/* 173 */       configurator.doConfigure(url);
/*     */     }
/*     */     else {
/* 176 */       new ContextInitializer(loggerContext).configureByResource(url);
/*     */     }
/*     */   }
/*     */   
/*     */   private void stopAndReset(LoggerContext loggerContext) {
/* 181 */     loggerContext.stop();
/* 182 */     loggerContext.reset();
/* 183 */     if (isBridgeHandlerAvailable()) {
/* 184 */       addLevelChangePropagator(loggerContext);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addLevelChangePropagator(LoggerContext loggerContext) {
/* 189 */     LevelChangePropagator levelChangePropagator = new LevelChangePropagator();
/* 190 */     levelChangePropagator.setResetJUL(true);
/* 191 */     levelChangePropagator.setContext(loggerContext);
/* 192 */     loggerContext.addListener(levelChangePropagator);
/*     */   }
/*     */   
/*     */   public void cleanUp()
/*     */   {
/* 197 */     LoggerContext context = getLoggerContext();
/* 198 */     markAsUninitialized(context);
/* 199 */     super.cleanUp();
/* 200 */     context.getStatusManager().clear();
/* 201 */     context.getTurboFilterList().remove(FILTER);
/*     */   }
/*     */   
/*     */   protected void reinitialize(LoggingInitializationContext initializationContext)
/*     */   {
/* 206 */     getLoggerContext().reset();
/* 207 */     getLoggerContext().getStatusManager().clear();
/* 208 */     loadConfiguration(initializationContext, getSelfInitializationConfig(), null);
/*     */   }
/*     */   
/*     */   private void configureJBossLoggingToUseSlf4j() {
/* 212 */     System.setProperty("org.jboss.logging.provider", "slf4j");
/*     */   }
/*     */   
/*     */   public List<LoggerConfiguration> getLoggerConfigurations()
/*     */   {
/* 217 */     List<LoggerConfiguration> result = new ArrayList();
/* 218 */     for (Logger logger : getLoggerContext().getLoggerList()) {
/* 219 */       result.add(getLoggerConfiguration(logger));
/*     */     }
/* 221 */     Collections.sort(result, CONFIGURATION_COMPARATOR);
/* 222 */     return result;
/*     */   }
/*     */   
/*     */   public LoggerConfiguration getLoggerConfiguration(String loggerName)
/*     */   {
/* 227 */     return getLoggerConfiguration(getLogger(loggerName));
/*     */   }
/*     */   
/*     */   private LoggerConfiguration getLoggerConfiguration(Logger logger)
/*     */   {
/* 232 */     if (logger == null) {
/* 233 */       return null;
/*     */     }
/* 235 */     LogLevel level = LEVELS.convertNativeToSystem(logger.getLevel());
/*     */     
/* 237 */     LogLevel effectiveLevel = LEVELS.convertNativeToSystem(logger.getEffectiveLevel());
/* 238 */     String name = logger.getName();
/* 239 */     if ((!StringUtils.hasLength(name)) || ("ROOT".equals(name))) {
/* 240 */       name = "ROOT";
/*     */     }
/* 242 */     return new LoggerConfiguration(name, level, effectiveLevel);
/*     */   }
/*     */   
/*     */   public Set<LogLevel> getSupportedLogLevels()
/*     */   {
/* 247 */     return LEVELS.getSupported();
/*     */   }
/*     */   
/*     */   public void setLogLevel(String loggerName, LogLevel level)
/*     */   {
/* 252 */     Logger logger = getLogger(loggerName);
/* 253 */     if (logger != null) {
/* 254 */       logger.setLevel((Level)LEVELS.convertSystemToNative(level));
/*     */     }
/*     */   }
/*     */   
/*     */   public Runnable getShutdownHandler()
/*     */   {
/* 260 */     return new ShutdownHandler(null);
/*     */   }
/*     */   
/*     */   private Logger getLogger(String name) {
/* 264 */     LoggerContext factory = getLoggerContext();
/* 265 */     if ((StringUtils.isEmpty(name)) || ("ROOT".equals(name))) {
/* 266 */       name = "ROOT";
/*     */     }
/* 268 */     return factory.getLogger(name);
/*     */   }
/*     */   
/*     */   private LoggerContext getLoggerContext()
/*     */   {
/* 273 */     ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();
/* 274 */     Assert.isInstanceOf(LoggerContext.class, factory, 
/* 275 */       String.format("LoggerFactory is not a Logback LoggerContext but Logback is on the classpath. Either remove Logback or the competing implementation (%s loaded from %s). If you are using WebLogic you will need to add 'org.slf4j' to prefer-application-packages in WEB-INF/weblogic.xml", new Object[] {factory
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 281 */       .getClass(), getLocation(factory) }));
/* 282 */     return (LoggerContext)factory;
/*     */   }
/*     */   
/*     */   private Object getLocation(ILoggerFactory factory) {
/*     */     try {
/* 287 */       ProtectionDomain protectionDomain = factory.getClass().getProtectionDomain();
/* 288 */       CodeSource codeSource = protectionDomain.getCodeSource();
/* 289 */       if (codeSource != null) {
/* 290 */         return codeSource.getLocation();
/*     */       }
/*     */     }
/*     */     catch (SecurityException localSecurityException) {}
/*     */     
/*     */ 
/* 296 */     return "unknown location";
/*     */   }
/*     */   
/*     */   private boolean isAlreadyInitialized(LoggerContext loggerContext) {
/* 300 */     return loggerContext.getObject(LoggingSystem.class.getName()) != null;
/*     */   }
/*     */   
/*     */   private void markAsInitialized(LoggerContext loggerContext) {
/* 304 */     loggerContext.putObject(LoggingSystem.class.getName(), new Object());
/*     */   }
/*     */   
/*     */   private void markAsUninitialized(LoggerContext loggerContext) {
/* 308 */     loggerContext.removeObject(LoggingSystem.class.getName());
/*     */   }
/*     */   
/*     */   private final class ShutdownHandler implements Runnable {
/*     */     private ShutdownHandler() {}
/*     */     
/*     */     public void run() {
/* 315 */       LogbackLoggingSystem.this.getLoggerContext().stop();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\LogbackLoggingSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */