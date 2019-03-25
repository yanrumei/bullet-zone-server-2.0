/*     */ package org.springframework.web.servlet.mvc.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;
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
/*     */ @Deprecated
/*     */ public abstract class AbstractControllerUrlHandlerMapping
/*     */   extends AbstractDetectingUrlHandlerMapping
/*     */ {
/*  39 */   private ControllerTypePredicate predicate = new AnnotationControllerTypePredicate();
/*     */   
/*  41 */   private Set<String> excludedPackages = Collections.singleton("org.springframework.web.servlet.mvc");
/*     */   
/*  43 */   private Set<Class<?>> excludedClasses = Collections.emptySet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIncludeAnnotatedControllers(boolean includeAnnotatedControllers)
/*     */   {
/*  50 */     this.predicate = (includeAnnotatedControllers ? new AnnotationControllerTypePredicate() : new ControllerTypePredicate());
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
/*     */   public void setExcludedPackages(String... excludedPackages)
/*     */   {
/*  67 */     this.excludedPackages = (excludedPackages != null ? new HashSet(Arrays.asList(excludedPackages)) : new HashSet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExcludedClasses(Class<?>... excludedClasses)
/*     */   {
/*  76 */     this.excludedClasses = (excludedClasses != null ? new HashSet(Arrays.asList(excludedClasses)) : new HashSet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] determineUrlsForHandler(String beanName)
/*     */   {
/*  86 */     Class<?> beanClass = getApplicationContext().getType(beanName);
/*  87 */     if (isEligibleForMapping(beanName, beanClass)) {
/*  88 */       return buildUrlsForHandler(beanName, beanClass);
/*     */     }
/*     */     
/*  91 */     return null;
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
/*     */   protected boolean isEligibleForMapping(String beanName, Class<?> beanClass)
/*     */   {
/* 104 */     if (beanClass == null) {
/* 105 */       if (this.logger.isDebugEnabled()) {
/* 106 */         this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping because its bean type could not be determined");
/*     */       }
/*     */       
/* 109 */       return false;
/*     */     }
/* 111 */     if (this.excludedClasses.contains(beanClass)) {
/* 112 */       if (this.logger.isDebugEnabled()) {
/* 113 */         this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping because its bean class is explicitly excluded: " + beanClass
/* 114 */           .getName());
/*     */       }
/* 116 */       return false;
/*     */     }
/* 118 */     String beanClassName = beanClass.getName();
/* 119 */     for (String packageName : this.excludedPackages) {
/* 120 */       if (beanClassName.startsWith(packageName)) {
/* 121 */         if (this.logger.isDebugEnabled()) {
/* 122 */           this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping because its bean class is defined in an excluded package: " + beanClass
/* 123 */             .getName());
/*     */         }
/* 125 */         return false;
/*     */       }
/*     */     }
/* 128 */     return isControllerType(beanClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isControllerType(Class<?> beanClass)
/*     */   {
/* 137 */     return this.predicate.isControllerType(beanClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isMultiActionControllerType(Class<?> beanClass)
/*     */   {
/* 146 */     return this.predicate.isMultiActionControllerType(beanClass);
/*     */   }
/*     */   
/*     */   protected abstract String[] buildUrlsForHandler(String paramString, Class<?> paramClass);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\support\AbstractControllerUrlHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */