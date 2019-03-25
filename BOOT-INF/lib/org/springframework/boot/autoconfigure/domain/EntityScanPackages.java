/*     */ package org.springframework.boot.autoconfigure.domain;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotationMetadata;
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
/*     */ public class EntityScanPackages
/*     */ {
/*  53 */   private static final String BEAN = EntityScanPackages.class.getName();
/*     */   
/*  55 */   private static final EntityScanPackages NONE = new EntityScanPackages(new String[0]);
/*     */   private final List<String> packageNames;
/*     */   
/*     */   EntityScanPackages(String... packageNames)
/*     */   {
/*  60 */     List<String> packages = new ArrayList();
/*  61 */     for (String name : packageNames) {
/*  62 */       if (StringUtils.hasText(name)) {
/*  63 */         packages.add(name);
/*     */       }
/*     */     }
/*  66 */     this.packageNames = Collections.unmodifiableList(packages);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getPackageNames()
/*     */   {
/*  75 */     return this.packageNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EntityScanPackages get(BeanFactory beanFactory)
/*     */   {
/*     */     try
/*     */     {
/*  87 */       return (EntityScanPackages)beanFactory.getBean(BEAN, EntityScanPackages.class);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {}
/*  90 */     return NONE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void register(BeanDefinitionRegistry registry, String... packageNames)
/*     */   {
/* 100 */     Assert.notNull(registry, "Registry must not be null");
/* 101 */     Assert.notNull(packageNames, "PackageNames must not be null");
/* 102 */     register(registry, Arrays.asList(packageNames));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void register(BeanDefinitionRegistry registry, Collection<String> packageNames)
/*     */   {
/* 112 */     Assert.notNull(registry, "Registry must not be null");
/* 113 */     Assert.notNull(packageNames, "PackageNames must not be null");
/* 114 */     if (registry.containsBeanDefinition(BEAN)) {
/* 115 */       BeanDefinition beanDefinition = registry.getBeanDefinition(BEAN);
/*     */       
/* 117 */       ConstructorArgumentValues constructorArguments = beanDefinition.getConstructorArgumentValues();
/* 118 */       constructorArguments.addIndexedArgumentValue(0, 
/* 119 */         addPackageNames(constructorArguments, packageNames));
/*     */     }
/*     */     else {
/* 122 */       GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 123 */       beanDefinition.setBeanClass(EntityScanPackages.class);
/* 124 */       beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, packageNames
/* 125 */         .toArray(new String[packageNames.size()]));
/* 126 */       beanDefinition.setRole(2);
/* 127 */       registry.registerBeanDefinition(BEAN, beanDefinition);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String[] addPackageNames(ConstructorArgumentValues constructorArguments, Collection<String> packageNames)
/*     */   {
/* 135 */     String[] existing = (String[])constructorArguments.getIndexedArgumentValue(0, String[].class).getValue();
/* 136 */     Set<String> merged = new LinkedHashSet();
/* 137 */     merged.addAll(Arrays.asList(existing));
/* 138 */     merged.addAll(packageNames);
/* 139 */     return (String[])merged.toArray(new String[merged.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Order(Integer.MIN_VALUE)
/*     */   static class Registrar
/*     */     implements ImportBeanDefinitionRegistrar
/*     */   {
/*     */     public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry)
/*     */     {
/* 152 */       EntityScanPackages.register(registry, getPackagesToScan(metadata));
/*     */     }
/*     */     
/*     */     private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
/* 156 */       AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
/* 157 */         .getAnnotationAttributes(EntityScan.class.getName()));
/* 158 */       String[] basePackages = attributes.getStringArray("basePackages");
/*     */       
/* 160 */       Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
/* 161 */       Set<String> packagesToScan = new LinkedHashSet();
/* 162 */       packagesToScan.addAll(Arrays.asList(basePackages));
/* 163 */       for (Class<?> basePackageClass : basePackageClasses) {
/* 164 */         packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
/*     */       }
/* 166 */       if (packagesToScan.isEmpty()) {
/* 167 */         String packageName = ClassUtils.getPackageName(metadata.getClassName());
/* 168 */         Assert.state(!StringUtils.isEmpty(packageName), "@EntityScan cannot be used with the default package");
/*     */         
/* 170 */         return Collections.singleton(packageName);
/*     */       }
/* 172 */       return packagesToScan;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\domain\EntityScanPackages.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */