/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.scope.ScopedProxyFactoryBean;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.support.SimpleInstantiationStrategy;
/*     */ import org.springframework.cglib.core.ClassGenerator;
/*     */ import org.springframework.cglib.core.DefaultGeneratorStrategy;
/*     */ import org.springframework.cglib.core.SpringNamingPolicy;
/*     */ import org.springframework.cglib.proxy.Callback;
/*     */ import org.springframework.cglib.proxy.CallbackFilter;
/*     */ import org.springframework.cglib.proxy.Enhancer;
/*     */ import org.springframework.cglib.proxy.Factory;
/*     */ import org.springframework.cglib.proxy.MethodInterceptor;
/*     */ import org.springframework.cglib.proxy.MethodProxy;
/*     */ import org.springframework.cglib.proxy.NoOp;
/*     */ import org.springframework.cglib.transform.ClassEmitterTransformer;
/*     */ import org.springframework.cglib.transform.TransformingClassGenerator;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.SpringObjenesis;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConfigurationClassEnhancer
/*     */ {
/*  77 */   private static final Callback[] CALLBACKS = { new BeanMethodInterceptor(null), new BeanFactoryAwareMethodInterceptor(null), NoOp.INSTANCE };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */   private static final ConditionalCallbackFilter CALLBACK_FILTER = new ConditionalCallbackFilter(CALLBACKS);
/*     */   
/*     */ 
/*     */   private static final String BEAN_FACTORY_FIELD = "$$beanFactory";
/*     */   
/*  88 */   private static final Log logger = LogFactory.getLog(ConfigurationClassEnhancer.class);
/*     */   
/*  90 */   private static final SpringObjenesis objenesis = new SpringObjenesis();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> enhance(Class<?> configClass, ClassLoader classLoader)
/*     */   {
/*  99 */     if (EnhancedConfiguration.class.isAssignableFrom(configClass)) {
/* 100 */       if (logger.isDebugEnabled()) {
/* 101 */         logger.debug(String.format("Ignoring request to enhance %s as it has already been enhanced. This usually indicates that more than one ConfigurationClassPostProcessor has been registered (e.g. via <context:annotation-config>). This is harmless, but you may want check your configuration and remove one CCPP if possible", new Object[] {configClass
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 106 */           .getName() }));
/*     */       }
/* 108 */       return configClass;
/*     */     }
/* 110 */     Class<?> enhancedClass = createClass(newEnhancer(configClass, classLoader));
/* 111 */     if (logger.isDebugEnabled()) {
/* 112 */       logger.debug(String.format("Successfully enhanced %s; enhanced class name is: %s", new Object[] {configClass
/* 113 */         .getName(), enhancedClass.getName() }));
/*     */     }
/* 115 */     return enhancedClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Enhancer newEnhancer(Class<?> superclass, ClassLoader classLoader)
/*     */   {
/* 122 */     Enhancer enhancer = new Enhancer();
/* 123 */     enhancer.setSuperclass(superclass);
/* 124 */     enhancer.setInterfaces(new Class[] { EnhancedConfiguration.class });
/* 125 */     enhancer.setUseFactory(false);
/* 126 */     enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
/* 127 */     enhancer.setStrategy(new BeanFactoryAwareGeneratorStrategy(classLoader));
/* 128 */     enhancer.setCallbackFilter(CALLBACK_FILTER);
/* 129 */     enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
/* 130 */     return enhancer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Class<?> createClass(Enhancer enhancer)
/*     */   {
/* 138 */     Class<?> subclass = enhancer.createClass();
/*     */     
/*     */ 
/* 141 */     Enhancer.registerStaticCallbacks(subclass, CALLBACKS);
/* 142 */     return subclass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract interface EnhancedConfiguration
/*     */     extends BeanFactoryAware
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static abstract interface ConditionalCallback
/*     */     extends Callback
/*     */   {
/*     */     public abstract boolean isMatch(Method paramMethod);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ConditionalCallbackFilter
/*     */     implements CallbackFilter
/*     */   {
/*     */     private final Callback[] callbacks;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private final Class<?>[] callbackTypes;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionalCallbackFilter(Callback[] callbacks)
/*     */     {
/* 182 */       this.callbacks = callbacks;
/* 183 */       this.callbackTypes = new Class[callbacks.length];
/* 184 */       for (int i = 0; i < callbacks.length; i++) {
/* 185 */         this.callbackTypes[i] = callbacks[i].getClass();
/*     */       }
/*     */     }
/*     */     
/*     */     public int accept(Method method)
/*     */     {
/* 191 */       for (int i = 0; i < this.callbacks.length; i++) {
/* 192 */         if ((!(this.callbacks[i] instanceof ConfigurationClassEnhancer.ConditionalCallback)) || 
/* 193 */           (((ConfigurationClassEnhancer.ConditionalCallback)this.callbacks[i]).isMatch(method))) {
/* 194 */           return i;
/*     */         }
/*     */       }
/* 197 */       throw new IllegalStateException("No callback available for method " + method.getName());
/*     */     }
/*     */     
/*     */     public Class<?>[] getCallbackTypes() {
/* 201 */       return this.callbackTypes;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class BeanFactoryAwareGeneratorStrategy
/*     */     extends DefaultGeneratorStrategy
/*     */   {
/*     */     private final ClassLoader classLoader;
/*     */     
/*     */ 
/*     */ 
/*     */     public BeanFactoryAwareGeneratorStrategy(ClassLoader classLoader)
/*     */     {
/* 216 */       this.classLoader = classLoader;
/*     */     }
/*     */     
/*     */     protected ClassGenerator transform(ClassGenerator cg) throws Exception
/*     */     {
/* 221 */       ClassEmitterTransformer transformer = new ClassEmitterTransformer()
/*     */       {
/*     */         public void end_class() {
/* 224 */           declare_field(1, "$$beanFactory", Type.getType(BeanFactory.class), null);
/* 225 */           super.end_class();
/*     */         }
/* 227 */       };
/* 228 */       return new TransformingClassGenerator(cg, transformer);
/*     */     }
/*     */     
/*     */     public byte[] generate(ClassGenerator cg) throws Exception
/*     */     {
/* 233 */       if (this.classLoader == null) {
/* 234 */         return super.generate(cg);
/*     */       }
/*     */       
/* 237 */       Thread currentThread = Thread.currentThread();
/*     */       try
/*     */       {
/* 240 */         threadContextClassLoader = currentThread.getContextClassLoader();
/*     */       }
/*     */       catch (Throwable ex) {
/*     */         ClassLoader threadContextClassLoader;
/* 244 */         return super.generate(cg);
/*     */       }
/*     */       ClassLoader threadContextClassLoader;
/* 247 */       boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
/* 248 */       if (overrideClassLoader) {
/* 249 */         currentThread.setContextClassLoader(this.classLoader);
/*     */       }
/*     */       try {
/* 252 */         return super.generate(cg);
/*     */       }
/*     */       finally {
/* 255 */         if (overrideClassLoader)
/*     */         {
/* 257 */           currentThread.setContextClassLoader(threadContextClassLoader);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class BeanFactoryAwareMethodInterceptor
/*     */     implements MethodInterceptor, ConfigurationClassEnhancer.ConditionalCallback
/*     */   {
/*     */     public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
/*     */       throws Throwable
/*     */     {
/* 273 */       Field field = ReflectionUtils.findField(obj.getClass(), "$$beanFactory");
/* 274 */       Assert.state(field != null, "Unable to find generated BeanFactory field");
/* 275 */       field.set(obj, args[0]);
/*     */       
/*     */ 
/*     */ 
/* 279 */       if (BeanFactoryAware.class.isAssignableFrom(ClassUtils.getUserClass(obj.getClass().getSuperclass()))) {
/* 280 */         return proxy.invokeSuper(obj, args);
/*     */       }
/* 282 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isMatch(Method candidateMethod)
/*     */     {
/* 287 */       return (candidateMethod.getName().equals("setBeanFactory")) && 
/* 288 */         (candidateMethod.getParameterTypes().length == 1) && 
/* 289 */         (BeanFactory.class == candidateMethod.getParameterTypes()[0]) && 
/* 290 */         (BeanFactoryAware.class.isAssignableFrom(candidateMethod.getDeclaringClass()));
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
/*     */ 
/*     */ 
/*     */   private static class BeanMethodInterceptor
/*     */     implements MethodInterceptor, ConfigurationClassEnhancer.ConditionalCallback
/*     */   {
/*     */     public Object intercept(Object enhancedConfigInstance, Method beanMethod, Object[] beanMethodArgs, MethodProxy cglibMethodProxy)
/*     */       throws Throwable
/*     */     {
/* 313 */       ConfigurableBeanFactory beanFactory = getBeanFactory(enhancedConfigInstance);
/* 314 */       String beanName = BeanAnnotationHelper.determineBeanNameFor(beanMethod);
/*     */       
/*     */ 
/* 317 */       Scope scope = (Scope)AnnotatedElementUtils.findMergedAnnotation(beanMethod, Scope.class);
/* 318 */       if ((scope != null) && (scope.proxyMode() != ScopedProxyMode.NO)) {
/* 319 */         String scopedBeanName = ScopedProxyCreator.getTargetBeanName(beanName);
/* 320 */         if (beanFactory.isCurrentlyInCreation(scopedBeanName)) {
/* 321 */           beanName = scopedBeanName;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 332 */       if ((factoryContainsBean(beanFactory, "&" + beanName)) && 
/* 333 */         (factoryContainsBean(beanFactory, beanName))) {
/* 334 */         Object factoryBean = beanFactory.getBean("&" + beanName);
/* 335 */         if (!(factoryBean instanceof ScopedProxyFactoryBean))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 340 */           return enhanceFactoryBean(factoryBean, beanMethod.getReturnType(), beanFactory, beanName);
/*     */         }
/*     */       }
/*     */       
/* 344 */       if (isCurrentlyInvokedFactoryMethod(beanMethod))
/*     */       {
/*     */ 
/*     */ 
/* 348 */         if ((ConfigurationClassEnhancer.logger.isWarnEnabled()) && 
/* 349 */           (BeanFactoryPostProcessor.class.isAssignableFrom(beanMethod.getReturnType()))) {
/* 350 */           ConfigurationClassEnhancer.logger.warn(String.format("@Bean method %s.%s is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. This will result in a failure to process annotations such as @Autowired, @Resource and @PostConstruct within the method's declaring @Configuration class. Add the 'static' modifier to this method to avoid these container lifecycle issues; see @Bean javadoc for complete details.", new Object[] {beanMethod
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 356 */             .getDeclaringClass().getSimpleName(), beanMethod.getName() }));
/*     */         }
/* 358 */         return cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
/*     */       }
/*     */       
/* 361 */       return obtainBeanInstanceFromFactory(beanMethod, beanMethodArgs, beanFactory, beanName);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Object obtainBeanInstanceFromFactory(Method beanMethod, Object[] beanMethodArgs, ConfigurableBeanFactory beanFactory, String beanName)
/*     */     {
/* 371 */       boolean alreadyInCreation = beanFactory.isCurrentlyInCreation(beanName);
/*     */       try {
/* 373 */         if (alreadyInCreation) {
/* 374 */           beanFactory.setCurrentlyInCreation(beanName, false);
/*     */         }
/* 376 */         boolean useArgs = !ObjectUtils.isEmpty(beanMethodArgs);
/* 377 */         if ((useArgs) && (beanFactory.isSingleton(beanName)))
/*     */         {
/*     */ 
/*     */ 
/* 381 */           for (Object arg : beanMethodArgs) {
/* 382 */             if (arg == null) {
/* 383 */               useArgs = false;
/* 384 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 389 */         Object beanInstance = useArgs ? beanFactory.getBean(beanName, beanMethodArgs) : beanFactory.getBean(beanName);
/* 390 */         if ((beanInstance != null) && (!ClassUtils.isAssignableValue(beanMethod.getReturnType(), beanInstance))) {
/* 391 */           String msg = String.format("@Bean method %s.%s called as a bean reference for type [%s] but overridden by non-compatible bean instance of type [%s].", new Object[] {beanMethod
/*     */           
/* 393 */             .getDeclaringClass().getSimpleName(), beanMethod.getName(), beanMethod
/* 394 */             .getReturnType().getName(), beanInstance.getClass().getName() });
/*     */           try {
/* 396 */             BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
/* 397 */             msg = msg + " Overriding bean of same name declared in: " + beanDefinition.getResourceDescription();
/*     */           }
/*     */           catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException1) {}
/*     */           
/*     */ 
/* 402 */           throw new IllegalStateException(msg);
/*     */         }
/* 404 */         Method currentlyInvoked = SimpleInstantiationStrategy.getCurrentlyInvokedFactoryMethod();
/* 405 */         Object outerBeanName; if (currentlyInvoked != null) {
/* 406 */           outerBeanName = BeanAnnotationHelper.determineBeanNameFor(currentlyInvoked);
/* 407 */           beanFactory.registerDependentBean(beanName, (String)outerBeanName);
/*     */         }
/* 409 */         return beanInstance;
/*     */       }
/*     */       finally {
/* 412 */         if (alreadyInCreation) {
/* 413 */           beanFactory.setCurrentlyInCreation(beanName, true);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isMatch(Method candidateMethod)
/*     */     {
/* 420 */       return BeanAnnotationHelper.isBeanAnnotated(candidateMethod);
/*     */     }
/*     */     
/*     */     private ConfigurableBeanFactory getBeanFactory(Object enhancedConfigInstance) {
/* 424 */       Field field = ReflectionUtils.findField(enhancedConfigInstance.getClass(), "$$beanFactory");
/* 425 */       Assert.state(field != null, "Unable to find generated bean factory field");
/* 426 */       Object beanFactory = ReflectionUtils.getField(field, enhancedConfigInstance);
/* 427 */       Assert.state(beanFactory != null, "BeanFactory has not been injected into @Configuration class");
/* 428 */       Assert.state(beanFactory instanceof ConfigurableBeanFactory, "Injected BeanFactory is not a ConfigurableBeanFactory");
/*     */       
/* 430 */       return (ConfigurableBeanFactory)beanFactory;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean factoryContainsBean(ConfigurableBeanFactory beanFactory, String beanName)
/*     */     {
/* 447 */       return (beanFactory.containsBean(beanName)) && (!beanFactory.isCurrentlyInCreation(beanName));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean isCurrentlyInvokedFactoryMethod(Method method)
/*     */     {
/* 457 */       Method currentlyInvoked = SimpleInstantiationStrategy.getCurrentlyInvokedFactoryMethod();
/* 458 */       return (currentlyInvoked != null) && (method.getName().equals(currentlyInvoked.getName())) && 
/* 459 */         (Arrays.equals(method.getParameterTypes(), currentlyInvoked.getParameterTypes()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Object enhanceFactoryBean(Object factoryBean, Class<?> exposedType, ConfigurableBeanFactory beanFactory, String beanName)
/*     */     {
/*     */       try
/*     */       {
/* 473 */         Class<?> clazz = factoryBean.getClass();
/* 474 */         boolean finalClass = Modifier.isFinal(clazz.getModifiers());
/* 475 */         boolean finalMethod = Modifier.isFinal(clazz.getMethod("getObject", new Class[0]).getModifiers());
/* 476 */         if ((finalClass) || (finalMethod)) {
/* 477 */           if (exposedType.isInterface()) {
/* 478 */             if (ConfigurationClassEnhancer.logger.isDebugEnabled()) {
/* 479 */               ConfigurationClassEnhancer.logger.debug("Creating interface proxy for FactoryBean '" + beanName + "' of type [" + clazz
/* 480 */                 .getName() + "] for use within another @Bean method because its " + (finalClass ? "implementation class" : "getObject() method") + " is final: Otherwise a getObject() call would not be routed to the factory.");
/*     */             }
/*     */             
/*     */ 
/* 484 */             return createInterfaceProxyForFactoryBean(factoryBean, exposedType, beanFactory, beanName);
/*     */           }
/*     */           
/* 487 */           if (ConfigurationClassEnhancer.logger.isInfoEnabled()) {
/* 488 */             ConfigurationClassEnhancer.logger.info("Unable to proxy FactoryBean '" + beanName + "' of type [" + clazz
/* 489 */               .getName() + "] for use within another @Bean method because its " + (finalClass ? "implementation class" : "getObject() method") + " is final: A getObject() call will NOT be routed to the factory. Consider declaring the return type as a FactoryBean interface.");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 494 */           return factoryBean;
/*     */         }
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 502 */       return createCglibProxyForFactoryBean(factoryBean, beanFactory, beanName);
/*     */     }
/*     */     
/*     */ 
/*     */     private Object createInterfaceProxyForFactoryBean(final Object factoryBean, Class<?> interfaceType, final ConfigurableBeanFactory beanFactory, final String beanName)
/*     */     {
/* 508 */       Proxy.newProxyInstance(factoryBean
/* 509 */         .getClass().getClassLoader(), new Class[] { interfaceType }, new InvocationHandler()
/*     */         {
/*     */           public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */           {
/* 513 */             if ((method.getName().equals("getObject")) && (args == null)) {
/* 514 */               return beanFactory.getBean(beanName);
/*     */             }
/* 516 */             return ReflectionUtils.invokeMethod(method, factoryBean, args);
/*     */           }
/*     */         });
/*     */     }
/*     */     
/*     */ 
/*     */     private Object createCglibProxyForFactoryBean(final Object factoryBean, final ConfigurableBeanFactory beanFactory, final String beanName)
/*     */     {
/* 524 */       Enhancer enhancer = new Enhancer();
/* 525 */       enhancer.setSuperclass(factoryBean.getClass());
/* 526 */       enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
/* 527 */       enhancer.setCallbackType(MethodInterceptor.class);
/*     */       
/*     */ 
/*     */ 
/* 531 */       Class<?> fbClass = enhancer.createClass();
/* 532 */       Object fbProxy = null;
/*     */       
/* 534 */       if (ConfigurationClassEnhancer.objenesis.isWorthTrying()) {
/*     */         try {
/* 536 */           fbProxy = ConfigurationClassEnhancer.objenesis.newInstance(fbClass, enhancer.getUseCache());
/*     */         }
/*     */         catch (ObjenesisException ex) {
/* 539 */           ConfigurationClassEnhancer.logger.debug("Unable to instantiate enhanced FactoryBean using Objenesis, falling back to regular construction", ex);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 544 */       if (fbProxy == null) {
/*     */         try {
/* 546 */           fbProxy = fbClass.newInstance();
/*     */         }
/*     */         catch (Throwable ex) {
/* 549 */           throw new IllegalStateException("Unable to instantiate enhanced FactoryBean using Objenesis, and regular FactoryBean instantiation via default constructor fails as well", ex);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 554 */       ((Factory)fbProxy).setCallback(0, new MethodInterceptor()
/*     */       {
/*     */         public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
/* 557 */           if ((method.getName().equals("getObject")) && (args.length == 0)) {
/* 558 */             return beanFactory.getBean(beanName);
/*     */           }
/* 560 */           return proxy.invoke(factoryBean, args);
/*     */         }
/*     */         
/* 563 */       });
/* 564 */       return fbProxy;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ConfigurationClassEnhancer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */