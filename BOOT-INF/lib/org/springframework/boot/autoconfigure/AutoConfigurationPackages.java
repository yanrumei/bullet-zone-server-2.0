/*     */ package org.springframework.boot.autoconfigure;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.boot.context.annotation.DeterminableImports;
/*     */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotationMetadata;
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
/*     */ public abstract class AutoConfigurationPackages
/*     */ {
/*  53 */   private static final Log logger = LogFactory.getLog(AutoConfigurationPackages.class);
/*     */   
/*  55 */   private static final String BEAN = AutoConfigurationPackages.class.getName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean has(BeanFactory beanFactory)
/*     */   {
/*  64 */     return (beanFactory.containsBean(BEAN)) && (!get(beanFactory).isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> get(BeanFactory beanFactory)
/*     */   {
/*     */     try
/*     */     {
/*  75 */       return ((BasePackages)beanFactory.getBean(BEAN, BasePackages.class)).get();
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {
/*  78 */       throw new IllegalStateException("Unable to retrieve @EnableAutoConfiguration base packages");
/*     */     }
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
/*     */   public static void register(BeanDefinitionRegistry registry, String... packageNames)
/*     */   {
/*  95 */     if (registry.containsBeanDefinition(BEAN)) {
/*  96 */       BeanDefinition beanDefinition = registry.getBeanDefinition(BEAN);
/*     */       
/*  98 */       ConstructorArgumentValues constructorArguments = beanDefinition.getConstructorArgumentValues();
/*  99 */       constructorArguments.addIndexedArgumentValue(0, 
/* 100 */         addBasePackages(constructorArguments, packageNames));
/*     */     }
/*     */     else {
/* 103 */       GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 104 */       beanDefinition.setBeanClass(BasePackages.class);
/* 105 */       beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, packageNames);
/*     */       
/* 107 */       beanDefinition.setRole(2);
/* 108 */       registry.registerBeanDefinition(BEAN, beanDefinition);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static String[] addBasePackages(ConstructorArgumentValues constructorArguments, String[] packageNames)
/*     */   {
/* 115 */     String[] existing = (String[])constructorArguments.getIndexedArgumentValue(0, String[].class).getValue();
/* 116 */     Set<String> merged = new LinkedHashSet();
/* 117 */     merged.addAll(Arrays.asList(existing));
/* 118 */     merged.addAll(Arrays.asList(packageNames));
/* 119 */     return (String[])merged.toArray(new String[merged.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Order(Integer.MIN_VALUE)
/*     */   static class Registrar
/*     */     implements ImportBeanDefinitionRegistrar, DeterminableImports
/*     */   {
/*     */     public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry)
/*     */     {
/* 132 */       AutoConfigurationPackages.register(registry, new String[] { new AutoConfigurationPackages.PackageImport(metadata).getPackageName() });
/*     */     }
/*     */     
/*     */     public Set<Object> determineImports(AnnotationMetadata metadata)
/*     */     {
/* 137 */       return Collections.singleton(new AutoConfigurationPackages.PackageImport(metadata));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class PackageImport
/*     */   {
/*     */     private final String packageName;
/*     */     
/*     */ 
/*     */     PackageImport(AnnotationMetadata metadata)
/*     */     {
/* 150 */       this.packageName = ClassUtils.getPackageName(metadata.getClassName());
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 155 */       return this.packageName.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 160 */       if ((obj == null) || (getClass() != obj.getClass())) {
/* 161 */         return false;
/*     */       }
/* 163 */       return this.packageName.equals(((PackageImport)obj).packageName);
/*     */     }
/*     */     
/*     */     public String getPackageName() {
/* 167 */       return this.packageName;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 172 */       return "Package Import " + this.packageName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final class BasePackages
/*     */   {
/*     */     private final List<String> packages;
/*     */     
/*     */     private boolean loggedBasePackageInfo;
/*     */     
/*     */ 
/*     */     BasePackages(String... names)
/*     */     {
/* 187 */       List<String> packages = new ArrayList();
/* 188 */       for (String name : names) {
/* 189 */         if (StringUtils.hasText(name)) {
/* 190 */           packages.add(name);
/*     */         }
/*     */       }
/* 193 */       this.packages = packages;
/*     */     }
/*     */     
/*     */     public List<String> get() {
/* 197 */       if (!this.loggedBasePackageInfo) {
/* 198 */         if (this.packages.isEmpty()) {
/* 199 */           if (AutoConfigurationPackages.logger.isWarnEnabled()) {
/* 200 */             AutoConfigurationPackages.logger.warn("@EnableAutoConfiguration was declared on a class in the default package. Automatic @Repository and @Entity scanning is not enabled.");
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 206 */         else if (AutoConfigurationPackages.logger.isDebugEnabled())
/*     */         {
/* 208 */           String packageNames = StringUtils.collectionToCommaDelimitedString(this.packages);
/* 209 */           AutoConfigurationPackages.logger.debug("@EnableAutoConfiguration was declared on a class in the package '" + packageNames + "'. Automatic @Repository and @Entity scanning is enabled.");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 215 */         this.loggedBasePackageInfo = true;
/*     */       }
/* 217 */       return this.packages;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationPackages.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */