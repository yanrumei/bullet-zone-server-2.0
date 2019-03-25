/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.LookupOverride;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.MethodOverrides;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.FieldCallback;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
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
/*     */ public class AutowiredAnnotationBeanPostProcessor
/*     */   extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*     */ {
/* 120 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 122 */   private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet();
/*     */   
/*     */ 
/* 125 */   private String requiredParameterName = "required";
/*     */   
/* 127 */   private boolean requiredParameterValue = true;
/*     */   
/* 129 */   private int order = 2147483645;
/*     */   
/*     */ 
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */   
/* 134 */   private final Set<String> lookupMethodsChecked = Collections.newSetFromMap(new ConcurrentHashMap(256));
/*     */   
/* 136 */   private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = new ConcurrentHashMap(256);
/*     */   
/*     */ 
/* 139 */   private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap(256);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AutowiredAnnotationBeanPostProcessor()
/*     */   {
/* 150 */     this.autowiredAnnotationTypes.add(Autowired.class);
/* 151 */     this.autowiredAnnotationTypes.add(Value.class);
/*     */     try {
/* 153 */       this.autowiredAnnotationTypes.add(
/* 154 */         ClassUtils.forName("javax.inject.Inject", AutowiredAnnotationBeanPostProcessor.class.getClassLoader()));
/* 155 */       this.logger.info("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
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
/*     */   public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType)
/*     */   {
/* 173 */     Assert.notNull(autowiredAnnotationType, "'autowiredAnnotationType' must not be null");
/* 174 */     this.autowiredAnnotationTypes.clear();
/* 175 */     this.autowiredAnnotationTypes.add(autowiredAnnotationType);
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
/*     */   public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes)
/*     */   {
/* 188 */     Assert.notEmpty(autowiredAnnotationTypes, "'autowiredAnnotationTypes' must not be empty");
/* 189 */     this.autowiredAnnotationTypes.clear();
/* 190 */     this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequiredParameterName(String requiredParameterName)
/*     */   {
/* 199 */     this.requiredParameterName = requiredParameterName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequiredParameterValue(boolean requiredParameterValue)
/*     */   {
/* 210 */     this.requiredParameterValue = requiredParameterValue;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 214 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 219 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/* 224 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/* 225 */       throw new IllegalArgumentException("AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/* 228 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*     */   }
/*     */   
/*     */ 
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
/*     */   {
/* 234 */     if (beanType != null) {
/* 235 */       InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
/* 236 */       metadata.checkConfigMembers(beanDefinition);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, final String beanName)
/*     */     throws BeanCreationException
/*     */   {
/* 245 */     if (!this.lookupMethodsChecked.contains(beanName)) {
/*     */       try {
/* 247 */         ReflectionUtils.doWithMethods(beanClass, new ReflectionUtils.MethodCallback()
/*     */         {
/*     */           public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 250 */             Lookup lookup = (Lookup)method.getAnnotation(Lookup.class);
/* 251 */             if (lookup != null) {
/* 252 */               LookupOverride override = new LookupOverride(method, lookup.value());
/*     */               try {
/* 254 */                 RootBeanDefinition mbd = (RootBeanDefinition)AutowiredAnnotationBeanPostProcessor.this.beanFactory.getMergedBeanDefinition(beanName);
/* 255 */                 mbd.getMethodOverrides().addOverride(override);
/*     */               }
/*     */               catch (NoSuchBeanDefinitionException ex) {
/* 258 */                 throw new BeanCreationException(beanName, "Cannot apply @Lookup to beans without corresponding bean definition");
/*     */               }
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (IllegalStateException ex)
/*     */       {
/* 266 */         throw new BeanCreationException(beanName, "Lookup method resolution failed", ex);
/*     */       }
/*     */       catch (NoClassDefFoundError err) {
/* 269 */         throw new BeanCreationException(beanName, "Failed to introspect bean class [" + beanClass.getName() + "] for lookup method metadata: could not find class that it depends on", err);
/*     */       }
/*     */       
/* 272 */       this.lookupMethodsChecked.add(beanName);
/*     */     }
/*     */     
/*     */ 
/* 276 */     Constructor<?>[] candidateConstructors = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 277 */     if (candidateConstructors == null)
/*     */     {
/* 279 */       synchronized (this.candidateConstructorsCache) {
/* 280 */         candidateConstructors = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 281 */         if (candidateConstructors == null)
/*     */         {
/*     */           try {
/* 284 */             rawCandidates = beanClass.getDeclaredConstructors();
/*     */           }
/*     */           catch (Throwable ex)
/*     */           {
/*     */             Constructor<?>[] rawCandidates;
/* 289 */             throw new BeanCreationException(beanName, "Resolution of declared constructors on bean Class [" + beanClass.getName() + "] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex); }
/*     */           Constructor<?>[] rawCandidates;
/* 291 */           List<Constructor<?>> candidates = new ArrayList(rawCandidates.length);
/* 292 */           Constructor<?> requiredConstructor = null;
/* 293 */           Constructor<?> defaultConstructor = null;
/* 294 */           for (Constructor<?> candidate : rawCandidates) {
/* 295 */             AnnotationAttributes ann = findAutowiredAnnotation(candidate);
/* 296 */             if (ann == null) {
/* 297 */               Class<?> userClass = ClassUtils.getUserClass(beanClass);
/* 298 */               if (userClass != beanClass) {
/*     */                 try
/*     */                 {
/* 301 */                   Constructor<?> superCtor = userClass.getDeclaredConstructor(candidate.getParameterTypes());
/* 302 */                   ann = findAutowiredAnnotation(superCtor);
/*     */                 }
/*     */                 catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 309 */             if (ann != null) {
/* 310 */               if (requiredConstructor != null) {
/* 311 */                 throw new BeanCreationException(beanName, "Invalid autowire-marked constructor: " + candidate + ". Found constructor with 'required' Autowired annotation already: " + requiredConstructor);
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 316 */               boolean required = determineRequiredStatus(ann);
/* 317 */               if (required) {
/* 318 */                 if (!candidates.isEmpty()) {
/* 319 */                   throw new BeanCreationException(beanName, "Invalid autowire-marked constructors: " + candidates + ". Found constructor with 'required' Autowired annotation: " + candidate);
/*     */                 }
/*     */                 
/*     */ 
/*     */ 
/* 324 */                 requiredConstructor = candidate;
/*     */               }
/* 326 */               candidates.add(candidate);
/*     */             }
/* 328 */             else if (candidate.getParameterTypes().length == 0) {
/* 329 */               defaultConstructor = candidate;
/*     */             }
/*     */           }
/* 332 */           if (!candidates.isEmpty())
/*     */           {
/* 334 */             if (requiredConstructor == null) {
/* 335 */               if (defaultConstructor != null) {
/* 336 */                 candidates.add(defaultConstructor);
/*     */               }
/* 338 */               else if ((candidates.size() == 1) && (this.logger.isWarnEnabled())) {
/* 339 */                 this.logger.warn("Inconsistent constructor declaration on bean with name '" + beanName + "': single autowire-marked constructor flagged as optional - this constructor is effectively required since there is no default constructor to fall back to: " + candidates
/*     */                 
/*     */ 
/* 342 */                   .get(0));
/*     */               }
/*     */             }
/* 345 */             candidateConstructors = (Constructor[])candidates.toArray(new Constructor[candidates.size()]);
/*     */           }
/* 347 */           else if ((rawCandidates.length == 1) && (rawCandidates[0].getParameterTypes().length > 0)) {
/* 348 */             candidateConstructors = new Constructor[] { rawCandidates[0] };
/*     */           }
/*     */           else {
/* 351 */             candidateConstructors = new Constructor[0];
/*     */           }
/* 353 */           this.candidateConstructorsCache.put(beanClass, candidateConstructors);
/*     */         }
/*     */       }
/*     */     }
/* 357 */     return candidateConstructors.length > 0 ? candidateConstructors : null;
/*     */   }
/*     */   
/*     */ 
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/*     */     throws BeanCreationException
/*     */   {
/* 364 */     InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
/*     */     try {
/* 366 */       metadata.inject(bean, beanName, pvs);
/*     */     }
/*     */     catch (BeanCreationException ex) {
/* 369 */       throw ex;
/*     */     }
/*     */     catch (Throwable ex) {
/* 372 */       throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
/*     */     }
/* 374 */     return pvs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processInjection(Object bean)
/*     */     throws BeanCreationException
/*     */   {
/* 384 */     Class<?> clazz = bean.getClass();
/* 385 */     InjectionMetadata metadata = findAutowiringMetadata(clazz.getName(), clazz, null);
/*     */     try {
/* 387 */       metadata.inject(bean, null, null);
/*     */     }
/*     */     catch (BeanCreationException ex) {
/* 390 */       throw ex;
/*     */     }
/*     */     catch (Throwable ex) {
/* 393 */       throw new BeanCreationException("Injection of autowired dependencies failed for class [" + clazz + "]", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, PropertyValues pvs)
/*     */   {
/* 401 */     String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
/*     */     
/* 403 */     InjectionMetadata metadata = (InjectionMetadata)this.injectionMetadataCache.get(cacheKey);
/* 404 */     if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 405 */       synchronized (this.injectionMetadataCache) {
/* 406 */         metadata = (InjectionMetadata)this.injectionMetadataCache.get(cacheKey);
/* 407 */         if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 408 */           if (metadata != null) {
/* 409 */             metadata.clear(pvs);
/*     */           }
/*     */           try {
/* 412 */             metadata = buildAutowiringMetadata(clazz);
/* 413 */             this.injectionMetadataCache.put(cacheKey, metadata);
/*     */           }
/*     */           catch (NoClassDefFoundError err) {
/* 416 */             throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() + "] for autowiring metadata: could not find class that it depends on", err);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 422 */     return metadata;
/*     */   }
/*     */   
/*     */   private InjectionMetadata buildAutowiringMetadata(final Class<?> clazz) {
/* 426 */     LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList();
/* 427 */     Class<?> targetClass = clazz;
/*     */     do
/*     */     {
/* 430 */       final LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList();
/*     */       
/*     */ 
/* 433 */       ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback()
/*     */       {
/*     */         public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 436 */           AnnotationAttributes ann = AutowiredAnnotationBeanPostProcessor.this.findAutowiredAnnotation(field);
/* 437 */           if (ann != null) {
/* 438 */             if (Modifier.isStatic(field.getModifiers())) {
/* 439 */               if (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
/* 440 */                 AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation is not supported on static fields: " + field);
/*     */               }
/* 442 */               return;
/*     */             }
/* 444 */             boolean required = AutowiredAnnotationBeanPostProcessor.this.determineRequiredStatus(ann);
/* 445 */             currElements.add(new AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement(AutowiredAnnotationBeanPostProcessor.this, field, required));
/*     */           }
/*     */           
/*     */         }
/* 449 */       });
/* 450 */       ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback()
/*     */       {
/*     */         public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 453 */           Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 454 */           if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
/* 455 */             return;
/*     */           }
/* 457 */           AnnotationAttributes ann = AutowiredAnnotationBeanPostProcessor.this.findAutowiredAnnotation(bridgedMethod);
/* 458 */           if ((ann != null) && (method.equals(ClassUtils.getMostSpecificMethod(method, clazz)))) {
/* 459 */             if (Modifier.isStatic(method.getModifiers())) {
/* 460 */               if (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
/* 461 */                 AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation is not supported on static methods: " + method);
/*     */               }
/* 463 */               return;
/*     */             }
/* 465 */             if ((method.getParameterTypes().length == 0) && 
/* 466 */               (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled())) {
/* 467 */               AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation should only be used on methods with parameters: " + method);
/*     */             }
/*     */             
/*     */ 
/* 471 */             boolean required = AutowiredAnnotationBeanPostProcessor.this.determineRequiredStatus(ann);
/* 472 */             PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/* 473 */             currElements.add(new AutowiredAnnotationBeanPostProcessor.AutowiredMethodElement(AutowiredAnnotationBeanPostProcessor.this, method, required, pd));
/*     */           }
/*     */           
/*     */         }
/* 477 */       });
/* 478 */       elements.addAll(0, currElements);
/* 479 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 481 */     while ((targetClass != null) && (targetClass != Object.class));
/*     */     
/* 483 */     return new InjectionMetadata(clazz, elements);
/*     */   }
/*     */   
/*     */   private AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
/* 487 */     if (ao.getAnnotations().length > 0) {
/* 488 */       for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
/* 489 */         AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
/* 490 */         if (attributes != null) {
/* 491 */           return attributes;
/*     */         }
/*     */       }
/*     */     }
/* 495 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean determineRequiredStatus(AnnotationAttributes ann)
/*     */   {
/* 507 */     return (!ann.containsKey(this.requiredParameterName)) || 
/* 508 */       (this.requiredParameterValue == ann.getBoolean(this.requiredParameterName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected <T> Map<String, T> findAutowireCandidates(Class<T> type)
/*     */     throws BeansException
/*     */   {
/* 518 */     if (this.beanFactory == null) {
/* 519 */       throw new IllegalStateException("No BeanFactory configured - override the getBeanOfType method or specify the 'beanFactory' property");
/*     */     }
/*     */     
/* 522 */     return BeanFactoryUtils.beansOfTypeIncludingAncestors(this.beanFactory, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void registerDependentBeans(String beanName, Set<String> autowiredBeanNames)
/*     */   {
/* 529 */     if (beanName != null) {
/* 530 */       for (String autowiredBeanName : autowiredBeanNames) {
/* 531 */         if (this.beanFactory.containsBean(autowiredBeanName)) {
/* 532 */           this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
/*     */         }
/* 534 */         if (this.logger.isDebugEnabled()) {
/* 535 */           this.logger.debug("Autowiring by type from bean name '" + beanName + "' to bean named '" + autowiredBeanName + "'");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object resolvedCachedArgument(String beanName, Object cachedArgument)
/*     */   {
/* 546 */     if ((cachedArgument instanceof DependencyDescriptor)) {
/* 547 */       DependencyDescriptor descriptor = (DependencyDescriptor)cachedArgument;
/* 548 */       return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
/*     */     }
/*     */     
/* 551 */     return cachedArgument;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private class AutowiredFieldElement
/*     */     extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     private final boolean required;
/*     */     
/*     */ 
/* 563 */     private volatile boolean cached = false;
/*     */     private volatile Object cachedFieldValue;
/*     */     
/*     */     public AutowiredFieldElement(Field field, boolean required)
/*     */     {
/* 568 */       super(null);
/* 569 */       this.required = required;
/*     */     }
/*     */     
/*     */     protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable
/*     */     {
/* 574 */       Field field = (Field)this.member;
/*     */       Object value;
/* 576 */       Object value; if (this.cached) {
/* 577 */         value = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedFieldValue);
/*     */       }
/*     */       else {
/* 580 */         DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
/* 581 */         desc.setContainingClass(bean.getClass());
/* 582 */         Set<String> autowiredBeanNames = new LinkedHashSet(1);
/* 583 */         TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/*     */         try {
/* 585 */           value = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
/*     */         } catch (BeansException ex) {
/*     */           Object value;
/* 588 */           throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
/*     */         }
/* 590 */         synchronized (this) {
/* 591 */           if (!this.cached) {
/* 592 */             if ((value != null) || (this.required)) {
/* 593 */               this.cachedFieldValue = desc;
/* 594 */               AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeanNames);
/* 595 */               if (autowiredBeanNames.size() == 1) {
/* 596 */                 String autowiredBeanName = (String)autowiredBeanNames.iterator().next();
/* 597 */                 if ((AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName)) && 
/* 598 */                   (AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, field.getType())))
/*     */                 {
/* 600 */                   this.cachedFieldValue = new AutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(desc, autowiredBeanName, field.getType());
/*     */                 }
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 606 */               this.cachedFieldValue = null;
/*     */             }
/* 608 */             this.cached = true;
/*     */           }
/*     */         }
/*     */       }
/* 612 */       if (value != null) {
/* 613 */         ReflectionUtils.makeAccessible(field);
/* 614 */         field.set(bean, value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class AutowiredMethodElement
/*     */     extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     private final boolean required;
/*     */     
/*     */ 
/* 627 */     private volatile boolean cached = false;
/*     */     private volatile Object[] cachedMethodArguments;
/*     */     
/*     */     public AutowiredMethodElement(Method method, boolean required, PropertyDescriptor pd)
/*     */     {
/* 632 */       super(pd);
/* 633 */       this.required = required;
/*     */     }
/*     */     
/*     */     protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable
/*     */     {
/* 638 */       if (checkPropertySkipping(pvs)) {
/* 639 */         return;
/*     */       }
/* 641 */       Method method = (Method)this.member;
/*     */       Object[] arguments;
/* 643 */       Object[] arguments; if (this.cached)
/*     */       {
/* 645 */         arguments = resolveCachedArguments(beanName);
/*     */       }
/*     */       else {
/* 648 */         Class<?>[] paramTypes = method.getParameterTypes();
/* 649 */         arguments = new Object[paramTypes.length];
/* 650 */         DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
/* 651 */         Set<String> autowiredBeans = new LinkedHashSet(paramTypes.length);
/* 652 */         TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/* 653 */         for (int i = 0; i < arguments.length; i++) {
/* 654 */           MethodParameter methodParam = new MethodParameter(method, i);
/* 655 */           DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
/* 656 */           currDesc.setContainingClass(bean.getClass());
/* 657 */           descriptors[i] = currDesc;
/*     */           try {
/* 659 */             Object arg = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
/* 660 */             if ((arg == null) && (!this.required)) {
/* 661 */               arguments = null;
/* 662 */               break;
/*     */             }
/* 664 */             arguments[i] = arg;
/*     */           }
/*     */           catch (BeansException ex) {
/* 667 */             throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(methodParam), ex);
/*     */           }
/*     */         }
/* 670 */         synchronized (this) {
/* 671 */           if (!this.cached) {
/* 672 */             if (arguments != null) {
/* 673 */               this.cachedMethodArguments = new Object[paramTypes.length];
/* 674 */               for (int i = 0; i < arguments.length; i++) {
/* 675 */                 this.cachedMethodArguments[i] = descriptors[i];
/*     */               }
/* 677 */               AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeans);
/* 678 */               if (autowiredBeans.size() == paramTypes.length) {
/* 679 */                 Iterator<String> it = autowiredBeans.iterator();
/* 680 */                 for (int i = 0; i < paramTypes.length; i++) {
/* 681 */                   String autowiredBeanName = (String)it.next();
/* 682 */                   if ((AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName)) && 
/* 683 */                     (AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i]))) {
/* 684 */                     this.cachedMethodArguments[i] = new AutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(descriptors[i], autowiredBeanName, paramTypes[i]);
/*     */                   }
/*     */                   
/*     */                 }
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 692 */               this.cachedMethodArguments = null;
/*     */             }
/* 694 */             this.cached = true;
/*     */           }
/*     */         }
/*     */       }
/* 698 */       if (arguments != null) {
/*     */         try {
/* 700 */           ReflectionUtils.makeAccessible(method);
/* 701 */           method.invoke(bean, arguments);
/*     */         }
/*     */         catch (InvocationTargetException ex) {
/* 704 */           throw ex.getTargetException();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private Object[] resolveCachedArguments(String beanName) {
/* 710 */       if (this.cachedMethodArguments == null) {
/* 711 */         return null;
/*     */       }
/* 713 */       Object[] arguments = new Object[this.cachedMethodArguments.length];
/* 714 */       for (int i = 0; i < arguments.length; i++) {
/* 715 */         arguments[i] = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedMethodArguments[i]);
/*     */       }
/* 717 */       return arguments;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ShortcutDependencyDescriptor
/*     */     extends DependencyDescriptor
/*     */   {
/*     */     private final String shortcut;
/*     */     
/*     */     private final Class<?> requiredType;
/*     */     
/*     */ 
/*     */     public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcut, Class<?> requiredType)
/*     */     {
/* 733 */       super();
/* 734 */       this.shortcut = shortcut;
/* 735 */       this.requiredType = requiredType;
/*     */     }
/*     */     
/*     */     public Object resolveShortcut(BeanFactory beanFactory)
/*     */     {
/* 740 */       return resolveCandidate(this.shortcut, this.requiredType, beanFactory);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\annotation\AutowiredAnnotationBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */