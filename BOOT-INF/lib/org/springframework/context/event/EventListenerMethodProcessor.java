/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
/*     */ import org.springframework.aop.scope.ScopedObject;
/*     */ import org.springframework.aop.scope.ScopedProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.MethodIntrospector.MetadataLookup;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventListenerMethodProcessor
/*     */   implements SmartInitializingSingleton, ApplicationContextAware
/*     */ {
/*     */   protected final Log logger;
/*     */   private ConfigurableApplicationContext applicationContext;
/*     */   private final EventExpressionEvaluator evaluator;
/*     */   
/*     */   public EventListenerMethodProcessor()
/*     */   {
/*  56 */     this.logger = LogFactory.getLog(getClass());
/*     */     
/*     */ 
/*     */ 
/*  60 */     this.evaluator = new EventExpressionEvaluator();
/*     */   }
/*     */   
/*  63 */   private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap(64));
/*     */   
/*     */ 
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/*  68 */     Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext, "ApplicationContext does not implement ConfigurableApplicationContext");
/*     */     
/*  70 */     this.applicationContext = ((ConfigurableApplicationContext)applicationContext);
/*     */   }
/*     */   
/*     */   public void afterSingletonsInstantiated()
/*     */   {
/*  75 */     List<EventListenerFactory> factories = getEventListenerFactories();
/*  76 */     String[] beanNames = this.applicationContext.getBeanNamesForType(Object.class);
/*  77 */     for (String beanName : beanNames) {
/*  78 */       if (!ScopedProxyUtils.isScopedTarget(beanName)) {
/*  79 */         Class<?> type = null;
/*     */         try {
/*  81 */           type = AutoProxyUtils.determineTargetClass(this.applicationContext.getBeanFactory(), beanName);
/*     */         }
/*     */         catch (Throwable ex)
/*     */         {
/*  85 */           if (this.logger.isDebugEnabled()) {
/*  86 */             this.logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
/*     */           }
/*     */         }
/*  89 */         if (type != null) {
/*  90 */           if (ScopedObject.class.isAssignableFrom(type)) {
/*     */             try {
/*  92 */               type = AutoProxyUtils.determineTargetClass(this.applicationContext.getBeanFactory(), 
/*  93 */                 ScopedProxyUtils.getTargetBeanName(beanName));
/*     */             }
/*     */             catch (Throwable ex)
/*     */             {
/*  97 */               if (this.logger.isDebugEnabled()) {
/*  98 */                 this.logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
/*     */               }
/*     */             }
/*     */           }
/*     */           try {
/* 103 */             processBean(factories, beanName, type);
/*     */           }
/*     */           catch (Throwable ex) {
/* 106 */             throw new BeanInitializationException("Failed to process @EventListener annotation on bean with name '" + beanName + "'", ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<EventListenerFactory> getEventListenerFactories()
/*     */   {
/* 120 */     Map<String, EventListenerFactory> beans = this.applicationContext.getBeansOfType(EventListenerFactory.class);
/* 121 */     List<EventListenerFactory> factories = new ArrayList(beans.values());
/* 122 */     AnnotationAwareOrderComparator.sort(factories);
/* 123 */     return factories;
/*     */   }
/*     */   
/*     */   protected void processBean(List<EventListenerFactory> factories, String beanName, Class<?> targetType) {
/* 127 */     if (!this.nonAnnotatedClasses.contains(targetType)) {
/* 128 */       Map<Method, EventListener> annotatedMethods = null;
/*     */       try {
/* 130 */         annotatedMethods = MethodIntrospector.selectMethods(targetType, new MethodIntrospector.MetadataLookup()
/*     */         {
/*     */           public EventListener inspect(Method method)
/*     */           {
/* 134 */             return (EventListener)AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class);
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 140 */         if (this.logger.isDebugEnabled()) {
/* 141 */           this.logger.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
/*     */         }
/*     */       }
/* 144 */       if (CollectionUtils.isEmpty(annotatedMethods)) {
/* 145 */         this.nonAnnotatedClasses.add(targetType);
/* 146 */         if (this.logger.isTraceEnabled()) {
/* 147 */           this.logger.trace("No @EventListener annotations found on bean class: " + targetType.getName());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 152 */         for (ex = annotatedMethods.keySet().iterator(); ex.hasNext();) { method = (Method)ex.next();
/* 153 */           for (EventListenerFactory factory : factories)
/* 154 */             if (factory.supportsMethod(method)) {
/* 155 */               Method methodToUse = AopUtils.selectInvocableMethod(method, this.applicationContext
/* 156 */                 .getType(beanName));
/*     */               
/* 158 */               ApplicationListener<?> applicationListener = factory.createApplicationListener(beanName, targetType, methodToUse);
/* 159 */               if ((applicationListener instanceof ApplicationListenerMethodAdapter))
/*     */               {
/* 161 */                 ((ApplicationListenerMethodAdapter)applicationListener).init(this.applicationContext, this.evaluator);
/*     */               }
/* 163 */               this.applicationContext.addApplicationListener(applicationListener);
/* 164 */               break;
/*     */             }
/*     */         }
/*     */         Method method;
/* 168 */         if (this.logger.isDebugEnabled()) {
/* 169 */           this.logger.debug(annotatedMethods.size() + " @EventListener methods processed on bean '" + beanName + "': " + annotatedMethods);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\EventListenerMethodProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */