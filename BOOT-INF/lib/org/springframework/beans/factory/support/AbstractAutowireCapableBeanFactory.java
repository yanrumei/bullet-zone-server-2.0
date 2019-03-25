/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.beans.PropertyDescriptor;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeanWrapper;
/*      */ import org.springframework.beans.BeanWrapperImpl;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.MutablePropertyValues;
/*      */ import org.springframework.beans.PropertyAccessorUtils;
/*      */ import org.springframework.beans.PropertyValue;
/*      */ import org.springframework.beans.PropertyValues;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.factory.Aware;
/*      */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.BeanNameAware;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.InitializingBean;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*      */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*      */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.TypedStringValue;
/*      */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*      */ import org.springframework.core.GenericTypeResolver;
/*      */ import org.springframework.core.MethodParameter;
/*      */ import org.springframework.core.ParameterNameDiscoverer;
/*      */ import org.springframework.core.PriorityOrdered;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractAutowireCapableBeanFactory
/*      */   extends AbstractBeanFactory
/*      */   implements AutowireCapableBeanFactory
/*      */ {
/*  122 */   private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
/*      */   
/*      */ 
/*  125 */   private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
/*      */   
/*      */ 
/*  128 */   private boolean allowCircularReferences = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  134 */   private boolean allowRawInjectionDespiteWrapping = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  140 */   private final Set<Class<?>> ignoredDependencyTypes = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  146 */   private final Set<Class<?>> ignoredDependencyInterfaces = new HashSet();
/*      */   
/*      */ 
/*  149 */   private final Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap(16);
/*      */   
/*      */ 
/*      */ 
/*  153 */   private final ConcurrentMap<Class<?>, PropertyDescriptor[]> filteredPropertyDescriptorsCache = new ConcurrentHashMap(256);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AbstractAutowireCapableBeanFactory()
/*      */   {
/*  162 */     ignoreDependencyInterface(BeanNameAware.class);
/*  163 */     ignoreDependencyInterface(BeanFactoryAware.class);
/*  164 */     ignoreDependencyInterface(BeanClassLoaderAware.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public AbstractAutowireCapableBeanFactory(BeanFactory parentBeanFactory)
/*      */   {
/*  172 */     this();
/*  173 */     setParentBeanFactory(parentBeanFactory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy)
/*      */   {
/*  183 */     this.instantiationStrategy = instantiationStrategy;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected InstantiationStrategy getInstantiationStrategy()
/*      */   {
/*  190 */     return this.instantiationStrategy;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*      */   {
/*  199 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ParameterNameDiscoverer getParameterNameDiscoverer()
/*      */   {
/*  207 */     return this.parameterNameDiscoverer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowCircularReferences(boolean allowCircularReferences)
/*      */   {
/*  224 */     this.allowCircularReferences = allowCircularReferences;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowRawInjectionDespiteWrapping(boolean allowRawInjectionDespiteWrapping)
/*      */   {
/*  242 */     this.allowRawInjectionDespiteWrapping = allowRawInjectionDespiteWrapping;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void ignoreDependencyType(Class<?> type)
/*      */   {
/*  250 */     this.ignoredDependencyTypes.add(type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void ignoreDependencyInterface(Class<?> ifc)
/*      */   {
/*  264 */     this.ignoredDependencyInterfaces.add(ifc);
/*      */   }
/*      */   
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*      */   {
/*  269 */     super.copyConfigurationFrom(otherFactory);
/*  270 */     if ((otherFactory instanceof AbstractAutowireCapableBeanFactory)) {
/*  271 */       AbstractAutowireCapableBeanFactory otherAutowireFactory = (AbstractAutowireCapableBeanFactory)otherFactory;
/*      */       
/*  273 */       this.instantiationStrategy = otherAutowireFactory.instantiationStrategy;
/*  274 */       this.allowCircularReferences = otherAutowireFactory.allowCircularReferences;
/*  275 */       this.ignoredDependencyTypes.addAll(otherAutowireFactory.ignoredDependencyTypes);
/*  276 */       this.ignoredDependencyInterfaces.addAll(otherAutowireFactory.ignoredDependencyInterfaces);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T createBean(Class<T> beanClass)
/*      */     throws BeansException
/*      */   {
/*  289 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass);
/*  290 */     bd.setScope("prototype");
/*  291 */     bd.allowCaching = ClassUtils.isCacheSafe(beanClass, getBeanClassLoader());
/*  292 */     return (T)createBean(beanClass.getName(), bd, null);
/*      */   }
/*      */   
/*      */ 
/*      */   public void autowireBean(Object existingBean)
/*      */   {
/*  298 */     RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean));
/*  299 */     bd.setScope("prototype");
/*  300 */     bd.allowCaching = ClassUtils.isCacheSafe(bd.getBeanClass(), getBeanClassLoader());
/*  301 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  302 */     initBeanWrapper(bw);
/*  303 */     populateBean(bd.getBeanClass().getName(), bd, bw);
/*      */   }
/*      */   
/*      */   public Object configureBean(Object existingBean, String beanName) throws BeansException
/*      */   {
/*  308 */     markBeanAsCreated(beanName);
/*  309 */     BeanDefinition mbd = getMergedBeanDefinition(beanName);
/*  310 */     RootBeanDefinition bd = null;
/*  311 */     if ((mbd instanceof RootBeanDefinition)) {
/*  312 */       RootBeanDefinition rbd = (RootBeanDefinition)mbd;
/*  313 */       bd = rbd.isPrototype() ? rbd : rbd.cloneBeanDefinition();
/*      */     }
/*  315 */     if (!mbd.isPrototype()) {
/*  316 */       if (bd == null) {
/*  317 */         bd = new RootBeanDefinition(mbd);
/*      */       }
/*  319 */       bd.setScope("prototype");
/*  320 */       bd.allowCaching = ClassUtils.isCacheSafe(ClassUtils.getUserClass(existingBean), getBeanClassLoader());
/*      */     }
/*  322 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  323 */     initBeanWrapper(bw);
/*  324 */     populateBean(beanName, bd, bw);
/*  325 */     return initializeBean(beanName, existingBean, bd);
/*      */   }
/*      */   
/*      */   public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName) throws BeansException
/*      */   {
/*  330 */     return resolveDependency(descriptor, requestingBeanName, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck)
/*      */     throws BeansException
/*      */   {
/*  341 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  342 */     bd.setScope("prototype");
/*  343 */     return createBean(beanClass.getName(), bd, null);
/*      */   }
/*      */   
/*      */   public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck)
/*      */     throws BeansException
/*      */   {
/*  349 */     final RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  350 */     bd.setScope("prototype");
/*  351 */     if (bd.getResolvedAutowireMode() == 3) {
/*  352 */       return autowireConstructor(beanClass.getName(), bd, null, null).getWrappedInstance();
/*      */     }
/*      */     
/*      */ 
/*  356 */     final BeanFactory parent = this;
/*  357 */     Object bean; Object bean; if (System.getSecurityManager() != null) {
/*  358 */       bean = AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/*  361 */           return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(bd, null, parent);
/*      */         }
/*  363 */       }, getAccessControlContext());
/*      */     }
/*      */     else {
/*  366 */       bean = getInstantiationStrategy().instantiate(bd, null, parent);
/*      */     }
/*  368 */     populateBean(beanClass.getName(), bd, new BeanWrapperImpl(bean));
/*  369 */     return bean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
/*      */     throws BeansException
/*      */   {
/*  377 */     if (autowireMode == 3) {
/*  378 */       throw new IllegalArgumentException("AUTOWIRE_CONSTRUCTOR not supported for existing bean instance");
/*      */     }
/*      */     
/*      */ 
/*  382 */     RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean), autowireMode, dependencyCheck);
/*  383 */     bd.setScope("prototype");
/*  384 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  385 */     initBeanWrapper(bw);
/*  386 */     populateBean(bd.getBeanClass().getName(), bd, bw);
/*      */   }
/*      */   
/*      */   public void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException
/*      */   {
/*  391 */     markBeanAsCreated(beanName);
/*  392 */     BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  393 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  394 */     initBeanWrapper(bw);
/*  395 */     applyPropertyValues(beanName, bd, bw, bd.getPropertyValues());
/*      */   }
/*      */   
/*      */   public Object initializeBean(Object existingBean, String beanName)
/*      */   {
/*  400 */     return initializeBean(beanName, existingBean, null);
/*      */   }
/*      */   
/*      */ 
/*      */   public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
/*      */     throws BeansException
/*      */   {
/*  407 */     Object result = existingBean;
/*  408 */     for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
/*  409 */       result = beanProcessor.postProcessBeforeInitialization(result, beanName);
/*  410 */       if (result == null) {
/*  411 */         return result;
/*      */       }
/*      */     }
/*  414 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
/*      */     throws BeansException
/*      */   {
/*  421 */     Object result = existingBean;
/*  422 */     for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
/*  423 */       result = beanProcessor.postProcessAfterInitialization(result, beanName);
/*  424 */       if (result == null) {
/*  425 */         return result;
/*      */       }
/*      */     }
/*  428 */     return result;
/*      */   }
/*      */   
/*      */   public void destroyBean(Object existingBean)
/*      */   {
/*  433 */     new DisposableBeanAdapter(existingBean, getBeanPostProcessors(), getAccessControlContext()).destroy();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args)
/*      */     throws BeanCreationException
/*      */   {
/*  448 */     if (this.logger.isDebugEnabled()) {
/*  449 */       this.logger.debug("Creating instance of bean '" + beanName + "'");
/*      */     }
/*  451 */     RootBeanDefinition mbdToUse = mbd;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  456 */     Class<?> resolvedClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*  457 */     if ((resolvedClass != null) && (!mbd.hasBeanClass()) && (mbd.getBeanClassName() != null)) {
/*  458 */       mbdToUse = new RootBeanDefinition(mbd);
/*  459 */       mbdToUse.setBeanClass(resolvedClass);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  464 */       mbdToUse.prepareMethodOverrides();
/*      */     }
/*      */     catch (BeanDefinitionValidationException ex) {
/*  467 */       throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(), beanName, "Validation of method overrides failed", ex);
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  473 */       Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
/*  474 */       if (bean != null) {
/*  475 */         return bean;
/*      */       }
/*      */     }
/*      */     catch (Throwable ex) {
/*  479 */       throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName, "BeanPostProcessor before instantiation of bean failed", ex);
/*      */     }
/*      */     
/*      */ 
/*  483 */     Object beanInstance = doCreateBean(beanName, mbdToUse, args);
/*  484 */     if (this.logger.isDebugEnabled()) {
/*  485 */       this.logger.debug("Finished creating instance of bean '" + beanName + "'");
/*      */     }
/*  487 */     return beanInstance;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, Object[] args)
/*      */     throws BeanCreationException
/*      */   {
/*  508 */     BeanWrapper instanceWrapper = null;
/*  509 */     if (mbd.isSingleton()) {
/*  510 */       instanceWrapper = (BeanWrapper)this.factoryBeanInstanceCache.remove(beanName);
/*      */     }
/*  512 */     if (instanceWrapper == null) {
/*  513 */       instanceWrapper = createBeanInstance(beanName, mbd, args);
/*      */     }
/*  515 */     final Object bean = instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null;
/*  516 */     Class<?> beanType = instanceWrapper != null ? instanceWrapper.getWrappedClass() : null;
/*  517 */     mbd.resolvedTargetType = beanType;
/*      */     
/*      */ 
/*  520 */     synchronized (mbd.postProcessingLock) {
/*  521 */       if (!mbd.postProcessed) {
/*      */         try {
/*  523 */           applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
/*      */         }
/*      */         catch (Throwable ex) {
/*  526 */           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Post-processing of merged bean definition failed", ex);
/*      */         }
/*      */         
/*  529 */         mbd.postProcessed = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  536 */     boolean earlySingletonExposure = (mbd.isSingleton()) && (this.allowCircularReferences) && (isSingletonCurrentlyInCreation(beanName));
/*  537 */     if (earlySingletonExposure) {
/*  538 */       if (this.logger.isDebugEnabled()) {
/*  539 */         this.logger.debug("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
/*      */       }
/*      */       
/*  542 */       addSingletonFactory(beanName, new ObjectFactory()
/*      */       {
/*      */         public Object getObject() throws BeansException {
/*  545 */           return AbstractAutowireCapableBeanFactory.this.getEarlyBeanReference(beanName, mbd, bean);
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*  551 */     Object exposedObject = bean;
/*      */     try {
/*  553 */       populateBean(beanName, mbd, instanceWrapper);
/*  554 */       if (exposedObject != null) {
/*  555 */         exposedObject = initializeBean(beanName, exposedObject, mbd);
/*      */       }
/*      */     }
/*      */     catch (Throwable ex) {
/*  559 */       if (((ex instanceof BeanCreationException)) && (beanName.equals(((BeanCreationException)ex).getBeanName()))) {
/*  560 */         throw ((BeanCreationException)ex);
/*      */       }
/*      */       
/*      */ 
/*  564 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
/*      */     }
/*      */     
/*      */ 
/*  568 */     if (earlySingletonExposure) {
/*  569 */       Object earlySingletonReference = getSingleton(beanName, false);
/*  570 */       if (earlySingletonReference != null) {
/*  571 */         if (exposedObject == bean) {
/*  572 */           exposedObject = earlySingletonReference;
/*      */         }
/*  574 */         else if ((!this.allowRawInjectionDespiteWrapping) && (hasDependentBean(beanName))) {
/*  575 */           String[] dependentBeans = getDependentBeans(beanName);
/*  576 */           Set<String> actualDependentBeans = new LinkedHashSet(dependentBeans.length);
/*  577 */           for (String dependentBean : dependentBeans) {
/*  578 */             if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
/*  579 */               actualDependentBeans.add(dependentBean);
/*      */             }
/*      */           }
/*  582 */           if (!actualDependentBeans.isEmpty())
/*      */           {
/*      */ 
/*  585 */             throw new BeanCurrentlyInCreationException(beanName, "Bean with name '" + beanName + "' has been injected into other beans [" + StringUtils.collectionToCommaDelimitedString(actualDependentBeans) + "] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  597 */       registerDisposableBeanIfNecessary(beanName, bean, mbd);
/*      */     }
/*      */     catch (BeanDefinitionValidationException ex)
/*      */     {
/*  601 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);
/*      */     }
/*      */     
/*  604 */     return exposedObject;
/*      */   }
/*      */   
/*      */   protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch)
/*      */   {
/*  609 */     Class<?> targetType = determineTargetType(beanName, mbd, typesToMatch);
/*      */     
/*      */ 
/*      */ 
/*  613 */     if ((targetType != null) && (!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/*  614 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  615 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor)) {
/*  616 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  617 */           Class<?> predicted = ibp.predictBeanType(targetType, beanName);
/*  618 */           if ((predicted != null) && ((typesToMatch.length != 1) || (FactoryBean.class != typesToMatch[0]) || 
/*  619 */             (FactoryBean.class.isAssignableFrom(predicted)))) {
/*  620 */             return predicted;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  625 */     return targetType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Class<?> determineTargetType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch)
/*      */   {
/*  637 */     Class<?> targetType = mbd.getTargetType();
/*  638 */     if (targetType == null)
/*      */     {
/*      */ 
/*  641 */       targetType = mbd.getFactoryMethodName() != null ? getTypeForFactoryMethod(beanName, mbd, typesToMatch) : resolveBeanClass(mbd, beanName, typesToMatch);
/*  642 */       if ((ObjectUtils.isEmpty(typesToMatch)) || (getTempClassLoader() == null)) {
/*  643 */         mbd.resolvedTargetType = targetType;
/*      */       }
/*      */     }
/*  646 */     return targetType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Class<?> getTypeForFactoryMethod(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch)
/*      */   {
/*  664 */     ResolvableType cachedReturnType = mbd.factoryMethodReturnType;
/*  665 */     if (cachedReturnType != null) {
/*  666 */       return cachedReturnType.resolve();
/*      */     }
/*      */     
/*      */ 
/*  670 */     boolean isStatic = true;
/*      */     
/*  672 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  673 */     if (factoryBeanName != null) {
/*  674 */       if (factoryBeanName.equals(beanName)) {
/*  675 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "factory-bean reference points back to the same bean definition");
/*      */       }
/*      */       
/*      */ 
/*  679 */       Class<?> factoryClass = getType(factoryBeanName);
/*  680 */       isStatic = false;
/*      */     }
/*      */     else
/*      */     {
/*  684 */       factoryClass = resolveBeanClass(mbd, beanName, typesToMatch);
/*      */     }
/*      */     
/*  687 */     if (factoryClass == null) {
/*  688 */       return null;
/*      */     }
/*  690 */     Class<?> factoryClass = ClassUtils.getUserClass(factoryClass);
/*      */     
/*      */ 
/*      */ 
/*  694 */     Class<?> commonType = null;
/*  695 */     Method uniqueCandidate = null;
/*  696 */     int minNrOfArgs = mbd.getConstructorArgumentValues().getArgumentCount();
/*  697 */     Method[] candidates = ReflectionUtils.getUniqueDeclaredMethods(factoryClass);
/*  698 */     for (Method factoryMethod : candidates) {
/*  699 */       if ((Modifier.isStatic(factoryMethod.getModifiers()) == isStatic) && 
/*  700 */         (factoryMethod.getName().equals(mbd.getFactoryMethodName())) && 
/*  701 */         (factoryMethod.getParameterTypes().length >= minNrOfArgs))
/*      */       {
/*  703 */         if (factoryMethod.getTypeParameters().length > 0) {
/*      */           try
/*      */           {
/*  706 */             Class<?>[] paramTypes = factoryMethod.getParameterTypes();
/*  707 */             String[] paramNames = null;
/*  708 */             ParameterNameDiscoverer pnd = getParameterNameDiscoverer();
/*  709 */             if (pnd != null) {
/*  710 */               paramNames = pnd.getParameterNames(factoryMethod);
/*      */             }
/*  712 */             ConstructorArgumentValues cav = mbd.getConstructorArgumentValues();
/*  713 */             Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet(paramTypes.length);
/*      */             
/*  715 */             Object[] args = new Object[paramTypes.length];
/*  716 */             for (int i = 0; i < args.length; i++) {
/*  717 */               ConstructorArgumentValues.ValueHolder valueHolder = cav.getArgumentValue(i, paramTypes[i], paramNames != null ? paramNames[i] : null, usedValueHolders);
/*      */               
/*  719 */               if (valueHolder == null) {
/*  720 */                 valueHolder = cav.getGenericArgumentValue(null, null, usedValueHolders);
/*      */               }
/*  722 */               if (valueHolder != null) {
/*  723 */                 args[i] = valueHolder.getValue();
/*  724 */                 usedValueHolders.add(valueHolder);
/*      */               }
/*      */             }
/*  727 */             Class<?> returnType = AutowireUtils.resolveReturnTypeForFactoryMethod(factoryMethod, args, 
/*  728 */               getBeanClassLoader());
/*  729 */             if (returnType != null) {
/*  730 */               uniqueCandidate = commonType == null ? factoryMethod : null;
/*  731 */               commonType = ClassUtils.determineCommonAncestor(returnType, commonType);
/*  732 */               if (commonType == null)
/*      */               {
/*  734 */                 return null;
/*      */               }
/*      */             }
/*      */           }
/*      */           catch (Throwable ex) {
/*  739 */             if (this.logger.isDebugEnabled()) {
/*  740 */               this.logger.debug("Failed to resolve generic return type for factory method: " + ex);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  745 */         uniqueCandidate = commonType == null ? factoryMethod : null;
/*  746 */         commonType = ClassUtils.determineCommonAncestor(factoryMethod.getReturnType(), commonType);
/*  747 */         if (commonType == null)
/*      */         {
/*  749 */           return null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  755 */     if (commonType != null)
/*      */     {
/*      */ 
/*  758 */       mbd.factoryMethodReturnType = (uniqueCandidate != null ? ResolvableType.forMethodReturnType(uniqueCandidate) : ResolvableType.forClass(commonType));
/*      */     }
/*  760 */     return commonType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd)
/*      */   {
/*  776 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  777 */     String factoryMethodName = mbd.getFactoryMethodName();
/*      */     
/*  779 */     if (factoryBeanName != null) {
/*  780 */       if (factoryMethodName != null)
/*      */       {
/*      */ 
/*  783 */         BeanDefinition fbDef = getBeanDefinition(factoryBeanName);
/*  784 */         if ((fbDef instanceof AbstractBeanDefinition)) {
/*  785 */           AbstractBeanDefinition afbDef = (AbstractBeanDefinition)fbDef;
/*  786 */           if (afbDef.hasBeanClass()) {
/*  787 */             Class<?> result = getTypeForFactoryBeanFromMethod(afbDef.getBeanClass(), factoryMethodName);
/*  788 */             if (result != null) {
/*  789 */               return result;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  797 */       if (!isBeanEligibleForMetadataCaching(factoryBeanName)) {
/*  798 */         return null;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  805 */     FactoryBean<?> fb = mbd.isSingleton() ? getSingletonFactoryBeanForTypeCheck(beanName, mbd) : getNonSingletonFactoryBeanForTypeCheck(beanName, mbd);
/*      */     
/*  807 */     if (fb != null)
/*      */     {
/*  809 */       Class<?> result = getTypeForFactoryBean(fb);
/*  810 */       if (result != null) {
/*  811 */         return result;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  816 */       return super.getTypeForFactoryBean(beanName, mbd);
/*      */     }
/*      */     
/*      */ 
/*  820 */     if ((factoryBeanName == null) && (mbd.hasBeanClass()))
/*      */     {
/*      */ 
/*  823 */       if (factoryMethodName != null) {
/*  824 */         return getTypeForFactoryBeanFromMethod(mbd.getBeanClass(), factoryMethodName);
/*      */       }
/*      */       
/*  827 */       return GenericTypeResolver.resolveTypeArgument(mbd.getBeanClass(), FactoryBean.class);
/*      */     }
/*      */     
/*      */ 
/*  831 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Class<?> getTypeForFactoryBeanFromMethod(Class<?> beanClass, final String factoryMethodName)
/*      */   {
/*  843 */     final Object objectType = new Object()
/*      */     {
/*  842 */       Class<?> value = null;
/*      */ 
/*      */ 
/*  845 */     };
/*  846 */     Class<?> fbClass = ClassUtils.getUserClass(beanClass);
/*      */     
/*      */ 
/*      */ 
/*  850 */     ReflectionUtils.doWithMethods(fbClass, new ReflectionUtils.MethodCallback()
/*      */     {
/*      */       public void doWith(Method method)
/*      */       {
/*  854 */         if ((method.getName().equals(factoryMethodName)) && 
/*  855 */           (FactoryBean.class.isAssignableFrom(method.getReturnType()))) {
/*  856 */           Class<?> currentType = GenericTypeResolver.resolveReturnTypeArgument(method, FactoryBean.class);
/*      */           
/*  858 */           if (currentType != null) {
/*  859 */             objectType.value = ClassUtils.determineCommonAncestor(currentType, objectType.value);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*  864 */     });
/*  865 */     return (objectType.value != null) && (Object.class != objectType.value) ? objectType.value : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean)
/*      */   {
/*  877 */     Object exposedObject = bean;
/*  878 */     if ((bean != null) && (!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/*  879 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  880 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor)) {
/*  881 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  882 */           exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
/*  883 */           if (exposedObject == null) {
/*  884 */             return null;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  889 */     return exposedObject;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private FactoryBean<?> getSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd)
/*      */   {
/*  906 */     synchronized (getSingletonMutex()) {
/*  907 */       BeanWrapper bw = (BeanWrapper)this.factoryBeanInstanceCache.get(beanName);
/*  908 */       if (bw != null) {
/*  909 */         return (FactoryBean)bw.getWrappedInstance();
/*      */       }
/*  911 */       if ((isSingletonCurrentlyInCreation(beanName)) || (
/*  912 */         (mbd.getFactoryBeanName() != null) && (isSingletonCurrentlyInCreation(mbd.getFactoryBeanName())))) {
/*  913 */         return null;
/*      */       }
/*      */       
/*  916 */       Object instance = null;
/*      */       try
/*      */       {
/*  919 */         beforeSingletonCreation(beanName);
/*      */         
/*  921 */         instance = resolveBeforeInstantiation(beanName, mbd);
/*  922 */         if (instance == null) {
/*  923 */           bw = createBeanInstance(beanName, mbd, null);
/*  924 */           instance = bw.getWrappedInstance();
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/*  929 */         afterSingletonCreation(beanName);
/*      */       }
/*      */       
/*  932 */       Object fb = getFactoryBean(beanName, instance);
/*  933 */       if (bw != null) {
/*  934 */         this.factoryBeanInstanceCache.put(beanName, bw);
/*      */       }
/*  936 */       return (FactoryBean<?>)fb;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private FactoryBean<?> getNonSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd)
/*      */   {
/*  949 */     if (isPrototypeCurrentlyInCreation(beanName)) {
/*  950 */       return null;
/*      */     }
/*      */     
/*  953 */     Object instance = null;
/*      */     try
/*      */     {
/*  956 */       beforePrototypeCreation(beanName);
/*      */       
/*  958 */       instance = resolveBeforeInstantiation(beanName, mbd);
/*  959 */       if (instance == null) {
/*  960 */         BeanWrapper bw = createBeanInstance(beanName, mbd, null);
/*  961 */         instance = bw.getWrappedInstance();
/*      */       }
/*      */     }
/*      */     catch (BeanCreationException ex)
/*      */     {
/*  966 */       if (this.logger.isDebugEnabled()) {
/*  967 */         this.logger.debug("Bean creation exception on non-singleton FactoryBean type check: " + ex);
/*      */       }
/*  969 */       onSuppressedException(ex);
/*  970 */       return null;
/*      */     }
/*      */     finally
/*      */     {
/*  974 */       afterPrototypeCreation(beanName);
/*      */     }
/*      */     
/*  977 */     return getFactoryBean(beanName, instance);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void applyMergedBeanDefinitionPostProcessors(RootBeanDefinition mbd, Class<?> beanType, String beanName)
/*      */   {
/*  989 */     for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  990 */       if ((bp instanceof MergedBeanDefinitionPostProcessor)) {
/*  991 */         MergedBeanDefinitionPostProcessor bdp = (MergedBeanDefinitionPostProcessor)bp;
/*  992 */         bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd)
/*      */   {
/* 1005 */     Object bean = null;
/* 1006 */     if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved))
/*      */     {
/* 1008 */       if ((!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/* 1009 */         Class<?> targetType = determineTargetType(beanName, mbd, new Class[0]);
/* 1010 */         if (targetType != null) {
/* 1011 */           bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
/* 1012 */           if (bean != null) {
/* 1013 */             bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
/*      */           }
/*      */         }
/*      */       }
/* 1017 */       mbd.beforeInstantiationResolved = Boolean.valueOf(bean != null);
/*      */     }
/* 1019 */     return bean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName)
/*      */   {
/* 1034 */     for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1035 */       if ((bp instanceof InstantiationAwareBeanPostProcessor)) {
/* 1036 */         InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1037 */         Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
/* 1038 */         if (result != null) {
/* 1039 */           return result;
/*      */         }
/*      */       }
/*      */     }
/* 1043 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args)
/*      */   {
/* 1059 */     Class<?> beanClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*      */     
/* 1061 */     if ((beanClass != null) && (!Modifier.isPublic(beanClass.getModifiers())) && (!mbd.isNonPublicAccessAllowed()))
/*      */     {
/* 1063 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
/*      */     }
/*      */     
/* 1066 */     if (mbd.getFactoryMethodName() != null) {
/* 1067 */       return instantiateUsingFactoryMethod(beanName, mbd, args);
/*      */     }
/*      */     
/*      */ 
/* 1071 */     boolean resolved = false;
/* 1072 */     boolean autowireNecessary = false;
/* 1073 */     if (args == null) {
/* 1074 */       synchronized (mbd.constructorArgumentLock) {
/* 1075 */         if (mbd.resolvedConstructorOrFactoryMethod != null) {
/* 1076 */           resolved = true;
/* 1077 */           autowireNecessary = mbd.constructorArgumentsResolved;
/*      */         }
/*      */       }
/*      */     }
/* 1081 */     if (resolved) {
/* 1082 */       if (autowireNecessary) {
/* 1083 */         return autowireConstructor(beanName, mbd, null, null);
/*      */       }
/*      */       
/* 1086 */       return instantiateBean(beanName, mbd);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1091 */     Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
/* 1092 */     if ((ctors != null) || 
/* 1093 */       (mbd.getResolvedAutowireMode() == 3) || 
/* 1094 */       (mbd.hasConstructorArgumentValues()) || (!ObjectUtils.isEmpty(args))) {
/* 1095 */       return autowireConstructor(beanName, mbd, ctors, args);
/*      */     }
/*      */     
/*      */ 
/* 1099 */     return instantiateBean(beanName, mbd);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Constructor<?>[] determineConstructorsFromBeanPostProcessors(Class<?> beanClass, String beanName)
/*      */     throws BeansException
/*      */   {
/* 1114 */     if ((beanClass != null) && (hasInstantiationAwareBeanPostProcessors())) {
/* 1115 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1116 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor)) {
/* 1117 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/* 1118 */           Constructor<?>[] ctors = ibp.determineCandidateConstructors(beanClass, beanName);
/* 1119 */           if (ctors != null) {
/* 1120 */             return ctors;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1125 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd)
/*      */   {
/*      */     try
/*      */     {
/* 1137 */       final BeanFactory parent = this;
/* 1138 */       Object beanInstance; Object beanInstance; if (System.getSecurityManager() != null) {
/* 1139 */         beanInstance = AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/* 1142 */             return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(mbd, beanName, parent);
/*      */           }
/* 1144 */         }, getAccessControlContext());
/*      */       }
/*      */       else {
/* 1147 */         beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
/*      */       }
/* 1149 */       BeanWrapper bw = new BeanWrapperImpl(beanInstance);
/* 1150 */       initBeanWrapper(bw);
/* 1151 */       return bw;
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/* 1155 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanWrapper instantiateUsingFactoryMethod(String beanName, RootBeanDefinition mbd, Object[] explicitArgs)
/*      */   {
/* 1173 */     return new ConstructorResolver(this).instantiateUsingFactoryMethod(beanName, mbd, explicitArgs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd, Constructor<?>[] ctors, Object[] explicitArgs)
/*      */   {
/* 1193 */     return new ConstructorResolver(this).autowireConstructor(beanName, mbd, ctors, explicitArgs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void populateBean(String beanName, RootBeanDefinition mbd, BeanWrapper bw)
/*      */   {
/* 1204 */     PropertyValues pvs = mbd.getPropertyValues();
/*      */     
/* 1206 */     if (bw == null) {
/* 1207 */       if (!pvs.isEmpty())
/*      */       {
/* 1209 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Cannot apply property values to null instance");
/*      */       }
/*      */       
/*      */ 
/* 1213 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1220 */     boolean continueWithPropertyPopulation = true;
/*      */     
/* 1222 */     if ((!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/* 1223 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1224 */         if ((bp instanceof InstantiationAwareBeanPostProcessor)) {
/* 1225 */           InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1226 */           if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
/* 1227 */             continueWithPropertyPopulation = false;
/* 1228 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1234 */     if (!continueWithPropertyPopulation) {
/* 1235 */       return;
/*      */     }
/*      */     
/* 1238 */     if ((mbd.getResolvedAutowireMode() == 1) || 
/* 1239 */       (mbd.getResolvedAutowireMode() == 2)) {
/* 1240 */       MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
/*      */       
/*      */ 
/* 1243 */       if (mbd.getResolvedAutowireMode() == 1) {
/* 1244 */         autowireByName(beanName, mbd, bw, newPvs);
/*      */       }
/*      */       
/*      */ 
/* 1248 */       if (mbd.getResolvedAutowireMode() == 2) {
/* 1249 */         autowireByType(beanName, mbd, bw, newPvs);
/*      */       }
/*      */       
/* 1252 */       pvs = newPvs;
/*      */     }
/*      */     
/* 1255 */     boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
/* 1256 */     boolean needsDepCheck = mbd.getDependencyCheck() != 0;
/*      */     
/* 1258 */     if ((hasInstAwareBpps) || (needsDepCheck)) {
/* 1259 */       PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
/* 1260 */       if (hasInstAwareBpps) {
/* 1261 */         for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1262 */           if ((bp instanceof InstantiationAwareBeanPostProcessor)) {
/* 1263 */             InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1264 */             pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
/* 1265 */             if (pvs == null) {
/* 1266 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1271 */       if (needsDepCheck) {
/* 1272 */         checkDependencies(beanName, mbd, filteredPds, pvs);
/*      */       }
/*      */     }
/*      */     
/* 1276 */     applyPropertyValues(beanName, mbd, bw, pvs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void autowireByName(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs)
/*      */   {
/* 1291 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/* 1292 */     for (String propertyName : propertyNames) {
/* 1293 */       if (containsBean(propertyName)) {
/* 1294 */         Object bean = getBean(propertyName);
/* 1295 */         pvs.add(propertyName, bean);
/* 1296 */         registerDependentBean(propertyName, beanName);
/* 1297 */         if (this.logger.isDebugEnabled()) {
/* 1298 */           this.logger.debug("Added autowiring by name from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + propertyName + "'");
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 1303 */       else if (this.logger.isTraceEnabled()) {
/* 1304 */         this.logger.trace("Not autowiring property '" + propertyName + "' of bean '" + beanName + "' by name: no matching bean found");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void autowireByType(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs)
/*      */   {
/* 1325 */     TypeConverter converter = getCustomTypeConverter();
/* 1326 */     if (converter == null) {
/* 1327 */       converter = bw;
/*      */     }
/*      */     
/* 1330 */     Set<String> autowiredBeanNames = new LinkedHashSet(4);
/* 1331 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/* 1332 */     for (String propertyName : propertyNames) {
/*      */       try {
/* 1334 */         PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/*      */         
/*      */ 
/* 1337 */         if (Object.class != pd.getPropertyType()) {
/* 1338 */           MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/*      */           
/* 1340 */           boolean eager = !PriorityOrdered.class.isAssignableFrom(bw.getWrappedClass());
/* 1341 */           DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
/* 1342 */           Object autowiredArgument = resolveDependency(desc, beanName, autowiredBeanNames, converter);
/* 1343 */           if (autowiredArgument != null) {
/* 1344 */             pvs.add(propertyName, autowiredArgument);
/*      */           }
/* 1346 */           for (String autowiredBeanName : autowiredBeanNames) {
/* 1347 */             registerDependentBean(autowiredBeanName, beanName);
/* 1348 */             if (this.logger.isDebugEnabled()) {
/* 1349 */               this.logger.debug("Autowiring by type from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + autowiredBeanName + "'");
/*      */             }
/*      */           }
/*      */           
/* 1353 */           autowiredBeanNames.clear();
/*      */         }
/*      */       }
/*      */       catch (BeansException ex) {
/* 1357 */         throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, propertyName, ex);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String[] unsatisfiedNonSimpleProperties(AbstractBeanDefinition mbd, BeanWrapper bw)
/*      */   {
/* 1373 */     Set<String> result = new TreeSet();
/* 1374 */     PropertyValues pvs = mbd.getPropertyValues();
/* 1375 */     PropertyDescriptor[] pds = bw.getPropertyDescriptors();
/* 1376 */     for (PropertyDescriptor pd : pds) {
/* 1377 */       if ((pd.getWriteMethod() != null) && (!isExcludedFromDependencyCheck(pd)) && (!pvs.contains(pd.getName())) && 
/* 1378 */         (!BeanUtils.isSimpleProperty(pd.getPropertyType()))) {
/* 1379 */         result.add(pd.getName());
/*      */       }
/*      */     }
/* 1382 */     return StringUtils.toStringArray(result);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw, boolean cache)
/*      */   {
/* 1395 */     PropertyDescriptor[] filtered = (PropertyDescriptor[])this.filteredPropertyDescriptorsCache.get(bw.getWrappedClass());
/* 1396 */     if (filtered == null) {
/* 1397 */       filtered = filterPropertyDescriptorsForDependencyCheck(bw);
/* 1398 */       if (cache)
/*      */       {
/* 1400 */         PropertyDescriptor[] existing = (PropertyDescriptor[])this.filteredPropertyDescriptorsCache.putIfAbsent(bw.getWrappedClass(), filtered);
/* 1401 */         if (existing != null) {
/* 1402 */           filtered = existing;
/*      */         }
/*      */       }
/*      */     }
/* 1406 */     return filtered;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw)
/*      */   {
/* 1418 */     List<PropertyDescriptor> pds = new LinkedList(Arrays.asList(bw.getPropertyDescriptors()));
/* 1419 */     for (Iterator<PropertyDescriptor> it = pds.iterator(); it.hasNext();) {
/* 1420 */       PropertyDescriptor pd = (PropertyDescriptor)it.next();
/* 1421 */       if (isExcludedFromDependencyCheck(pd)) {
/* 1422 */         it.remove();
/*      */       }
/*      */     }
/* 1425 */     return (PropertyDescriptor[])pds.toArray(new PropertyDescriptor[pds.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isExcludedFromDependencyCheck(PropertyDescriptor pd)
/*      */   {
/* 1439 */     return (AutowireUtils.isExcludedFromDependencyCheck(pd)) || 
/* 1440 */       (this.ignoredDependencyTypes.contains(pd.getPropertyType())) || 
/* 1441 */       (AutowireUtils.isSetterDefinedInInterface(pd, this.ignoredDependencyInterfaces));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkDependencies(String beanName, AbstractBeanDefinition mbd, PropertyDescriptor[] pds, PropertyValues pvs)
/*      */     throws UnsatisfiedDependencyException
/*      */   {
/* 1458 */     int dependencyCheck = mbd.getDependencyCheck();
/* 1459 */     for (PropertyDescriptor pd : pds) {
/* 1460 */       if ((pd.getWriteMethod() != null) && (!pvs.contains(pd.getName()))) {
/* 1461 */         boolean isSimple = BeanUtils.isSimpleProperty(pd.getPropertyType());
/* 1462 */         boolean unsatisfied = (dependencyCheck == 3) || ((isSimple) && (dependencyCheck == 2)) || ((!isSimple) && (dependencyCheck == 1));
/*      */         
/*      */ 
/* 1465 */         if (unsatisfied) {
/* 1466 */           throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, pd.getName(), "Set this property value or disable dependency checking for this bean.");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs)
/*      */   {
/* 1483 */     if ((pvs == null) || (pvs.isEmpty())) {
/* 1484 */       return;
/*      */     }
/*      */     
/* 1487 */     MutablePropertyValues mpvs = null;
/*      */     
/*      */ 
/* 1490 */     if ((System.getSecurityManager() != null) && 
/* 1491 */       ((bw instanceof BeanWrapperImpl))) {
/* 1492 */       ((BeanWrapperImpl)bw).setSecurityContext(getAccessControlContext());
/*      */     }
/*      */     List<PropertyValue> original;
/*      */     List<PropertyValue> original;
/* 1496 */     if ((pvs instanceof MutablePropertyValues)) {
/* 1497 */       mpvs = (MutablePropertyValues)pvs;
/* 1498 */       if (mpvs.isConverted()) {
/*      */         try
/*      */         {
/* 1501 */           bw.setPropertyValues(mpvs);
/* 1502 */           return;
/*      */         }
/*      */         catch (BeansException ex)
/*      */         {
/* 1506 */           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Error setting property values", ex);
/*      */         }
/*      */       }
/* 1509 */       original = mpvs.getPropertyValueList();
/*      */     }
/*      */     else {
/* 1512 */       original = Arrays.asList(pvs.getPropertyValues());
/*      */     }
/*      */     
/* 1515 */     TypeConverter converter = getCustomTypeConverter();
/* 1516 */     if (converter == null) {
/* 1517 */       converter = bw;
/*      */     }
/* 1519 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, converter);
/*      */     
/*      */ 
/* 1522 */     List<PropertyValue> deepCopy = new ArrayList(original.size());
/* 1523 */     boolean resolveNecessary = false;
/* 1524 */     for (PropertyValue pv : original) {
/* 1525 */       if (pv.isConverted()) {
/* 1526 */         deepCopy.add(pv);
/*      */       }
/*      */       else {
/* 1529 */         String propertyName = pv.getName();
/* 1530 */         Object originalValue = pv.getValue();
/* 1531 */         Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
/* 1532 */         Object convertedValue = resolvedValue;
/*      */         
/* 1534 */         boolean convertible = (bw.isWritableProperty(propertyName)) && (!PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName));
/* 1535 */         if (convertible) {
/* 1536 */           convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);
/*      */         }
/*      */         
/*      */ 
/* 1540 */         if (resolvedValue == originalValue) {
/* 1541 */           if (convertible) {
/* 1542 */             pv.setConvertedValue(convertedValue);
/*      */           }
/* 1544 */           deepCopy.add(pv);
/*      */         }
/* 1546 */         else if ((convertible) && ((originalValue instanceof TypedStringValue)) && 
/* 1547 */           (!((TypedStringValue)originalValue).isDynamic()) && (!(convertedValue instanceof Collection)) && 
/* 1548 */           (!ObjectUtils.isArray(convertedValue))) {
/* 1549 */           pv.setConvertedValue(convertedValue);
/* 1550 */           deepCopy.add(pv);
/*      */         }
/*      */         else {
/* 1553 */           resolveNecessary = true;
/* 1554 */           deepCopy.add(new PropertyValue(pv, convertedValue));
/*      */         }
/*      */       }
/*      */     }
/* 1558 */     if ((mpvs != null) && (!resolveNecessary)) {
/* 1559 */       mpvs.setConverted();
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1564 */       bw.setPropertyValues(new MutablePropertyValues(deepCopy));
/*      */     }
/*      */     catch (BeansException ex)
/*      */     {
/* 1568 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Error setting property values", ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private Object convertForProperty(Object value, String propertyName, BeanWrapper bw, TypeConverter converter)
/*      */   {
/* 1576 */     if ((converter instanceof BeanWrapperImpl)) {
/* 1577 */       return ((BeanWrapperImpl)converter).convertForProperty(value, propertyName);
/*      */     }
/*      */     
/* 1580 */     PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/* 1581 */     MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/* 1582 */     return converter.convertIfNecessary(value, pd.getPropertyType(), methodParam);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd)
/*      */   {
/* 1605 */     if (System.getSecurityManager() != null) {
/* 1606 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/* 1609 */           AbstractAutowireCapableBeanFactory.this.invokeAwareMethods(beanName, bean);
/* 1610 */           return null;
/*      */         }
/* 1612 */       }, getAccessControlContext());
/*      */     }
/*      */     else {
/* 1615 */       invokeAwareMethods(beanName, bean);
/*      */     }
/*      */     
/* 1618 */     Object wrappedBean = bean;
/* 1619 */     if ((mbd == null) || (!mbd.isSynthetic())) {
/* 1620 */       wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
/*      */     }
/*      */     try
/*      */     {
/* 1624 */       invokeInitMethods(beanName, wrappedBean, mbd);
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/* 1628 */       throw new BeanCreationException(mbd != null ? mbd.getResourceDescription() : null, beanName, "Invocation of init method failed", ex);
/*      */     }
/*      */     
/*      */ 
/* 1632 */     if ((mbd == null) || (!mbd.isSynthetic())) {
/* 1633 */       wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
/*      */     }
/* 1635 */     return wrappedBean;
/*      */   }
/*      */   
/*      */   private void invokeAwareMethods(String beanName, Object bean) {
/* 1639 */     if ((bean instanceof Aware)) {
/* 1640 */       if ((bean instanceof BeanNameAware)) {
/* 1641 */         ((BeanNameAware)bean).setBeanName(beanName);
/*      */       }
/* 1643 */       if ((bean instanceof BeanClassLoaderAware)) {
/* 1644 */         ((BeanClassLoaderAware)bean).setBeanClassLoader(getBeanClassLoader());
/*      */       }
/* 1646 */       if ((bean instanceof BeanFactoryAware)) {
/* 1647 */         ((BeanFactoryAware)bean).setBeanFactory(this);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)
/*      */     throws Throwable
/*      */   {
/* 1667 */     boolean isInitializingBean = bean instanceof InitializingBean;
/* 1668 */     if ((isInitializingBean) && ((mbd == null) || (!mbd.isExternallyManagedInitMethod("afterPropertiesSet")))) {
/* 1669 */       if (this.logger.isDebugEnabled()) {
/* 1670 */         this.logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
/*      */       }
/* 1672 */       if (System.getSecurityManager() != null) {
/*      */         try {
/* 1674 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public Object run() throws Exception {
/* 1677 */               ((InitializingBean)bean).afterPropertiesSet();
/* 1678 */               return null;
/*      */             }
/* 1680 */           }, getAccessControlContext());
/*      */         }
/*      */         catch (PrivilegedActionException pae) {
/* 1683 */           throw pae.getException();
/*      */         }
/*      */         
/*      */       } else {
/* 1687 */         ((InitializingBean)bean).afterPropertiesSet();
/*      */       }
/*      */     }
/*      */     
/* 1691 */     if (mbd != null) {
/* 1692 */       String initMethodName = mbd.getInitMethodName();
/* 1693 */       if ((initMethodName != null) && ((!isInitializingBean) || (!"afterPropertiesSet".equals(initMethodName))) && 
/* 1694 */         (!mbd.isExternallyManagedInitMethod(initMethodName))) {
/* 1695 */         invokeCustomInitMethod(beanName, bean, mbd);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void invokeCustomInitMethod(String beanName, final Object bean, RootBeanDefinition mbd)
/*      */     throws Throwable
/*      */   {
/* 1710 */     String initMethodName = mbd.getInitMethodName();
/*      */     
/*      */ 
/* 1713 */     final Method initMethod = mbd.isNonPublicAccessAllowed() ? BeanUtils.findMethod(bean.getClass(), initMethodName, new Class[0]) : ClassUtils.getMethodIfAvailable(bean.getClass(), initMethodName, new Class[0]);
/* 1714 */     if (initMethod == null) {
/* 1715 */       if (mbd.isEnforceInitMethod()) {
/* 1716 */         throw new BeanDefinitionValidationException("Couldn't find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
/*      */       }
/*      */       
/*      */ 
/* 1720 */       if (this.logger.isDebugEnabled()) {
/* 1721 */         this.logger.debug("No default init method named '" + initMethodName + "' found on bean with name '" + beanName + "'");
/*      */       }
/*      */       
/*      */ 
/* 1725 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1729 */     if (this.logger.isDebugEnabled()) {
/* 1730 */       this.logger.debug("Invoking init method  '" + initMethodName + "' on bean with name '" + beanName + "'");
/*      */     }
/*      */     
/* 1733 */     if (System.getSecurityManager() != null) {
/* 1734 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Object run() throws Exception {
/* 1737 */           ReflectionUtils.makeAccessible(initMethod);
/* 1738 */           return null;
/*      */         }
/*      */       });
/*      */       try {
/* 1742 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public Object run() throws Exception {
/* 1745 */             initMethod.invoke(bean, new Object[0]);
/* 1746 */             return null;
/*      */           }
/* 1748 */         }, getAccessControlContext());
/*      */       }
/*      */       catch (PrivilegedActionException pae) {
/* 1751 */         InvocationTargetException ex = (InvocationTargetException)pae.getException();
/* 1752 */         throw ex.getTargetException();
/*      */       }
/*      */     }
/*      */     else {
/*      */       try {
/* 1757 */         ReflectionUtils.makeAccessible(initMethod);
/* 1758 */         initMethod.invoke(bean, new Object[0]);
/*      */       }
/*      */       catch (InvocationTargetException ex) {
/* 1761 */         throw ex.getTargetException();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object postProcessObjectFromFactoryBean(Object object, String beanName)
/*      */   {
/* 1775 */     return applyBeanPostProcessorsAfterInitialization(object, beanName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void removeSingleton(String beanName)
/*      */   {
/* 1783 */     super.removeSingleton(beanName);
/* 1784 */     this.factoryBeanInstanceCache.remove(beanName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class AutowireByTypeDependencyDescriptor
/*      */     extends DependencyDescriptor
/*      */   {
/*      */     public AutowireByTypeDependencyDescriptor(MethodParameter methodParameter, boolean eager)
/*      */     {
/* 1796 */       super(false, eager);
/*      */     }
/*      */     
/*      */     public String getDependencyName()
/*      */     {
/* 1801 */       return null;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\AbstractAutowireCapableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */