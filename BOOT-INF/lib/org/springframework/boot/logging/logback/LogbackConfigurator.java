/*     */ package org.springframework.boot.logging.logback;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.Appender;
/*     */ import ch.qos.logback.core.pattern.Converter;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.spi.PropertyContainer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LogbackConfigurator
/*     */ {
/*     */   private LoggerContext context;
/*     */   
/*     */   LogbackConfigurator(LoggerContext context)
/*     */   {
/*  46 */     Assert.notNull(context, "Context must not be null");
/*  47 */     this.context = context;
/*     */   }
/*     */   
/*     */   public PropertyContainer getContext() {
/*  51 */     return this.context;
/*     */   }
/*     */   
/*     */   public Object getConfigurationLock() {
/*  55 */     return this.context.getConfigurationLock();
/*     */   }
/*     */   
/*     */ 
/*     */   public void conversionRule(String conversionWord, Class<? extends Converter> converterClass)
/*     */   {
/*  61 */     Assert.hasLength(conversionWord, "Conversion word must not be empty");
/*  62 */     Assert.notNull(converterClass, "Converter class must not be null");
/*     */     
/*  64 */     Map<String, String> registry = (Map)this.context.getObject("PATTERN_RULE_REGISTRY");
/*  65 */     if (registry == null) {
/*  66 */       registry = new HashMap();
/*  67 */       this.context.putObject("PATTERN_RULE_REGISTRY", registry);
/*     */     }
/*  69 */     registry.put(conversionWord, converterClass.getName());
/*     */   }
/*     */   
/*     */   public void appender(String name, Appender<?> appender) {
/*  73 */     appender.setName(name);
/*  74 */     start(appender);
/*     */   }
/*     */   
/*     */   public void logger(String name, Level level) {
/*  78 */     logger(name, level, true);
/*     */   }
/*     */   
/*     */   public void logger(String name, Level level, boolean additive) {
/*  82 */     logger(name, level, additive, null);
/*     */   }
/*     */   
/*     */   public void logger(String name, Level level, boolean additive, Appender<ILoggingEvent> appender)
/*     */   {
/*  87 */     Logger logger = this.context.getLogger(name);
/*  88 */     if (level != null) {
/*  89 */       logger.setLevel(level);
/*     */     }
/*  91 */     logger.setAdditive(additive);
/*  92 */     if (appender != null) {
/*  93 */       logger.addAppender(appender);
/*     */     }
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public final void root(Level level, Appender<ILoggingEvent>... appenders) {
/*  99 */     Logger logger = this.context.getLogger("ROOT");
/* 100 */     if (level != null) {
/* 101 */       logger.setLevel(level);
/*     */     }
/* 103 */     for (Appender<ILoggingEvent> appender : appenders) {
/* 104 */       logger.addAppender(appender);
/*     */     }
/*     */   }
/*     */   
/*     */   public void start(LifeCycle lifeCycle) {
/* 109 */     if ((lifeCycle instanceof ContextAware)) {
/* 110 */       ((ContextAware)lifeCycle).setContext(this.context);
/*     */     }
/* 112 */     lifeCycle.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\LogbackConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */