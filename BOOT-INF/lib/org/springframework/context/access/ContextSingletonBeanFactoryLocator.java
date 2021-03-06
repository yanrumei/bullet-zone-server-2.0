/*     */ package org.springframework.context.access;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*     */ import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*     */ import org.springframework.core.io.support.ResourcePatternUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextSingletonBeanFactoryLocator
/*     */   extends SingletonBeanFactoryLocator
/*     */ {
/*     */   private static final String DEFAULT_RESOURCE_LOCATION = "classpath*:beanRefContext.xml";
/*  57 */   private static final Map<String, BeanFactoryLocator> instances = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BeanFactoryLocator getInstance()
/*     */     throws BeansException
/*     */   {
/*  69 */     return getInstance(null);
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
/*     */   public static BeanFactoryLocator getInstance(String selector)
/*     */     throws BeansException
/*     */   {
/*  88 */     String resourceLocation = selector;
/*  89 */     if (resourceLocation == null) {
/*  90 */       resourceLocation = "classpath*:beanRefContext.xml";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  95 */     if (!ResourcePatternUtils.isUrl(resourceLocation)) {
/*  96 */       resourceLocation = "classpath*:" + resourceLocation;
/*     */     }
/*     */     
/*  99 */     synchronized (instances) {
/* 100 */       if (logger.isTraceEnabled()) {
/* 101 */         logger.trace("ContextSingletonBeanFactoryLocator.getInstance(): instances.hashCode=" + instances
/* 102 */           .hashCode() + ", instances=" + instances);
/*     */       }
/* 104 */       BeanFactoryLocator bfl = (BeanFactoryLocator)instances.get(resourceLocation);
/* 105 */       if (bfl == null) {
/* 106 */         bfl = new ContextSingletonBeanFactoryLocator(resourceLocation);
/* 107 */         instances.put(resourceLocation, bfl);
/*     */       }
/* 109 */       return bfl;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ContextSingletonBeanFactoryLocator(String resourceLocation)
/*     */   {
/* 121 */     super(resourceLocation);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanFactory createDefinition(String resourceLocation, String factoryKey)
/*     */   {
/* 133 */     return new ClassPathXmlApplicationContext(new String[] { resourceLocation }, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initializeDefinition(BeanFactory groupDef)
/*     */   {
/* 142 */     if ((groupDef instanceof ConfigurableApplicationContext)) {
/* 143 */       ((ConfigurableApplicationContext)groupDef).refresh();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void destroyDefinition(BeanFactory groupDef, String selector)
/*     */   {
/* 153 */     if ((groupDef instanceof ConfigurableApplicationContext)) {
/* 154 */       if (logger.isTraceEnabled()) {
/* 155 */         logger.trace("Context group with selector '" + selector + "' being released, as there are no more references to it");
/*     */       }
/*     */       
/* 158 */       ((ConfigurableApplicationContext)groupDef).close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\access\ContextSingletonBeanFactoryLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */