/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.NotSerializableException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.io.Serializable;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import javax.inject.Provider;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.InjectionPoint;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.ObjectProvider;
/*      */ import org.springframework.beans.factory.SmartFactoryBean;
/*      */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*      */ import org.springframework.beans.factory.config.NamedBeanHolder;
/*      */ import org.springframework.core.OrderComparator;
/*      */ import org.springframework.core.OrderComparator.OrderSourceProvider;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.annotation.AnnotationUtils;
/*      */ import org.springframework.lang.UsesJava8;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CompositeIterator;
/*      */ import org.springframework.util.ObjectUtils;
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
/*      */ public class DefaultListableBeanFactory
/*      */   extends AbstractAutowireCapableBeanFactory
/*      */   implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable
/*      */ {
/*  120 */   private static Class<?> javaUtilOptionalClass = null;
/*      */   
/*  122 */   private static Class<?> javaxInjectProviderClass = null;
/*      */   
/*      */   static
/*      */   {
/*      */     try {
/*  127 */       javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", DefaultListableBeanFactory.class.getClassLoader());
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException1) {}
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  134 */       javaxInjectProviderClass = ClassUtils.forName("javax.inject.Provider", DefaultListableBeanFactory.class.getClassLoader());
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException2) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  143 */   private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories = new ConcurrentHashMap(8);
/*      */   
/*      */ 
/*      */ 
/*      */   private String serializationId;
/*      */   
/*      */ 
/*  150 */   private boolean allowBeanDefinitionOverriding = true;
/*      */   
/*      */ 
/*  153 */   private boolean allowEagerClassLoading = true;
/*      */   
/*      */ 
/*      */   private Comparator<Object> dependencyComparator;
/*      */   
/*      */ 
/*  159 */   private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
/*      */   
/*      */ 
/*  162 */   private final Map<Class<?>, Object> resolvableDependencies = new ConcurrentHashMap(16);
/*      */   
/*      */ 
/*  165 */   private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);
/*      */   
/*      */ 
/*  168 */   private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap(64);
/*      */   
/*      */ 
/*  171 */   private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap(64);
/*      */   
/*      */ 
/*  174 */   private volatile List<String> beanDefinitionNames = new ArrayList(256);
/*      */   
/*      */ 
/*  177 */   private volatile Set<String> manualSingletonNames = new LinkedHashSet(16);
/*      */   
/*      */ 
/*      */   private volatile String[] frozenBeanDefinitionNames;
/*      */   
/*      */ 
/*  183 */   private volatile boolean configurationFrozen = false;
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
/*      */   public DefaultListableBeanFactory(BeanFactory parentBeanFactory)
/*      */   {
/*  198 */     super(parentBeanFactory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSerializationId(String serializationId)
/*      */   {
/*  207 */     if (serializationId != null) {
/*  208 */       serializableFactories.put(serializationId, new WeakReference(this));
/*      */     }
/*  210 */     else if (this.serializationId != null) {
/*  211 */       serializableFactories.remove(this.serializationId);
/*      */     }
/*  213 */     this.serializationId = serializationId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSerializationId()
/*      */   {
/*  222 */     return this.serializationId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding)
/*      */   {
/*  233 */     this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAllowBeanDefinitionOverriding()
/*      */   {
/*  242 */     return this.allowBeanDefinitionOverriding;
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
/*      */   public void setAllowEagerClassLoading(boolean allowEagerClassLoading)
/*      */   {
/*  256 */     this.allowEagerClassLoading = allowEagerClassLoading;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAllowEagerClassLoading()
/*      */   {
/*  265 */     return this.allowEagerClassLoading;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDependencyComparator(Comparator<Object> dependencyComparator)
/*      */   {
/*  275 */     this.dependencyComparator = dependencyComparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Comparator<Object> getDependencyComparator()
/*      */   {
/*  283 */     return this.dependencyComparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutowireCandidateResolver(final AutowireCandidateResolver autowireCandidateResolver)
/*      */   {
/*  292 */     Assert.notNull(autowireCandidateResolver, "AutowireCandidateResolver must not be null");
/*  293 */     if ((autowireCandidateResolver instanceof BeanFactoryAware)) {
/*  294 */       if (System.getSecurityManager() != null) {
/*  295 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/*  298 */             ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory(DefaultListableBeanFactory.this);
/*  299 */             return null;
/*      */           }
/*  301 */         }, getAccessControlContext());
/*      */       }
/*      */       else {
/*  304 */         ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory(this);
/*      */       }
/*      */     }
/*  307 */     this.autowireCandidateResolver = autowireCandidateResolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AutowireCandidateResolver getAutowireCandidateResolver()
/*      */   {
/*  314 */     return this.autowireCandidateResolver;
/*      */   }
/*      */   
/*      */ 
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*      */   {
/*  320 */     super.copyConfigurationFrom(otherFactory);
/*  321 */     if ((otherFactory instanceof DefaultListableBeanFactory)) {
/*  322 */       DefaultListableBeanFactory otherListableFactory = (DefaultListableBeanFactory)otherFactory;
/*  323 */       this.allowBeanDefinitionOverriding = otherListableFactory.allowBeanDefinitionOverriding;
/*  324 */       this.allowEagerClassLoading = otherListableFactory.allowEagerClassLoading;
/*  325 */       this.dependencyComparator = otherListableFactory.dependencyComparator;
/*      */       
/*  327 */       setAutowireCandidateResolver((AutowireCandidateResolver)BeanUtils.instantiateClass(getAutowireCandidateResolver().getClass()));
/*      */       
/*  329 */       this.resolvableDependencies.putAll(otherListableFactory.resolvableDependencies);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T getBean(Class<T> requiredType)
/*      */     throws BeansException
/*      */   {
/*  340 */     return (T)getBean(requiredType, (Object[])null);
/*      */   }
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException
/*      */   {
/*  345 */     NamedBeanHolder<T> namedBean = resolveNamedBean(requiredType, args);
/*  346 */     if (namedBean != null) {
/*  347 */       return (T)namedBean.getBeanInstance();
/*      */     }
/*  349 */     BeanFactory parent = getParentBeanFactory();
/*  350 */     if (parent != null) {
/*  351 */       return (T)parent.getBean(requiredType, args);
/*      */     }
/*  353 */     throw new NoSuchBeanDefinitionException(requiredType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean containsBeanDefinition(String beanName)
/*      */   {
/*  363 */     Assert.notNull(beanName, "Bean name must not be null");
/*  364 */     return this.beanDefinitionMap.containsKey(beanName);
/*      */   }
/*      */   
/*      */   public int getBeanDefinitionCount()
/*      */   {
/*  369 */     return this.beanDefinitionMap.size();
/*      */   }
/*      */   
/*      */   public String[] getBeanDefinitionNames()
/*      */   {
/*  374 */     if (this.frozenBeanDefinitionNames != null) {
/*  375 */       return (String[])this.frozenBeanDefinitionNames.clone();
/*      */     }
/*      */     
/*  378 */     return StringUtils.toStringArray(this.beanDefinitionNames);
/*      */   }
/*      */   
/*      */ 
/*      */   public String[] getBeanNamesForType(ResolvableType type)
/*      */   {
/*  384 */     return doGetBeanNamesForType(type, true, true);
/*      */   }
/*      */   
/*      */   public String[] getBeanNamesForType(Class<?> type)
/*      */   {
/*  389 */     return getBeanNamesForType(type, true, true);
/*      */   }
/*      */   
/*      */   public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit)
/*      */   {
/*  394 */     if ((!isConfigurationFrozen()) || (type == null) || (!allowEagerInit)) {
/*  395 */       return doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, allowEagerInit);
/*      */     }
/*  397 */     Map<Class<?>, String[]> cache = includeNonSingletons ? this.allBeanNamesByType : this.singletonBeanNamesByType;
/*      */     
/*  399 */     String[] resolvedBeanNames = (String[])cache.get(type);
/*  400 */     if (resolvedBeanNames != null) {
/*  401 */       return resolvedBeanNames;
/*      */     }
/*  403 */     resolvedBeanNames = doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, true);
/*  404 */     if (ClassUtils.isCacheSafe(type, getBeanClassLoader())) {
/*  405 */       cache.put(type, resolvedBeanNames);
/*      */     }
/*  407 */     return resolvedBeanNames;
/*      */   }
/*      */   
/*      */   private String[] doGetBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
/*  411 */     List<String> result = new ArrayList();
/*      */     
/*      */ 
/*  414 */     for (String beanName : this.beanDefinitionNames)
/*      */     {
/*      */ 
/*  417 */       if (!isAlias(beanName)) {
/*      */         try {
/*  419 */           RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */           
/*  421 */           if ((!mbd.isAbstract()) && ((allowEagerInit) || (
/*  422 */             ((mbd.hasBeanClass()) || (!mbd.isLazyInit()) || (isAllowEagerClassLoading())) && 
/*  423 */             (!requiresEagerInitForType(mbd.getFactoryBeanName())))))
/*      */           {
/*  425 */             boolean isFactoryBean = isFactoryBean(beanName, mbd);
/*  426 */             BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  432 */             boolean matchFound = ((allowEagerInit) || (!isFactoryBean) || ((dbd != null) && (!mbd.isLazyInit())) || (containsSingleton(beanName))) && ((includeNonSingletons) || (dbd != null ? mbd.isSingleton() : isSingleton(beanName))) && (isTypeMatch(beanName, type));
/*  433 */             if ((!matchFound) && (isFactoryBean))
/*      */             {
/*  435 */               beanName = "&" + beanName;
/*  436 */               matchFound = ((includeNonSingletons) || (mbd.isSingleton())) && (isTypeMatch(beanName, type));
/*      */             }
/*  438 */             if (matchFound) {
/*  439 */               result.add(beanName);
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (CannotLoadBeanClassException ex) {
/*  444 */           if (allowEagerInit) {
/*  445 */             throw ex;
/*      */           }
/*      */           
/*  448 */           if (this.logger.isDebugEnabled()) {
/*  449 */             this.logger.debug("Ignoring bean class loading failure for bean '" + beanName + "'", ex);
/*      */           }
/*  451 */           onSuppressedException(ex);
/*      */         }
/*      */         catch (BeanDefinitionStoreException ex) {
/*  454 */           if (allowEagerInit) {
/*  455 */             throw ex;
/*      */           }
/*      */           
/*  458 */           if (this.logger.isDebugEnabled()) {
/*  459 */             this.logger.debug("Ignoring unresolvable metadata in bean definition '" + beanName + "'", ex);
/*      */           }
/*  461 */           onSuppressedException(ex);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  467 */     for (String beanName : this.manualSingletonNames) {
/*      */       try
/*      */       {
/*  470 */         if (isFactoryBean(beanName)) {
/*  471 */           if (((includeNonSingletons) || (isSingleton(beanName))) && (isTypeMatch(beanName, type))) {
/*  472 */             result.add(beanName);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  477 */             beanName = "&" + beanName;
/*      */           }
/*      */         }
/*  480 */         else if (isTypeMatch(beanName, type)) {
/*  481 */           result.add(beanName);
/*      */         }
/*      */       }
/*      */       catch (NoSuchBeanDefinitionException ex)
/*      */       {
/*  486 */         if (this.logger.isDebugEnabled()) {
/*  487 */           this.logger.debug("Failed to check manually registered singleton with name '" + beanName + "'", ex);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  492 */     return StringUtils.toStringArray(result);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean requiresEagerInitForType(String factoryBeanName)
/*      */   {
/*  503 */     return (factoryBeanName != null) && (isFactoryBean(factoryBeanName)) && (!containsSingleton(factoryBeanName));
/*      */   }
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException
/*      */   {
/*  508 */     return getBeansOfType(type, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/*      */     throws BeansException
/*      */   {
/*  515 */     String[] beanNames = getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*  516 */     Map<String, T> result = new LinkedHashMap(beanNames.length);
/*  517 */     for (String beanName : beanNames) {
/*      */       try {
/*  519 */         result.put(beanName, getBean(beanName, type));
/*      */       }
/*      */       catch (BeanCreationException ex) {
/*  522 */         Throwable rootCause = ex.getMostSpecificCause();
/*  523 */         if ((rootCause instanceof BeanCurrentlyInCreationException)) {
/*  524 */           BeanCreationException bce = (BeanCreationException)rootCause;
/*  525 */           if (isCurrentlyInCreation(bce.getBeanName())) {
/*  526 */             if (this.logger.isDebugEnabled()) {
/*  527 */               this.logger.debug("Ignoring match to currently created bean '" + beanName + "': " + ex
/*  528 */                 .getMessage());
/*      */             }
/*  530 */             onSuppressedException(ex);
/*      */             
/*      */ 
/*  533 */             continue;
/*      */           }
/*      */         }
/*  536 */         throw ex;
/*      */       }
/*      */     }
/*  539 */     return result;
/*      */   }
/*      */   
/*      */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType)
/*      */   {
/*  544 */     List<String> results = new ArrayList();
/*  545 */     for (String beanName : this.beanDefinitionNames) {
/*  546 */       BeanDefinition beanDefinition = getBeanDefinition(beanName);
/*  547 */       if ((!beanDefinition.isAbstract()) && (findAnnotationOnBean(beanName, annotationType) != null)) {
/*  548 */         results.add(beanName);
/*      */       }
/*      */     }
/*  551 */     for (String beanName : this.manualSingletonNames) {
/*  552 */       if ((!results.contains(beanName)) && (findAnnotationOnBean(beanName, annotationType) != null)) {
/*  553 */         results.add(beanName);
/*      */       }
/*      */     }
/*  556 */     return (String[])results.toArray(new String[results.size()]);
/*      */   }
/*      */   
/*      */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
/*      */   {
/*  561 */     String[] beanNames = getBeanNamesForAnnotation(annotationType);
/*  562 */     Map<String, Object> results = new LinkedHashMap(beanNames.length);
/*  563 */     for (String beanName : beanNames) {
/*  564 */       results.put(beanName, getBean(beanName));
/*      */     }
/*  566 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  579 */     A ann = null;
/*  580 */     Class<?> beanType = getType(beanName);
/*  581 */     if (beanType != null) {
/*  582 */       ann = AnnotationUtils.findAnnotation(beanType, annotationType);
/*      */     }
/*  584 */     if ((ann == null) && (containsBeanDefinition(beanName))) {
/*  585 */       BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  586 */       if ((bd instanceof AbstractBeanDefinition)) {
/*  587 */         AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/*  588 */         if (abd.hasBeanClass()) {
/*  589 */           ann = AnnotationUtils.findAnnotation(abd.getBeanClass(), annotationType);
/*      */         }
/*      */       }
/*      */     }
/*  593 */     return ann;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue)
/*      */   {
/*  603 */     Assert.notNull(dependencyType, "Dependency type must not be null");
/*  604 */     if (autowiredValue != null) {
/*  605 */       if ((!(autowiredValue instanceof ObjectFactory)) && (!dependencyType.isInstance(autowiredValue)))
/*      */       {
/*  607 */         throw new IllegalArgumentException("Value [" + autowiredValue + "] does not implement specified dependency type [" + dependencyType.getName() + "]");
/*      */       }
/*  609 */       this.resolvableDependencies.put(dependencyType, autowiredValue);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  617 */     return isAutowireCandidate(beanName, descriptor, getAutowireCandidateResolver());
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
/*      */   protected boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor, AutowireCandidateResolver resolver)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  631 */     String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
/*  632 */     if (containsBeanDefinition(beanDefinitionName)) {
/*  633 */       return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanDefinitionName), descriptor, resolver);
/*      */     }
/*  635 */     if (containsSingleton(beanName)) {
/*  636 */       return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor, resolver);
/*      */     }
/*      */     
/*  639 */     BeanFactory parent = getParentBeanFactory();
/*  640 */     if ((parent instanceof DefaultListableBeanFactory))
/*      */     {
/*  642 */       return ((DefaultListableBeanFactory)parent).isAutowireCandidate(beanName, descriptor, resolver);
/*      */     }
/*  644 */     if ((parent instanceof ConfigurableListableBeanFactory))
/*      */     {
/*  646 */       return ((ConfigurableListableBeanFactory)parent).isAutowireCandidate(beanName, descriptor);
/*      */     }
/*      */     
/*  649 */     return true;
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
/*      */   protected boolean isAutowireCandidate(String beanName, RootBeanDefinition mbd, DependencyDescriptor descriptor, AutowireCandidateResolver resolver)
/*      */   {
/*  665 */     String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
/*  666 */     resolveBeanClass(mbd, beanDefinitionName, new Class[0]);
/*  667 */     if (mbd.isFactoryMethodUnique) {
/*      */       boolean resolve;
/*  669 */       synchronized (mbd.constructorArgumentLock) {
/*  670 */         resolve = mbd.resolvedConstructorOrFactoryMethod == null; }
/*      */       boolean resolve;
/*  672 */       if (resolve) {
/*  673 */         new ConstructorResolver(this).resolveFactoryMethodIfPossible(mbd);
/*      */       }
/*      */     }
/*  676 */     return resolver.isAutowireCandidate(new BeanDefinitionHolder(mbd, beanName, 
/*  677 */       getAliases(beanDefinitionName)), descriptor);
/*      */   }
/*      */   
/*      */   public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException
/*      */   {
/*  682 */     BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(beanName);
/*  683 */     if (bd == null) {
/*  684 */       if (this.logger.isTraceEnabled()) {
/*  685 */         this.logger.trace("No bean named '" + beanName + "' found in " + this);
/*      */       }
/*  687 */       throw new NoSuchBeanDefinitionException(beanName);
/*      */     }
/*  689 */     return bd;
/*      */   }
/*      */   
/*      */   public Iterator<String> getBeanNamesIterator()
/*      */   {
/*  694 */     CompositeIterator<String> iterator = new CompositeIterator();
/*  695 */     iterator.add(this.beanDefinitionNames.iterator());
/*  696 */     iterator.add(this.manualSingletonNames.iterator());
/*  697 */     return iterator;
/*      */   }
/*      */   
/*      */   public void clearMetadataCache()
/*      */   {
/*  702 */     super.clearMetadataCache();
/*  703 */     clearByTypeCache();
/*      */   }
/*      */   
/*      */   public void freezeConfiguration()
/*      */   {
/*  708 */     this.configurationFrozen = true;
/*  709 */     this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.beanDefinitionNames);
/*      */   }
/*      */   
/*      */   public boolean isConfigurationFrozen()
/*      */   {
/*  714 */     return this.configurationFrozen;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isBeanEligibleForMetadataCaching(String beanName)
/*      */   {
/*  724 */     return (this.configurationFrozen) || (super.isBeanEligibleForMetadataCaching(beanName));
/*      */   }
/*      */   
/*      */   public void preInstantiateSingletons() throws BeansException
/*      */   {
/*  729 */     if (this.logger.isDebugEnabled()) {
/*  730 */       this.logger.debug("Pre-instantiating singletons in " + this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  735 */     List<String> beanNames = new ArrayList(this.beanDefinitionNames);
/*      */     
/*      */ 
/*  738 */     for (String beanName : beanNames) {
/*  739 */       RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
/*  740 */       if ((!bd.isAbstract()) && (bd.isSingleton()) && (!bd.isLazyInit())) {
/*  741 */         if (isFactoryBean(beanName)) {
/*  742 */           final FactoryBean<?> factory = (FactoryBean)getBean("&" + beanName);
/*      */           boolean isEagerInit;
/*  744 */           boolean isEagerInit; if ((System.getSecurityManager() != null) && ((factory instanceof SmartFactoryBean))) {
/*  745 */             isEagerInit = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */             {
/*      */               public Boolean run() {
/*  748 */                 return Boolean.valueOf(((SmartFactoryBean)factory).isEagerInit());
/*      */               }
/*  750 */             }, getAccessControlContext())).booleanValue();
/*      */           }
/*      */           else
/*      */           {
/*  754 */             isEagerInit = ((factory instanceof SmartFactoryBean)) && (((SmartFactoryBean)factory).isEagerInit());
/*      */           }
/*  756 */           if (isEagerInit) {
/*  757 */             getBean(beanName);
/*      */           }
/*      */         }
/*      */         else {
/*  761 */           getBean(beanName);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  767 */     for (String beanName : beanNames) {
/*  768 */       Object singletonInstance = getSingleton(beanName);
/*  769 */       if ((singletonInstance instanceof SmartInitializingSingleton)) {
/*  770 */         final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton)singletonInstance;
/*  771 */         if (System.getSecurityManager() != null) {
/*  772 */           AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public Object run() {
/*  775 */               smartSingleton.afterSingletonsInstantiated();
/*  776 */               return null;
/*      */             }
/*  778 */           }, getAccessControlContext());
/*      */         }
/*      */         else {
/*  781 */           smartSingleton.afterSingletonsInstantiated();
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
/*      */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
/*      */     throws BeanDefinitionStoreException
/*      */   {
/*  796 */     Assert.hasText(beanName, "Bean name must not be empty");
/*  797 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/*      */     
/*  799 */     if ((beanDefinition instanceof AbstractBeanDefinition)) {
/*      */       try {
/*  801 */         ((AbstractBeanDefinition)beanDefinition).validate();
/*      */       }
/*      */       catch (BeanDefinitionValidationException ex) {
/*  804 */         throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Validation of bean definition failed", ex);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  811 */     BeanDefinition oldBeanDefinition = (BeanDefinition)this.beanDefinitionMap.get(beanName);
/*  812 */     if (oldBeanDefinition != null) {
/*  813 */       if (!isAllowBeanDefinitionOverriding()) {
/*  814 */         throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Cannot register bean definition [" + beanDefinition + "] for bean '" + beanName + "': There is already [" + oldBeanDefinition + "] bound.");
/*      */       }
/*      */       
/*      */ 
/*  818 */       if (oldBeanDefinition.getRole() < beanDefinition.getRole())
/*      */       {
/*  820 */         if (this.logger.isWarnEnabled()) {
/*  821 */           this.logger.warn("Overriding user-defined bean definition for bean '" + beanName + "' with a framework-generated bean definition: replacing [" + oldBeanDefinition + "] with [" + beanDefinition + "]");
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*  826 */       else if (!beanDefinition.equals(oldBeanDefinition)) {
/*  827 */         if (this.logger.isInfoEnabled()) {
/*  828 */           this.logger.info("Overriding bean definition for bean '" + beanName + "' with a different definition: replacing [" + oldBeanDefinition + "] with [" + beanDefinition + "]");
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*  834 */       else if (this.logger.isDebugEnabled()) {
/*  835 */         this.logger.debug("Overriding bean definition for bean '" + beanName + "' with an equivalent definition: replacing [" + oldBeanDefinition + "] with [" + beanDefinition + "]");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  840 */       this.beanDefinitionMap.put(beanName, beanDefinition);
/*      */     }
/*      */     else {
/*  843 */       if (hasBeanCreationStarted())
/*      */       {
/*  845 */         synchronized (this.beanDefinitionMap) {
/*  846 */           this.beanDefinitionMap.put(beanName, beanDefinition);
/*  847 */           List<String> updatedDefinitions = new ArrayList(this.beanDefinitionNames.size() + 1);
/*  848 */           updatedDefinitions.addAll(this.beanDefinitionNames);
/*  849 */           updatedDefinitions.add(beanName);
/*  850 */           this.beanDefinitionNames = updatedDefinitions;
/*  851 */           if (this.manualSingletonNames.contains(beanName)) {
/*  852 */             Set<String> updatedSingletons = new LinkedHashSet(this.manualSingletonNames);
/*  853 */             updatedSingletons.remove(beanName);
/*  854 */             this.manualSingletonNames = updatedSingletons;
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  860 */         this.beanDefinitionMap.put(beanName, beanDefinition);
/*  861 */         this.beanDefinitionNames.add(beanName);
/*  862 */         this.manualSingletonNames.remove(beanName);
/*      */       }
/*  864 */       this.frozenBeanDefinitionNames = null;
/*      */     }
/*      */     
/*  867 */     if ((oldBeanDefinition != null) || (containsSingleton(beanName))) {
/*  868 */       resetBeanDefinition(beanName);
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException
/*      */   {
/*  874 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*      */     
/*  876 */     BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.remove(beanName);
/*  877 */     if (bd == null) {
/*  878 */       if (this.logger.isTraceEnabled()) {
/*  879 */         this.logger.trace("No bean named '" + beanName + "' found in " + this);
/*      */       }
/*  881 */       throw new NoSuchBeanDefinitionException(beanName);
/*      */     }
/*      */     
/*  884 */     if (hasBeanCreationStarted())
/*      */     {
/*  886 */       synchronized (this.beanDefinitionMap) {
/*  887 */         List<String> updatedDefinitions = new ArrayList(this.beanDefinitionNames);
/*  888 */         updatedDefinitions.remove(beanName);
/*  889 */         this.beanDefinitionNames = updatedDefinitions;
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/*  894 */       this.beanDefinitionNames.remove(beanName);
/*      */     }
/*  896 */     this.frozenBeanDefinitionNames = null;
/*      */     
/*  898 */     resetBeanDefinition(beanName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void resetBeanDefinition(String beanName)
/*      */   {
/*  908 */     clearMergedBeanDefinition(beanName);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  913 */     destroySingleton(beanName);
/*      */     
/*      */ 
/*  916 */     for (String bdName : this.beanDefinitionNames) {
/*  917 */       if (!beanName.equals(bdName)) {
/*  918 */         BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(bdName);
/*  919 */         if (beanName.equals(bd.getParentName())) {
/*  920 */           resetBeanDefinition(bdName);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean allowAliasOverriding()
/*      */   {
/*  931 */     return isAllowBeanDefinitionOverriding();
/*      */   }
/*      */   
/*      */   public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException
/*      */   {
/*  936 */     super.registerSingleton(beanName, singletonObject);
/*      */     
/*  938 */     if (hasBeanCreationStarted())
/*      */     {
/*  940 */       synchronized (this.beanDefinitionMap) {
/*  941 */         if (!this.beanDefinitionMap.containsKey(beanName)) {
/*  942 */           Set<String> updatedSingletons = new LinkedHashSet(this.manualSingletonNames.size() + 1);
/*  943 */           updatedSingletons.addAll(this.manualSingletonNames);
/*  944 */           updatedSingletons.add(beanName);
/*  945 */           this.manualSingletonNames = updatedSingletons;
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/*  951 */     else if (!this.beanDefinitionMap.containsKey(beanName)) {
/*  952 */       this.manualSingletonNames.add(beanName);
/*      */     }
/*      */     
/*      */ 
/*  956 */     clearByTypeCache();
/*      */   }
/*      */   
/*      */   public void destroySingleton(String beanName)
/*      */   {
/*  961 */     super.destroySingleton(beanName);
/*  962 */     this.manualSingletonNames.remove(beanName);
/*  963 */     clearByTypeCache();
/*      */   }
/*      */   
/*      */   public void destroySingletons()
/*      */   {
/*  968 */     super.destroySingletons();
/*  969 */     this.manualSingletonNames.clear();
/*  970 */     clearByTypeCache();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void clearByTypeCache()
/*      */   {
/*  977 */     this.allBeanNamesByType.clear();
/*  978 */     this.singletonBeanNamesByType.clear();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType)
/*      */     throws BeansException
/*      */   {
/*  988 */     NamedBeanHolder<T> namedBean = resolveNamedBean(requiredType, (Object[])null);
/*  989 */     if (namedBean != null) {
/*  990 */       return namedBean;
/*      */     }
/*  992 */     BeanFactory parent = getParentBeanFactory();
/*  993 */     if ((parent instanceof AutowireCapableBeanFactory)) {
/*  994 */       return ((AutowireCapableBeanFactory)parent).resolveNamedBean(requiredType);
/*      */     }
/*  996 */     throw new NoSuchBeanDefinitionException(requiredType);
/*      */   }
/*      */   
/*      */   private <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType, Object... args) throws BeansException
/*      */   {
/* 1001 */     Assert.notNull(requiredType, "Required type must not be null");
/* 1002 */     String[] candidateNames = getBeanNamesForType(requiredType);
/*      */     
/* 1004 */     if (candidateNames.length > 1) {
/* 1005 */       List<String> autowireCandidates = new ArrayList(candidateNames.length);
/* 1006 */       for (String beanName : candidateNames) {
/* 1007 */         if ((!containsBeanDefinition(beanName)) || (getBeanDefinition(beanName).isAutowireCandidate())) {
/* 1008 */           autowireCandidates.add(beanName);
/*      */         }
/*      */       }
/* 1011 */       if (!autowireCandidates.isEmpty()) {
/* 1012 */         candidateNames = (String[])autowireCandidates.toArray(new String[autowireCandidates.size()]);
/*      */       }
/*      */     }
/*      */     
/* 1016 */     if (candidateNames.length == 1) {
/* 1017 */       String beanName = candidateNames[0];
/* 1018 */       return new NamedBeanHolder(beanName, getBean(beanName, requiredType, args));
/*      */     }
/* 1020 */     if (candidateNames.length > 1) {
/* 1021 */       Map<String, Object> candidates = new LinkedHashMap(candidateNames.length);
/* 1022 */       for (String beanName : candidateNames) {
/* 1023 */         if (containsSingleton(beanName)) {
/* 1024 */           candidates.put(beanName, getBean(beanName, requiredType, args));
/*      */         }
/*      */         else {
/* 1027 */           candidates.put(beanName, getType(beanName));
/*      */         }
/*      */       }
/* 1030 */       String candidateName = determinePrimaryCandidate(candidates, requiredType);
/* 1031 */       if (candidateName == null) {
/* 1032 */         candidateName = determineHighestPriorityCandidate(candidates, requiredType);
/*      */       }
/* 1034 */       if (candidateName != null) {
/* 1035 */         Object beanInstance = candidates.get(candidateName);
/* 1036 */         if ((beanInstance instanceof Class)) {
/* 1037 */           beanInstance = getBean(candidateName, requiredType, args);
/*      */         }
/* 1039 */         return new NamedBeanHolder(candidateName, beanInstance);
/*      */       }
/* 1041 */       throw new NoUniqueBeanDefinitionException(requiredType, candidates.keySet());
/*      */     }
/*      */     
/* 1044 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*      */     throws BeansException
/*      */   {
/* 1051 */     descriptor.initParameterNameDiscovery(getParameterNameDiscoverer());
/* 1052 */     if (javaUtilOptionalClass == descriptor.getDependencyType()) {
/* 1053 */       return new OptionalDependencyFactory(null).createOptionalDependency(descriptor, requestingBeanName, new Object[0]);
/*      */     }
/* 1055 */     if ((ObjectFactory.class == descriptor.getDependencyType()) || 
/* 1056 */       (ObjectProvider.class == descriptor.getDependencyType())) {
/* 1057 */       return new DependencyObjectProvider(descriptor, requestingBeanName);
/*      */     }
/* 1059 */     if (javaxInjectProviderClass == descriptor.getDependencyType()) {
/* 1060 */       return new Jsr330ProviderFactory(null).createDependencyProvider(descriptor, requestingBeanName);
/*      */     }
/*      */     
/* 1063 */     Object result = getAutowireCandidateResolver().getLazyResolutionProxyIfNecessary(descriptor, requestingBeanName);
/*      */     
/* 1065 */     if (result == null) {
/* 1066 */       result = doResolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
/*      */     }
/* 1068 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object doResolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*      */     throws BeansException
/*      */   {
/* 1075 */     InjectionPoint previousInjectionPoint = ConstructorResolver.setCurrentInjectionPoint(descriptor);
/*      */     try {
/* 1077 */       Object shortcut = descriptor.resolveShortcut(this);
/* 1078 */       if (shortcut != null) {
/* 1079 */         return shortcut;
/*      */       }
/*      */       
/* 1082 */       Object type = descriptor.getDependencyType();
/* 1083 */       Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
/* 1084 */       BeanDefinition bd; if (value != null) {
/* 1085 */         if ((value instanceof String)) {
/* 1086 */           String strVal = resolveEmbeddedValue((String)value);
/* 1087 */           bd = (beanName != null) && (containsBean(beanName)) ? getMergedBeanDefinition(beanName) : null;
/* 1088 */           value = evaluateBeanDefinitionString(strVal, bd);
/*      */         }
/* 1090 */         TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/* 1091 */         return descriptor.getField() != null ? converter
/* 1092 */           .convertIfNecessary(value, (Class)type, descriptor.getField()) : converter
/* 1093 */           .convertIfNecessary(value, (Class)type, descriptor.getMethodParameter());
/*      */       }
/*      */       
/* 1096 */       Object multipleBeans = resolveMultipleBeans(descriptor, beanName, autowiredBeanNames, typeConverter);
/* 1097 */       if (multipleBeans != null) {
/* 1098 */         return (BeanDefinition)multipleBeans;
/*      */       }
/*      */       
/* 1101 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, (Class)type, descriptor);
/* 1102 */       if (matchingBeans.isEmpty()) {
/* 1103 */         if (isRequired(descriptor)) {
/* 1104 */           raiseNoMatchingBeanFound((Class)type, descriptor.getResolvableType(), descriptor);
/*      */         }
/* 1106 */         return null;
/*      */       }
/*      */       Object instanceCandidate;
/*      */       Object entry;
/*      */       String autowiredBeanName;
/*      */       Object instanceCandidate;
/* 1112 */       if (matchingBeans.size() > 1) {
/* 1113 */         String autowiredBeanName = determineAutowireCandidate(matchingBeans, descriptor);
/* 1114 */         if (autowiredBeanName == null) { Object localObject3;
/* 1115 */           if ((isRequired(descriptor)) || (!indicatesMultipleBeans((Class)type))) {
/* 1116 */             return descriptor.resolveNotUnique((Class)type, matchingBeans);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1122 */           return null;
/*      */         }
/*      */         
/* 1125 */         instanceCandidate = matchingBeans.get(autowiredBeanName);
/*      */       }
/*      */       else
/*      */       {
/* 1129 */         entry = (Map.Entry)matchingBeans.entrySet().iterator().next();
/* 1130 */         autowiredBeanName = (String)((Map.Entry)entry).getKey();
/* 1131 */         instanceCandidate = ((Map.Entry)entry).getValue();
/*      */       }
/*      */       
/* 1134 */       if (autowiredBeanNames != null) {
/* 1135 */         autowiredBeanNames.add(autowiredBeanName);
/*      */       }
/* 1137 */       return (instanceCandidate instanceof Class) ? descriptor
/* 1138 */         .resolveCandidate(autowiredBeanName, (Class)type, this) : instanceCandidate;
/*      */     }
/*      */     finally {
/* 1141 */       ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private Object resolveMultipleBeans(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*      */   {
/* 1148 */     Class<?> type = descriptor.getDependencyType();
/* 1149 */     if (type.isArray()) {
/* 1150 */       Class<?> componentType = type.getComponentType();
/* 1151 */       ResolvableType resolvableType = descriptor.getResolvableType();
/* 1152 */       Class<?> resolvedArrayType = resolvableType.resolve();
/* 1153 */       if ((resolvedArrayType != null) && (resolvedArrayType != type)) {
/* 1154 */         type = resolvedArrayType;
/* 1155 */         componentType = resolvableType.getComponentType().resolve();
/*      */       }
/* 1157 */       if (componentType == null) {
/* 1158 */         return null;
/*      */       }
/* 1160 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1162 */       if (matchingBeans.isEmpty()) {
/* 1163 */         return null;
/*      */       }
/* 1165 */       if (autowiredBeanNames != null) {
/* 1166 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1168 */       TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/* 1169 */       Object result = converter.convertIfNecessary(matchingBeans.values(), type);
/* 1170 */       if ((getDependencyComparator() != null) && ((result instanceof Object[]))) {
/* 1171 */         Arrays.sort((Object[])result, adaptDependencyComparator(matchingBeans));
/*      */       }
/* 1173 */       return result;
/*      */     }
/* 1175 */     if ((Collection.class.isAssignableFrom(type)) && (type.isInterface())) {
/* 1176 */       Class<?> elementType = descriptor.getResolvableType().asCollection().resolveGeneric(new int[0]);
/* 1177 */       if (elementType == null) {
/* 1178 */         return null;
/*      */       }
/* 1180 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1182 */       if (matchingBeans.isEmpty()) {
/* 1183 */         return null;
/*      */       }
/* 1185 */       if (autowiredBeanNames != null) {
/* 1186 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1188 */       TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/* 1189 */       Object result = converter.convertIfNecessary(matchingBeans.values(), type);
/* 1190 */       if ((getDependencyComparator() != null) && ((result instanceof List))) {
/* 1191 */         Collections.sort((List)result, adaptDependencyComparator(matchingBeans));
/*      */       }
/* 1193 */       return result;
/*      */     }
/* 1195 */     if (Map.class == type) {
/* 1196 */       ResolvableType mapType = descriptor.getResolvableType().asMap();
/* 1197 */       Class<?> keyType = mapType.resolveGeneric(new int[] { 0 });
/* 1198 */       if (String.class != keyType) {
/* 1199 */         return null;
/*      */       }
/* 1201 */       Class<?> valueType = mapType.resolveGeneric(new int[] { 1 });
/* 1202 */       if (valueType == null) {
/* 1203 */         return null;
/*      */       }
/* 1205 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1207 */       if (matchingBeans.isEmpty()) {
/* 1208 */         return null;
/*      */       }
/* 1210 */       if (autowiredBeanNames != null) {
/* 1211 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1213 */       return matchingBeans;
/*      */     }
/*      */     
/* 1216 */     return null;
/*      */   }
/*      */   
/*      */   private boolean isRequired(DependencyDescriptor descriptor)
/*      */   {
/* 1221 */     AutowireCandidateResolver resolver = getAutowireCandidateResolver();
/* 1222 */     return (resolver instanceof SimpleAutowireCandidateResolver) ? ((SimpleAutowireCandidateResolver)resolver)
/* 1223 */       .isRequired(descriptor) : descriptor
/* 1224 */       .isRequired();
/*      */   }
/*      */   
/*      */   private boolean indicatesMultipleBeans(Class<?> type) {
/* 1228 */     return (type.isArray()) || ((type.isInterface()) && (
/* 1229 */       (Collection.class.isAssignableFrom(type)) || (Map.class.isAssignableFrom(type))));
/*      */   }
/*      */   
/*      */   private Comparator<Object> adaptDependencyComparator(Map<String, Object> matchingBeans) {
/* 1233 */     Comparator<Object> comparator = getDependencyComparator();
/* 1234 */     if ((comparator instanceof OrderComparator)) {
/* 1235 */       return ((OrderComparator)comparator).withSourceProvider(
/* 1236 */         createFactoryAwareOrderSourceProvider(matchingBeans));
/*      */     }
/*      */     
/* 1239 */     return comparator;
/*      */   }
/*      */   
/*      */   private FactoryAwareOrderSourceProvider createFactoryAwareOrderSourceProvider(Map<String, Object> beans)
/*      */   {
/* 1244 */     IdentityHashMap<Object, String> instancesToBeanNames = new IdentityHashMap();
/* 1245 */     for (Map.Entry<String, Object> entry : beans.entrySet()) {
/* 1246 */       instancesToBeanNames.put(entry.getValue(), entry.getKey());
/*      */     }
/* 1248 */     return new FactoryAwareOrderSourceProvider(instancesToBeanNames);
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
/*      */   protected Map<String, Object> findAutowireCandidates(String beanName, Class<?> requiredType, DependencyDescriptor descriptor)
/*      */   {
/* 1267 */     String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this, requiredType, true, descriptor
/* 1268 */       .isEager());
/* 1269 */     Map<String, Object> result = new LinkedHashMap(candidateNames.length);
/* 1270 */     for (Object localObject1 = this.resolvableDependencies.keySet().iterator(); ((Iterator)localObject1).hasNext();) { autowiringType = (Class)((Iterator)localObject1).next();
/* 1271 */       if (autowiringType.isAssignableFrom(requiredType)) {
/* 1272 */         autowiringValue = this.resolvableDependencies.get(autowiringType);
/* 1273 */         autowiringValue = AutowireUtils.resolveAutowiringValue(autowiringValue, requiredType);
/* 1274 */         if (requiredType.isInstance(autowiringValue)) {
/* 1275 */           result.put(ObjectUtils.identityToString(autowiringValue), autowiringValue);
/* 1276 */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1280 */     localObject1 = candidateNames;Class<?> autowiringType = localObject1.length; String candidate; for (Object autowiringValue = 0; autowiringValue < autowiringType; autowiringValue++) { candidate = localObject1[autowiringValue];
/* 1281 */       if ((!isSelfReference(beanName, candidate)) && (isAutowireCandidate(candidate, descriptor))) {
/* 1282 */         addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */       }
/*      */     }
/* 1285 */     if ((result.isEmpty()) && (!indicatesMultipleBeans(requiredType)))
/*      */     {
/* 1287 */       DependencyDescriptor fallbackDescriptor = descriptor.forFallbackMatch();
/* 1288 */       autowiringType = candidateNames;autowiringValue = autowiringType.length; for (candidate = 0; candidate < autowiringValue; candidate++) { String candidate = autowiringType[candidate];
/* 1289 */         if ((!isSelfReference(beanName, candidate)) && (isAutowireCandidate(candidate, fallbackDescriptor))) {
/* 1290 */           addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */         }
/*      */       }
/* 1293 */       if (result.isEmpty())
/*      */       {
/*      */ 
/* 1296 */         autowiringType = candidateNames;autowiringValue = autowiringType.length; for (candidate = 0; candidate < autowiringValue; candidate++) { String candidate = autowiringType[candidate];
/* 1297 */           if ((isSelfReference(beanName, candidate)) && ((!(descriptor instanceof MultiElementDescriptor)) || 
/* 1298 */             (!beanName.equals(candidate))) && 
/* 1299 */             (isAutowireCandidate(candidate, fallbackDescriptor))) {
/* 1300 */             addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1305 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void addCandidateEntry(Map<String, Object> candidates, String candidateName, DependencyDescriptor descriptor, Class<?> requiredType)
/*      */   {
/* 1315 */     if (((descriptor instanceof MultiElementDescriptor)) || (containsSingleton(candidateName))) {
/* 1316 */       candidates.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
/*      */     }
/*      */     else {
/* 1319 */       candidates.put(candidateName, getType(candidateName));
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
/*      */   protected String determineAutowireCandidate(Map<String, Object> candidates, DependencyDescriptor descriptor)
/*      */   {
/* 1332 */     Class<?> requiredType = descriptor.getDependencyType();
/* 1333 */     String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
/* 1334 */     if (primaryCandidate != null) {
/* 1335 */       return primaryCandidate;
/*      */     }
/* 1337 */     String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
/* 1338 */     if (priorityCandidate != null) {
/* 1339 */       return priorityCandidate;
/*      */     }
/*      */     
/* 1342 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1343 */       String candidateName = (String)entry.getKey();
/* 1344 */       Object beanInstance = entry.getValue();
/* 1345 */       if (((beanInstance != null) && (this.resolvableDependencies.containsValue(beanInstance))) || 
/* 1346 */         (matchesBeanName(candidateName, descriptor.getDependencyName()))) {
/* 1347 */         return candidateName;
/*      */       }
/*      */     }
/* 1350 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String determinePrimaryCandidate(Map<String, Object> candidates, Class<?> requiredType)
/*      */   {
/* 1362 */     String primaryBeanName = null;
/* 1363 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1364 */       String candidateBeanName = (String)entry.getKey();
/* 1365 */       Object beanInstance = entry.getValue();
/* 1366 */       if (isPrimary(candidateBeanName, beanInstance)) {
/* 1367 */         if (primaryBeanName != null) {
/* 1368 */           boolean candidateLocal = containsBeanDefinition(candidateBeanName);
/* 1369 */           boolean primaryLocal = containsBeanDefinition(primaryBeanName);
/* 1370 */           if ((candidateLocal) && (primaryLocal))
/*      */           {
/* 1372 */             throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(), "more than one 'primary' bean found among candidates: " + candidates.keySet());
/*      */           }
/* 1374 */           if (candidateLocal) {
/* 1375 */             primaryBeanName = candidateBeanName;
/*      */           }
/*      */         }
/*      */         else {
/* 1379 */           primaryBeanName = candidateBeanName;
/*      */         }
/*      */       }
/*      */     }
/* 1383 */     return primaryBeanName;
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
/*      */   protected String determineHighestPriorityCandidate(Map<String, Object> candidates, Class<?> requiredType)
/*      */   {
/* 1399 */     String highestPriorityBeanName = null;
/* 1400 */     Integer highestPriority = null;
/* 1401 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1402 */       String candidateBeanName = (String)entry.getKey();
/* 1403 */       Object beanInstance = entry.getValue();
/* 1404 */       Integer candidatePriority = getPriority(beanInstance);
/* 1405 */       if (candidatePriority != null) {
/* 1406 */         if (highestPriorityBeanName != null) {
/* 1407 */           if (candidatePriority.equals(highestPriority))
/*      */           {
/*      */ 
/* 1410 */             throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(), "Multiple beans found with the same priority ('" + highestPriority + "') among candidates: " + candidates.keySet());
/*      */           }
/* 1412 */           if (candidatePriority.intValue() < highestPriority.intValue()) {
/* 1413 */             highestPriorityBeanName = candidateBeanName;
/* 1414 */             highestPriority = candidatePriority;
/*      */           }
/*      */         }
/*      */         else {
/* 1418 */           highestPriorityBeanName = candidateBeanName;
/* 1419 */           highestPriority = candidatePriority;
/*      */         }
/*      */       }
/*      */     }
/* 1423 */     return highestPriorityBeanName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isPrimary(String beanName, Object beanInstance)
/*      */   {
/* 1434 */     if (containsBeanDefinition(beanName)) {
/* 1435 */       return getMergedLocalBeanDefinition(beanName).isPrimary();
/*      */     }
/* 1437 */     BeanFactory parent = getParentBeanFactory();
/* 1438 */     return ((parent instanceof DefaultListableBeanFactory)) && 
/* 1439 */       (((DefaultListableBeanFactory)parent).isPrimary(beanName, beanInstance));
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
/*      */   protected Integer getPriority(Object beanInstance)
/*      */   {
/* 1455 */     Comparator<Object> comparator = getDependencyComparator();
/* 1456 */     if ((comparator instanceof OrderComparator)) {
/* 1457 */       return ((OrderComparator)comparator).getPriority(beanInstance);
/*      */     }
/* 1459 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean matchesBeanName(String beanName, String candidateName)
/*      */   {
/* 1467 */     return (candidateName != null) && (
/* 1468 */       (candidateName.equals(beanName)) || (ObjectUtils.containsElement(getAliases(beanName), candidateName)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isSelfReference(String beanName, String candidateName)
/*      */   {
/* 1477 */     return (beanName != null) && (candidateName != null) && (
/* 1478 */       (beanName.equals(candidateName)) || ((containsBeanDefinition(candidateName)) && 
/* 1479 */       (beanName.equals(getMergedLocalBeanDefinition(candidateName).getFactoryBeanName()))));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void raiseNoMatchingBeanFound(Class<?> type, ResolvableType resolvableType, DependencyDescriptor descriptor)
/*      */     throws BeansException
/*      */   {
/* 1489 */     checkBeanNotOfRequiredType(type, descriptor);
/*      */     
/*      */ 
/*      */ 
/* 1493 */     throw new NoSuchBeanDefinitionException(resolvableType, "expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: " + ObjectUtils.nullSafeToString(descriptor.getAnnotations()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkBeanNotOfRequiredType(Class<?> type, DependencyDescriptor descriptor)
/*      */   {
/* 1501 */     for (String beanName : this.beanDefinitionNames) {
/* 1502 */       RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/* 1503 */       Class<?> targetType = mbd.getTargetType();
/* 1504 */       if ((targetType != null) && (type.isAssignableFrom(targetType)) && 
/* 1505 */         (isAutowireCandidate(beanName, mbd, descriptor, getAutowireCandidateResolver())))
/*      */       {
/* 1507 */         Object beanInstance = getSingleton(beanName, false);
/* 1508 */         Class<?> beanType = beanInstance != null ? beanInstance.getClass() : predictBeanType(beanName, mbd, new Class[0]);
/* 1509 */         if (!type.isAssignableFrom(beanType)) {
/* 1510 */           throw new BeanNotOfRequiredTypeException(beanName, type, beanType);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1515 */     BeanFactory parent = getParentBeanFactory();
/* 1516 */     if ((parent instanceof DefaultListableBeanFactory)) {
/* 1517 */       ((DefaultListableBeanFactory)parent).checkBeanNotOfRequiredType(type, descriptor);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1524 */     StringBuilder sb = new StringBuilder(ObjectUtils.identityToString(this));
/* 1525 */     sb.append(": defining beans [");
/* 1526 */     sb.append(StringUtils.collectionToCommaDelimitedString(this.beanDefinitionNames));
/* 1527 */     sb.append("]; ");
/* 1528 */     BeanFactory parent = getParentBeanFactory();
/* 1529 */     if (parent == null) {
/* 1530 */       sb.append("root of factory hierarchy");
/*      */     }
/*      */     else {
/* 1533 */       sb.append("parent: ").append(ObjectUtils.identityToString(parent));
/*      */     }
/* 1535 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream ois)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1544 */     throw new NotSerializableException("DefaultListableBeanFactory itself is not deserializable - just a SerializedBeanFactoryReference is");
/*      */   }
/*      */   
/*      */   protected Object writeReplace() throws ObjectStreamException
/*      */   {
/* 1549 */     if (this.serializationId != null) {
/* 1550 */       return new SerializedBeanFactoryReference(this.serializationId);
/*      */     }
/*      */     
/* 1553 */     throw new NotSerializableException("DefaultListableBeanFactory has no serialization id");
/*      */   }
/*      */   
/*      */ 
/*      */   public DefaultListableBeanFactory() {}
/*      */   
/*      */ 
/*      */   private static class SerializedBeanFactoryReference
/*      */     implements Serializable
/*      */   {
/*      */     private final String id;
/*      */     
/*      */     public SerializedBeanFactoryReference(String id)
/*      */     {
/* 1567 */       this.id = id;
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 1571 */       Reference<?> ref = (Reference)DefaultListableBeanFactory.serializableFactories.get(this.id);
/* 1572 */       if (ref != null) {
/* 1573 */         Object result = ref.get();
/* 1574 */         if (result != null) {
/* 1575 */           return result;
/*      */         }
/*      */       }
/*      */       
/* 1579 */       return new DefaultListableBeanFactory();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   @UsesJava8
/*      */   private class OptionalDependencyFactory
/*      */   {
/*      */     private OptionalDependencyFactory() {}
/*      */     
/*      */     public Object createOptionalDependency(DependencyDescriptor descriptor, String beanName, final Object... args)
/*      */     {
/* 1591 */       DependencyDescriptor descriptorToUse = new DefaultListableBeanFactory.NestedDependencyDescriptor(descriptor)
/*      */       {
/*      */         public boolean isRequired() {
/* 1594 */           return false;
/*      */         }
/*      */         
/*      */         public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
/* 1598 */           return !ObjectUtils.isEmpty(args) ? beanFactory.getBean(beanName, new Object[] { requiredType, args }) : 
/* 1599 */             super.resolveCandidate(beanName, requiredType, beanFactory);
/*      */         }
/* 1601 */       };
/* 1602 */       return Optional.ofNullable(DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, beanName, null, null));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class DependencyObjectProvider
/*      */     implements ObjectProvider<Object>, Serializable
/*      */   {
/*      */     private final DependencyDescriptor descriptor;
/*      */     
/*      */     private final boolean optional;
/*      */     
/*      */     private final String beanName;
/*      */     
/*      */ 
/*      */     public DependencyObjectProvider(DependencyDescriptor descriptor, String beanName)
/*      */     {
/* 1619 */       this.descriptor = new DefaultListableBeanFactory.NestedDependencyDescriptor(descriptor);
/* 1620 */       this.optional = (this.descriptor.getDependencyType() == DefaultListableBeanFactory.javaUtilOptionalClass);
/* 1621 */       this.beanName = beanName;
/*      */     }
/*      */     
/*      */     public Object getObject() throws BeansException
/*      */     {
/* 1626 */       if (this.optional) {
/* 1627 */         return new DefaultListableBeanFactory.OptionalDependencyFactory(DefaultListableBeanFactory.this, null).createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1630 */       return DefaultListableBeanFactory.this.doResolveDependency(this.descriptor, this.beanName, null, null);
/*      */     }
/*      */     
/*      */     public Object getObject(final Object... args)
/*      */       throws BeansException
/*      */     {
/* 1636 */       if (this.optional) {
/* 1637 */         return new DefaultListableBeanFactory.OptionalDependencyFactory(DefaultListableBeanFactory.this, null).createOptionalDependency(this.descriptor, this.beanName, args);
/*      */       }
/*      */       
/* 1640 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */       {
/*      */         public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
/* 1643 */           return ((AbstractBeanFactory)beanFactory).getBean(beanName, requiredType, args);
/*      */         }
/* 1645 */       };
/* 1646 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
/*      */     }
/*      */     
/*      */     public Object getIfAvailable()
/*      */       throws BeansException
/*      */     {
/* 1652 */       if (this.optional) {
/* 1653 */         return new DefaultListableBeanFactory.OptionalDependencyFactory(DefaultListableBeanFactory.this, null).createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1656 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */       {
/*      */         public boolean isRequired() {
/* 1659 */           return false;
/*      */         }
/* 1661 */       };
/* 1662 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
/*      */     }
/*      */     
/*      */     public Object getIfUnique()
/*      */       throws BeansException
/*      */     {
/* 1668 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */       {
/*      */         public boolean isRequired() {
/* 1671 */           return false;
/*      */         }
/*      */         
/*      */         public Object resolveNotUnique(Class<?> type, Map<String, Object> matchingBeans) {
/* 1675 */           return null;
/*      */         }
/*      */       };
/* 1678 */       if (this.optional) {
/* 1679 */         return new DefaultListableBeanFactory.OptionalDependencyFactory(DefaultListableBeanFactory.this, null).createOptionalDependency(descriptorToUse, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1682 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private class Jsr330DependencyProvider
/*      */     extends DefaultListableBeanFactory.DependencyObjectProvider
/*      */     implements Provider<Object>
/*      */   {
/*      */     public Jsr330DependencyProvider(DependencyDescriptor descriptor, String beanName)
/*      */     {
/* 1694 */       super(descriptor, beanName);
/*      */     }
/*      */     
/*      */     public Object get() throws BeansException
/*      */     {
/* 1699 */       return getObject();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class Jsr330ProviderFactory
/*      */   {
/*      */     private Jsr330ProviderFactory() {}
/*      */     
/*      */     public Object createDependencyProvider(DependencyDescriptor descriptor, String beanName)
/*      */     {
/* 1710 */       return new DefaultListableBeanFactory.Jsr330DependencyProvider(DefaultListableBeanFactory.this, descriptor, beanName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private class FactoryAwareOrderSourceProvider
/*      */     implements OrderComparator.OrderSourceProvider
/*      */   {
/*      */     private final Map<Object, String> instancesToBeanNames;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public FactoryAwareOrderSourceProvider()
/*      */     {
/* 1727 */       this.instancesToBeanNames = instancesToBeanNames;
/*      */     }
/*      */     
/*      */     public Object getOrderSource(Object obj)
/*      */     {
/* 1732 */       RootBeanDefinition beanDefinition = getRootBeanDefinition((String)this.instancesToBeanNames.get(obj));
/* 1733 */       if (beanDefinition == null) {
/* 1734 */         return null;
/*      */       }
/* 1736 */       List<Object> sources = new ArrayList(2);
/* 1737 */       Method factoryMethod = beanDefinition.getResolvedFactoryMethod();
/* 1738 */       if (factoryMethod != null) {
/* 1739 */         sources.add(factoryMethod);
/*      */       }
/* 1741 */       Class<?> targetType = beanDefinition.getTargetType();
/* 1742 */       if ((targetType != null) && (targetType != obj.getClass())) {
/* 1743 */         sources.add(targetType);
/*      */       }
/* 1745 */       return sources.toArray(new Object[sources.size()]);
/*      */     }
/*      */     
/*      */     private RootBeanDefinition getRootBeanDefinition(String beanName) {
/* 1749 */       if ((beanName != null) && (DefaultListableBeanFactory.this.containsBeanDefinition(beanName))) {
/* 1750 */         BeanDefinition bd = DefaultListableBeanFactory.this.getMergedBeanDefinition(beanName);
/* 1751 */         if ((bd instanceof RootBeanDefinition)) {
/* 1752 */           return (RootBeanDefinition)bd;
/*      */         }
/*      */       }
/* 1755 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class NestedDependencyDescriptor extends DependencyDescriptor
/*      */   {
/*      */     public NestedDependencyDescriptor(DependencyDescriptor original)
/*      */     {
/* 1763 */       super();
/* 1764 */       increaseNestingLevel();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class MultiElementDescriptor extends DefaultListableBeanFactory.NestedDependencyDescriptor
/*      */   {
/*      */     public MultiElementDescriptor(DependencyDescriptor original)
/*      */     {
/* 1772 */       super();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\DefaultListableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */