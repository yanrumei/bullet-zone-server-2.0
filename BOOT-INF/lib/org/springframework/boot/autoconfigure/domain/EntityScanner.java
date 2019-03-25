/*    */ package org.springframework.boot.autoconfigure.domain;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
/*    */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityScanner
/*    */ {
/*    */   private final ApplicationContext context;
/*    */   
/*    */   public EntityScanner(ApplicationContext context)
/*    */   {
/* 50 */     Assert.notNull(context, "Context must not be null");
/* 51 */     this.context = context;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @SafeVarargs
/*    */   public final Set<Class<?>> scan(Class<? extends Annotation>... annotationTypes)
/*    */     throws ClassNotFoundException
/*    */   {
/* 63 */     List<String> packages = getPackages();
/* 64 */     if (packages.isEmpty()) {
/* 65 */       return Collections.emptySet();
/*    */     }
/* 67 */     Set<Class<?>> entitySet = new HashSet();
/* 68 */     ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
/*    */     
/* 70 */     scanner.setEnvironment(this.context.getEnvironment());
/* 71 */     scanner.setResourceLoader(this.context);
/* 72 */     for (Class<? extends Annotation> annotationType : annotationTypes) {
/* 73 */       scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));
/*    */     }
/* 75 */     for (??? = packages.iterator(); ((Iterator)???).hasNext();) { String basePackage = (String)((Iterator)???).next();
/* 76 */       if (StringUtils.hasText(basePackage)) {
/* 77 */         for (BeanDefinition candidate : scanner
/* 78 */           .findCandidateComponents(basePackage)) {
/* 79 */           entitySet.add(ClassUtils.forName(candidate.getBeanClassName(), this.context
/* 80 */             .getClassLoader()));
/*    */         }
/*    */       }
/*    */     }
/* 84 */     return entitySet;
/*    */   }
/*    */   
/*    */   private List<String> getPackages() {
/* 88 */     List<String> packages = EntityScanPackages.get(this.context).getPackageNames();
/* 89 */     if ((packages.isEmpty()) && (AutoConfigurationPackages.has(this.context))) {
/* 90 */       packages = AutoConfigurationPackages.get(this.context);
/*    */     }
/* 92 */     return packages;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\domain\EntityScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */