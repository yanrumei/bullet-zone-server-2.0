/*     */ package org.springframework.boot.context.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.event.SimpleApplicationEventMulticaster;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ public class DelegatingApplicationListener
/*     */   implements ApplicationListener<ApplicationEvent>, Ordered
/*     */ {
/*     */   private static final String PROPERTY_NAME = "context.listener.classes";
/*  50 */   private int order = 0;
/*     */   
/*     */   private SimpleApplicationEventMulticaster multicaster;
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event)
/*     */   {
/*  56 */     if ((event instanceof ApplicationEnvironmentPreparedEvent)) {
/*  57 */       List<ApplicationListener<ApplicationEvent>> delegates = getListeners(((ApplicationEnvironmentPreparedEvent)event)
/*  58 */         .getEnvironment());
/*  59 */       if (delegates.isEmpty()) {
/*  60 */         return;
/*     */       }
/*  62 */       this.multicaster = new SimpleApplicationEventMulticaster();
/*  63 */       for (ApplicationListener<ApplicationEvent> listener : delegates) {
/*  64 */         this.multicaster.addApplicationListener(listener);
/*     */       }
/*     */     }
/*  67 */     if (this.multicaster != null) {
/*  68 */       this.multicaster.multicastEvent(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private List<ApplicationListener<ApplicationEvent>> getListeners(ConfigurableEnvironment environment)
/*     */   {
/*  75 */     if (environment == null) {
/*  76 */       return Collections.emptyList();
/*     */     }
/*  78 */     String classNames = environment.getProperty("context.listener.classes");
/*  79 */     List<ApplicationListener<ApplicationEvent>> listeners = new ArrayList();
/*  80 */     if (StringUtils.hasLength(classNames)) {
/*  81 */       for (String className : StringUtils.commaDelimitedListToSet(classNames)) {
/*     */         try {
/*  83 */           Class<?> clazz = ClassUtils.forName(className, 
/*  84 */             ClassUtils.getDefaultClassLoader());
/*  85 */           Assert.isAssignable(ApplicationListener.class, clazz, "class [" + className + "] must implement ApplicationListener");
/*     */           
/*  87 */           listeners.add(
/*  88 */             (ApplicationListener)BeanUtils.instantiateClass(clazz));
/*     */         }
/*     */         catch (Exception ex) {
/*  91 */           throw new ApplicationContextException("Failed to load context listener class [" + className + "]", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  97 */     AnnotationAwareOrderComparator.sort(listeners);
/*  98 */     return listeners;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 102 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 107 */     return this.order;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\config\DelegatingApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */