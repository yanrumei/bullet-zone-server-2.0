/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
/*     */ import org.springframework.context.annotation.AnnotationConfigRegistry;
/*     */ import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
/*     */ import org.springframework.context.annotation.ScopeMetadataResolver;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class AnnotationConfigWebApplicationContext
/*     */   extends AbstractRefreshableWebApplicationContext
/*     */   implements AnnotationConfigRegistry
/*     */ {
/*     */   private BeanNameGenerator beanNameGenerator;
/*     */   private ScopeMetadataResolver scopeMetadataResolver;
/*  89 */   private final Set<Class<?>> annotatedClasses = new LinkedHashSet();
/*     */   
/*  91 */   private final Set<String> basePackages = new LinkedHashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*     */   {
/* 102 */     this.beanNameGenerator = beanNameGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanNameGenerator getBeanNameGenerator()
/*     */   {
/* 110 */     return this.beanNameGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver)
/*     */   {
/* 121 */     this.scopeMetadataResolver = scopeMetadataResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ScopeMetadataResolver getScopeMetadataResolver()
/*     */   {
/* 129 */     return this.scopeMetadataResolver;
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
/*     */   public void register(Class<?>... annotatedClasses)
/*     */   {
/* 145 */     Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
/* 146 */     this.annotatedClasses.addAll(Arrays.asList(annotatedClasses));
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
/*     */   public void scan(String... basePackages)
/*     */   {
/* 160 */     Assert.notEmpty(basePackages, "At least one base package must be specified");
/* 161 */     this.basePackages.addAll(Arrays.asList(basePackages));
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
/*     */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
/*     */   {
/* 189 */     AnnotatedBeanDefinitionReader reader = getAnnotatedBeanDefinitionReader(beanFactory);
/* 190 */     ClassPathBeanDefinitionScanner scanner = getClassPathBeanDefinitionScanner(beanFactory);
/*     */     
/* 192 */     BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
/* 193 */     if (beanNameGenerator != null) {
/* 194 */       reader.setBeanNameGenerator(beanNameGenerator);
/* 195 */       scanner.setBeanNameGenerator(beanNameGenerator);
/* 196 */       beanFactory.registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", beanNameGenerator);
/*     */     }
/*     */     
/* 199 */     ScopeMetadataResolver scopeMetadataResolver = getScopeMetadataResolver();
/* 200 */     if (scopeMetadataResolver != null) {
/* 201 */       reader.setScopeMetadataResolver(scopeMetadataResolver);
/* 202 */       scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*     */     }
/*     */     
/* 205 */     if (!this.annotatedClasses.isEmpty()) {
/* 206 */       if (this.logger.isInfoEnabled()) {
/* 207 */         this.logger.info("Registering annotated classes: [" + 
/* 208 */           StringUtils.collectionToCommaDelimitedString(this.annotatedClasses) + "]");
/*     */       }
/* 210 */       reader.register((Class[])this.annotatedClasses.toArray(new Class[this.annotatedClasses.size()]));
/*     */     }
/*     */     
/* 213 */     if (!this.basePackages.isEmpty()) {
/* 214 */       if (this.logger.isInfoEnabled()) {
/* 215 */         this.logger.info("Scanning base packages: [" + 
/* 216 */           StringUtils.collectionToCommaDelimitedString(this.basePackages) + "]");
/*     */       }
/* 218 */       scanner.scan((String[])this.basePackages.toArray(new String[this.basePackages.size()]));
/*     */     }
/*     */     
/* 221 */     String[] configLocations = getConfigLocations();
/* 222 */     if (configLocations != null) {
/* 223 */       for (String configLocation : configLocations) {
/*     */         try {
/* 225 */           Class<?> clazz = getClassLoader().loadClass(configLocation);
/* 226 */           if (this.logger.isInfoEnabled()) {
/* 227 */             this.logger.info("Successfully resolved class for [" + configLocation + "]");
/*     */           }
/* 229 */           reader.register(new Class[] { clazz });
/*     */         }
/*     */         catch (ClassNotFoundException ex) {
/* 232 */           if (this.logger.isDebugEnabled()) {
/* 233 */             this.logger.debug("Could not load class for config location [" + configLocation + "] - trying package scan. " + ex);
/*     */           }
/*     */           
/* 236 */           int count = scanner.scan(new String[] { configLocation });
/* 237 */           if (this.logger.isInfoEnabled()) {
/* 238 */             if (count == 0) {
/* 239 */               this.logger.info("No annotated classes found for specified class/package [" + configLocation + "]");
/*     */             }
/*     */             else {
/* 242 */               this.logger.info("Found " + count + " annotated classes in package [" + configLocation + "]");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
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
/*     */   protected AnnotatedBeanDefinitionReader getAnnotatedBeanDefinitionReader(DefaultListableBeanFactory beanFactory)
/*     */   {
/* 262 */     return new AnnotatedBeanDefinitionReader(beanFactory, getEnvironment());
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
/*     */   protected ClassPathBeanDefinitionScanner getClassPathBeanDefinitionScanner(DefaultListableBeanFactory beanFactory)
/*     */   {
/* 276 */     return new ClassPathBeanDefinitionScanner(beanFactory, true, getEnvironment());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\support\AnnotationConfigWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */