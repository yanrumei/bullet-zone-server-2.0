/*     */ package org.springframework.boot.autoconfigure.logging;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
/*     */ import org.springframework.boot.context.event.ApplicationFailedEvent;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.event.ApplicationContextEvent;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.context.event.GenericApplicationListener;
/*     */ import org.springframework.context.support.GenericApplicationContext;
/*     */ import org.springframework.core.ResolvableType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoConfigurationReportLoggingInitializer
/*     */   implements ApplicationContextInitializer<ConfigurableApplicationContext>
/*     */ {
/*  51 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private ConfigurableApplicationContext applicationContext;
/*     */   
/*     */   private ConditionEvaluationReport report;
/*     */   
/*     */   public void initialize(ConfigurableApplicationContext applicationContext)
/*     */   {
/*  59 */     this.applicationContext = applicationContext;
/*  60 */     applicationContext.addApplicationListener(new AutoConfigurationReportListener(null));
/*  61 */     if ((applicationContext instanceof GenericApplicationContext))
/*     */     {
/*     */ 
/*  64 */       this.report = ConditionEvaluationReport.get(this.applicationContext.getBeanFactory());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onApplicationEvent(ApplicationEvent event) {
/*  69 */     ConfigurableApplicationContext initializerApplicationContext = this.applicationContext;
/*  70 */     if ((event instanceof ContextRefreshedEvent))
/*     */     {
/*  72 */       if (((ApplicationContextEvent)event).getApplicationContext() == initializerApplicationContext) {
/*  73 */         logAutoConfigurationReport();
/*     */       }
/*     */     }
/*  76 */     else if ((event instanceof ApplicationFailedEvent))
/*     */     {
/*  78 */       if (((ApplicationFailedEvent)event).getApplicationContext() == initializerApplicationContext) {
/*  79 */         logAutoConfigurationReport(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void logAutoConfigurationReport() {
/*  85 */     logAutoConfigurationReport(!this.applicationContext.isActive());
/*     */   }
/*     */   
/*     */   public void logAutoConfigurationReport(boolean isCrashReport) {
/*  89 */     if (this.report == null) {
/*  90 */       if (this.applicationContext == null) {
/*  91 */         this.logger.info("Unable to provide auto-configuration report due to missing ApplicationContext");
/*     */         
/*  93 */         return;
/*     */       }
/*     */       
/*  96 */       this.report = ConditionEvaluationReport.get(this.applicationContext.getBeanFactory());
/*     */     }
/*  98 */     if (!this.report.getConditionAndOutcomesBySource().isEmpty()) {
/*  99 */       if ((isCrashReport) && (this.logger.isInfoEnabled()) && 
/* 100 */         (!this.logger.isDebugEnabled())) {
/* 101 */         this.logger.info(
/* 102 */           String.format("%n%nError starting ApplicationContext. To display the auto-configuration report re-run your application with 'debug' enabled.", new Object[0]));
/*     */       }
/*     */       
/*     */ 
/* 106 */       if (this.logger.isDebugEnabled()) {
/* 107 */         this.logger.debug(new ConditionEvaluationReportMessage(this.report));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class AutoConfigurationReportListener implements GenericApplicationListener {
/*     */     private AutoConfigurationReportListener() {}
/*     */     
/*     */     public int getOrder() {
/* 116 */       return Integer.MAX_VALUE;
/*     */     }
/*     */     
/*     */     public boolean supportsEventType(ResolvableType resolvableType)
/*     */     {
/* 121 */       Class<?> type = resolvableType.getRawClass();
/* 122 */       if (type == null) {
/* 123 */         return false;
/*     */       }
/* 125 */       return (ContextRefreshedEvent.class.isAssignableFrom(type)) || 
/* 126 */         (ApplicationFailedEvent.class.isAssignableFrom(type));
/*     */     }
/*     */     
/*     */     public boolean supportsSourceType(Class<?> sourceType)
/*     */     {
/* 131 */       return true;
/*     */     }
/*     */     
/*     */     public void onApplicationEvent(ApplicationEvent event)
/*     */     {
/* 136 */       AutoConfigurationReportLoggingInitializer.this.onApplicationEvent(event);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\logging\AutoConfigurationReportLoggingInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */