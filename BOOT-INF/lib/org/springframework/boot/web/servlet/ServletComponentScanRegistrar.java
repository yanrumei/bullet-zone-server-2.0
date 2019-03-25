/*    */ package org.springframework.boot.web.servlet;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*    */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*    */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ class ServletComponentScanRegistrar
/*    */   implements ImportBeanDefinitionRegistrar
/*    */ {
/*    */   private static final String BEAN_NAME = "servletComponentRegisteringPostProcessor";
/*    */   
/*    */   public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*    */   {
/* 46 */     Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);
/* 47 */     if (registry.containsBeanDefinition("servletComponentRegisteringPostProcessor")) {
/* 48 */       updatePostProcessor(registry, packagesToScan);
/*    */     }
/*    */     else {
/* 51 */       addPostProcessor(registry, packagesToScan);
/*    */     }
/*    */   }
/*    */   
/*    */   private void updatePostProcessor(BeanDefinitionRegistry registry, Set<String> packagesToScan)
/*    */   {
/* 57 */     BeanDefinition definition = registry.getBeanDefinition("servletComponentRegisteringPostProcessor");
/*    */     
/* 59 */     ConstructorArgumentValues.ValueHolder constructorArguments = definition.getConstructorArgumentValues().getGenericArgumentValue(Set.class);
/*    */     
/* 61 */     Set<String> mergedPackages = (Set)constructorArguments.getValue();
/* 62 */     mergedPackages.addAll(packagesToScan);
/* 63 */     constructorArguments.setValue(mergedPackages);
/*    */   }
/*    */   
/*    */   private void addPostProcessor(BeanDefinitionRegistry registry, Set<String> packagesToScan)
/*    */   {
/* 68 */     GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 69 */     beanDefinition.setBeanClass(ServletComponentRegisteringPostProcessor.class);
/* 70 */     beanDefinition.getConstructorArgumentValues()
/* 71 */       .addGenericArgumentValue(packagesToScan);
/* 72 */     beanDefinition.setRole(2);
/* 73 */     registry.registerBeanDefinition("servletComponentRegisteringPostProcessor", beanDefinition);
/*    */   }
/*    */   
/*    */   private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
/* 77 */     AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
/* 78 */       .getAnnotationAttributes(ServletComponentScan.class.getName()));
/* 79 */     String[] basePackages = attributes.getStringArray("basePackages");
/* 80 */     Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
/* 81 */     Set<String> packagesToScan = new LinkedHashSet();
/* 82 */     packagesToScan.addAll(Arrays.asList(basePackages));
/* 83 */     for (Class<?> basePackageClass : basePackageClasses) {
/* 84 */       packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
/*    */     }
/* 86 */     if (packagesToScan.isEmpty()) {
/* 87 */       return 
/* 88 */         Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
/*    */     }
/* 90 */     return packagesToScan;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ServletComponentScanRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */