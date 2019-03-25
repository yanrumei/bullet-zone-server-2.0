/*     */ package org.jboss.logging;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.logging.LogManager;
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
/*     */ final class LoggerProviders
/*     */ {
/*     */   static final String LOGGING_PROVIDER_KEY = "org.jboss.logging.provider";
/*  31 */   static final LoggerProvider PROVIDER = ;
/*     */   
/*     */   private static LoggerProvider find() {
/*  34 */     return findProvider();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static LoggerProvider findProvider()
/*     */   {
/*  41 */     ClassLoader cl = LoggerProviders.class.getClassLoader();
/*     */     try
/*     */     {
/*  44 */       String loggerProvider = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public String run() {
/*  46 */           return System.getProperty("org.jboss.logging.provider");
/*     */         }
/*     */       });
/*  49 */       if (loggerProvider != null) {
/*  50 */         if ("jboss".equalsIgnoreCase(loggerProvider))
/*  51 */           return tryJBossLogManager(cl, "system property");
/*  52 */         if ("jdk".equalsIgnoreCase(loggerProvider))
/*  53 */           return tryJDK("system property");
/*  54 */         if ("log4j2".equalsIgnoreCase(loggerProvider))
/*  55 */           return tryLog4j2(cl, "system property");
/*  56 */         if ("log4j".equalsIgnoreCase(loggerProvider))
/*  57 */           return tryLog4j(cl, "system property");
/*  58 */         if ("slf4j".equalsIgnoreCase(loggerProvider)) {
/*  59 */           return trySlf4j("system property");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  68 */       ServiceLoader<LoggerProvider> loader = ServiceLoader.load(LoggerProvider.class, cl);
/*  69 */       Iterator<LoggerProvider> iter = loader.iterator();
/*     */       for (;;) {
/*     */         try {
/*  72 */           if (!iter.hasNext()) break;
/*  73 */           LoggerProvider provider = (LoggerProvider)iter.next();
/*     */           
/*  75 */           logProvider(provider, "service loader");
/*  76 */           return provider;
/*     */         }
/*     */         catch (ServiceConfigurationError localServiceConfigurationError) {}
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable1) {}
/*     */     
/*     */     try
/*     */     {
/*  85 */       return tryJBossLogManager(cl, null);
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/*     */       try
/*     */       {
/*  91 */         return tryLog4j2(cl, null);
/*     */       }
/*     */       catch (Throwable localThrowable3)
/*     */       {
/*     */         try {
/*  96 */           return tryLog4j(cl, null);
/*     */         }
/*     */         catch (Throwable localThrowable4)
/*     */         {
/*     */           try
/*     */           {
/* 102 */             Class.forName("ch.qos.logback.classic.Logger", false, cl);
/* 103 */             return trySlf4j(null);
/*     */           } catch (Throwable localThrowable5) {}
/*     */         }
/*     */       } }
/* 107 */     return tryJDK(null);
/*     */   }
/*     */   
/*     */   private static JDKLoggerProvider tryJDK(String via) {
/* 111 */     JDKLoggerProvider provider = new JDKLoggerProvider();
/* 112 */     logProvider(provider, via);
/* 113 */     return provider;
/*     */   }
/*     */   
/*     */   private static LoggerProvider trySlf4j(String via) {
/* 117 */     LoggerProvider provider = new Slf4jLoggerProvider();
/* 118 */     logProvider(provider, via);
/* 119 */     return provider;
/*     */   }
/*     */   
/*     */   private static LoggerProvider tryLog4j2(ClassLoader cl, String via) throws ClassNotFoundException
/*     */   {
/* 124 */     Class.forName("org.apache.logging.log4j.Logger", true, cl);
/* 125 */     Class.forName("org.apache.logging.log4j.LogManager", true, cl);
/* 126 */     Class.forName("org.apache.logging.log4j.spi.AbstractLogger", true, cl);
/* 127 */     LoggerProvider provider = new Log4j2LoggerProvider();
/*     */     
/* 129 */     logProvider(provider, via);
/* 130 */     return provider;
/*     */   }
/*     */   
/*     */   private static LoggerProvider tryLog4j(ClassLoader cl, String via) throws ClassNotFoundException {
/* 134 */     Class.forName("org.apache.log4j.LogManager", true, cl);
/*     */     
/*     */ 
/* 137 */     Class.forName("org.apache.log4j.config.PropertySetter", true, cl);
/* 138 */     LoggerProvider provider = new Log4jLoggerProvider();
/* 139 */     logProvider(provider, via);
/* 140 */     return provider;
/*     */   }
/*     */   
/*     */   private static LoggerProvider tryJBossLogManager(ClassLoader cl, String via) throws ClassNotFoundException {
/* 144 */     Class<? extends LogManager> logManagerClass = LogManager.getLogManager().getClass();
/* 145 */     if ((logManagerClass == Class.forName("org.jboss.logmanager.LogManager", false, cl)) && 
/* 146 */       (Class.forName("org.jboss.logmanager.Logger$AttachmentKey", true, cl).getClassLoader() == logManagerClass.getClassLoader())) {
/* 147 */       LoggerProvider provider = new JBossLogManagerProvider();
/* 148 */       logProvider(provider, via);
/* 149 */       return provider;
/*     */     }
/* 151 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   private static void logProvider(LoggerProvider provider, String via)
/*     */   {
/* 156 */     Logger logger = provider.getLogger(LoggerProviders.class.getPackage().getName());
/* 157 */     if (via == null) {
/* 158 */       logger.debugf("Logging Provider: %s", provider.getClass().getName());
/*     */     } else {
/* 160 */       logger.debugf("Logging Provider: %s found via %s", provider.getClass().getName(), via);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\LoggerProviders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */