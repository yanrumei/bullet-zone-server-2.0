/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.beans.PropertyEditor;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeanWrapper;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.PropertyEditorRegistrar;
/*      */ import org.springframework.beans.PropertyEditorRegistry;
/*      */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*      */ import org.springframework.beans.SimpleTypeConverter;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.TypeMismatchException;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanIsAbstractException;
/*      */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*      */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.SmartFactoryBean;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*      */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.Scope;
/*      */ import org.springframework.core.DecoratingClassLoader;
/*      */ import org.springframework.core.NamedThreadLocal;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.convert.ConversionService;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.util.StringValueResolver;
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
/*      */ public abstract class AbstractBeanFactory
/*      */   extends FactoryBeanRegistrySupport
/*      */   implements ConfigurableBeanFactory
/*      */ {
/*      */   private BeanFactory parentBeanFactory;
/*  119 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*      */   
/*      */ 
/*      */   private ClassLoader tempClassLoader;
/*      */   
/*      */ 
/*  125 */   private boolean cacheBeanMetadata = true;
/*      */   
/*      */ 
/*      */   private BeanExpressionResolver beanExpressionResolver;
/*      */   
/*      */ 
/*      */   private ConversionService conversionService;
/*      */   
/*      */ 
/*  134 */   private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet(4);
/*      */   
/*      */ 
/*      */ 
/*  138 */   private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap(4);
/*      */   
/*      */ 
/*      */ 
/*      */   private TypeConverter typeConverter;
/*      */   
/*      */ 
/*  145 */   private final List<StringValueResolver> embeddedValueResolvers = new LinkedList();
/*      */   
/*      */ 
/*  148 */   private final List<BeanPostProcessor> beanPostProcessors = new ArrayList();
/*      */   
/*      */ 
/*      */   private boolean hasInstantiationAwareBeanPostProcessors;
/*      */   
/*      */ 
/*      */   private boolean hasDestructionAwareBeanPostProcessors;
/*      */   
/*      */ 
/*  157 */   private final Map<String, Scope> scopes = new LinkedHashMap(8);
/*      */   
/*      */ 
/*      */   private SecurityContextProvider securityContextProvider;
/*      */   
/*      */ 
/*  163 */   private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap(256);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  168 */   private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap(256));
/*      */   
/*      */ 
/*  171 */   private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal("Prototype beans currently in creation");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AbstractBeanFactory() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AbstractBeanFactory(BeanFactory parentBeanFactory)
/*      */   {
/*  187 */     this.parentBeanFactory = parentBeanFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getBean(String name)
/*      */     throws BeansException
/*      */   {
/*  197 */     return doGetBean(name, null, null, false);
/*      */   }
/*      */   
/*      */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException
/*      */   {
/*  202 */     return (T)doGetBean(name, requiredType, null, false);
/*      */   }
/*      */   
/*      */   public Object getBean(String name, Object... args) throws BeansException
/*      */   {
/*  207 */     return doGetBean(name, null, args, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T getBean(String name, Class<T> requiredType, Object... args)
/*      */     throws BeansException
/*      */   {
/*  220 */     return (T)doGetBean(name, requiredType, args, false);
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
/*      */   protected <T> T doGetBean(String name, Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
/*      */     throws BeansException
/*      */   {
/*  239 */     final String beanName = transformedBeanName(name);
/*      */     
/*      */ 
/*      */ 
/*  243 */     Object sharedInstance = getSingleton(beanName);
/*  244 */     Object bean; if ((sharedInstance != null) && (args == null)) {
/*  245 */       if (this.logger.isDebugEnabled()) {
/*  246 */         if (isSingletonCurrentlyInCreation(beanName)) {
/*  247 */           this.logger.debug("Returning eagerly cached instance of singleton bean '" + beanName + "' that is not fully initialized yet - a consequence of a circular reference");
/*      */         }
/*      */         else
/*      */         {
/*  251 */           this.logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
/*      */         }
/*      */       }
/*  254 */       bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  260 */       if (isPrototypeCurrentlyInCreation(beanName)) {
/*  261 */         throw new BeanCurrentlyInCreationException(beanName);
/*      */       }
/*      */       
/*      */ 
/*  265 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  266 */       if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */       {
/*  268 */         String nameToLookup = originalBeanName(name);
/*  269 */         if (args != null)
/*      */         {
/*  271 */           return (T)parentBeanFactory.getBean(nameToLookup, args);
/*      */         }
/*      */         
/*      */ 
/*  275 */         return (T)parentBeanFactory.getBean(nameToLookup, requiredType);
/*      */       }
/*      */       
/*      */ 
/*  279 */       if (!typeCheckOnly) {
/*  280 */         markBeanAsCreated(beanName);
/*      */       }
/*      */       try
/*      */       {
/*  284 */         final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  285 */         checkMergedBeanDefinition(mbd, beanName, args);
/*      */         
/*      */ 
/*  288 */         String[] dependsOn = mbd.getDependsOn();
/*  289 */         if (dependsOn != null) {
/*  290 */           for (String dep : dependsOn) {
/*  291 */             if (isDependent(beanName, dep)) {
/*  292 */               throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
/*      */             }
/*      */             
/*  295 */             registerDependentBean(dep, beanName);
/*  296 */             getBean(dep);
/*      */           }
/*      */         }
/*      */         
/*      */         Object bean;
/*  301 */         if (mbd.isSingleton()) {
/*  302 */           sharedInstance = getSingleton(beanName, new ObjectFactory()
/*      */           {
/*      */             public Object getObject() throws BeansException {
/*      */               try {
/*  306 */                 return AbstractBeanFactory.this.createBean(beanName, mbd, args);
/*      */ 
/*      */               }
/*      */               catch (BeansException ex)
/*      */               {
/*      */ 
/*  312 */                 AbstractBeanFactory.this.destroySingleton(beanName);
/*  313 */                 throw ex;
/*      */               }
/*      */             }
/*  316 */           });
/*  317 */           bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
/*      */         } else {
/*      */           Object bean;
/*  320 */           if (mbd.isPrototype())
/*      */           {
/*  322 */             Object prototypeInstance = null;
/*      */             try {
/*  324 */               beforePrototypeCreation(beanName);
/*  325 */               prototypeInstance = createBean(beanName, mbd, args);
/*      */             }
/*      */             finally {
/*  328 */               afterPrototypeCreation(beanName);
/*      */             }
/*  330 */             bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
/*      */           }
/*      */           else
/*      */           {
/*  334 */             String scopeName = mbd.getScope();
/*  335 */             Scope scope = (Scope)this.scopes.get(scopeName);
/*  336 */             if (scope == null) {
/*  337 */               throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
/*      */             }
/*      */             try {
/*  340 */               Object scopedInstance = scope.get(beanName, new ObjectFactory()
/*      */               {
/*      */                 public Object getObject() throws BeansException {
/*  343 */                   AbstractBeanFactory.this.beforePrototypeCreation(beanName);
/*      */                   try {
/*  345 */                     return AbstractBeanFactory.this.createBean(beanName, mbd, args);
/*      */                   }
/*      */                   finally {
/*  348 */                     AbstractBeanFactory.this.afterPrototypeCreation(beanName);
/*      */                   }
/*      */                 }
/*  351 */               });
/*  352 */               bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
/*      */             } catch (IllegalStateException ex) {
/*      */               Object bean;
/*  355 */               throw new BeanCreationException(beanName, "Scope '" + scopeName + "' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton", ex);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (BeansException ex)
/*      */       {
/*      */         Object bean;
/*  363 */         cleanupAfterBeanCreationFailure(beanName);
/*  364 */         throw ex;
/*      */       }
/*      */     }
/*      */     
/*      */     Object bean;
/*  369 */     if ((requiredType != null) && (bean != null) && (!requiredType.isInstance(bean))) {
/*      */       try {
/*  371 */         return (T)getTypeConverter().convertIfNecessary(bean, requiredType);
/*      */       }
/*      */       catch (TypeMismatchException ex) {
/*  374 */         if (this.logger.isDebugEnabled()) {
/*  375 */           this.logger.debug("Failed to convert bean '" + name + "' to required type '" + 
/*  376 */             ClassUtils.getQualifiedName(requiredType) + "'", ex);
/*      */         }
/*  378 */         throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*      */       }
/*      */     }
/*  381 */     return (T)bean;
/*      */   }
/*      */   
/*      */   public boolean containsBean(String name)
/*      */   {
/*  386 */     String beanName = transformedBeanName(name);
/*  387 */     if ((containsSingleton(beanName)) || (containsBeanDefinition(beanName))) {
/*  388 */       return (!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(name));
/*      */     }
/*      */     
/*  391 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  392 */     return (parentBeanFactory != null) && (parentBeanFactory.containsBean(originalBeanName(name)));
/*      */   }
/*      */   
/*      */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException
/*      */   {
/*  397 */     String beanName = transformedBeanName(name);
/*      */     
/*  399 */     Object beanInstance = getSingleton(beanName, false);
/*  400 */     if (beanInstance != null) {
/*  401 */       if ((beanInstance instanceof FactoryBean)) {
/*  402 */         return (BeanFactoryUtils.isFactoryDereference(name)) || (((FactoryBean)beanInstance).isSingleton());
/*      */       }
/*      */       
/*  405 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*      */     }
/*      */     
/*  408 */     if (containsSingleton(beanName)) {
/*  409 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  413 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  414 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  416 */       return parentBeanFactory.isSingleton(originalBeanName(name));
/*      */     }
/*      */     
/*  419 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */     
/*      */ 
/*  422 */     if (mbd.isSingleton()) {
/*  423 */       if (isFactoryBean(beanName, mbd)) {
/*  424 */         if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  425 */           return true;
/*      */         }
/*  427 */         FactoryBean<?> factoryBean = (FactoryBean)getBean("&" + beanName);
/*  428 */         return factoryBean.isSingleton();
/*      */       }
/*      */       
/*  431 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*      */     }
/*      */     
/*      */ 
/*  435 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isPrototype(String name)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  441 */     String beanName = transformedBeanName(name);
/*      */     
/*  443 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  444 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  446 */       return parentBeanFactory.isPrototype(originalBeanName(name));
/*      */     }
/*      */     
/*  449 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  450 */     if (mbd.isPrototype())
/*      */     {
/*  452 */       return (!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(beanName, mbd));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  457 */     if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  458 */       return false;
/*      */     }
/*  460 */     if (isFactoryBean(beanName, mbd)) {
/*  461 */       final FactoryBean<?> fb = (FactoryBean)getBean("&" + beanName);
/*  462 */       if (System.getSecurityManager() != null) {
/*  463 */         ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Boolean run() {
/*  466 */             return Boolean.valueOf((((fb instanceof SmartFactoryBean)) && (((SmartFactoryBean)fb).isPrototype())) || 
/*  467 */               (!fb.isSingleton()));
/*      */           }
/*  469 */         }, getAccessControlContext())).booleanValue();
/*      */       }
/*      */       
/*  472 */       return (((fb instanceof SmartFactoryBean)) && (((SmartFactoryBean)fb).isPrototype())) || 
/*  473 */         (!fb.isSingleton());
/*      */     }
/*      */     
/*      */ 
/*  477 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isTypeMatch(String name, ResolvableType typeToMatch)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  483 */     String beanName = transformedBeanName(name);
/*      */     
/*      */ 
/*  486 */     Object beanInstance = getSingleton(beanName, false);
/*  487 */     if (beanInstance != null) {
/*  488 */       if ((beanInstance instanceof FactoryBean)) {
/*  489 */         if (!BeanFactoryUtils.isFactoryDereference(name)) {
/*  490 */           Class<?> type = getTypeForFactoryBean((FactoryBean)beanInstance);
/*  491 */           return (type != null) && (typeToMatch.isAssignableFrom(type));
/*      */         }
/*      */         
/*  494 */         return typeToMatch.isInstance(beanInstance);
/*      */       }
/*      */       
/*  497 */       if (!BeanFactoryUtils.isFactoryDereference(name)) {
/*  498 */         if (typeToMatch.isInstance(beanInstance))
/*      */         {
/*  500 */           return true;
/*      */         }
/*  502 */         if ((typeToMatch.hasGenerics()) && (containsBeanDefinition(beanName)))
/*      */         {
/*  504 */           RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  505 */           Class<?> targetType = mbd.getTargetType();
/*  506 */           if ((targetType != null) && (targetType != ClassUtils.getUserClass(beanInstance)) && 
/*  507 */             (typeToMatch.isAssignableFrom(targetType)))
/*      */           {
/*  509 */             Class<?> classToMatch = typeToMatch.resolve();
/*  510 */             return (classToMatch == null) || (classToMatch.isInstance(beanInstance));
/*      */           }
/*      */         }
/*      */       }
/*  514 */       return false;
/*      */     }
/*  516 */     if ((containsSingleton(beanName)) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  518 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  522 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  523 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  525 */       return parentBeanFactory.isTypeMatch(originalBeanName(name), typeToMatch);
/*      */     }
/*      */     
/*      */ 
/*  529 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */     
/*  531 */     Class<?> classToMatch = typeToMatch.resolve();
/*  532 */     if (classToMatch == null) {
/*  533 */       classToMatch = FactoryBean.class;
/*      */     }
/*  535 */     Class<?>[] typesToMatch = { FactoryBean.class, FactoryBean.class == classToMatch ? new Class[] { classToMatch } : classToMatch };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  540 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  541 */     if ((dbd != null) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*  542 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  543 */       Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, typesToMatch);
/*  544 */       if ((targetClass != null) && (!FactoryBean.class.isAssignableFrom(targetClass))) {
/*  545 */         return typeToMatch.isAssignableFrom(targetClass);
/*      */       }
/*      */     }
/*      */     
/*  549 */     Class<?> beanType = predictBeanType(beanName, mbd, typesToMatch);
/*  550 */     if (beanType == null) {
/*  551 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  555 */     if (FactoryBean.class.isAssignableFrom(beanType)) {
/*  556 */       if (!BeanFactoryUtils.isFactoryDereference(name))
/*      */       {
/*  558 */         beanType = getTypeForFactoryBean(beanName, mbd);
/*  559 */         if (beanType == null) {
/*  560 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*  564 */     else if (BeanFactoryUtils.isFactoryDereference(name))
/*      */     {
/*      */ 
/*      */ 
/*  568 */       beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/*  569 */       if ((beanType == null) || (!FactoryBean.class.isAssignableFrom(beanType))) {
/*  570 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  574 */     ResolvableType resolvableType = mbd.targetType;
/*  575 */     if (resolvableType == null) {
/*  576 */       resolvableType = mbd.factoryMethodReturnType;
/*      */     }
/*  578 */     if ((resolvableType != null) && (resolvableType.resolve() == beanType)) {
/*  579 */       return typeToMatch.isAssignableFrom(resolvableType);
/*      */     }
/*  581 */     return typeToMatch.isAssignableFrom(beanType);
/*      */   }
/*      */   
/*      */   public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException
/*      */   {
/*  586 */     return isTypeMatch(name, ResolvableType.forRawClass(typeToMatch));
/*      */   }
/*      */   
/*      */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException
/*      */   {
/*  591 */     String beanName = transformedBeanName(name);
/*      */     
/*      */ 
/*  594 */     Object beanInstance = getSingleton(beanName, false);
/*  595 */     if (beanInstance != null) {
/*  596 */       if (((beanInstance instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*  597 */         return getTypeForFactoryBean((FactoryBean)beanInstance);
/*      */       }
/*      */       
/*  600 */       return beanInstance.getClass();
/*      */     }
/*      */     
/*  603 */     if ((containsSingleton(beanName)) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  605 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  609 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  610 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  612 */       return parentBeanFactory.getType(originalBeanName(name));
/*      */     }
/*      */     
/*  615 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */     
/*      */ 
/*      */ 
/*  619 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  620 */     if ((dbd != null) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*  621 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  622 */       Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, new Class[0]);
/*  623 */       if ((targetClass != null) && (!FactoryBean.class.isAssignableFrom(targetClass))) {
/*  624 */         return targetClass;
/*      */       }
/*      */     }
/*      */     
/*  628 */     Class<?> beanClass = predictBeanType(beanName, mbd, new Class[0]);
/*      */     
/*      */ 
/*  631 */     if ((beanClass != null) && (FactoryBean.class.isAssignableFrom(beanClass))) {
/*  632 */       if (!BeanFactoryUtils.isFactoryDereference(name))
/*      */       {
/*  634 */         return getTypeForFactoryBean(beanName, mbd);
/*      */       }
/*      */       
/*  637 */       return beanClass;
/*      */     }
/*      */     
/*      */ 
/*  641 */     return !BeanFactoryUtils.isFactoryDereference(name) ? beanClass : null;
/*      */   }
/*      */   
/*      */ 
/*      */   public String[] getAliases(String name)
/*      */   {
/*  647 */     String beanName = transformedBeanName(name);
/*  648 */     List<String> aliases = new ArrayList();
/*  649 */     boolean factoryPrefix = name.startsWith("&");
/*  650 */     String fullBeanName = beanName;
/*  651 */     if (factoryPrefix) {
/*  652 */       fullBeanName = "&" + beanName;
/*      */     }
/*  654 */     if (!fullBeanName.equals(name)) {
/*  655 */       aliases.add(fullBeanName);
/*      */     }
/*  657 */     String[] retrievedAliases = super.getAliases(beanName);
/*  658 */     for (String retrievedAlias : retrievedAliases) {
/*  659 */       String alias = (factoryPrefix ? "&" : "") + retrievedAlias;
/*  660 */       if (!alias.equals(name)) {
/*  661 */         aliases.add(alias);
/*      */       }
/*      */     }
/*  664 */     if ((!containsSingleton(beanName)) && (!containsBeanDefinition(beanName))) {
/*  665 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  666 */       if (parentBeanFactory != null) {
/*  667 */         aliases.addAll(Arrays.asList(parentBeanFactory.getAliases(fullBeanName)));
/*      */       }
/*      */     }
/*  670 */     return StringUtils.toStringArray(aliases);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BeanFactory getParentBeanFactory()
/*      */   {
/*  680 */     return this.parentBeanFactory;
/*      */   }
/*      */   
/*      */   public boolean containsLocalBean(String name)
/*      */   {
/*  685 */     String beanName = transformedBeanName(name);
/*  686 */     return ((containsSingleton(beanName)) || (containsBeanDefinition(beanName))) && (
/*  687 */       (!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(beanName)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParentBeanFactory(BeanFactory parentBeanFactory)
/*      */   {
/*  697 */     if ((this.parentBeanFactory != null) && (this.parentBeanFactory != parentBeanFactory)) {
/*  698 */       throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
/*      */     }
/*  700 */     this.parentBeanFactory = parentBeanFactory;
/*      */   }
/*      */   
/*      */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*      */   {
/*  705 */     this.beanClassLoader = (beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader());
/*      */   }
/*      */   
/*      */   public ClassLoader getBeanClassLoader()
/*      */   {
/*  710 */     return this.beanClassLoader;
/*      */   }
/*      */   
/*      */   public void setTempClassLoader(ClassLoader tempClassLoader)
/*      */   {
/*  715 */     this.tempClassLoader = tempClassLoader;
/*      */   }
/*      */   
/*      */   public ClassLoader getTempClassLoader()
/*      */   {
/*  720 */     return this.tempClassLoader;
/*      */   }
/*      */   
/*      */   public void setCacheBeanMetadata(boolean cacheBeanMetadata)
/*      */   {
/*  725 */     this.cacheBeanMetadata = cacheBeanMetadata;
/*      */   }
/*      */   
/*      */   public boolean isCacheBeanMetadata()
/*      */   {
/*  730 */     return this.cacheBeanMetadata;
/*      */   }
/*      */   
/*      */   public void setBeanExpressionResolver(BeanExpressionResolver resolver)
/*      */   {
/*  735 */     this.beanExpressionResolver = resolver;
/*      */   }
/*      */   
/*      */   public BeanExpressionResolver getBeanExpressionResolver()
/*      */   {
/*  740 */     return this.beanExpressionResolver;
/*      */   }
/*      */   
/*      */   public void setConversionService(ConversionService conversionService)
/*      */   {
/*  745 */     this.conversionService = conversionService;
/*      */   }
/*      */   
/*      */   public ConversionService getConversionService()
/*      */   {
/*  750 */     return this.conversionService;
/*      */   }
/*      */   
/*      */   public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar)
/*      */   {
/*  755 */     Assert.notNull(registrar, "PropertyEditorRegistrar must not be null");
/*  756 */     this.propertyEditorRegistrars.add(registrar);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Set<PropertyEditorRegistrar> getPropertyEditorRegistrars()
/*      */   {
/*  763 */     return this.propertyEditorRegistrars;
/*      */   }
/*      */   
/*      */   public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass)
/*      */   {
/*  768 */     Assert.notNull(requiredType, "Required type must not be null");
/*  769 */     Assert.notNull(propertyEditorClass, "PropertyEditor class must not be null");
/*  770 */     this.customEditors.put(requiredType, propertyEditorClass);
/*      */   }
/*      */   
/*      */   public void copyRegisteredEditorsTo(PropertyEditorRegistry registry)
/*      */   {
/*  775 */     registerCustomEditors(registry);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Map<Class<?>, Class<? extends PropertyEditor>> getCustomEditors()
/*      */   {
/*  782 */     return this.customEditors;
/*      */   }
/*      */   
/*      */   public void setTypeConverter(TypeConverter typeConverter)
/*      */   {
/*  787 */     this.typeConverter = typeConverter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected TypeConverter getCustomTypeConverter()
/*      */   {
/*  795 */     return this.typeConverter;
/*      */   }
/*      */   
/*      */   public TypeConverter getTypeConverter()
/*      */   {
/*  800 */     TypeConverter customConverter = getCustomTypeConverter();
/*  801 */     if (customConverter != null) {
/*  802 */       return customConverter;
/*      */     }
/*      */     
/*      */ 
/*  806 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/*  807 */     typeConverter.setConversionService(getConversionService());
/*  808 */     registerCustomEditors(typeConverter);
/*  809 */     return typeConverter;
/*      */   }
/*      */   
/*      */ 
/*      */   public void addEmbeddedValueResolver(StringValueResolver valueResolver)
/*      */   {
/*  815 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  816 */     this.embeddedValueResolvers.add(valueResolver);
/*      */   }
/*      */   
/*      */   public boolean hasEmbeddedValueResolver()
/*      */   {
/*  821 */     return !this.embeddedValueResolvers.isEmpty();
/*      */   }
/*      */   
/*      */   public String resolveEmbeddedValue(String value)
/*      */   {
/*  826 */     if (value == null) {
/*  827 */       return null;
/*      */     }
/*  829 */     String result = value;
/*  830 */     for (StringValueResolver resolver : this.embeddedValueResolvers) {
/*  831 */       result = resolver.resolveStringValue(result);
/*  832 */       if (result == null) {
/*  833 */         return null;
/*      */       }
/*      */     }
/*  836 */     return result;
/*      */   }
/*      */   
/*      */   public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor)
/*      */   {
/*  841 */     Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
/*  842 */     this.beanPostProcessors.remove(beanPostProcessor);
/*  843 */     this.beanPostProcessors.add(beanPostProcessor);
/*  844 */     if ((beanPostProcessor instanceof InstantiationAwareBeanPostProcessor)) {
/*  845 */       this.hasInstantiationAwareBeanPostProcessors = true;
/*      */     }
/*  847 */     if ((beanPostProcessor instanceof DestructionAwareBeanPostProcessor)) {
/*  848 */       this.hasDestructionAwareBeanPostProcessors = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public int getBeanPostProcessorCount()
/*      */   {
/*  854 */     return this.beanPostProcessors.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<BeanPostProcessor> getBeanPostProcessors()
/*      */   {
/*  862 */     return this.beanPostProcessors;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean hasInstantiationAwareBeanPostProcessors()
/*      */   {
/*  872 */     return this.hasInstantiationAwareBeanPostProcessors;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean hasDestructionAwareBeanPostProcessors()
/*      */   {
/*  882 */     return this.hasDestructionAwareBeanPostProcessors;
/*      */   }
/*      */   
/*      */   public void registerScope(String scopeName, Scope scope)
/*      */   {
/*  887 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  888 */     Assert.notNull(scope, "Scope must not be null");
/*  889 */     if (("singleton".equals(scopeName)) || ("prototype".equals(scopeName))) {
/*  890 */       throw new IllegalArgumentException("Cannot replace existing scopes 'singleton' and 'prototype'");
/*      */     }
/*  892 */     Scope previous = (Scope)this.scopes.put(scopeName, scope);
/*  893 */     if ((previous != null) && (previous != scope)) {
/*  894 */       if (this.logger.isInfoEnabled()) {
/*  895 */         this.logger.info("Replacing scope '" + scopeName + "' from [" + previous + "] to [" + scope + "]");
/*      */       }
/*      */       
/*      */     }
/*  899 */     else if (this.logger.isDebugEnabled()) {
/*  900 */       this.logger.debug("Registering scope '" + scopeName + "' with implementation [" + scope + "]");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String[] getRegisteredScopeNames()
/*      */   {
/*  907 */     return StringUtils.toStringArray(this.scopes.keySet());
/*      */   }
/*      */   
/*      */   public Scope getRegisteredScope(String scopeName)
/*      */   {
/*  912 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  913 */     return (Scope)this.scopes.get(scopeName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecurityContextProvider(SecurityContextProvider securityProvider)
/*      */   {
/*  922 */     this.securityContextProvider = securityProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AccessControlContext getAccessControlContext()
/*      */   {
/*  931 */     return this.securityContextProvider != null ? this.securityContextProvider
/*  932 */       .getAccessControlContext() : 
/*  933 */       AccessController.getContext();
/*      */   }
/*      */   
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*      */   {
/*  938 */     Assert.notNull(otherFactory, "BeanFactory must not be null");
/*  939 */     setBeanClassLoader(otherFactory.getBeanClassLoader());
/*  940 */     setCacheBeanMetadata(otherFactory.isCacheBeanMetadata());
/*  941 */     setBeanExpressionResolver(otherFactory.getBeanExpressionResolver());
/*  942 */     setConversionService(otherFactory.getConversionService());
/*  943 */     if ((otherFactory instanceof AbstractBeanFactory)) {
/*  944 */       AbstractBeanFactory otherAbstractFactory = (AbstractBeanFactory)otherFactory;
/*  945 */       this.propertyEditorRegistrars.addAll(otherAbstractFactory.propertyEditorRegistrars);
/*  946 */       this.customEditors.putAll(otherAbstractFactory.customEditors);
/*  947 */       this.typeConverter = otherAbstractFactory.typeConverter;
/*  948 */       this.beanPostProcessors.addAll(otherAbstractFactory.beanPostProcessors);
/*  949 */       this.hasInstantiationAwareBeanPostProcessors = ((this.hasInstantiationAwareBeanPostProcessors) || (otherAbstractFactory.hasInstantiationAwareBeanPostProcessors));
/*      */       
/*  951 */       this.hasDestructionAwareBeanPostProcessors = ((this.hasDestructionAwareBeanPostProcessors) || (otherAbstractFactory.hasDestructionAwareBeanPostProcessors));
/*      */       
/*  953 */       this.scopes.putAll(otherAbstractFactory.scopes);
/*  954 */       this.securityContextProvider = otherAbstractFactory.securityContextProvider;
/*      */     }
/*      */     else {
/*  957 */       setTypeConverter(otherFactory.getTypeConverter());
/*  958 */       String[] otherScopeNames = otherFactory.getRegisteredScopeNames();
/*  959 */       for (String scopeName : otherScopeNames) {
/*  960 */         this.scopes.put(scopeName, otherFactory.getRegisteredScope(scopeName));
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
/*      */   public BeanDefinition getMergedBeanDefinition(String name)
/*      */     throws BeansException
/*      */   {
/*  978 */     String beanName = transformedBeanName(name);
/*      */     
/*      */ 
/*  981 */     if ((!containsBeanDefinition(beanName)) && ((getParentBeanFactory() instanceof ConfigurableBeanFactory))) {
/*  982 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).getMergedBeanDefinition(beanName);
/*      */     }
/*      */     
/*  985 */     return getMergedLocalBeanDefinition(beanName);
/*      */   }
/*      */   
/*      */   public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException
/*      */   {
/*  990 */     String beanName = transformedBeanName(name);
/*      */     
/*  992 */     Object beanInstance = getSingleton(beanName, false);
/*  993 */     if (beanInstance != null) {
/*  994 */       return beanInstance instanceof FactoryBean;
/*      */     }
/*  996 */     if (containsSingleton(beanName))
/*      */     {
/*  998 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1002 */     if ((!containsBeanDefinition(beanName)) && ((getParentBeanFactory() instanceof ConfigurableBeanFactory)))
/*      */     {
/* 1004 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).isFactoryBean(name);
/*      */     }
/*      */     
/* 1007 */     return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
/*      */   }
/*      */   
/*      */   public boolean isActuallyInCreation(String beanName)
/*      */   {
/* 1012 */     return (isSingletonCurrentlyInCreation(beanName)) || (isPrototypeCurrentlyInCreation(beanName));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isPrototypeCurrentlyInCreation(String beanName)
/*      */   {
/* 1021 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1022 */     return (curVal != null) && (
/* 1023 */       (curVal.equals(beanName)) || (((curVal instanceof Set)) && (((Set)curVal).contains(beanName))));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void beforePrototypeCreation(String beanName)
/*      */   {
/* 1034 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1035 */     if (curVal == null) {
/* 1036 */       this.prototypesCurrentlyInCreation.set(beanName);
/*      */     }
/* 1038 */     else if ((curVal instanceof String)) {
/* 1039 */       Set<String> beanNameSet = new HashSet(2);
/* 1040 */       beanNameSet.add((String)curVal);
/* 1041 */       beanNameSet.add(beanName);
/* 1042 */       this.prototypesCurrentlyInCreation.set(beanNameSet);
/*      */     }
/*      */     else {
/* 1045 */       Set<String> beanNameSet = (Set)curVal;
/* 1046 */       beanNameSet.add(beanName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void afterPrototypeCreation(String beanName)
/*      */   {
/* 1058 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1059 */     if ((curVal instanceof String)) {
/* 1060 */       this.prototypesCurrentlyInCreation.remove();
/*      */     }
/* 1062 */     else if ((curVal instanceof Set)) {
/* 1063 */       Set<String> beanNameSet = (Set)curVal;
/* 1064 */       beanNameSet.remove(beanName);
/* 1065 */       if (beanNameSet.isEmpty()) {
/* 1066 */         this.prototypesCurrentlyInCreation.remove();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void destroyBean(String beanName, Object beanInstance)
/*      */   {
/* 1073 */     destroyBean(beanName, beanInstance, getMergedLocalBeanDefinition(beanName));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void destroyBean(String beanName, Object bean, RootBeanDefinition mbd)
/*      */   {
/* 1084 */     new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), getAccessControlContext()).destroy();
/*      */   }
/*      */   
/*      */   public void destroyScopedBean(String beanName)
/*      */   {
/* 1089 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/* 1090 */     if ((mbd.isSingleton()) || (mbd.isPrototype())) {
/* 1091 */       throw new IllegalArgumentException("Bean name '" + beanName + "' does not correspond to an object in a mutable scope");
/*      */     }
/*      */     
/* 1094 */     String scopeName = mbd.getScope();
/* 1095 */     Scope scope = (Scope)this.scopes.get(scopeName);
/* 1096 */     if (scope == null) {
/* 1097 */       throw new IllegalStateException("No Scope SPI registered for scope name '" + scopeName + "'");
/*      */     }
/* 1099 */     Object bean = scope.remove(beanName);
/* 1100 */     if (bean != null) {
/* 1101 */       destroyBean(beanName, bean, mbd);
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
/*      */   protected String transformedBeanName(String name)
/*      */   {
/* 1117 */     return canonicalName(BeanFactoryUtils.transformedBeanName(name));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String originalBeanName(String name)
/*      */   {
/* 1126 */     String beanName = transformedBeanName(name);
/* 1127 */     if (name.startsWith("&")) {
/* 1128 */       beanName = "&" + beanName;
/*      */     }
/* 1130 */     return beanName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initBeanWrapper(BeanWrapper bw)
/*      */   {
/* 1142 */     bw.setConversionService(getConversionService());
/* 1143 */     registerCustomEditors(bw);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void registerCustomEditors(PropertyEditorRegistry registry)
/*      */   {
/* 1155 */     PropertyEditorRegistrySupport registrySupport = (registry instanceof PropertyEditorRegistrySupport) ? (PropertyEditorRegistrySupport)registry : null;
/*      */     
/* 1157 */     if (registrySupport != null) {
/* 1158 */       registrySupport.useConfigValueEditors();
/*      */     }
/* 1160 */     if (!this.propertyEditorRegistrars.isEmpty()) {
/* 1161 */       for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars) {
/*      */         try {
/* 1163 */           registrar.registerCustomEditors(registry);
/*      */         }
/*      */         catch (BeanCreationException ex) {
/* 1166 */           Throwable rootCause = ex.getMostSpecificCause();
/* 1167 */           if ((rootCause instanceof BeanCurrentlyInCreationException)) {
/* 1168 */             BeanCreationException bce = (BeanCreationException)rootCause;
/* 1169 */             if (isCurrentlyInCreation(bce.getBeanName())) {
/* 1170 */               if (this.logger.isDebugEnabled()) {
/* 1171 */                 this.logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName() + "] failed because it tried to obtain currently created bean '" + ex
/*      */                 
/* 1173 */                   .getBeanName() + "': " + ex.getMessage());
/*      */               }
/* 1175 */               onSuppressedException(ex);
/* 1176 */               continue;
/*      */             }
/*      */           }
/* 1179 */           throw ex;
/*      */         }
/*      */       }
/*      */     }
/* 1183 */     if (!this.customEditors.isEmpty()) {
/* 1184 */       for (Map.Entry<Class<?>, Class<? extends PropertyEditor>> entry : this.customEditors.entrySet()) {
/* 1185 */         Class<?> requiredType = (Class)entry.getKey();
/* 1186 */         Class<? extends PropertyEditor> editorClass = (Class)entry.getValue();
/* 1187 */         registry.registerCustomEditor(requiredType, (PropertyEditor)BeanUtils.instantiateClass(editorClass));
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
/*      */   protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName)
/*      */     throws BeansException
/*      */   {
/* 1203 */     RootBeanDefinition mbd = (RootBeanDefinition)this.mergedBeanDefinitions.get(beanName);
/* 1204 */     if (mbd != null) {
/* 1205 */       return mbd;
/*      */     }
/* 1207 */     return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
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
/*      */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd)
/*      */     throws BeanDefinitionStoreException
/*      */   {
/* 1221 */     return getMergedBeanDefinition(beanName, bd, null);
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
/*      */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd, BeanDefinition containingBd)
/*      */     throws BeanDefinitionStoreException
/*      */   {
/* 1238 */     synchronized (this.mergedBeanDefinitions) {
/* 1239 */       RootBeanDefinition mbd = null;
/*      */       
/*      */ 
/* 1242 */       if (containingBd == null) {
/* 1243 */         mbd = (RootBeanDefinition)this.mergedBeanDefinitions.get(beanName);
/*      */       }
/*      */       
/* 1246 */       if (mbd == null) {
/* 1247 */         if (bd.getParentName() == null)
/*      */         {
/* 1249 */           if ((bd instanceof RootBeanDefinition)) {
/* 1250 */             mbd = ((RootBeanDefinition)bd).cloneBeanDefinition();
/*      */           }
/*      */           else {
/* 1253 */             mbd = new RootBeanDefinition(bd);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           try
/*      */           {
/* 1260 */             String parentBeanName = transformedBeanName(bd.getParentName());
/* 1261 */             BeanDefinition pbd; if (!beanName.equals(parentBeanName)) {
/* 1262 */               pbd = getMergedBeanDefinition(parentBeanName);
/*      */             }
/*      */             else {
/* 1265 */               BeanFactory parent = getParentBeanFactory();
/* 1266 */               BeanDefinition pbd; if ((parent instanceof ConfigurableBeanFactory)) {
/* 1267 */                 pbd = ((ConfigurableBeanFactory)parent).getMergedBeanDefinition(parentBeanName);
/*      */               }
/*      */               else {
/* 1270 */                 throw new NoSuchBeanDefinitionException(parentBeanName, "Parent name '" + parentBeanName + "' is equal to bean name '" + beanName + "': cannot be resolved without an AbstractBeanFactory parent");
/*      */               }
/*      */             }
/*      */           }
/*      */           catch (NoSuchBeanDefinitionException ex)
/*      */           {
/*      */             BeanDefinition pbd;
/*      */             
/* 1278 */             throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName, "Could not resolve parent bean definition '" + bd.getParentName() + "'", ex);
/*      */           }
/*      */           BeanDefinition pbd;
/* 1281 */           mbd = new RootBeanDefinition(pbd);
/* 1282 */           mbd.overrideFrom(bd);
/*      */         }
/*      */         
/*      */ 
/* 1286 */         if (!StringUtils.hasLength(mbd.getScope())) {
/* 1287 */           mbd.setScope("singleton");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1294 */         if ((containingBd != null) && (!containingBd.isSingleton()) && (mbd.isSingleton())) {
/* 1295 */           mbd.setScope(containingBd.getScope());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1300 */         if ((containingBd == null) && (isCacheBeanMetadata())) {
/* 1301 */           this.mergedBeanDefinitions.put(beanName, mbd);
/*      */         }
/*      */       }
/*      */       
/* 1305 */       return mbd;
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
/*      */   protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, Object[] args)
/*      */     throws BeanDefinitionStoreException
/*      */   {
/* 1320 */     if (mbd.isAbstract()) {
/* 1321 */       throw new BeanIsAbstractException(beanName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void clearMergedBeanDefinition(String beanName)
/*      */   {
/* 1331 */     this.mergedBeanDefinitions.remove(beanName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearMetadataCache()
/*      */   {
/* 1343 */     Iterator<String> mergedBeans = this.mergedBeanDefinitions.keySet().iterator();
/* 1344 */     while (mergedBeans.hasNext()) {
/* 1345 */       if (!isBeanEligibleForMetadataCaching((String)mergedBeans.next())) {
/* 1346 */         mergedBeans.remove();
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
/*      */   protected Class<?> resolveBeanClass(final RootBeanDefinition mbd, String beanName, final Class<?>... typesToMatch)
/*      */     throws CannotLoadBeanClassException
/*      */   {
/*      */     try
/*      */     {
/* 1365 */       if (mbd.hasBeanClass()) {
/* 1366 */         return mbd.getBeanClass();
/*      */       }
/* 1368 */       if (System.getSecurityManager() != null) {
/* 1369 */         (Class)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public Class<?> run() throws Exception {
/* 1372 */             return AbstractBeanFactory.this.doResolveBeanClass(mbd, typesToMatch);
/*      */           }
/* 1374 */         }, getAccessControlContext());
/*      */       }
/*      */       
/* 1377 */       return doResolveBeanClass(mbd, typesToMatch);
/*      */     }
/*      */     catch (PrivilegedActionException pae)
/*      */     {
/* 1381 */       ClassNotFoundException ex = (ClassNotFoundException)pae.getException();
/* 1382 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*      */     }
/*      */     catch (ClassNotFoundException ex) {
/* 1385 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*      */     }
/*      */     catch (LinkageError err) {
/* 1388 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
/*      */     }
/*      */   }
/*      */   
/*      */   private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch)
/*      */     throws ClassNotFoundException
/*      */   {
/* 1395 */     ClassLoader beanClassLoader = getBeanClassLoader();
/* 1396 */     ClassLoader classLoaderToUse = beanClassLoader;
/* 1397 */     if (!ObjectUtils.isEmpty(typesToMatch))
/*      */     {
/*      */ 
/* 1400 */       ClassLoader tempClassLoader = getTempClassLoader();
/* 1401 */       if (tempClassLoader != null) {
/* 1402 */         classLoaderToUse = tempClassLoader;
/* 1403 */         if ((tempClassLoader instanceof DecoratingClassLoader)) {
/* 1404 */           DecoratingClassLoader dcl = (DecoratingClassLoader)tempClassLoader;
/* 1405 */           for (Class<?> typeToMatch : typesToMatch) {
/* 1406 */             dcl.excludeClass(typeToMatch.getName());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1411 */     String className = mbd.getBeanClassName();
/* 1412 */     if (className != null) {
/* 1413 */       Object evaluated = evaluateBeanDefinitionString(className, mbd);
/* 1414 */       if (!className.equals(evaluated))
/*      */       {
/* 1416 */         if ((evaluated instanceof Class)) {
/* 1417 */           return (Class)evaluated;
/*      */         }
/* 1419 */         if ((evaluated instanceof String)) {
/* 1420 */           return ClassUtils.forName((String)evaluated, classLoaderToUse);
/*      */         }
/*      */         
/* 1423 */         throw new IllegalStateException("Invalid class name expression result: " + evaluated);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1428 */       if (classLoaderToUse != beanClassLoader) {
/* 1429 */         return ClassUtils.forName(className, classLoaderToUse);
/*      */       }
/*      */     }
/* 1432 */     return mbd.resolveBeanClass(beanClassLoader);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object evaluateBeanDefinitionString(String value, BeanDefinition beanDefinition)
/*      */   {
/* 1444 */     if (this.beanExpressionResolver == null) {
/* 1445 */       return value;
/*      */     }
/* 1447 */     Scope scope = beanDefinition != null ? getRegisteredScope(beanDefinition.getScope()) : null;
/* 1448 */     return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
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
/*      */   protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch)
/*      */   {
/* 1468 */     Class<?> targetType = mbd.getTargetType();
/* 1469 */     if (targetType != null) {
/* 1470 */       return targetType;
/*      */     }
/* 1472 */     if (mbd.getFactoryMethodName() != null) {
/* 1473 */       return null;
/*      */     }
/* 1475 */     return resolveBeanClass(mbd, beanName, typesToMatch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isFactoryBean(String beanName, RootBeanDefinition mbd)
/*      */   {
/* 1484 */     Class<?> beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/* 1485 */     return (beanType != null) && (FactoryBean.class.isAssignableFrom(beanType));
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
/*      */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd)
/*      */   {
/* 1504 */     if (!mbd.isSingleton()) {
/* 1505 */       return null;
/*      */     }
/*      */     try {
/* 1508 */       FactoryBean<?> factoryBean = (FactoryBean)doGetBean("&" + beanName, FactoryBean.class, null, true);
/* 1509 */       return getTypeForFactoryBean(factoryBean);
/*      */     }
/*      */     catch (BeanCreationException ex) {
/* 1512 */       if ((ex instanceof BeanCurrentlyInCreationException)) {
/* 1513 */         if (this.logger.isDebugEnabled()) {
/* 1514 */           this.logger.debug("Bean currently in creation on FactoryBean type check: " + ex);
/*      */         }
/*      */       }
/* 1517 */       else if (mbd.isLazyInit()) {
/* 1518 */         if (this.logger.isDebugEnabled()) {
/* 1519 */           this.logger.debug("Bean creation exception on lazy FactoryBean type check: " + ex);
/*      */         }
/*      */         
/*      */       }
/* 1523 */       else if (this.logger.isWarnEnabled()) {
/* 1524 */         this.logger.warn("Bean creation exception on non-lazy FactoryBean type check: " + ex);
/*      */       }
/*      */       
/* 1527 */       onSuppressedException(ex); }
/* 1528 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void markBeanAsCreated(String beanName)
/*      */   {
/* 1539 */     if (!this.alreadyCreated.contains(beanName)) {
/* 1540 */       synchronized (this.mergedBeanDefinitions) {
/* 1541 */         if (!this.alreadyCreated.contains(beanName))
/*      */         {
/*      */ 
/* 1544 */           clearMergedBeanDefinition(beanName);
/* 1545 */           this.alreadyCreated.add(beanName);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void cleanupAfterBeanCreationFailure(String beanName)
/*      */   {
/* 1556 */     synchronized (this.mergedBeanDefinitions) {
/* 1557 */       this.alreadyCreated.remove(beanName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isBeanEligibleForMetadataCaching(String beanName)
/*      */   {
/* 1569 */     return this.alreadyCreated.contains(beanName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName)
/*      */   {
/* 1579 */     if (!this.alreadyCreated.contains(beanName)) {
/* 1580 */       removeSingleton(beanName);
/* 1581 */       return true;
/*      */     }
/*      */     
/* 1584 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean hasBeanCreationStarted()
/*      */   {
/* 1595 */     return !this.alreadyCreated.isEmpty();
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
/*      */   protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd)
/*      */   {
/* 1611 */     if ((BeanFactoryUtils.isFactoryDereference(name)) && (!(beanInstance instanceof FactoryBean))) {
/* 1612 */       throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1618 */     if ((!(beanInstance instanceof FactoryBean)) || (BeanFactoryUtils.isFactoryDereference(name))) {
/* 1619 */       return beanInstance;
/*      */     }
/*      */     
/* 1622 */     Object object = null;
/* 1623 */     if (mbd == null) {
/* 1624 */       object = getCachedObjectForFactoryBean(beanName);
/*      */     }
/* 1626 */     if (object == null)
/*      */     {
/* 1628 */       FactoryBean<?> factory = (FactoryBean)beanInstance;
/*      */       
/* 1630 */       if ((mbd == null) && (containsBeanDefinition(beanName))) {
/* 1631 */         mbd = getMergedLocalBeanDefinition(beanName);
/*      */       }
/* 1633 */       boolean synthetic = (mbd != null) && (mbd.isSynthetic());
/* 1634 */       object = getObjectFromFactoryBean(factory, beanName, !synthetic);
/*      */     }
/* 1636 */     return object;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isBeanNameInUse(String beanName)
/*      */   {
/* 1646 */     return (isAlias(beanName)) || (containsLocalBean(beanName)) || (hasDependentBean(beanName));
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
/*      */   protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd)
/*      */   {
/* 1660 */     return (bean != null) && (
/* 1661 */       (DisposableBeanAdapter.hasDestroyMethod(bean, mbd)) || ((hasDestructionAwareBeanPostProcessors()) && 
/* 1662 */       (DisposableBeanAdapter.hasApplicableProcessors(bean, getBeanPostProcessors()))));
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
/*      */   protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd)
/*      */   {
/* 1678 */     AccessControlContext acc = System.getSecurityManager() != null ? getAccessControlContext() : null;
/* 1679 */     if ((!mbd.isPrototype()) && (requiresDestruction(bean, mbd))) {
/* 1680 */       if (mbd.isSingleton())
/*      */       {
/*      */ 
/*      */ 
/* 1684 */         registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, mbd, 
/* 1685 */           getBeanPostProcessors(), acc));
/*      */       }
/*      */       else
/*      */       {
/* 1689 */         Scope scope = (Scope)this.scopes.get(mbd.getScope());
/* 1690 */         if (scope == null) {
/* 1691 */           throw new IllegalStateException("No Scope registered for scope name '" + mbd.getScope() + "'");
/*      */         }
/* 1693 */         scope.registerDestructionCallback(beanName, new DisposableBeanAdapter(bean, beanName, mbd, 
/* 1694 */           getBeanPostProcessors(), acc));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected abstract boolean containsBeanDefinition(String paramString);
/*      */   
/*      */   protected abstract BeanDefinition getBeanDefinition(String paramString)
/*      */     throws BeansException;
/*      */   
/*      */   protected abstract Object createBean(String paramString, RootBeanDefinition paramRootBeanDefinition, Object[] paramArrayOfObject)
/*      */     throws BeanCreationException;
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\AbstractBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */