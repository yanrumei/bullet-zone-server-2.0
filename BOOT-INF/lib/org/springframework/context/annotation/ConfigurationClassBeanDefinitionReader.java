/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.Autowire;
/*     */ import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
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
/*     */ class ConfigurationClassBeanDefinitionReader
/*     */ {
/*  72 */   private static final Log logger = LogFactory.getLog(ConfigurationClassBeanDefinitionReader.class);
/*     */   
/*  74 */   private static final ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*     */   
/*     */ 
/*     */   private final BeanDefinitionRegistry registry;
/*     */   
/*     */ 
/*     */   private final SourceExtractor sourceExtractor;
/*     */   
/*     */ 
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */ 
/*     */   private final Environment environment;
/*     */   
/*     */ 
/*     */   private final BeanNameGenerator importBeanNameGenerator;
/*     */   
/*     */ 
/*     */   private final ImportRegistry importRegistry;
/*     */   
/*     */   private final ConditionEvaluator conditionEvaluator;
/*     */   
/*     */ 
/*     */   ConfigurationClassBeanDefinitionReader(BeanDefinitionRegistry registry, SourceExtractor sourceExtractor, ResourceLoader resourceLoader, Environment environment, BeanNameGenerator importBeanNameGenerator, ImportRegistry importRegistry)
/*     */   {
/*  99 */     this.registry = registry;
/* 100 */     this.sourceExtractor = sourceExtractor;
/* 101 */     this.resourceLoader = resourceLoader;
/* 102 */     this.environment = environment;
/* 103 */     this.importBeanNameGenerator = importBeanNameGenerator;
/* 104 */     this.importRegistry = importRegistry;
/* 105 */     this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadBeanDefinitions(Set<ConfigurationClass> configurationModel)
/*     */   {
/* 114 */     TrackedConditionEvaluator trackedConditionEvaluator = new TrackedConditionEvaluator(null);
/* 115 */     for (ConfigurationClass configClass : configurationModel) {
/* 116 */       loadBeanDefinitionsForConfigurationClass(configClass, trackedConditionEvaluator);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void loadBeanDefinitionsForConfigurationClass(ConfigurationClass configClass, TrackedConditionEvaluator trackedConditionEvaluator)
/*     */   {
/*     */     String beanName;
/*     */     
/*     */ 
/* 127 */     if (trackedConditionEvaluator.shouldSkip(configClass)) {
/* 128 */       beanName = configClass.getBeanName();
/* 129 */       if ((StringUtils.hasLength(beanName)) && (this.registry.containsBeanDefinition(beanName))) {
/* 130 */         this.registry.removeBeanDefinition(beanName);
/*     */       }
/* 132 */       this.importRegistry.removeImportingClass(configClass.getMetadata().getClassName());
/* 133 */       return;
/*     */     }
/*     */     
/* 136 */     if (configClass.isImported()) {
/* 137 */       registerBeanDefinitionForImportedConfigurationClass(configClass);
/*     */     }
/* 139 */     for (BeanMethod beanMethod : configClass.getBeanMethods()) {
/* 140 */       loadBeanDefinitionsForBeanMethod(beanMethod);
/*     */     }
/* 142 */     loadBeanDefinitionsFromImportedResources(configClass.getImportedResources());
/* 143 */     loadBeanDefinitionsFromRegistrars(configClass.getImportBeanDefinitionRegistrars());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void registerBeanDefinitionForImportedConfigurationClass(ConfigurationClass configClass)
/*     */   {
/* 150 */     AnnotationMetadata metadata = configClass.getMetadata();
/* 151 */     AnnotatedGenericBeanDefinition configBeanDef = new AnnotatedGenericBeanDefinition(metadata);
/*     */     
/* 153 */     ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata(configBeanDef);
/* 154 */     configBeanDef.setScope(scopeMetadata.getScopeName());
/* 155 */     String configBeanName = this.importBeanNameGenerator.generateBeanName(configBeanDef, this.registry);
/* 156 */     AnnotationConfigUtils.processCommonDefinitionAnnotations(configBeanDef, metadata);
/*     */     
/* 158 */     BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(configBeanDef, configBeanName);
/* 159 */     definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/* 160 */     this.registry.registerBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
/* 161 */     configClass.setBeanName(configBeanName);
/*     */     
/* 163 */     if (logger.isDebugEnabled()) {
/* 164 */       logger.debug("Registered bean definition for imported class '" + configBeanName + "'");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void loadBeanDefinitionsForBeanMethod(BeanMethod beanMethod)
/*     */   {
/* 173 */     ConfigurationClass configClass = beanMethod.getConfigurationClass();
/* 174 */     MethodMetadata metadata = beanMethod.getMetadata();
/* 175 */     String methodName = metadata.getMethodName();
/*     */     
/*     */ 
/* 178 */     if (this.conditionEvaluator.shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN)) {
/* 179 */       configClass.skippedBeanMethods.add(methodName);
/* 180 */       return;
/*     */     }
/* 182 */     if (configClass.skippedBeanMethods.contains(methodName)) {
/* 183 */       return;
/*     */     }
/*     */     
/*     */ 
/* 187 */     AnnotationAttributes bean = AnnotationConfigUtils.attributesFor(metadata, Bean.class);
/* 188 */     List<String> names = new ArrayList(Arrays.asList(bean.getStringArray("name")));
/* 189 */     String beanName = !names.isEmpty() ? (String)names.remove(0) : methodName;
/*     */     
/*     */ 
/* 192 */     for (String alias : names) {
/* 193 */       this.registry.registerAlias(beanName, alias);
/*     */     }
/*     */     
/*     */ 
/* 197 */     if (isOverriddenByExistingDefinition(beanMethod, beanName)) {
/* 198 */       if (beanName.equals(beanMethod.getConfigurationClass().getBeanName()))
/*     */       {
/* 200 */         throw new BeanDefinitionStoreException(beanMethod.getConfigurationClass().getResource().getDescription(), beanName, "Bean name derived from @Bean method '" + beanMethod.getMetadata().getMethodName() + "' clashes with bean name for containing configuration class; please make those names unique!");
/*     */       }
/*     */       
/* 203 */       return;
/*     */     }
/*     */     
/* 206 */     ConfigurationClassBeanDefinition beanDef = new ConfigurationClassBeanDefinition(configClass, metadata);
/* 207 */     beanDef.setResource(configClass.getResource());
/* 208 */     beanDef.setSource(this.sourceExtractor.extractSource(metadata, configClass.getResource()));
/*     */     
/* 210 */     if (metadata.isStatic())
/*     */     {
/* 212 */       beanDef.setBeanClassName(configClass.getMetadata().getClassName());
/* 213 */       beanDef.setFactoryMethodName(methodName);
/*     */     }
/*     */     else
/*     */     {
/* 217 */       beanDef.setFactoryBeanName(configClass.getBeanName());
/* 218 */       beanDef.setUniqueFactoryMethodName(methodName);
/*     */     }
/* 220 */     beanDef.setAutowireMode(3);
/* 221 */     beanDef.setAttribute(RequiredAnnotationBeanPostProcessor.SKIP_REQUIRED_CHECK_ATTRIBUTE, Boolean.TRUE);
/*     */     
/* 223 */     AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDef, metadata);
/*     */     
/* 225 */     Autowire autowire = (Autowire)bean.getEnum("autowire");
/* 226 */     if (autowire.isAutowire()) {
/* 227 */       beanDef.setAutowireMode(autowire.value());
/*     */     }
/*     */     
/* 230 */     String initMethodName = bean.getString("initMethod");
/* 231 */     if (StringUtils.hasText(initMethodName)) {
/* 232 */       beanDef.setInitMethodName(initMethodName);
/*     */     }
/*     */     
/* 235 */     String destroyMethodName = bean.getString("destroyMethod");
/* 236 */     if (destroyMethodName != null) {
/* 237 */       beanDef.setDestroyMethodName(destroyMethodName);
/*     */     }
/*     */     
/*     */ 
/* 241 */     ScopedProxyMode proxyMode = ScopedProxyMode.NO;
/* 242 */     AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(metadata, Scope.class);
/* 243 */     if (attributes != null) {
/* 244 */       beanDef.setScope(attributes.getString("value"));
/* 245 */       proxyMode = (ScopedProxyMode)attributes.getEnum("proxyMode");
/* 246 */       if (proxyMode == ScopedProxyMode.DEFAULT) {
/* 247 */         proxyMode = ScopedProxyMode.NO;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 252 */     BeanDefinition beanDefToRegister = beanDef;
/* 253 */     if (proxyMode != ScopedProxyMode.NO) {
/* 254 */       BeanDefinitionHolder proxyDef = ScopedProxyCreator.createScopedProxy(new BeanDefinitionHolder(beanDef, beanName), this.registry, proxyMode == ScopedProxyMode.TARGET_CLASS);
/*     */       
/*     */ 
/*     */ 
/* 258 */       beanDefToRegister = new ConfigurationClassBeanDefinition((RootBeanDefinition)proxyDef.getBeanDefinition(), configClass, metadata);
/*     */     }
/*     */     
/* 261 */     if (logger.isDebugEnabled()) {
/* 262 */       logger.debug(String.format("Registering bean definition for @Bean method %s.%s()", new Object[] {configClass
/* 263 */         .getMetadata().getClassName(), beanName }));
/*     */     }
/*     */     
/* 266 */     this.registry.registerBeanDefinition(beanName, beanDefToRegister);
/*     */   }
/*     */   
/*     */   protected boolean isOverriddenByExistingDefinition(BeanMethod beanMethod, String beanName) {
/* 270 */     if (!this.registry.containsBeanDefinition(beanName)) {
/* 271 */       return false;
/*     */     }
/* 273 */     BeanDefinition existingBeanDef = this.registry.getBeanDefinition(beanName);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 279 */     if ((existingBeanDef instanceof ConfigurationClassBeanDefinition)) {
/* 280 */       ConfigurationClassBeanDefinition ccbd = (ConfigurationClassBeanDefinition)existingBeanDef;
/* 281 */       return ccbd.getMetadata().getClassName().equals(beanMethod
/* 282 */         .getConfigurationClass().getMetadata().getClassName());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 287 */     if ((existingBeanDef instanceof ScannedGenericBeanDefinition)) {
/* 288 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 293 */     if (existingBeanDef.getRole() > 0) {
/* 294 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 299 */     if (((this.registry instanceof DefaultListableBeanFactory)) && 
/* 300 */       (!((DefaultListableBeanFactory)this.registry).isAllowBeanDefinitionOverriding())) {
/* 301 */       throw new BeanDefinitionStoreException(beanMethod.getConfigurationClass().getResource().getDescription(), beanName, "@Bean definition illegally overridden by existing bean definition: " + existingBeanDef);
/*     */     }
/*     */     
/* 304 */     if (logger.isInfoEnabled()) {
/* 305 */       logger.info(String.format("Skipping bean definition for %s: a definition for bean '%s' already exists. This top-level bean definition is considered as an override.", new Object[] { beanMethod, beanName }));
/*     */     }
/*     */     
/*     */ 
/* 309 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void loadBeanDefinitionsFromImportedResources(Map<String, Class<? extends BeanDefinitionReader>> importedResources)
/*     */   {
/* 315 */     Map<Class<?>, BeanDefinitionReader> readerInstanceCache = new HashMap();
/*     */     
/* 317 */     for (Map.Entry<String, Class<? extends BeanDefinitionReader>> entry : importedResources.entrySet()) {
/* 318 */       String resource = (String)entry.getKey();
/* 319 */       Class<? extends BeanDefinitionReader> readerClass = (Class)entry.getValue();
/*     */       
/*     */ 
/* 322 */       if (BeanDefinitionReader.class == readerClass) {
/* 323 */         if (StringUtils.endsWithIgnoreCase(resource, ".groovy"))
/*     */         {
/* 325 */           readerClass = GroovyBeanDefinitionReader.class;
/*     */         }
/*     */         else
/*     */         {
/* 329 */           readerClass = XmlBeanDefinitionReader.class;
/*     */         }
/*     */       }
/*     */       
/* 333 */       BeanDefinitionReader reader = (BeanDefinitionReader)readerInstanceCache.get(readerClass);
/* 334 */       if (reader == null) {
/*     */         try
/*     */         {
/* 337 */           reader = (BeanDefinitionReader)readerClass.getConstructor(new Class[] { BeanDefinitionRegistry.class }).newInstance(new Object[] { this.registry });
/*     */           
/* 339 */           if ((reader instanceof AbstractBeanDefinitionReader)) {
/* 340 */             AbstractBeanDefinitionReader abdr = (AbstractBeanDefinitionReader)reader;
/* 341 */             abdr.setResourceLoader(this.resourceLoader);
/* 342 */             abdr.setEnvironment(this.environment);
/*     */           }
/* 344 */           readerInstanceCache.put(readerClass, reader);
/*     */         }
/*     */         catch (Throwable ex)
/*     */         {
/* 348 */           throw new IllegalStateException("Could not instantiate BeanDefinitionReader class [" + readerClass.getName() + "]");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 353 */       reader.loadBeanDefinitions(resource);
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadBeanDefinitionsFromRegistrars(Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> registrars) {
/* 358 */     for (Map.Entry<ImportBeanDefinitionRegistrar, AnnotationMetadata> entry : registrars.entrySet()) {
/* 359 */       ((ImportBeanDefinitionRegistrar)entry.getKey()).registerBeanDefinitions((AnnotationMetadata)entry.getValue(), this.registry);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ConfigurationClassBeanDefinition
/*     */     extends RootBeanDefinition
/*     */     implements AnnotatedBeanDefinition
/*     */   {
/*     */     private final AnnotationMetadata annotationMetadata;
/*     */     
/*     */ 
/*     */     private final MethodMetadata factoryMethodMetadata;
/*     */     
/*     */ 
/*     */ 
/*     */     public ConfigurationClassBeanDefinition(ConfigurationClass configClass, MethodMetadata beanMethodMetadata)
/*     */     {
/* 378 */       this.annotationMetadata = configClass.getMetadata();
/* 379 */       this.factoryMethodMetadata = beanMethodMetadata;
/* 380 */       setLenientConstructorResolution(false);
/*     */     }
/*     */     
/*     */     public ConfigurationClassBeanDefinition(RootBeanDefinition original, ConfigurationClass configClass, MethodMetadata beanMethodMetadata)
/*     */     {
/* 385 */       super();
/* 386 */       this.annotationMetadata = configClass.getMetadata();
/* 387 */       this.factoryMethodMetadata = beanMethodMetadata;
/*     */     }
/*     */     
/*     */     private ConfigurationClassBeanDefinition(ConfigurationClassBeanDefinition original) {
/* 391 */       super();
/* 392 */       this.annotationMetadata = original.annotationMetadata;
/* 393 */       this.factoryMethodMetadata = original.factoryMethodMetadata;
/*     */     }
/*     */     
/*     */     public AnnotationMetadata getMetadata()
/*     */     {
/* 398 */       return this.annotationMetadata;
/*     */     }
/*     */     
/*     */     public MethodMetadata getFactoryMethodMetadata()
/*     */     {
/* 403 */       return this.factoryMethodMetadata;
/*     */     }
/*     */     
/*     */     public boolean isFactoryMethod(Method candidate)
/*     */     {
/* 408 */       return (super.isFactoryMethod(candidate)) && (BeanAnnotationHelper.isBeanAnnotated(candidate));
/*     */     }
/*     */     
/*     */     public ConfigurationClassBeanDefinition cloneBeanDefinition()
/*     */     {
/* 413 */       return new ConfigurationClassBeanDefinition(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class TrackedConditionEvaluator
/*     */   {
/*     */     private TrackedConditionEvaluator() {}
/*     */     
/*     */ 
/* 424 */     private final Map<ConfigurationClass, Boolean> skipped = new HashMap();
/*     */     
/*     */     public boolean shouldSkip(ConfigurationClass configClass) {
/* 427 */       Boolean skip = (Boolean)this.skipped.get(configClass);
/* 428 */       if (skip == null) {
/* 429 */         if (configClass.isImported()) {
/* 430 */           boolean allSkipped = true;
/* 431 */           for (ConfigurationClass importedBy : configClass.getImportedBy()) {
/* 432 */             if (!shouldSkip(importedBy)) {
/* 433 */               allSkipped = false;
/* 434 */               break;
/*     */             }
/*     */           }
/* 437 */           if (allSkipped)
/*     */           {
/* 439 */             skip = Boolean.valueOf(true);
/*     */           }
/*     */         }
/* 442 */         if (skip == null) {
/* 443 */           skip = Boolean.valueOf(ConfigurationClassBeanDefinitionReader.this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN));
/*     */         }
/* 445 */         this.skipped.put(configClass, skip);
/*     */       }
/* 447 */       return skip.booleanValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ConfigurationClassBeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */