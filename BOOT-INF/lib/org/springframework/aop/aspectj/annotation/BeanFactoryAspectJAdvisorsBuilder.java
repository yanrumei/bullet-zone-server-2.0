/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aspectj.lang.reflect.AjType;
/*     */ import org.aspectj.lang.reflect.PerClause;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
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
/*     */ public class BeanFactoryAspectJAdvisorsBuilder
/*     */ {
/*     */   private final ListableBeanFactory beanFactory;
/*     */   private final AspectJAdvisorFactory advisorFactory;
/*     */   private volatile List<String> aspectBeanNames;
/*  48 */   private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap();
/*     */   
/*  50 */   private final Map<String, MetadataAwareAspectInstanceFactory> aspectFactoryCache = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory)
/*     */   {
/*  59 */     this(beanFactory, new ReflectiveAspectJAdvisorFactory(beanFactory));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory, AspectJAdvisorFactory advisorFactory)
/*     */   {
/*  68 */     Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
/*  69 */     Assert.notNull(advisorFactory, "AspectJAdvisorFactory must not be null");
/*  70 */     this.beanFactory = beanFactory;
/*  71 */     this.advisorFactory = advisorFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Advisor> buildAspectJAdvisors()
/*     */   {
/*  83 */     List<String> aspectNames = this.aspectBeanNames;
/*     */     List<Advisor> advisors;
/*  85 */     if (aspectNames == null) {
/*  86 */       synchronized (this) {
/*  87 */         aspectNames = this.aspectBeanNames;
/*  88 */         if (aspectNames == null) {
/*  89 */           advisors = new LinkedList();
/*  90 */           aspectNames = new LinkedList();
/*  91 */           String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Object.class, true, false);
/*     */           
/*  93 */           for (String beanName : beanNames)
/*  94 */             if (isEligibleBean(beanName))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*  99 */               Class<?> beanType = this.beanFactory.getType(beanName);
/* 100 */               if (beanType != null)
/*     */               {
/*     */ 
/* 103 */                 if (this.advisorFactory.isAspect(beanType)) {
/* 104 */                   aspectNames.add(beanName);
/* 105 */                   AspectMetadata amd = new AspectMetadata(beanType, beanName);
/* 106 */                   if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
/* 107 */                     MetadataAwareAspectInstanceFactory factory = new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
/*     */                     
/* 109 */                     List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
/* 110 */                     if (this.beanFactory.isSingleton(beanName)) {
/* 111 */                       this.advisorsCache.put(beanName, classAdvisors);
/*     */                     }
/*     */                     else {
/* 114 */                       this.aspectFactoryCache.put(beanName, factory);
/*     */                     }
/* 116 */                     advisors.addAll(classAdvisors);
/*     */                   }
/*     */                   else
/*     */                   {
/* 120 */                     if (this.beanFactory.isSingleton(beanName)) {
/* 121 */                       throw new IllegalArgumentException("Bean with name '" + beanName + "' is a singleton, but aspect instantiation model is not singleton");
/*     */                     }
/*     */                     
/* 124 */                     MetadataAwareAspectInstanceFactory factory = new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
/*     */                     
/* 126 */                     this.aspectFactoryCache.put(beanName, factory);
/* 127 */                     advisors.addAll(this.advisorFactory.getAdvisors(factory));
/*     */                   }
/*     */                 } }
/*     */             }
/* 131 */           this.aspectBeanNames = aspectNames;
/* 132 */           return advisors;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 137 */     if (aspectNames.isEmpty()) {
/* 138 */       return Collections.emptyList();
/*     */     }
/* 140 */     List<Advisor> advisors = new LinkedList();
/* 141 */     for (String aspectName : aspectNames) {
/* 142 */       Object cachedAdvisors = (List)this.advisorsCache.get(aspectName);
/* 143 */       if (cachedAdvisors != null) {
/* 144 */         advisors.addAll((Collection)cachedAdvisors);
/*     */       }
/*     */       else {
/* 147 */         MetadataAwareAspectInstanceFactory factory = (MetadataAwareAspectInstanceFactory)this.aspectFactoryCache.get(aspectName);
/* 148 */         advisors.addAll(this.advisorFactory.getAdvisors(factory));
/*     */       }
/*     */     }
/* 151 */     return advisors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isEligibleBean(String beanName)
/*     */   {
/* 160 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\annotation\BeanFactoryAspectJAdvisorsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */