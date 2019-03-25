/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.Location;
/*     */ import org.springframework.beans.factory.parsing.Problem;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.NestedIOException;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.CompositePropertySource;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.DefaultPropertySourceFactory;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.core.io.support.PropertySourceFactory;
/*     */ import org.springframework.core.io.support.ResourcePropertySource;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.ClassMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ class ConfigurationClassParser
/*     */ {
/* 102 */   private static final PropertySourceFactory DEFAULT_PROPERTY_SOURCE_FACTORY = new DefaultPropertySourceFactory();
/*     */   
/* 104 */   private static final Comparator<DeferredImportSelectorHolder> DEFERRED_IMPORT_COMPARATOR = new Comparator()
/*     */   {
/*     */     public int compare(ConfigurationClassParser.DeferredImportSelectorHolder o1, ConfigurationClassParser.DeferredImportSelectorHolder o2)
/*     */     {
/* 108 */       return AnnotationAwareOrderComparator.INSTANCE.compare(o1.getImportSelector(), o2.getImportSelector());
/*     */     }
/*     */   };
/*     */   
/*     */ 
/* 113 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final MetadataReaderFactory metadataReaderFactory;
/*     */   
/*     */   private final ProblemReporter problemReporter;
/*     */   
/*     */   private final Environment environment;
/*     */   
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   private final BeanDefinitionRegistry registry;
/*     */   
/*     */   private final ComponentScanAnnotationParser componentScanParser;
/*     */   
/*     */   private final ConditionEvaluator conditionEvaluator;
/*     */   
/* 129 */   private final Map<ConfigurationClass, ConfigurationClass> configurationClasses = new LinkedHashMap();
/*     */   
/*     */ 
/* 132 */   private final Map<String, ConfigurationClass> knownSuperclasses = new HashMap();
/*     */   
/* 134 */   private final List<String> propertySourceNames = new ArrayList();
/*     */   
/* 136 */   private final ImportStack importStack = new ImportStack(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<DeferredImportSelectorHolder> deferredImportSelectors;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConfigurationClassParser(MetadataReaderFactory metadataReaderFactory, ProblemReporter problemReporter, Environment environment, ResourceLoader resourceLoader, BeanNameGenerator componentScanBeanNameGenerator, BeanDefinitionRegistry registry)
/*     */   {
/* 149 */     this.metadataReaderFactory = metadataReaderFactory;
/* 150 */     this.problemReporter = problemReporter;
/* 151 */     this.environment = environment;
/* 152 */     this.resourceLoader = resourceLoader;
/* 153 */     this.registry = registry;
/* 154 */     this.componentScanParser = new ComponentScanAnnotationParser(environment, resourceLoader, componentScanBeanNameGenerator, registry);
/*     */     
/* 156 */     this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
/*     */   }
/*     */   
/*     */   public void parse(Set<BeanDefinitionHolder> configCandidates)
/*     */   {
/* 161 */     this.deferredImportSelectors = new LinkedList();
/*     */     
/* 163 */     for (BeanDefinitionHolder holder : configCandidates) {
/* 164 */       BeanDefinition bd = holder.getBeanDefinition();
/*     */       try {
/* 166 */         if ((bd instanceof AnnotatedBeanDefinition)) {
/* 167 */           parse(((AnnotatedBeanDefinition)bd).getMetadata(), holder.getBeanName());
/*     */         }
/* 169 */         else if (((bd instanceof AbstractBeanDefinition)) && (((AbstractBeanDefinition)bd).hasBeanClass())) {
/* 170 */           parse(((AbstractBeanDefinition)bd).getBeanClass(), holder.getBeanName());
/*     */         }
/*     */         else {
/* 173 */           parse(bd.getBeanClassName(), holder.getBeanName());
/*     */         }
/*     */       }
/*     */       catch (BeanDefinitionStoreException ex) {
/* 177 */         throw ex;
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 181 */         throw new BeanDefinitionStoreException("Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
/*     */       }
/*     */     }
/*     */     
/* 185 */     processDeferredImportSelectors();
/*     */   }
/*     */   
/*     */   protected final void parse(String className, String beanName) throws IOException {
/* 189 */     MetadataReader reader = this.metadataReaderFactory.getMetadataReader(className);
/* 190 */     processConfigurationClass(new ConfigurationClass(reader, beanName));
/*     */   }
/*     */   
/*     */   protected final void parse(Class<?> clazz, String beanName) throws IOException {
/* 194 */     processConfigurationClass(new ConfigurationClass(clazz, beanName));
/*     */   }
/*     */   
/*     */   protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
/* 198 */     processConfigurationClass(new ConfigurationClass(metadata, beanName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate()
/*     */   {
/* 206 */     for (ConfigurationClass configClass : this.configurationClasses.keySet()) {
/* 207 */       configClass.validate(this.problemReporter);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<ConfigurationClass> getConfigurationClasses() {
/* 212 */     return this.configurationClasses.keySet();
/*     */   }
/*     */   
/*     */   protected void processConfigurationClass(ConfigurationClass configClass) throws IOException
/*     */   {
/* 217 */     if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION)) {
/* 218 */       return;
/*     */     }
/*     */     
/* 221 */     ConfigurationClass existingClass = (ConfigurationClass)this.configurationClasses.get(configClass);
/* 222 */     Iterator<ConfigurationClass> it; if (existingClass != null) {
/* 223 */       if (configClass.isImported()) {
/* 224 */         if (existingClass.isImported()) {
/* 225 */           existingClass.mergeImportedBy(configClass);
/*     */         }
/*     */         
/* 228 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 233 */       this.configurationClasses.remove(configClass);
/* 234 */       for (it = this.knownSuperclasses.values().iterator(); it.hasNext();) {
/* 235 */         if (configClass.equals(it.next())) {
/* 236 */           it.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 243 */     SourceClass sourceClass = asSourceClass(configClass);
/*     */     do {
/* 245 */       sourceClass = doProcessConfigurationClass(configClass, sourceClass);
/*     */     }
/* 247 */     while (sourceClass != null);
/*     */     
/* 249 */     this.configurationClasses.put(configClass, configClass);
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
/*     */   protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass)
/*     */     throws IOException
/*     */   {
/* 264 */     processMemberClasses(configClass, sourceClass);
/*     */     
/*     */ 
/* 267 */     for (Iterator localIterator = AnnotationConfigUtils.attributesForRepeatable(sourceClass
/* 268 */           .getMetadata(), PropertySources.class, PropertySource.class).iterator(); localIterator.hasNext();) { propertySource = (AnnotationAttributes)localIterator.next();
/*     */       
/*     */ 
/* 270 */       if ((this.environment instanceof ConfigurableEnvironment)) {
/* 271 */         processPropertySource(propertySource);
/*     */       }
/*     */       else {
/* 274 */         this.logger.warn("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() + "]. Reason: Environment must implement ConfigurableEnvironment");
/*     */       }
/*     */     }
/*     */     
/*     */     AnnotationAttributes propertySource;
/*     */     
/* 280 */     Object componentScans = AnnotationConfigUtils.attributesForRepeatable(sourceClass
/* 281 */       .getMetadata(), ComponentScans.class, ComponentScan.class);
/* 282 */     if ((!((Set)componentScans).isEmpty()) && 
/* 283 */       (!this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN))) {
/* 284 */       for (AnnotationAttributes componentScan : (Set)componentScans)
/*     */       {
/*     */ 
/* 287 */         Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
/*     */         
/* 289 */         for (localObject1 = scannedBeanDefinitions.iterator(); ((Iterator)localObject1).hasNext();) { holder = (BeanDefinitionHolder)((Iterator)localObject1).next();
/* 290 */           if (ConfigurationClassUtils.checkConfigurationClassCandidate(holder
/* 291 */             .getBeanDefinition(), this.metadataReaderFactory)) {
/* 292 */             parse(holder.getBeanDefinition().getBeanClassName(), holder.getBeanName());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Object localObject1;
/*     */     BeanDefinitionHolder holder;
/* 299 */     processImports(configClass, sourceClass, getImports(sourceClass), true);
/*     */     
/*     */     String[] resources;
/* 302 */     if (sourceClass.getMetadata().isAnnotated(ImportResource.class.getName()))
/*     */     {
/* 304 */       AnnotationAttributes importResource = AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
/* 305 */       resources = importResource.getStringArray("locations");
/* 306 */       Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
/* 307 */       localObject1 = resources;holder = localObject1.length; for (BeanDefinitionHolder localBeanDefinitionHolder1 = 0; localBeanDefinitionHolder1 < holder; localBeanDefinitionHolder1++) { String resource = localObject1[localBeanDefinitionHolder1];
/* 308 */         String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
/* 309 */         configClass.addImportedResource(resolvedResource, readerClass);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 314 */     Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
/* 315 */     for (MethodMetadata methodMetadata : beanMethods) {
/* 316 */       configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
/*     */     }
/*     */     
/*     */ 
/* 320 */     processInterfaces(configClass, sourceClass);
/*     */     
/*     */ 
/* 323 */     if (sourceClass.getMetadata().hasSuperClass()) {
/* 324 */       String superclass = sourceClass.getMetadata().getSuperClassName();
/* 325 */       if ((!superclass.startsWith("java")) && (!this.knownSuperclasses.containsKey(superclass))) {
/* 326 */         this.knownSuperclasses.put(superclass, configClass);
/*     */         
/* 328 */         return sourceClass.getSuperClass();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 333 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private void processMemberClasses(ConfigurationClass configClass, SourceClass sourceClass)
/*     */     throws IOException
/*     */   {
/* 340 */     for (SourceClass memberClass : sourceClass.getMemberClasses()) {
/* 341 */       if ((ConfigurationClassUtils.isConfigurationCandidate(memberClass.getMetadata())) && 
/* 342 */         (!memberClass.getMetadata().getClassName().equals(configClass.getMetadata().getClassName()))) {
/* 343 */         if (this.importStack.contains(configClass)) {
/* 344 */           this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
/*     */         }
/*     */         else {
/* 347 */           this.importStack.push(configClass);
/*     */           try {
/* 349 */             processConfigurationClass(memberClass.asConfigClass(configClass));
/*     */           }
/*     */           finally {
/* 352 */             this.importStack.pop();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void processInterfaces(ConfigurationClass configClass, SourceClass sourceClass)
/*     */     throws IOException
/*     */   {
/* 363 */     for (SourceClass ifc : sourceClass.getInterfaces()) {
/* 364 */       Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(ifc);
/* 365 */       for (MethodMetadata methodMetadata : beanMethods) {
/* 366 */         if (!methodMetadata.isAbstract())
/*     */         {
/* 368 */           configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
/*     */         }
/*     */       }
/* 371 */       processInterfaces(configClass, ifc);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Set<MethodMetadata> retrieveBeanMethodMetadata(SourceClass sourceClass)
/*     */   {
/* 379 */     AnnotationMetadata original = sourceClass.getMetadata();
/* 380 */     Set<MethodMetadata> beanMethods = original.getAnnotatedMethods(Bean.class.getName());
/* 381 */     if ((beanMethods.size() > 1) && ((original instanceof StandardAnnotationMetadata)))
/*     */     {
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/* 387 */         AnnotationMetadata asm = this.metadataReaderFactory.getMetadataReader(original.getClassName()).getAnnotationMetadata();
/* 388 */         Set<MethodMetadata> asmMethods = asm.getAnnotatedMethods(Bean.class.getName());
/* 389 */         if (asmMethods.size() >= beanMethods.size()) {
/* 390 */           Set<MethodMetadata> selectedMethods = new LinkedHashSet(asmMethods.size());
/* 391 */           for (Iterator localIterator1 = asmMethods.iterator(); localIterator1.hasNext();) { asmMethod = (MethodMetadata)localIterator1.next();
/* 392 */             for (MethodMetadata beanMethod : beanMethods)
/* 393 */               if (beanMethod.getMethodName().equals(asmMethod.getMethodName())) {
/* 394 */                 selectedMethods.add(beanMethod);
/* 395 */                 break;
/*     */               }
/*     */           }
/*     */           MethodMetadata asmMethod;
/* 399 */           if (selectedMethods.size() == beanMethods.size())
/*     */           {
/* 401 */             beanMethods = selectedMethods;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException ex) {
/* 406 */         this.logger.debug("Failed to read class file via ASM for determining @Bean method order", ex);
/*     */       }
/*     */     }
/*     */     
/* 410 */     return beanMethods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void processPropertySource(AnnotationAttributes propertySource)
/*     */     throws IOException
/*     */   {
/* 420 */     String name = propertySource.getString("name");
/* 421 */     if (!StringUtils.hasLength(name)) {
/* 422 */       name = null;
/*     */     }
/* 424 */     String encoding = propertySource.getString("encoding");
/* 425 */     if (!StringUtils.hasLength(encoding)) {
/* 426 */       encoding = null;
/*     */     }
/* 428 */     String[] locations = propertySource.getStringArray("value");
/* 429 */     Assert.isTrue(locations.length > 0, "At least one @PropertySource(value) location is required");
/* 430 */     boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");
/*     */     
/* 432 */     Class<? extends PropertySourceFactory> factoryClass = propertySource.getClass("factory");
/*     */     
/* 434 */     PropertySourceFactory factory = factoryClass == PropertySourceFactory.class ? DEFAULT_PROPERTY_SOURCE_FACTORY : (PropertySourceFactory)BeanUtils.instantiateClass(factoryClass);
/*     */     
/* 436 */     for (String location : locations) {
/*     */       try {
/* 438 */         String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
/* 439 */         Resource resource = this.resourceLoader.getResource(resolvedLocation);
/* 440 */         addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
/*     */       }
/*     */       catch (IllegalArgumentException ex)
/*     */       {
/* 444 */         if (ignoreResourceNotFound) {
/* 445 */           if (this.logger.isInfoEnabled()) {
/* 446 */             this.logger.info("Properties location [" + location + "] not resolvable: " + ex.getMessage());
/*     */           }
/*     */         }
/*     */         else {
/* 450 */           throw ex;
/*     */         }
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 455 */         if ((ignoreResourceNotFound) && (((ex instanceof FileNotFoundException)) || ((ex instanceof UnknownHostException))))
/*     */         {
/* 457 */           if (this.logger.isInfoEnabled()) {
/* 458 */             this.logger.info("Properties location [" + location + "] not resolvable: " + ex.getMessage());
/*     */           }
/*     */         }
/*     */         else {
/* 462 */           throw ex;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addPropertySource(org.springframework.core.env.PropertySource<?> propertySource) {
/* 469 */     String name = propertySource.getName();
/* 470 */     MutablePropertySources propertySources = ((ConfigurableEnvironment)this.environment).getPropertySources();
/* 471 */     if ((propertySources.contains(name)) && (this.propertySourceNames.contains(name)))
/*     */     {
/* 473 */       org.springframework.core.env.PropertySource<?> existing = propertySources.get(name);
/*     */       
/* 475 */       org.springframework.core.env.PropertySource<?> newSource = (propertySource instanceof ResourcePropertySource) ? ((ResourcePropertySource)propertySource).withResourceName() : propertySource;
/* 476 */       if ((existing instanceof CompositePropertySource)) {
/* 477 */         ((CompositePropertySource)existing).addFirstPropertySource(newSource);
/*     */       }
/*     */       else {
/* 480 */         if ((existing instanceof ResourcePropertySource)) {
/* 481 */           existing = ((ResourcePropertySource)existing).withResourceName();
/*     */         }
/* 483 */         CompositePropertySource composite = new CompositePropertySource(name);
/* 484 */         composite.addPropertySource(newSource);
/* 485 */         composite.addPropertySource(existing);
/* 486 */         propertySources.replace(name, composite);
/*     */       }
/*     */       
/*     */     }
/* 490 */     else if (this.propertySourceNames.isEmpty()) {
/* 491 */       propertySources.addLast(propertySource);
/*     */     }
/*     */     else {
/* 494 */       String firstProcessed = (String)this.propertySourceNames.get(this.propertySourceNames.size() - 1);
/* 495 */       propertySources.addBefore(firstProcessed, propertySource);
/*     */     }
/*     */     
/* 498 */     this.propertySourceNames.add(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Set<SourceClass> getImports(SourceClass sourceClass)
/*     */     throws IOException
/*     */   {
/* 506 */     Set<SourceClass> imports = new LinkedHashSet();
/* 507 */     Set<SourceClass> visited = new LinkedHashSet();
/* 508 */     collectImports(sourceClass, imports, visited);
/* 509 */     return imports;
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
/*     */   private void collectImports(SourceClass sourceClass, Set<SourceClass> imports, Set<SourceClass> visited)
/*     */     throws IOException
/*     */   {
/* 528 */     if (visited.add(sourceClass)) {
/* 529 */       for (SourceClass annotation : sourceClass.getAnnotations()) {
/* 530 */         String annName = annotation.getMetadata().getClassName();
/* 531 */         if ((!annName.startsWith("java")) && (!annName.equals(Import.class.getName()))) {
/* 532 */           collectImports(annotation, imports, visited);
/*     */         }
/*     */       }
/* 535 */       imports.addAll(sourceClass.getAnnotationAttributes(Import.class.getName(), "value"));
/*     */     }
/*     */   }
/*     */   
/*     */   private void processDeferredImportSelectors() {
/* 540 */     List<DeferredImportSelectorHolder> deferredImports = this.deferredImportSelectors;
/* 541 */     this.deferredImportSelectors = null;
/* 542 */     Collections.sort(deferredImports, DEFERRED_IMPORT_COMPARATOR);
/*     */     
/* 544 */     for (DeferredImportSelectorHolder deferredImport : deferredImports) {
/* 545 */       ConfigurationClass configClass = deferredImport.getConfigurationClass();
/*     */       try {
/* 547 */         String[] imports = deferredImport.getImportSelector().selectImports(configClass.getMetadata());
/* 548 */         processImports(configClass, asSourceClass(configClass), asSourceClasses(imports), false);
/*     */       }
/*     */       catch (BeanDefinitionStoreException ex) {
/* 551 */         throw ex;
/*     */ 
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 556 */         throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configClass.getMetadata().getClassName() + "]", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processImports(ConfigurationClass configClass, SourceClass currentSourceClass, Collection<SourceClass> importCandidates, boolean checkForCircularImports)
/*     */     throws IOException
/*     */   {
/* 564 */     if (importCandidates.isEmpty()) {
/* 565 */       return;
/*     */     }
/*     */     
/* 568 */     if ((checkForCircularImports) && (isChainedImportOnStack(configClass))) {
/* 569 */       this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
/*     */     }
/*     */     else {
/* 572 */       this.importStack.push(configClass);
/*     */       try {
/* 574 */         for (SourceClass candidate : importCandidates) {
/* 575 */           if (candidate.isAssignable(ImportSelector.class))
/*     */           {
/* 577 */             Class<?> candidateClass = candidate.loadClass();
/* 578 */             ImportSelector selector = (ImportSelector)BeanUtils.instantiateClass(candidateClass, ImportSelector.class);
/* 579 */             ParserStrategyUtils.invokeAwareMethods(selector, this.environment, this.resourceLoader, this.registry);
/*     */             
/* 581 */             if ((this.deferredImportSelectors != null) && ((selector instanceof DeferredImportSelector))) {
/* 582 */               this.deferredImportSelectors.add(new DeferredImportSelectorHolder(configClass, (DeferredImportSelector)selector));
/*     */             }
/*     */             else
/*     */             {
/* 586 */               String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());
/* 587 */               Collection<SourceClass> importSourceClasses = asSourceClasses(importClassNames);
/* 588 */               processImports(configClass, currentSourceClass, importSourceClasses, false);
/*     */             }
/*     */           }
/* 591 */           else if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class))
/*     */           {
/*     */ 
/* 594 */             Class<?> candidateClass = candidate.loadClass();
/*     */             
/* 596 */             ImportBeanDefinitionRegistrar registrar = (ImportBeanDefinitionRegistrar)BeanUtils.instantiateClass(candidateClass, ImportBeanDefinitionRegistrar.class);
/* 597 */             ParserStrategyUtils.invokeAwareMethods(registrar, this.environment, this.resourceLoader, this.registry);
/*     */             
/* 599 */             configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 604 */             this.importStack.registerImport(currentSourceClass
/* 605 */               .getMetadata(), candidate.getMetadata().getClassName());
/* 606 */             processConfigurationClass(candidate.asConfigClass(configClass));
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (BeanDefinitionStoreException ex) {
/* 611 */         throw ex;
/*     */ 
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 616 */         throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configClass.getMetadata().getClassName() + "]", ex);
/*     */       }
/*     */       finally {
/* 619 */         this.importStack.pop();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isChainedImportOnStack(ConfigurationClass configClass) {
/* 625 */     if (this.importStack.contains(configClass)) {
/* 626 */       String configClassName = configClass.getMetadata().getClassName();
/* 627 */       AnnotationMetadata importingClass = this.importStack.getImportingClassFor(configClassName);
/* 628 */       while (importingClass != null) {
/* 629 */         if (configClassName.equals(importingClass.getClassName())) {
/* 630 */           return true;
/*     */         }
/* 632 */         importingClass = this.importStack.getImportingClassFor(importingClass.getClassName());
/*     */       }
/*     */     }
/* 635 */     return false;
/*     */   }
/*     */   
/*     */   ImportRegistry getImportRegistry() {
/* 639 */     return this.importStack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private SourceClass asSourceClass(ConfigurationClass configurationClass)
/*     */     throws IOException
/*     */   {
/* 647 */     AnnotationMetadata metadata = configurationClass.getMetadata();
/* 648 */     if ((metadata instanceof StandardAnnotationMetadata)) {
/* 649 */       return asSourceClass(((StandardAnnotationMetadata)metadata).getIntrospectedClass());
/*     */     }
/* 651 */     return asSourceClass(metadata.getClassName());
/*     */   }
/*     */   
/*     */ 
/*     */   SourceClass asSourceClass(Class<?> classType)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 660 */       classType.getAnnotations();
/* 661 */       return new SourceClass(classType);
/*     */     }
/*     */     catch (Throwable ex) {}
/*     */     
/* 665 */     return asSourceClass(classType.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Collection<SourceClass> asSourceClasses(String[] classNames)
/*     */     throws IOException
/*     */   {
/* 673 */     List<SourceClass> annotatedClasses = new ArrayList(classNames.length);
/* 674 */     for (String className : classNames) {
/* 675 */       annotatedClasses.add(asSourceClass(className));
/*     */     }
/* 677 */     return annotatedClasses;
/*     */   }
/*     */   
/*     */ 
/*     */   SourceClass asSourceClass(String className)
/*     */     throws IOException
/*     */   {
/* 684 */     if (className.startsWith("java")) {
/*     */       try
/*     */       {
/* 687 */         return new SourceClass(this.resourceLoader.getClassLoader().loadClass(className));
/*     */       }
/*     */       catch (ClassNotFoundException ex) {
/* 690 */         throw new NestedIOException("Failed to load class [" + className + "]", ex);
/*     */       }
/*     */     }
/* 693 */     return new SourceClass(this.metadataReaderFactory.getMetadataReader(className));
/*     */   }
/*     */   
/*     */   private static class ImportStack
/*     */     extends ArrayDeque<ConfigurationClass>
/*     */     implements ImportRegistry
/*     */   {
/* 700 */     private final MultiValueMap<String, AnnotationMetadata> imports = new LinkedMultiValueMap();
/*     */     
/*     */     public void registerImport(AnnotationMetadata importingClass, String importedClass) {
/* 703 */       this.imports.add(importedClass, importingClass);
/*     */     }
/*     */     
/*     */     public AnnotationMetadata getImportingClassFor(String importedClass)
/*     */     {
/* 708 */       List<AnnotationMetadata> list = (List)this.imports.get(importedClass);
/* 709 */       return !CollectionUtils.isEmpty(list) ? (AnnotationMetadata)list.get(list.size() - 1) : null;
/*     */     }
/*     */     
/*     */     public void removeImportingClass(String importingClass)
/*     */     {
/* 714 */       for (List<AnnotationMetadata> list : this.imports.values()) {
/* 715 */         for (iterator = list.iterator(); iterator.hasNext();) {
/* 716 */           if (((AnnotationMetadata)iterator.next()).getClassName().equals(importingClass)) {
/* 717 */             iterator.remove();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       Iterator<AnnotationMetadata> iterator;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 735 */       StringBuilder builder = new StringBuilder("[");
/* 736 */       Iterator<ConfigurationClass> iterator = iterator();
/* 737 */       while (iterator.hasNext()) {
/* 738 */         builder.append(((ConfigurationClass)iterator.next()).getSimpleName());
/* 739 */         if (iterator.hasNext()) {
/* 740 */           builder.append("->");
/*     */         }
/*     */       }
/* 743 */       return ']';
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class DeferredImportSelectorHolder
/*     */   {
/*     */     private final ConfigurationClass configurationClass;
/*     */     private final DeferredImportSelector importSelector;
/*     */     
/*     */     public DeferredImportSelectorHolder(ConfigurationClass configClass, DeferredImportSelector selector)
/*     */     {
/* 755 */       this.configurationClass = configClass;
/* 756 */       this.importSelector = selector;
/*     */     }
/*     */     
/*     */     public ConfigurationClass getConfigurationClass() {
/* 760 */       return this.configurationClass;
/*     */     }
/*     */     
/*     */     public DeferredImportSelector getImportSelector() {
/* 764 */       return this.importSelector;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class SourceClass
/*     */   {
/*     */     private final Object source;
/*     */     
/*     */ 
/*     */     private final AnnotationMetadata metadata;
/*     */     
/*     */ 
/*     */     public SourceClass(Object source)
/*     */     {
/* 780 */       this.source = source;
/* 781 */       if ((source instanceof Class)) {
/* 782 */         this.metadata = new StandardAnnotationMetadata((Class)source, true);
/*     */       }
/*     */       else {
/* 785 */         this.metadata = ((MetadataReader)source).getAnnotationMetadata();
/*     */       }
/*     */     }
/*     */     
/*     */     public final AnnotationMetadata getMetadata() {
/* 790 */       return this.metadata;
/*     */     }
/*     */     
/*     */     public Class<?> loadClass() throws ClassNotFoundException {
/* 794 */       if ((this.source instanceof Class)) {
/* 795 */         return (Class)this.source;
/*     */       }
/* 797 */       String className = ((MetadataReader)this.source).getClassMetadata().getClassName();
/* 798 */       return ConfigurationClassParser.this.resourceLoader.getClassLoader().loadClass(className);
/*     */     }
/*     */     
/*     */     public boolean isAssignable(Class<?> clazz) throws IOException {
/* 802 */       if ((this.source instanceof Class)) {
/* 803 */         return clazz.isAssignableFrom((Class)this.source);
/*     */       }
/* 805 */       return new AssignableTypeFilter(clazz).match((MetadataReader)this.source, ConfigurationClassParser.this.metadataReaderFactory);
/*     */     }
/*     */     
/*     */     public ConfigurationClass asConfigClass(ConfigurationClass importedBy) throws IOException {
/* 809 */       if ((this.source instanceof Class)) {
/* 810 */         return new ConfigurationClass((Class)this.source, importedBy);
/*     */       }
/* 812 */       return new ConfigurationClass((MetadataReader)this.source, importedBy);
/*     */     }
/*     */     
/*     */     public Collection<SourceClass> getMemberClasses() throws IOException {
/* 816 */       Object sourceToProcess = this.source;
/* 817 */       if ((sourceToProcess instanceof Class)) {
/* 818 */         Class<?> sourceClass = (Class)sourceToProcess;
/*     */         try {
/* 820 */           Class<?>[] declaredClasses = sourceClass.getDeclaredClasses();
/* 821 */           List<SourceClass> members = new ArrayList(declaredClasses.length);
/* 822 */           for (Class<?> declaredClass : declaredClasses) {
/* 823 */             members.add(ConfigurationClassParser.this.asSourceClass(declaredClass));
/*     */           }
/* 825 */           return members;
/*     */ 
/*     */         }
/*     */         catch (NoClassDefFoundError err)
/*     */         {
/* 830 */           sourceToProcess = ConfigurationClassParser.this.metadataReaderFactory.getMetadataReader(sourceClass.getName());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 835 */       MetadataReader sourceReader = (MetadataReader)sourceToProcess;
/* 836 */       String[] memberClassNames = sourceReader.getClassMetadata().getMemberClassNames();
/* 837 */       List<SourceClass> members = new ArrayList(memberClassNames.length);
/* 838 */       for (String memberClassName : memberClassNames) {
/*     */         try {
/* 840 */           members.add(ConfigurationClassParser.this.asSourceClass(memberClassName));
/*     */         }
/*     */         catch (IOException ex)
/*     */         {
/* 844 */           if (ConfigurationClassParser.this.logger.isDebugEnabled()) {
/* 845 */             ConfigurationClassParser.this.logger.debug("Failed to resolve member class [" + memberClassName + "] - not considering it as a configuration class candidate");
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 850 */       return members;
/*     */     }
/*     */     
/*     */     public SourceClass getSuperClass() throws IOException {
/* 854 */       if ((this.source instanceof Class)) {
/* 855 */         return ConfigurationClassParser.this.asSourceClass(((Class)this.source).getSuperclass());
/*     */       }
/* 857 */       return ConfigurationClassParser.this.asSourceClass(((MetadataReader)this.source).getClassMetadata().getSuperClassName());
/*     */     }
/*     */     
/*     */     public Set<SourceClass> getInterfaces() throws IOException {
/* 861 */       Set<SourceClass> result = new LinkedHashSet();
/* 862 */       Class<?> sourceClass; if ((this.source instanceof Class)) {
/* 863 */         sourceClass = (Class)this.source;
/* 864 */         for (Class<?> ifcClass : sourceClass.getInterfaces()) {
/* 865 */           result.add(ConfigurationClassParser.this.asSourceClass(ifcClass));
/*     */         }
/*     */       }
/*     */       else {
/* 869 */         for (String className : this.metadata.getInterfaceNames()) {
/* 870 */           result.add(ConfigurationClassParser.this.asSourceClass(className));
/*     */         }
/*     */       }
/* 873 */       return result;
/*     */     }
/*     */     
/*     */     public Set<SourceClass> getAnnotations() throws IOException {
/* 877 */       Set<SourceClass> result = new LinkedHashSet();
/* 878 */       for (String className : this.metadata.getAnnotationTypes()) {
/*     */         try {
/* 880 */           result.add(getRelated(className));
/*     */         }
/*     */         catch (Throwable localThrowable) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 887 */       return result;
/*     */     }
/*     */     
/*     */     public Collection<SourceClass> getAnnotationAttributes(String annType, String attribute) throws IOException {
/* 891 */       Map<String, Object> annotationAttributes = this.metadata.getAnnotationAttributes(annType, true);
/* 892 */       if ((annotationAttributes == null) || (!annotationAttributes.containsKey(attribute))) {
/* 893 */         return Collections.emptySet();
/*     */       }
/* 895 */       String[] classNames = (String[])annotationAttributes.get(attribute);
/* 896 */       Set<SourceClass> result = new LinkedHashSet();
/* 897 */       for (String className : classNames) {
/* 898 */         result.add(getRelated(className));
/*     */       }
/* 900 */       return result;
/*     */     }
/*     */     
/*     */     private SourceClass getRelated(String className) throws IOException {
/* 904 */       if ((this.source instanceof Class)) {
/*     */         try {
/* 906 */           Class<?> clazz = ((Class)this.source).getClassLoader().loadClass(className);
/* 907 */           return ConfigurationClassParser.this.asSourceClass(clazz);
/*     */         }
/*     */         catch (ClassNotFoundException ex)
/*     */         {
/* 911 */           if (className.startsWith("java")) {
/* 912 */             throw new NestedIOException("Failed to load class [" + className + "]", ex);
/*     */           }
/* 914 */           return new SourceClass(ConfigurationClassParser.this, ConfigurationClassParser.this.metadataReaderFactory.getMetadataReader(className));
/*     */         }
/*     */       }
/* 917 */       return ConfigurationClassParser.this.asSourceClass(className);
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 922 */       return (this == other) || (((other instanceof SourceClass)) && 
/* 923 */         (this.metadata.getClassName().equals(((SourceClass)other).metadata.getClassName())));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 928 */       return this.metadata.getClassName().hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 933 */       return this.metadata.getClassName();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class CircularImportProblem
/*     */     extends Problem
/*     */   {
/*     */     public CircularImportProblem(ConfigurationClass attemptedImport, Deque<ConfigurationClass> importStack)
/*     */     {
/* 944 */       super(new Location(
/*     */       
/*     */ 
/*     */ 
/* 948 */         ((ConfigurationClass)importStack.peek()).getResource(), attemptedImport.getMetadata()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ConfigurationClassParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */