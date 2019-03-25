/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
/*     */ import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
/*     */ import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
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
/*     */ public abstract class AopConfigUtils
/*     */ {
/*     */   public static final String AUTO_PROXY_CREATOR_BEAN_NAME = "org.springframework.aop.config.internalAutoProxyCreator";
/*  57 */   private static final List<Class<?>> APC_PRIORITY_LIST = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  63 */     APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class);
/*  64 */     APC_PRIORITY_LIST.add(AspectJAwareAdvisorAutoProxyCreator.class);
/*  65 */     APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry)
/*     */   {
/*  70 */     return registerAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, Object source) {
/*  74 */     return registerOrEscalateApcAsRequired(InfrastructureAdvisorAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
/*  78 */     return registerAspectJAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, Object source) {
/*  82 */     return registerOrEscalateApcAsRequired(AspectJAwareAdvisorAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
/*  86 */     return registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */   
/*     */   public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, Object source) {
/*  90 */     return registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   public static void forceAutoProxyCreatorToUseClassProxying(BeanDefinitionRegistry registry) {
/*  94 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/*  95 */       BeanDefinition definition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/*  96 */       definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void forceAutoProxyCreatorToExposeProxy(BeanDefinitionRegistry registry) {
/* 101 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/* 102 */       BeanDefinition definition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 103 */       definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
/*     */     }
/*     */   }
/*     */   
/*     */   private static BeanDefinition registerOrEscalateApcAsRequired(Class<?> cls, BeanDefinitionRegistry registry, Object source)
/*     */   {
/* 109 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/* 110 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/* 111 */       BeanDefinition apcDefinition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 112 */       if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
/* 113 */         int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
/* 114 */         int requiredPriority = findPriorityForClass(cls);
/* 115 */         if (currentPriority < requiredPriority) {
/* 116 */           apcDefinition.setBeanClassName(cls.getName());
/*     */         }
/*     */       }
/* 119 */       return null;
/*     */     }
/* 121 */     RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
/* 122 */     beanDefinition.setSource(source);
/* 123 */     beanDefinition.getPropertyValues().add("order", Integer.valueOf(Integer.MIN_VALUE));
/* 124 */     beanDefinition.setRole(2);
/* 125 */     registry.registerBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator", beanDefinition);
/* 126 */     return beanDefinition;
/*     */   }
/*     */   
/*     */   private static int findPriorityForClass(Class<?> clazz) {
/* 130 */     return APC_PRIORITY_LIST.indexOf(clazz);
/*     */   }
/*     */   
/*     */   private static int findPriorityForClass(String className) {
/* 134 */     for (int i = 0; i < APC_PRIORITY_LIST.size(); i++) {
/* 135 */       Class<?> clazz = (Class)APC_PRIORITY_LIST.get(i);
/* 136 */       if (clazz.getName().equals(className)) {
/* 137 */         return i;
/*     */       }
/*     */     }
/* 140 */     throw new IllegalArgumentException("Class name [" + className + "] is not a known auto-proxy creator class");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\config\AopConfigUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */