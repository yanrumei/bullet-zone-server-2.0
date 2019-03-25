/*     */ package org.springframework.boot.logging;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
/*     */ import org.springframework.boot.context.event.ApplicationFailedEvent;
/*     */ import org.springframework.boot.context.event.ApplicationPreparedEvent;
/*     */ import org.springframework.boot.context.event.ApplicationStartingEvent;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.event.ContextClosedEvent;
/*     */ import org.springframework.context.event.GenericApplicationListener;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class LoggingApplicationListener
/*     */   implements GenericApplicationListener
/*     */ {
/*     */   public static final int DEFAULT_ORDER = -2147483628;
/*     */   public static final String CONFIG_PROPERTY = "logging.config";
/*     */   public static final String REGISTER_SHUTDOWN_HOOK_PROPERTY = "logging.register-shutdown-hook";
/*     */   @Deprecated
/*     */   public static final String PATH_PROPERTY = "logging.path";
/*     */   @Deprecated
/*     */   public static final String FILE_PROPERTY = "logging.file";
/*     */   public static final String PID_KEY = "PID";
/*     */   public static final String EXCEPTION_CONVERSION_WORD = "LOG_EXCEPTION_CONVERSION_WORD";
/*     */   public static final String LOG_FILE = "LOG_FILE";
/*     */   public static final String LOG_PATH = "LOG_PATH";
/*     */   public static final String CONSOLE_LOG_PATTERN = "CONSOLE_LOG_PATTERN";
/*     */   public static final String FILE_LOG_PATTERN = "FILE_LOG_PATTERN";
/*     */   public static final String LOG_LEVEL_PATTERN = "LOG_LEVEL_PATTERN";
/*     */   public static final String LOGGING_SYSTEM_BEAN_NAME = "springBootLoggingSystem";
/*     */   private static MultiValueMap<LogLevel, String> LOG_LEVEL_LOGGERS;
/* 155 */   private static AtomicBoolean shutdownHookRegistered = new AtomicBoolean(false);
/*     */   
/*     */   static {
/* 158 */     LOG_LEVEL_LOGGERS = new LinkedMultiValueMap();
/* 159 */     LOG_LEVEL_LOGGERS.add(LogLevel.DEBUG, "org.springframework.boot");
/* 160 */     LOG_LEVEL_LOGGERS.add(LogLevel.TRACE, "org.springframework");
/* 161 */     LOG_LEVEL_LOGGERS.add(LogLevel.TRACE, "org.apache.tomcat");
/* 162 */     LOG_LEVEL_LOGGERS.add(LogLevel.TRACE, "org.apache.catalina");
/* 163 */     LOG_LEVEL_LOGGERS.add(LogLevel.TRACE, "org.eclipse.jetty");
/* 164 */     LOG_LEVEL_LOGGERS.add(LogLevel.TRACE, "org.hibernate.tool.hbm2ddl");
/* 165 */     LOG_LEVEL_LOGGERS.add(LogLevel.DEBUG, "org.hibernate.SQL");
/*     */   }
/*     */   
/* 168 */   private static Class<?>[] EVENT_TYPES = { ApplicationStartingEvent.class, ApplicationEnvironmentPreparedEvent.class, ApplicationPreparedEvent.class, ContextClosedEvent.class, ApplicationFailedEvent.class };
/*     */   
/*     */ 
/*     */ 
/* 172 */   private static Class<?>[] SOURCE_TYPES = { SpringApplication.class, ApplicationContext.class };
/*     */   
/*     */ 
/* 175 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private LoggingSystem loggingSystem;
/*     */   
/* 179 */   private int order = -2147483628;
/*     */   
/* 181 */   private boolean parseArgs = true;
/*     */   
/* 183 */   private LogLevel springBootLogging = null;
/*     */   
/*     */   public boolean supportsEventType(ResolvableType resolvableType)
/*     */   {
/* 187 */     return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
/*     */   }
/*     */   
/*     */   public boolean supportsSourceType(Class<?> sourceType)
/*     */   {
/* 192 */     return isAssignableFrom(sourceType, SOURCE_TYPES);
/*     */   }
/*     */   
/*     */   private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
/* 196 */     if (type != null) {
/* 197 */       for (Class<?> supportedType : supportedTypes) {
/* 198 */         if (supportedType.isAssignableFrom(type)) {
/* 199 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 203 */     return false;
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event)
/*     */   {
/* 208 */     if ((event instanceof ApplicationStartingEvent)) {
/* 209 */       onApplicationStartingEvent((ApplicationStartingEvent)event);
/*     */     }
/* 211 */     else if ((event instanceof ApplicationEnvironmentPreparedEvent)) {
/* 212 */       onApplicationEnvironmentPreparedEvent((ApplicationEnvironmentPreparedEvent)event);
/*     */ 
/*     */     }
/* 215 */     else if ((event instanceof ApplicationPreparedEvent)) {
/* 216 */       onApplicationPreparedEvent((ApplicationPreparedEvent)event);
/*     */     }
/* 218 */     else if (((event instanceof ContextClosedEvent)) && 
/* 219 */       (((ContextClosedEvent)event).getApplicationContext().getParent() == null)) {
/* 220 */       onContextClosedEvent();
/*     */     }
/* 222 */     else if ((event instanceof ApplicationFailedEvent)) {
/* 223 */       onApplicationFailedEvent();
/*     */     }
/*     */   }
/*     */   
/*     */   private void onApplicationStartingEvent(ApplicationStartingEvent event)
/*     */   {
/* 229 */     this.loggingSystem = LoggingSystem.get(event.getSpringApplication().getClassLoader());
/* 230 */     this.loggingSystem.beforeInitialize();
/*     */   }
/*     */   
/*     */   private void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event)
/*     */   {
/* 235 */     if (this.loggingSystem == null)
/*     */     {
/* 237 */       this.loggingSystem = LoggingSystem.get(event.getSpringApplication().getClassLoader());
/*     */     }
/* 239 */     initialize(event.getEnvironment(), event.getSpringApplication().getClassLoader());
/*     */   }
/*     */   
/*     */   private void onApplicationPreparedEvent(ApplicationPreparedEvent event)
/*     */   {
/* 244 */     ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();
/* 245 */     if (!beanFactory.containsBean("springBootLoggingSystem")) {
/* 246 */       beanFactory.registerSingleton("springBootLoggingSystem", this.loggingSystem);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onContextClosedEvent() {
/* 251 */     if (this.loggingSystem != null) {
/* 252 */       this.loggingSystem.cleanUp();
/*     */     }
/*     */   }
/*     */   
/*     */   private void onApplicationFailedEvent() {
/* 257 */     if (this.loggingSystem != null) {
/* 258 */       this.loggingSystem.cleanUp();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initialize(ConfigurableEnvironment environment, ClassLoader classLoader)
/*     */   {
/* 270 */     new LoggingSystemProperties(environment).apply();
/* 271 */     LogFile logFile = LogFile.get(environment);
/* 272 */     if (logFile != null) {
/* 273 */       logFile.applyToSystemProperties();
/*     */     }
/* 275 */     initializeEarlyLoggingLevel(environment);
/* 276 */     initializeSystem(environment, this.loggingSystem, logFile);
/* 277 */     initializeFinalLoggingLevels(environment, this.loggingSystem);
/* 278 */     registerShutdownHookIfNecessary(environment, this.loggingSystem);
/*     */   }
/*     */   
/*     */   private void initializeEarlyLoggingLevel(ConfigurableEnvironment environment) {
/* 282 */     if ((this.parseArgs) && (this.springBootLogging == null)) {
/* 283 */       if (isSet(environment, "debug")) {
/* 284 */         this.springBootLogging = LogLevel.DEBUG;
/*     */       }
/* 286 */       if (isSet(environment, "trace")) {
/* 287 */         this.springBootLogging = LogLevel.TRACE;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isSet(ConfigurableEnvironment environment, String property) {
/* 293 */     String value = environment.getProperty(property);
/* 294 */     return (value != null) && (!value.equals("false"));
/*     */   }
/*     */   
/*     */   private void initializeSystem(ConfigurableEnvironment environment, LoggingSystem system, LogFile logFile)
/*     */   {
/* 299 */     LoggingInitializationContext initializationContext = new LoggingInitializationContext(environment);
/*     */     
/* 301 */     String logConfig = environment.getProperty("logging.config");
/* 302 */     if (ignoreLogConfig(logConfig)) {
/* 303 */       system.initialize(initializationContext, null, logFile);
/*     */     } else {
/*     */       try
/*     */       {
/* 307 */         ResourceUtils.getURL(logConfig).openStream().close();
/* 308 */         system.initialize(initializationContext, logConfig, logFile);
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 312 */         System.err.println("Logging system failed to initialize using configuration from '" + logConfig + "'");
/*     */         
/* 314 */         ex.printStackTrace(System.err);
/* 315 */         throw new IllegalStateException(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean ignoreLogConfig(String logConfig) {
/* 321 */     return (!StringUtils.hasLength(logConfig)) || (logConfig.startsWith("-D"));
/*     */   }
/*     */   
/*     */   private void initializeFinalLoggingLevels(ConfigurableEnvironment environment, LoggingSystem system)
/*     */   {
/* 326 */     if (this.springBootLogging != null) {
/* 327 */       initializeLogLevel(system, this.springBootLogging);
/*     */     }
/* 329 */     setLogLevels(system, environment);
/*     */   }
/*     */   
/*     */   protected void initializeLogLevel(LoggingSystem system, LogLevel level) {
/* 333 */     List<String> loggers = (List)LOG_LEVEL_LOGGERS.get(level);
/* 334 */     if (loggers != null) {
/* 335 */       for (String logger : loggers) {
/* 336 */         system.setLogLevel(logger, level);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setLogLevels(LoggingSystem system, Environment environment)
/*     */   {
/* 343 */     Map<String, Object> levels = new RelaxedPropertyResolver(environment).getSubProperties("logging.level.");
/* 344 */     boolean rootProcessed = false;
/* 345 */     for (Map.Entry<String, Object> entry : levels.entrySet()) {
/* 346 */       String name = (String)entry.getKey();
/* 347 */       if (name.equalsIgnoreCase("ROOT")) {
/* 348 */         if (!rootProcessed)
/*     */         {
/*     */ 
/* 351 */           name = null;
/* 352 */           rootProcessed = true;
/*     */         }
/* 354 */       } else setLogLevel(system, environment, name, entry.getValue().toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private void setLogLevel(LoggingSystem system, Environment environment, String name, String level)
/*     */   {
/*     */     try {
/* 361 */       level = environment.resolvePlaceholders(level);
/* 362 */       system.setLogLevel(name, coerceLogLevel(level));
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 365 */       this.logger.error("Cannot set level: " + level + " for '" + name + "'");
/*     */     }
/*     */   }
/*     */   
/*     */   private LogLevel coerceLogLevel(String level) {
/* 370 */     if ("false".equalsIgnoreCase(level)) {
/* 371 */       return LogLevel.OFF;
/*     */     }
/* 373 */     return LogLevel.valueOf(level.toUpperCase());
/*     */   }
/*     */   
/*     */ 
/*     */   private void registerShutdownHookIfNecessary(Environment environment, LoggingSystem loggingSystem)
/*     */   {
/* 379 */     boolean registerShutdownHook = ((Boolean)new RelaxedPropertyResolver(environment).getProperty("logging.register-shutdown-hook", Boolean.class, Boolean.valueOf(false))).booleanValue();
/* 380 */     if (registerShutdownHook) {
/* 381 */       Runnable shutdownHandler = loggingSystem.getShutdownHandler();
/* 382 */       if ((shutdownHandler != null) && 
/* 383 */         (shutdownHookRegistered.compareAndSet(false, true))) {
/* 384 */         registerShutdownHook(new Thread(shutdownHandler));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void registerShutdownHook(Thread shutdownHook) {
/* 390 */     Runtime.getRuntime().addShutdownHook(shutdownHook);
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 394 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 399 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpringBootLogging(LogLevel springBootLogging)
/*     */   {
/* 407 */     this.springBootLogging = springBootLogging;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParseArgs(boolean parseArgs)
/*     */   {
/* 416 */     this.parseArgs = parseArgs;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\LoggingApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */