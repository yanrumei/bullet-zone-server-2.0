/*     */ package org.springframework.boot.context;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.annotation.ComponentScan;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
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
/*     */ 
/*     */ public class ConfigurationWarningsApplicationContextInitializer
/*     */   implements ApplicationContextInitializer<ConfigurableApplicationContext>
/*     */ {
/*  56 */   private static final Log logger = LogFactory.getLog(ConfigurationWarningsApplicationContextInitializer.class);
/*     */   
/*     */   public void initialize(ConfigurableApplicationContext context)
/*     */   {
/*  60 */     context.addBeanFactoryPostProcessor(new ConfigurationWarningsPostProcessor(
/*  61 */       getChecks()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Check[] getChecks()
/*     */   {
/*  69 */     return new Check[] { new ComponentScanPackageCheck() };
/*     */   }
/*     */   
/*     */ 
/*     */   protected static final class ConfigurationWarningsPostProcessor
/*     */     implements PriorityOrdered, BeanDefinitionRegistryPostProcessor
/*     */   {
/*     */     private ConfigurationWarningsApplicationContextInitializer.Check[] checks;
/*     */     
/*     */ 
/*     */     public ConfigurationWarningsPostProcessor(ConfigurationWarningsApplicationContextInitializer.Check[] checks)
/*     */     {
/*  81 */       this.checks = checks;
/*     */     }
/*     */     
/*     */     public int getOrder()
/*     */     {
/*  86 */       return 2147483646;
/*     */     }
/*     */     
/*     */ 
/*     */     public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */       throws BeansException
/*     */     {}
/*     */     
/*     */     public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
/*     */       throws BeansException
/*     */     {
/*  97 */       for (ConfigurationWarningsApplicationContextInitializer.Check check : this.checks) {
/*  98 */         String message = check.getWarning(registry);
/*  99 */         if (StringUtils.hasLength(message)) {
/* 100 */           warn(message);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void warn(String message)
/*     */     {
/* 107 */       if (ConfigurationWarningsApplicationContextInitializer.logger.isWarnEnabled()) {
/* 108 */         ConfigurationWarningsApplicationContextInitializer.logger.warn(String.format("%n%n** WARNING ** : %s%n%n", new Object[] { message }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static abstract interface Check
/*     */   {
/*     */     public abstract String getWarning(BeanDefinitionRegistry paramBeanDefinitionRegistry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static class ComponentScanPackageCheck
/*     */     implements ConfigurationWarningsApplicationContextInitializer.Check
/*     */   {
/*     */     private static final Set<String> PROBLEM_PACKAGES;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     static
/*     */     {
/* 136 */       Set<String> packages = new HashSet();
/* 137 */       packages.add("org.springframework");
/* 138 */       packages.add("org");
/* 139 */       PROBLEM_PACKAGES = Collections.unmodifiableSet(packages);
/*     */     }
/*     */     
/*     */     public String getWarning(BeanDefinitionRegistry registry)
/*     */     {
/* 144 */       Set<String> scannedPackages = getComponentScanningPackages(registry);
/* 145 */       List<String> problematicPackages = getProblematicPackages(scannedPackages);
/* 146 */       if (problematicPackages.isEmpty()) {
/* 147 */         return null;
/*     */       }
/* 149 */       return 
/*     */       
/* 151 */         "Your ApplicationContext is unlikely to start due to a @ComponentScan of " + StringUtils.collectionToDelimitedString(problematicPackages, ", ") + ".";
/*     */     }
/*     */     
/*     */ 
/*     */     protected Set<String> getComponentScanningPackages(BeanDefinitionRegistry registry)
/*     */     {
/* 157 */       Set<String> packages = new LinkedHashSet();
/* 158 */       String[] names = registry.getBeanDefinitionNames();
/* 159 */       for (String name : names) {
/* 160 */         BeanDefinition definition = registry.getBeanDefinition(name);
/* 161 */         if ((definition instanceof AnnotatedBeanDefinition)) {
/* 162 */           AnnotatedBeanDefinition annotatedDefinition = (AnnotatedBeanDefinition)definition;
/* 163 */           addComponentScanningPackages(packages, annotatedDefinition
/* 164 */             .getMetadata());
/*     */         }
/*     */       }
/* 167 */       return packages;
/*     */     }
/*     */     
/*     */     private void addComponentScanningPackages(Set<String> packages, AnnotationMetadata metadata)
/*     */     {
/* 172 */       AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
/* 173 */         .getAnnotationAttributes(ComponentScan.class.getName(), true));
/* 174 */       if (attributes != null) {
/* 175 */         addPackages(packages, attributes.getStringArray("value"));
/* 176 */         addPackages(packages, attributes.getStringArray("basePackages"));
/* 177 */         addClasses(packages, attributes.getStringArray("basePackageClasses"));
/* 178 */         if (packages.isEmpty()) {
/* 179 */           packages.add(ClassUtils.getPackageName(metadata.getClassName()));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void addPackages(Set<String> packages, String[] values) {
/* 185 */       if (values != null) {
/* 186 */         Collections.addAll(packages, values);
/*     */       }
/*     */     }
/*     */     
/*     */     private void addClasses(Set<String> packages, String[] values) {
/* 191 */       if (values != null) {
/* 192 */         for (String value : values) {
/* 193 */           packages.add(ClassUtils.getPackageName(value));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private List<String> getProblematicPackages(Set<String> scannedPackages) {
/* 199 */       List<String> problematicPackages = new ArrayList();
/* 200 */       for (String scannedPackage : scannedPackages) {
/* 201 */         if (isProblematicPackage(scannedPackage)) {
/* 202 */           problematicPackages.add(getDisplayName(scannedPackage));
/*     */         }
/*     */       }
/* 205 */       return problematicPackages;
/*     */     }
/*     */     
/*     */     private boolean isProblematicPackage(String scannedPackage) {
/* 209 */       if ((scannedPackage == null) || (scannedPackage.isEmpty())) {
/* 210 */         return true;
/*     */       }
/* 212 */       return PROBLEM_PACKAGES.contains(scannedPackage);
/*     */     }
/*     */     
/*     */     private String getDisplayName(String scannedPackage) {
/* 216 */       if ((scannedPackage == null) || (scannedPackage.isEmpty())) {
/* 217 */         return "the default package";
/*     */       }
/* 219 */       return "'" + scannedPackage + "'";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\ConfigurationWarningsApplicationContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */