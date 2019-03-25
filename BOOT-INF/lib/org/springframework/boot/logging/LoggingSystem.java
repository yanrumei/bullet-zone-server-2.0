/*     */ package org.springframework.boot.logging;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public abstract class LoggingSystem
/*     */ {
/*  42 */   public static final String SYSTEM_PROPERTY = LoggingSystem.class.getName();
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String NONE = "none";
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String ROOT_LOGGER_NAME = "ROOT";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final Map<String, String> SYSTEMS;
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  60 */     Map<String, String> systems = new LinkedHashMap();
/*  61 */     systems.put("ch.qos.logback.core.Appender", "org.springframework.boot.logging.logback.LogbackLoggingSystem");
/*     */     
/*  63 */     systems.put("org.apache.logging.log4j.core.impl.Log4jContextFactory", "org.springframework.boot.logging.log4j2.Log4J2LoggingSystem");
/*     */     
/*  65 */     systems.put("java.util.logging.LogManager", "org.springframework.boot.logging.java.JavaLoggingSystem");
/*     */     
/*  67 */     SYSTEMS = Collections.unmodifiableMap(systems);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Runnable getShutdownHandler()
/*     */   {
/* 103 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<LogLevel> getSupportedLogLevels()
/*     */   {
/* 112 */     return EnumSet.allOf(LogLevel.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLogLevel(String loggerName, LogLevel level)
/*     */   {
/* 122 */     throw new UnsupportedOperationException("Unable to set log level");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<LoggerConfiguration> getLoggerConfigurations()
/*     */   {
/* 132 */     throw new UnsupportedOperationException("Unable to get logger configurations");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoggerConfiguration getLoggerConfiguration(String loggerName)
/*     */   {
/* 142 */     throw new UnsupportedOperationException("Unable to get logger configuration");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LoggingSystem get(ClassLoader classLoader)
/*     */   {
/* 151 */     String loggingSystem = System.getProperty(SYSTEM_PROPERTY);
/* 152 */     if (StringUtils.hasLength(loggingSystem)) {
/* 153 */       if ("none".equals(loggingSystem)) {
/* 154 */         return new NoOpLoggingSystem();
/*     */       }
/* 156 */       return get(classLoader, loggingSystem);
/*     */     }
/* 158 */     for (Map.Entry<String, String> entry : SYSTEMS.entrySet()) {
/* 159 */       if (ClassUtils.isPresent((String)entry.getKey(), classLoader)) {
/* 160 */         return get(classLoader, (String)entry.getValue());
/*     */       }
/*     */     }
/* 163 */     throw new IllegalStateException("No suitable logging system located");
/*     */   }
/*     */   
/*     */   private static LoggingSystem get(ClassLoader classLoader, String loggingSystemClass) {
/*     */     try {
/* 168 */       Class<?> systemClass = ClassUtils.forName(loggingSystemClass, classLoader);
/* 169 */       return 
/* 170 */         (LoggingSystem)systemClass.getConstructor(new Class[] { ClassLoader.class }).newInstance(new Object[] { classLoader });
/*     */     }
/*     */     catch (Exception ex) {
/* 173 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void beforeInitialize();
/*     */   
/*     */ 
/*     */   public void initialize(LoggingInitializationContext initializationContext, String configLocation, LogFile logFile) {}
/*     */   
/*     */   public void cleanUp() {}
/*     */   
/*     */   static class NoOpLoggingSystem
/*     */     extends LoggingSystem
/*     */   {
/*     */     public void beforeInitialize() {}
/*     */     
/*     */     public void setLogLevel(String loggerName, LogLevel level) {}
/*     */     
/*     */     public List<LoggerConfiguration> getLoggerConfigurations()
/*     */     {
/* 194 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */     public LoggerConfiguration getLoggerConfiguration(String loggerName)
/*     */     {
/* 199 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\LoggingSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */