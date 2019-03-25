/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
/*     */ public class BeanFactoryAdvisorRetrievalHelper
/*     */ {
/*  42 */   private static final Log logger = LogFactory.getLog(BeanFactoryAdvisorRetrievalHelper.class);
/*     */   
/*     */ 
/*     */   private final ConfigurableListableBeanFactory beanFactory;
/*     */   
/*     */ 
/*     */   private String[] cachedAdvisorBeanNames;
/*     */   
/*     */ 
/*     */ 
/*     */   public BeanFactoryAdvisorRetrievalHelper(ConfigurableListableBeanFactory beanFactory)
/*     */   {
/*  54 */     Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
/*  55 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Advisor> findAdvisorBeans()
/*     */   {
/*  67 */     String[] advisorNames = null;
/*  68 */     synchronized (this) {
/*  69 */       advisorNames = this.cachedAdvisorBeanNames;
/*  70 */       if (advisorNames == null)
/*     */       {
/*     */ 
/*  73 */         advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Advisor.class, true, false);
/*     */         
/*  75 */         this.cachedAdvisorBeanNames = advisorNames;
/*     */       }
/*     */     }
/*  78 */     if (advisorNames.length == 0) {
/*  79 */       return new LinkedList();
/*     */     }
/*     */     
/*  82 */     List<Advisor> advisors = new LinkedList();
/*  83 */     for (String name : advisorNames) {
/*  84 */       if (isEligibleBean(name)) {
/*  85 */         if (this.beanFactory.isCurrentlyInCreation(name)) {
/*  86 */           if (logger.isDebugEnabled()) {
/*  87 */             logger.debug("Skipping currently created advisor '" + name + "'");
/*     */           }
/*     */         }
/*     */         else {
/*     */           try {
/*  92 */             advisors.add(this.beanFactory.getBean(name, Advisor.class));
/*     */           }
/*     */           catch (BeanCreationException ex) {
/*  95 */             Throwable rootCause = ex.getMostSpecificCause();
/*  96 */             if ((rootCause instanceof BeanCurrentlyInCreationException)) {
/*  97 */               BeanCreationException bce = (BeanCreationException)rootCause;
/*  98 */               if (this.beanFactory.isCurrentlyInCreation(bce.getBeanName())) {
/*  99 */                 if (logger.isDebugEnabled()) {
/* 100 */                   logger.debug("Skipping advisor '" + name + "' with dependency on currently created bean: " + ex
/* 101 */                     .getMessage());
/*     */                 }
/*     */                 
/*     */ 
/* 105 */                 continue;
/*     */               }
/*     */             }
/* 108 */             throw ex;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 113 */     return advisors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isEligibleBean(String beanName)
/*     */   {
/* 123 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\autoproxy\BeanFactoryAdvisorRetrievalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */