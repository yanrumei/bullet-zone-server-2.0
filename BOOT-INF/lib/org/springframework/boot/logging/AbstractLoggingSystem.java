/*     */ package org.springframework.boot.logging;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.SystemPropertyUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLoggingSystem
/*     */   extends LoggingSystem
/*     */ {
/*  39 */   protected static final Comparator<LoggerConfiguration> CONFIGURATION_COMPARATOR = new LoggerConfigurationComparator("ROOT");
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   public AbstractLoggingSystem(ClassLoader classLoader)
/*     */   {
/*  45 */     this.classLoader = classLoader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void beforeInitialize() {}
/*     */   
/*     */ 
/*     */   public void initialize(LoggingInitializationContext initializationContext, String configLocation, LogFile logFile)
/*     */   {
/*  55 */     if (StringUtils.hasLength(configLocation)) {
/*  56 */       initializeWithSpecificConfig(initializationContext, configLocation, logFile);
/*  57 */       return;
/*     */     }
/*  59 */     initializeWithConventions(initializationContext, logFile);
/*     */   }
/*     */   
/*     */ 
/*     */   private void initializeWithSpecificConfig(LoggingInitializationContext initializationContext, String configLocation, LogFile logFile)
/*     */   {
/*  65 */     configLocation = SystemPropertyUtils.resolvePlaceholders(configLocation);
/*  66 */     loadConfiguration(initializationContext, configLocation, logFile);
/*     */   }
/*     */   
/*     */   private void initializeWithConventions(LoggingInitializationContext initializationContext, LogFile logFile)
/*     */   {
/*  71 */     String config = getSelfInitializationConfig();
/*  72 */     if ((config != null) && (logFile == null))
/*     */     {
/*  74 */       reinitialize(initializationContext);
/*  75 */       return;
/*     */     }
/*  77 */     if (config == null) {
/*  78 */       config = getSpringInitializationConfig();
/*     */     }
/*  80 */     if (config != null) {
/*  81 */       loadConfiguration(initializationContext, config, logFile);
/*  82 */       return;
/*     */     }
/*  84 */     loadDefaults(initializationContext, logFile);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getSelfInitializationConfig()
/*     */   {
/*  94 */     return findConfig(getStandardConfigLocations());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getSpringInitializationConfig()
/*     */   {
/* 103 */     return findConfig(getSpringConfigLocations());
/*     */   }
/*     */   
/*     */   private String findConfig(String[] locations) {
/* 107 */     for (String location : locations) {
/* 108 */       ClassPathResource resource = new ClassPathResource(location, this.classLoader);
/*     */       
/* 110 */       if (resource.exists()) {
/* 111 */         return "classpath:" + location;
/*     */       }
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String[] getStandardConfigLocations();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] getSpringConfigLocations()
/*     */   {
/* 131 */     String[] locations = getStandardConfigLocations();
/* 132 */     for (int i = 0; i < locations.length; i++) {
/* 133 */       String extension = StringUtils.getFilenameExtension(locations[i]);
/* 134 */       locations[i] = (locations[i].substring(0, locations[i]
/* 135 */         .length() - extension.length() - 1) + "-spring." + extension);
/*     */     }
/*     */     
/*     */ 
/* 138 */     return locations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void loadDefaults(LoggingInitializationContext paramLoggingInitializationContext, LogFile paramLogFile);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void loadConfiguration(LoggingInitializationContext paramLoggingInitializationContext, String paramString, LogFile paramLogFile);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void reinitialize(LoggingInitializationContext initializationContext) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ClassLoader getClassLoader()
/*     */   {
/* 170 */     return this.classLoader;
/*     */   }
/*     */   
/*     */   protected final String getPackagedConfigFile(String fileName) {
/* 174 */     String defaultPath = ClassUtils.getPackageName(getClass());
/* 175 */     defaultPath = defaultPath.replace('.', '/');
/* 176 */     defaultPath = defaultPath + "/" + fileName;
/* 177 */     defaultPath = "classpath:" + defaultPath;
/* 178 */     return defaultPath;
/*     */   }
/*     */   
/*     */   protected final void applySystemProperties(Environment environment, LogFile logFile) {
/* 182 */     new LoggingSystemProperties(environment).apply(logFile);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class LogLevels<T>
/*     */   {
/*     */     private final Map<LogLevel, T> systemToNative;
/*     */     
/*     */ 
/*     */     private final Map<T, LogLevel> nativeToSystem;
/*     */     
/*     */ 
/*     */     public LogLevels()
/*     */     {
/* 197 */       this.systemToNative = new HashMap();
/* 198 */       this.nativeToSystem = new HashMap();
/*     */     }
/*     */     
/*     */     public void map(LogLevel system, T nativeLevel) {
/* 202 */       if (!this.systemToNative.containsKey(system)) {
/* 203 */         this.systemToNative.put(system, nativeLevel);
/*     */       }
/* 205 */       if (!this.nativeToSystem.containsKey(nativeLevel)) {
/* 206 */         this.nativeToSystem.put(nativeLevel, system);
/*     */       }
/*     */     }
/*     */     
/*     */     public LogLevel convertNativeToSystem(T level) {
/* 211 */       return (LogLevel)this.nativeToSystem.get(level);
/*     */     }
/*     */     
/*     */     public T convertSystemToNative(LogLevel level) {
/* 215 */       return (T)this.systemToNative.get(level);
/*     */     }
/*     */     
/*     */     public Set<LogLevel> getSupported() {
/* 219 */       return new LinkedHashSet(this.nativeToSystem.values());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\AbstractLoggingSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */