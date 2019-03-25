/*     */ package org.springframework.boot.context.event;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.boot.SpringApplicationRunListener;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.event.SimpleApplicationEventMulticaster;
/*     */ import org.springframework.context.support.AbstractApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.util.ErrorHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventPublishingRunListener
/*     */   implements SpringApplicationRunListener, Ordered
/*     */ {
/*     */   private final SpringApplication application;
/*     */   private final String[] args;
/*     */   private final SimpleApplicationEventMulticaster initialMulticaster;
/*     */   
/*     */   public EventPublishingRunListener(SpringApplication application, String[] args)
/*     */   {
/*  52 */     this.application = application;
/*  53 */     this.args = args;
/*  54 */     this.initialMulticaster = new SimpleApplicationEventMulticaster();
/*  55 */     for (ApplicationListener<?> listener : application.getListeners()) {
/*  56 */       this.initialMulticaster.addApplicationListener(listener);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  62 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void starting()
/*     */   {
/*  69 */     this.initialMulticaster.multicastEvent(new ApplicationStartedEvent(this.application, this.args));
/*     */   }
/*     */   
/*     */   public void environmentPrepared(ConfigurableEnvironment environment)
/*     */   {
/*  74 */     this.initialMulticaster.multicastEvent(new ApplicationEnvironmentPreparedEvent(this.application, this.args, environment));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void contextPrepared(ConfigurableApplicationContext context) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void contextLoaded(ConfigurableApplicationContext context)
/*     */   {
/*  85 */     for (ApplicationListener<?> listener : this.application.getListeners()) {
/*  86 */       if ((listener instanceof ApplicationContextAware)) {
/*  87 */         ((ApplicationContextAware)listener).setApplicationContext(context);
/*     */       }
/*  89 */       context.addApplicationListener(listener);
/*     */     }
/*  91 */     this.initialMulticaster.multicastEvent(new ApplicationPreparedEvent(this.application, this.args, context));
/*     */   }
/*     */   
/*     */ 
/*     */   public void finished(ConfigurableApplicationContext context, Throwable exception)
/*     */   {
/*  97 */     SpringApplicationEvent event = getFinishedEvent(context, exception);
/*  98 */     if ((context != null) && (context.isActive()))
/*     */     {
/*     */ 
/* 101 */       context.publishEvent(event);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 106 */       if ((context instanceof AbstractApplicationContext)) {
/* 107 */         for (ApplicationListener<?> listener : ((AbstractApplicationContext)context)
/* 108 */           .getApplicationListeners()) {
/* 109 */           this.initialMulticaster.addApplicationListener(listener);
/*     */         }
/*     */       }
/* 112 */       if ((event instanceof ApplicationFailedEvent)) {
/* 113 */         this.initialMulticaster.setErrorHandler(new LoggingErrorHandler(null));
/*     */       }
/* 115 */       this.initialMulticaster.multicastEvent(event);
/*     */     }
/*     */   }
/*     */   
/*     */   private SpringApplicationEvent getFinishedEvent(ConfigurableApplicationContext context, Throwable exception)
/*     */   {
/* 121 */     if (exception != null) {
/* 122 */       return new ApplicationFailedEvent(this.application, this.args, context, exception);
/*     */     }
/*     */     
/* 125 */     return new ApplicationReadyEvent(this.application, this.args, context);
/*     */   }
/*     */   
/*     */   private static class LoggingErrorHandler implements ErrorHandler
/*     */   {
/* 130 */     private static Log logger = LogFactory.getLog(EventPublishingRunListener.class);
/*     */     
/*     */     public void handleError(Throwable throwable)
/*     */     {
/* 134 */       logger.warn("Error calling ApplicationEventListener", throwable);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\event\EventPublishingRunListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */