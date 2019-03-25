/*     */ package org.springframework.boot.autoconfigure;
/*     */ 
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.bootstrap.GenericBootstrap;
/*     */ import org.apache.catalina.mbeans.MBeanFactory;
/*     */ import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
/*     */ import org.springframework.boot.context.event.ApplicationFailedEvent;
/*     */ import org.springframework.boot.context.event.ApplicationReadyEvent;
/*     */ import org.springframework.boot.context.event.SpringApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.format.support.DefaultFormattingConversionService;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Order(-2147483627)
/*     */ public class BackgroundPreinitializer
/*     */   implements ApplicationListener<SpringApplicationEvent>
/*     */ {
/*  49 */   private static final AtomicBoolean preinitializationStarted = new AtomicBoolean(false);
/*     */   
/*     */ 
/*  52 */   private static final CountDownLatch preinitializationComplete = new CountDownLatch(1);
/*     */   
/*     */   public void onApplicationEvent(SpringApplicationEvent event)
/*     */   {
/*  56 */     if (((event instanceof ApplicationEnvironmentPreparedEvent)) && 
/*  57 */       (preinitializationStarted.compareAndSet(false, true))) {
/*  58 */       performPreinitialization();
/*     */     }
/*     */     
/*  61 */     if (((event instanceof ApplicationReadyEvent)) || ((event instanceof ApplicationFailedEvent)))
/*     */     {
/*  63 */       if (preinitializationStarted.get())
/*     */         try {
/*  65 */           preinitializationComplete.await();
/*     */         }
/*     */         catch (InterruptedException ex) {
/*  68 */           Thread.currentThread().interrupt();
/*     */         }
/*     */     }
/*     */   }
/*     */   
/*     */   private void performPreinitialization() {
/*     */     try {
/*  75 */       Thread thread = new Thread(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*  79 */           runSafely(new BackgroundPreinitializer.MessageConverterInitializer(null));
/*  80 */           runSafely(new BackgroundPreinitializer.MBeanFactoryInitializer(null));
/*  81 */           runSafely(new BackgroundPreinitializer.ValidationInitializer(null));
/*  82 */           runSafely(new BackgroundPreinitializer.JacksonInitializer(null));
/*  83 */           runSafely(new BackgroundPreinitializer.ConversionServiceInitializer(null));
/*  84 */           BackgroundPreinitializer.preinitializationComplete.countDown();
/*     */         }
/*     */         
/*     */         public void runSafely(Runnable runnable) {
/*     */           try {
/*  89 */             runnable.run(); } catch (Throwable localThrowable) {} } }, "background-preinit");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */       thread.start();
/*     */ 
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */ 
/* 103 */       preinitializationComplete.countDown();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class MessageConverterInitializer
/*     */     implements Runnable
/*     */   {
/*     */     public void run()
/*     */     {
/* 114 */       new AllEncompassingFormHttpMessageConverter();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class MBeanFactoryInitializer
/*     */     implements Runnable
/*     */   {
/*     */     public void run()
/*     */     {
/* 126 */       new MBeanFactory();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ValidationInitializer
/*     */     implements Runnable
/*     */   {
/*     */     public void run()
/*     */     {
/* 138 */       Validation.byDefaultProvider().configure();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class JacksonInitializer
/*     */     implements Runnable
/*     */   {
/*     */     public void run()
/*     */     {
/* 150 */       Jackson2ObjectMapperBuilder.json().build();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ConversionServiceInitializer
/*     */     implements Runnable
/*     */   {
/*     */     public void run()
/*     */     {
/* 162 */       new DefaultFormattingConversionService();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\BackgroundPreinitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */