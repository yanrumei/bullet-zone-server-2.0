/*     */ package org.springframework.boot.logging.logback;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.Appender;
/*     */ import ch.qos.logback.core.ConsoleAppender;
/*     */ import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
/*     */ import ch.qos.logback.core.rolling.RollingFileAppender;
/*     */ import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
/*     */ import ch.qos.logback.core.util.FileSize;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.boot.logging.LogFile;
/*     */ import org.springframework.boot.logging.LoggingInitializationContext;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.env.PropertySourcesPropertyResolver;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ class DefaultLogbackConfiguration
/*     */ {
/*     */   private static final String CONSOLE_LOG_PATTERN = "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";
/*     */   private static final String FILE_LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";
/*  59 */   private static final Charset UTF8 = Charset.forName("UTF-8");
/*     */   
/*     */   private final PropertyResolver patterns;
/*     */   
/*     */   private final LogFile logFile;
/*     */   
/*     */   DefaultLogbackConfiguration(LoggingInitializationContext initializationContext, LogFile logFile)
/*     */   {
/*  67 */     this.patterns = getPatternsResolver(initializationContext.getEnvironment());
/*  68 */     this.logFile = logFile;
/*     */   }
/*     */   
/*     */   private PropertyResolver getPatternsResolver(Environment environment) {
/*  72 */     if (environment == null) {
/*  73 */       return new PropertySourcesPropertyResolver(null);
/*     */     }
/*  75 */     return RelaxedPropertyResolver.ignoringUnresolvableNestedPlaceholders(environment, "logging.pattern.");
/*     */   }
/*     */   
/*     */   public void apply(LogbackConfigurator config)
/*     */   {
/*  80 */     synchronized (config.getConfigurationLock()) {
/*  81 */       base(config);
/*  82 */       Appender<ILoggingEvent> consoleAppender = consoleAppender(config);
/*  83 */       if (this.logFile != null) {
/*  84 */         Appender<ILoggingEvent> fileAppender = fileAppender(config, this.logFile
/*  85 */           .toString());
/*  86 */         config.root(Level.INFO, new Appender[] { consoleAppender, fileAppender });
/*     */       }
/*     */       else {
/*  89 */         config.root(Level.INFO, new Appender[] { consoleAppender });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void base(LogbackConfigurator config) {
/*  95 */     config.conversionRule("clr", ColorConverter.class);
/*  96 */     config.conversionRule("wex", WhitespaceThrowableProxyConverter.class);
/*  97 */     config.conversionRule("wEx", ExtendedWhitespaceThrowableProxyConverter.class);
/*  98 */     LevelRemappingAppender debugRemapAppender = new LevelRemappingAppender("org.springframework.boot");
/*     */     
/* 100 */     config.start(debugRemapAppender);
/* 101 */     config.appender("DEBUG_LEVEL_REMAPPER", debugRemapAppender);
/* 102 */     config.logger("org.apache.catalina.startup.DigesterFactory", Level.ERROR);
/* 103 */     config.logger("org.apache.catalina.util.LifecycleBase", Level.ERROR);
/* 104 */     config.logger("org.apache.coyote.http11.Http11NioProtocol", Level.WARN);
/* 105 */     config.logger("org.apache.sshd.common.util.SecurityUtils", Level.WARN);
/* 106 */     config.logger("org.apache.tomcat.util.net.NioSelectorPool", Level.WARN);
/* 107 */     config.logger("org.crsh.plugin", Level.WARN);
/* 108 */     config.logger("org.crsh.ssh", Level.WARN);
/* 109 */     config.logger("org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR);
/* 110 */     config.logger("org.hibernate.validator.internal.util.Version", Level.WARN);
/* 111 */     config.logger("org.springframework.boot.actuate.autoconfigure.CrshAutoConfiguration", Level.WARN);
/*     */     
/* 113 */     config.logger("org.springframework.boot.actuate.endpoint.jmx", null, false, debugRemapAppender);
/*     */     
/* 115 */     config.logger("org.thymeleaf", null, false, debugRemapAppender);
/*     */   }
/*     */   
/*     */   private Appender<ILoggingEvent> consoleAppender(LogbackConfigurator config) {
/* 119 */     ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender();
/* 120 */     PatternLayoutEncoder encoder = new PatternLayoutEncoder();
/* 121 */     String logPattern = this.patterns.getProperty("console", "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}");
/* 122 */     encoder.setPattern(OptionHelper.substVars(logPattern, config.getContext()));
/* 123 */     encoder.setCharset(UTF8);
/* 124 */     config.start(encoder);
/* 125 */     appender.setEncoder(encoder);
/* 126 */     config.appender("CONSOLE", appender);
/* 127 */     return appender;
/*     */   }
/*     */   
/*     */   private Appender<ILoggingEvent> fileAppender(LogbackConfigurator config, String logFile)
/*     */   {
/* 132 */     RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender();
/* 133 */     PatternLayoutEncoder encoder = new PatternLayoutEncoder();
/* 134 */     String logPattern = this.patterns.getProperty("file", "%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}");
/* 135 */     encoder.setPattern(OptionHelper.substVars(logPattern, config.getContext()));
/* 136 */     appender.setEncoder(encoder);
/* 137 */     config.start(encoder);
/* 138 */     appender.setFile(logFile);
/* 139 */     setRollingPolicy(appender, config, logFile);
/* 140 */     setMaxFileSize(appender, config);
/* 141 */     config.appender("FILE", appender);
/* 142 */     return appender;
/*     */   }
/*     */   
/*     */   private void setRollingPolicy(RollingFileAppender<ILoggingEvent> appender, LogbackConfigurator config, String logFile)
/*     */   {
/* 147 */     FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
/* 148 */     rollingPolicy.setFileNamePattern(logFile + ".%i");
/* 149 */     appender.setRollingPolicy(rollingPolicy);
/* 150 */     rollingPolicy.setParent(appender);
/* 151 */     config.start(rollingPolicy);
/*     */   }
/*     */   
/*     */   private void setMaxFileSize(RollingFileAppender<ILoggingEvent> appender, LogbackConfigurator config)
/*     */   {
/* 156 */     SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy();
/*     */     try {
/* 158 */       triggeringPolicy.setMaxFileSize(FileSize.valueOf("10MB"));
/*     */     }
/*     */     catch (NoSuchMethodError ex)
/*     */     {
/* 162 */       Method method = ReflectionUtils.findMethod(SizeBasedTriggeringPolicy.class, "setMaxFileSize", new Class[] { String.class });
/*     */       
/* 164 */       ReflectionUtils.invokeMethod(method, triggeringPolicy, new Object[] { "10MB" });
/*     */     }
/* 166 */     appender.setTriggeringPolicy(triggeringPolicy);
/* 167 */     config.start(triggeringPolicy);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\DefaultLogbackConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */