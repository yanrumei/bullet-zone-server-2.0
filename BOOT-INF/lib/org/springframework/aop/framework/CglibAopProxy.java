/*      */ package org.springframework.aop.framework;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ import org.aopalliance.aop.Advice;
/*      */ import org.aopalliance.intercept.MethodInvocation;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.aop.Advisor;
/*      */ import org.springframework.aop.AopInvocationException;
/*      */ import org.springframework.aop.PointcutAdvisor;
/*      */ import org.springframework.aop.RawTargetAccess;
/*      */ import org.springframework.aop.TargetSource;
/*      */ import org.springframework.aop.support.AopUtils;
/*      */ import org.springframework.cglib.core.ClassGenerator;
/*      */ import org.springframework.cglib.core.CodeGenerationException;
/*      */ import org.springframework.cglib.core.SpringNamingPolicy;
/*      */ import org.springframework.cglib.proxy.Callback;
/*      */ import org.springframework.cglib.proxy.CallbackFilter;
/*      */ import org.springframework.cglib.proxy.Dispatcher;
/*      */ import org.springframework.cglib.proxy.Enhancer;
/*      */ import org.springframework.cglib.proxy.Factory;
/*      */ import org.springframework.cglib.proxy.MethodInterceptor;
/*      */ import org.springframework.cglib.proxy.MethodProxy;
/*      */ import org.springframework.cglib.proxy.NoOp;
/*      */ import org.springframework.cglib.transform.impl.UndeclaredThrowableStrategy;
/*      */ import org.springframework.core.SmartClassLoader;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
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
/*      */ class CglibAopProxy
/*      */   implements AopProxy, Serializable
/*      */ {
/*      */   private static final int AOP_PROXY = 0;
/*      */   private static final int INVOKE_TARGET = 1;
/*      */   private static final int NO_OVERRIDE = 2;
/*      */   private static final int DISPATCH_TARGET = 3;
/*      */   private static final int DISPATCH_ADVISED = 4;
/*      */   private static final int INVOKE_EQUALS = 5;
/*      */   private static final int INVOKE_HASHCODE = 6;
/*   95 */   protected static final Log logger = LogFactory.getLog(CglibAopProxy.class);
/*      */   
/*      */ 
/*   98 */   private static final Map<Class<?>, Boolean> validatedClasses = new WeakHashMap();
/*      */   
/*      */ 
/*      */   protected final AdvisedSupport advised;
/*      */   
/*      */ 
/*      */   protected Object[] constructorArgs;
/*      */   
/*      */ 
/*      */   protected Class<?>[] constructorArgTypes;
/*      */   
/*      */ 
/*      */   private final transient AdvisedDispatcher advisedDispatcher;
/*      */   
/*      */ 
/*      */   private transient Map<String, Integer> fixedInterceptorMap;
/*      */   
/*      */ 
/*      */   private transient int fixedInterceptorOffset;
/*      */   
/*      */ 
/*      */ 
/*      */   public CglibAopProxy(AdvisedSupport config)
/*      */     throws AopConfigException
/*      */   {
/*  123 */     Assert.notNull(config, "AdvisedSupport must not be null");
/*  124 */     if ((config.getAdvisors().length == 0) && (config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE)) {
/*  125 */       throw new AopConfigException("No advisors and no TargetSource specified");
/*      */     }
/*  127 */     this.advised = config;
/*  128 */     this.advisedDispatcher = new AdvisedDispatcher(this.advised);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConstructorArguments(Object[] constructorArgs, Class<?>[] constructorArgTypes)
/*      */   {
/*  137 */     if ((constructorArgs == null) || (constructorArgTypes == null)) {
/*  138 */       throw new IllegalArgumentException("Both 'constructorArgs' and 'constructorArgTypes' need to be specified");
/*      */     }
/*  140 */     if (constructorArgs.length != constructorArgTypes.length) {
/*  141 */       throw new IllegalArgumentException("Number of 'constructorArgs' (" + constructorArgs.length + ") must match number of 'constructorArgTypes' (" + constructorArgTypes.length + ")");
/*      */     }
/*      */     
/*  144 */     this.constructorArgs = constructorArgs;
/*  145 */     this.constructorArgTypes = constructorArgTypes;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object getProxy()
/*      */   {
/*  151 */     return getProxy(null);
/*      */   }
/*      */   
/*      */   public Object getProxy(ClassLoader classLoader)
/*      */   {
/*  156 */     if (logger.isDebugEnabled()) {
/*  157 */       logger.debug("Creating CGLIB proxy: target source is " + this.advised.getTargetSource());
/*      */     }
/*      */     try
/*      */     {
/*  161 */       Class<?> rootClass = this.advised.getTargetClass();
/*  162 */       Assert.state(rootClass != null, "Target class must be available for creating a CGLIB proxy");
/*      */       
/*  164 */       Class<?> proxySuperClass = rootClass;
/*  165 */       if (ClassUtils.isCglibProxyClass(rootClass)) {
/*  166 */         proxySuperClass = rootClass.getSuperclass();
/*  167 */         Class<?>[] additionalInterfaces = rootClass.getInterfaces();
/*  168 */         for (Class<?> additionalInterface : additionalInterfaces) {
/*  169 */           this.advised.addInterface(additionalInterface);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  174 */       validateClassIfNecessary(proxySuperClass, classLoader);
/*      */       
/*      */ 
/*  177 */       Enhancer enhancer = createEnhancer();
/*  178 */       if (classLoader != null) {
/*  179 */         enhancer.setClassLoader(classLoader);
/*  180 */         if (((classLoader instanceof SmartClassLoader)) && 
/*  181 */           (((SmartClassLoader)classLoader).isClassReloadable(proxySuperClass))) {
/*  182 */           enhancer.setUseCache(false);
/*      */         }
/*      */       }
/*  185 */       enhancer.setSuperclass(proxySuperClass);
/*  186 */       enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
/*  187 */       enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
/*  188 */       enhancer.setStrategy(new ClassLoaderAwareUndeclaredThrowableStrategy(classLoader));
/*      */       
/*  190 */       Callback[] callbacks = getCallbacks(rootClass);
/*  191 */       Object types = new Class[callbacks.length];
/*  192 */       for (int x = 0; x < types.length; x++) {
/*  193 */         types[x] = callbacks[x].getClass();
/*      */       }
/*      */       
/*  196 */       enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised
/*  197 */         .getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
/*  198 */       enhancer.setCallbackTypes((Class[])types);
/*      */       
/*      */ 
/*  201 */       return createProxyClassAndInstance(enhancer, callbacks);
/*      */     }
/*      */     catch (CodeGenerationException ex)
/*      */     {
/*  205 */       throw new AopConfigException("Could not generate CGLIB subclass of class [" + this.advised.getTargetClass() + "]: Common causes of this problem include using a final class or a non-visible class", ex);
/*      */ 
/*      */     }
/*      */     catch (IllegalArgumentException ex)
/*      */     {
/*      */ 
/*  211 */       throw new AopConfigException("Could not generate CGLIB subclass of class [" + this.advised.getTargetClass() + "]: Common causes of this problem include using a final class or a non-visible class", ex);
/*      */ 
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/*      */ 
/*  217 */       throw new AopConfigException("Unexpected AOP exception", ex);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
/*  222 */     enhancer.setInterceptDuringConstruction(false);
/*  223 */     enhancer.setCallbacks(callbacks);
/*  224 */     return this.constructorArgs != null ? enhancer
/*  225 */       .create(this.constructorArgTypes, this.constructorArgs) : enhancer
/*  226 */       .create();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Enhancer createEnhancer()
/*      */   {
/*  234 */     return new Enhancer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void validateClassIfNecessary(Class<?> proxySuperClass, ClassLoader proxyClassLoader)
/*      */   {
/*  242 */     if (logger.isWarnEnabled()) {
/*  243 */       synchronized (validatedClasses) {
/*  244 */         if (!validatedClasses.containsKey(proxySuperClass)) {
/*  245 */           doValidateClass(proxySuperClass, proxyClassLoader, 
/*  246 */             ClassUtils.getAllInterfacesForClassAsSet(proxySuperClass));
/*  247 */           validatedClasses.put(proxySuperClass, Boolean.TRUE);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void doValidateClass(Class<?> proxySuperClass, ClassLoader proxyClassLoader, Set<Class<?>> ifcs)
/*      */   {
/*  258 */     if (proxySuperClass != Object.class) {
/*  259 */       Method[] methods = proxySuperClass.getDeclaredMethods();
/*  260 */       for (Method method : methods) {
/*  261 */         int mod = method.getModifiers();
/*  262 */         if (!Modifier.isStatic(mod)) {
/*  263 */           if (Modifier.isFinal(mod)) {
/*  264 */             if (implementsInterface(method, ifcs)) {
/*  265 */               logger.warn("Unable to proxy interface-implementing method [" + method + "] because it is marked as final: Consider using interface-based JDK proxies instead!");
/*      */             }
/*      */             
/*  268 */             logger.info("Final method [" + method + "] cannot get proxied via CGLIB: Calls to this method will NOT be routed to the target instance and might lead to NPEs against uninitialized fields in the proxy instance.");
/*      */ 
/*      */ 
/*      */           }
/*  272 */           else if ((!Modifier.isPublic(mod)) && (!Modifier.isProtected(mod)) && (!Modifier.isPrivate(mod)) && (proxyClassLoader != null) && 
/*  273 */             (proxySuperClass.getClassLoader() != proxyClassLoader)) {
/*  274 */             logger.info("Method [" + method + "] is package-visible across different ClassLoaders and cannot get proxied via CGLIB: Declare this method as public or protected if you need to support invocations through the proxy.");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  280 */       doValidateClass(proxySuperClass.getSuperclass(), proxyClassLoader, ifcs);
/*      */     }
/*      */   }
/*      */   
/*      */   private Callback[] getCallbacks(Class<?> rootClass) throws Exception
/*      */   {
/*  286 */     boolean exposeProxy = this.advised.isExposeProxy();
/*  287 */     boolean isFrozen = this.advised.isFrozen();
/*  288 */     boolean isStatic = this.advised.getTargetSource().isStatic();
/*      */     
/*      */ 
/*  291 */     Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);
/*      */     
/*      */     Callback targetInterceptor;
/*      */     
/*      */     Callback targetInterceptor;
/*  296 */     if (exposeProxy)
/*      */     {
/*      */ 
/*  299 */       targetInterceptor = isStatic ? new StaticUnadvisedExposedInterceptor(this.advised.getTargetSource().getTarget()) : new DynamicUnadvisedExposedInterceptor(this.advised.getTargetSource());
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  304 */       targetInterceptor = isStatic ? new StaticUnadvisedInterceptor(this.advised.getTargetSource().getTarget()) : new DynamicUnadvisedInterceptor(this.advised.getTargetSource());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  310 */     Callback targetDispatcher = (Callback)(isStatic ? new StaticDispatcher(this.advised.getTargetSource().getTarget()) : new SerializableNoOp());
/*      */     
/*  312 */     Callback[] mainCallbacks = { aopInterceptor, targetInterceptor, new SerializableNoOp(), targetDispatcher, this.advisedDispatcher, new EqualsInterceptor(this.advised), new HashCodeInterceptor(this.advised) };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     Callback[] callbacks;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  326 */     if ((isStatic) && (isFrozen)) {
/*  327 */       Method[] methods = rootClass.getMethods();
/*  328 */       Callback[] fixedCallbacks = new Callback[methods.length];
/*  329 */       this.fixedInterceptorMap = new HashMap(methods.length);
/*      */       
/*      */ 
/*  332 */       for (int x = 0; x < methods.length; x++) {
/*  333 */         List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(methods[x], rootClass);
/*  334 */         fixedCallbacks[x] = new FixedChainStaticTargetInterceptor(chain, this.advised
/*  335 */           .getTargetSource().getTarget(), this.advised.getTargetClass());
/*  336 */         this.fixedInterceptorMap.put(methods[x].toString(), Integer.valueOf(x));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  341 */       Callback[] callbacks = new Callback[mainCallbacks.length + fixedCallbacks.length];
/*  342 */       System.arraycopy(mainCallbacks, 0, callbacks, 0, mainCallbacks.length);
/*  343 */       System.arraycopy(fixedCallbacks, 0, callbacks, mainCallbacks.length, fixedCallbacks.length);
/*  344 */       this.fixedInterceptorOffset = mainCallbacks.length;
/*      */     }
/*      */     else {
/*  347 */       callbacks = mainCallbacks;
/*      */     }
/*  349 */     return callbacks;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean equals(Object other)
/*      */   {
/*  355 */     return (this == other) || (((other instanceof CglibAopProxy)) && 
/*  356 */       (AopProxyUtils.equalsInProxy(this.advised, ((CglibAopProxy)other).advised)));
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/*  361 */     return CglibAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean implementsInterface(Method method, Set<Class<?>> ifcs)
/*      */   {
/*  369 */     for (Class<?> ifc : ifcs) {
/*  370 */       if (ClassUtils.hasMethod(ifc, method.getName(), method.getParameterTypes())) {
/*  371 */         return true;
/*      */       }
/*      */     }
/*  374 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Object processReturnType(Object proxy, Object target, Method method, Object retVal)
/*      */   {
/*  383 */     if ((retVal != null) && (retVal == target) && 
/*  384 */       (!RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())))
/*      */     {
/*      */ 
/*  387 */       retVal = proxy;
/*      */     }
/*  389 */     Class<?> returnType = method.getReturnType();
/*  390 */     if ((retVal == null) && (returnType != Void.TYPE) && (returnType.isPrimitive())) {
/*  391 */       throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
/*      */     }
/*      */     
/*  394 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class SerializableNoOp
/*      */     implements NoOp, Serializable
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class StaticUnadvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final Object target;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public StaticUnadvisedInterceptor(Object target)
/*      */     {
/*  417 */       this.target = target;
/*      */     }
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
/*      */     {
/*  422 */       Object retVal = methodProxy.invoke(this.target, args);
/*  423 */       return CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class StaticUnadvisedExposedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final Object target;
/*      */     
/*      */ 
/*      */     public StaticUnadvisedExposedInterceptor(Object target)
/*      */     {
/*  437 */       this.target = target;
/*      */     }
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
/*      */     {
/*  442 */       Object oldProxy = null;
/*      */       try {
/*  444 */         oldProxy = AopContext.setCurrentProxy(proxy);
/*  445 */         Object retVal = methodProxy.invoke(this.target, args);
/*  446 */         return CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*      */       }
/*      */       finally {
/*  449 */         AopContext.setCurrentProxy(oldProxy);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class DynamicUnadvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final TargetSource targetSource;
/*      */     
/*      */ 
/*      */ 
/*      */     public DynamicUnadvisedInterceptor(TargetSource targetSource)
/*      */     {
/*  465 */       this.targetSource = targetSource;
/*      */     }
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
/*      */     {
/*  470 */       Object target = this.targetSource.getTarget();
/*      */       try {
/*  472 */         Object retVal = methodProxy.invoke(target, args);
/*  473 */         return CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*      */       }
/*      */       finally {
/*  476 */         this.targetSource.releaseTarget(target);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class DynamicUnadvisedExposedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final TargetSource targetSource;
/*      */     
/*      */ 
/*      */     public DynamicUnadvisedExposedInterceptor(TargetSource targetSource)
/*      */     {
/*  490 */       this.targetSource = targetSource;
/*      */     }
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
/*      */     {
/*  495 */       Object oldProxy = null;
/*  496 */       Object target = this.targetSource.getTarget();
/*      */       try {
/*  498 */         oldProxy = AopContext.setCurrentProxy(proxy);
/*  499 */         Object retVal = methodProxy.invoke(target, args);
/*  500 */         return CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*      */       }
/*      */       finally {
/*  503 */         AopContext.setCurrentProxy(oldProxy);
/*  504 */         this.targetSource.releaseTarget(target);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class StaticDispatcher
/*      */     implements Dispatcher, Serializable
/*      */   {
/*      */     private Object target;
/*      */     
/*      */ 
/*      */ 
/*      */     public StaticDispatcher(Object target)
/*      */     {
/*  520 */       this.target = target;
/*      */     }
/*      */     
/*      */     public Object loadObject()
/*      */     {
/*  525 */       return this.target;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class AdvisedDispatcher
/*      */     implements Dispatcher, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */     
/*      */ 
/*      */     public AdvisedDispatcher(AdvisedSupport advised)
/*      */     {
/*  538 */       this.advised = advised;
/*      */     }
/*      */     
/*      */     public Object loadObject() throws Exception
/*      */     {
/*  543 */       return this.advised;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class EqualsInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */     
/*      */ 
/*      */     public EqualsInterceptor(AdvisedSupport advised)
/*      */     {
/*  557 */       this.advised = advised;
/*      */     }
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
/*      */     {
/*  562 */       Object other = args[0];
/*  563 */       if (proxy == other) {
/*  564 */         return Boolean.valueOf(true);
/*      */       }
/*  566 */       if ((other instanceof Factory)) {
/*  567 */         Callback callback = ((Factory)other).getCallback(5);
/*  568 */         if (!(callback instanceof EqualsInterceptor)) {
/*  569 */           return Boolean.valueOf(false);
/*      */         }
/*  571 */         AdvisedSupport otherAdvised = ((EqualsInterceptor)callback).advised;
/*  572 */         return Boolean.valueOf(AopProxyUtils.equalsInProxy(this.advised, otherAdvised));
/*      */       }
/*      */       
/*  575 */       return Boolean.valueOf(false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class HashCodeInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */     
/*      */ 
/*      */ 
/*      */     public HashCodeInterceptor(AdvisedSupport advised)
/*      */     {
/*  590 */       this.advised = advised;
/*      */     }
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
/*      */     {
/*  595 */       return Integer.valueOf(CglibAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class FixedChainStaticTargetInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final List<Object> adviceChain;
/*      */     
/*      */     private final Object target;
/*      */     
/*      */     private final Class<?> targetClass;
/*      */     
/*      */ 
/*      */     public FixedChainStaticTargetInterceptor(List<Object> adviceChain, Object target, Class<?> targetClass)
/*      */     {
/*  612 */       this.adviceChain = adviceChain;
/*  613 */       this.target = target;
/*  614 */       this.targetClass = targetClass;
/*      */     }
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
/*      */     {
/*  619 */       MethodInvocation invocation = new CglibAopProxy.CglibMethodInvocation(proxy, this.target, method, args, this.targetClass, this.adviceChain, methodProxy);
/*      */       
/*      */ 
/*  622 */       Object retVal = invocation.proceed();
/*  623 */       retVal = CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*  624 */       return retVal;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class DynamicAdvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */     
/*      */ 
/*      */     public DynamicAdvisedInterceptor(AdvisedSupport advised)
/*      */     {
/*  638 */       this.advised = advised;
/*      */     }
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
/*      */     {
/*  643 */       Object oldProxy = null;
/*  644 */       boolean setProxyContext = false;
/*  645 */       Class<?> targetClass = null;
/*  646 */       Object target = null;
/*      */       try {
/*  648 */         if (this.advised.exposeProxy)
/*      */         {
/*  650 */           oldProxy = AopContext.setCurrentProxy(proxy);
/*  651 */           setProxyContext = true;
/*      */         }
/*      */         
/*      */ 
/*  655 */         target = getTarget();
/*  656 */         if (target != null) {
/*  657 */           targetClass = target.getClass();
/*      */         }
/*  659 */         List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
/*      */         
/*      */         Object[] argsToUse;
/*      */         Object retVal;
/*  663 */         if ((chain.isEmpty()) && (Modifier.isPublic(method.getModifiers())))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  668 */           argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
/*  669 */           retVal = methodProxy.invoke(target, argsToUse);
/*      */         }
/*      */         else
/*      */         {
/*  673 */           retVal = new CglibAopProxy.CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
/*      */         }
/*  675 */         Object retVal = CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*  676 */         return (Object[])retVal;
/*      */       }
/*      */       finally {
/*  679 */         if (target != null) {
/*  680 */           releaseTarget(target);
/*      */         }
/*  682 */         if (setProxyContext)
/*      */         {
/*  684 */           AopContext.setCurrentProxy(oldProxy);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean equals(Object other)
/*      */     {
/*  691 */       if (this != other) if (!(other instanceof DynamicAdvisedInterceptor)) break label33; label33: return this.advised
/*      */       
/*  693 */         .equals(((DynamicAdvisedInterceptor)other).advised);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  701 */       return this.advised.hashCode();
/*      */     }
/*      */     
/*      */     protected Object getTarget() throws Exception {
/*  705 */       return this.advised.getTargetSource().getTarget();
/*      */     }
/*      */     
/*      */     protected void releaseTarget(Object target) throws Exception {
/*  709 */       this.advised.getTargetSource().releaseTarget(target);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class CglibMethodInvocation
/*      */     extends ReflectiveMethodInvocation
/*      */   {
/*      */     private final MethodProxy methodProxy;
/*      */     
/*      */ 
/*      */     private final boolean publicMethod;
/*      */     
/*      */ 
/*      */     public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy)
/*      */     {
/*  726 */       super(target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
/*  727 */       this.methodProxy = methodProxy;
/*  728 */       this.publicMethod = Modifier.isPublic(method.getModifiers());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Object invokeJoinpoint()
/*      */       throws Throwable
/*      */     {
/*  737 */       if (this.publicMethod) {
/*  738 */         return this.methodProxy.invoke(this.target, this.arguments);
/*      */       }
/*      */       
/*  741 */       return super.invokeJoinpoint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class ProxyCallbackFilter
/*      */     implements CallbackFilter
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */     
/*      */     private final Map<String, Integer> fixedInterceptorMap;
/*      */     
/*      */     private final int fixedInterceptorOffset;
/*      */     
/*      */ 
/*      */     public ProxyCallbackFilter(AdvisedSupport advised, Map<String, Integer> fixedInterceptorMap, int fixedInterceptorOffset)
/*      */     {
/*  759 */       this.advised = advised;
/*  760 */       this.fixedInterceptorMap = fixedInterceptorMap;
/*  761 */       this.fixedInterceptorOffset = fixedInterceptorOffset;
/*      */     }
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
/*      */     public int accept(Method method)
/*      */     {
/*  802 */       if (AopUtils.isFinalizeMethod(method)) {
/*  803 */         CglibAopProxy.logger.debug("Found finalize() method - using NO_OVERRIDE");
/*  804 */         return 2;
/*      */       }
/*  806 */       if ((!this.advised.isOpaque()) && (method.getDeclaringClass().isInterface()) && 
/*  807 */         (method.getDeclaringClass().isAssignableFrom(Advised.class))) {
/*  808 */         if (CglibAopProxy.logger.isDebugEnabled()) {
/*  809 */           CglibAopProxy.logger.debug("Method is declared on Advised interface: " + method);
/*      */         }
/*  811 */         return 4;
/*      */       }
/*      */       
/*  814 */       if (AopUtils.isEqualsMethod(method)) {
/*  815 */         CglibAopProxy.logger.debug("Found 'equals' method: " + method);
/*  816 */         return 5;
/*      */       }
/*      */       
/*  819 */       if (AopUtils.isHashCodeMethod(method)) {
/*  820 */         CglibAopProxy.logger.debug("Found 'hashCode' method: " + method);
/*  821 */         return 6;
/*      */       }
/*  823 */       Class<?> targetClass = this.advised.getTargetClass();
/*      */       
/*  825 */       List<?> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
/*  826 */       boolean haveAdvice = !chain.isEmpty();
/*  827 */       boolean exposeProxy = this.advised.isExposeProxy();
/*  828 */       boolean isStatic = this.advised.getTargetSource().isStatic();
/*  829 */       boolean isFrozen = this.advised.isFrozen();
/*  830 */       if ((haveAdvice) || (!isFrozen))
/*      */       {
/*  832 */         if (exposeProxy) {
/*  833 */           if (CglibAopProxy.logger.isDebugEnabled()) {
/*  834 */             CglibAopProxy.logger.debug("Must expose proxy on advised method: " + method);
/*      */           }
/*  836 */           return 0;
/*      */         }
/*  838 */         String key = method.toString();
/*      */         
/*      */ 
/*  841 */         if ((isStatic) && (isFrozen) && (this.fixedInterceptorMap.containsKey(key))) {
/*  842 */           if (CglibAopProxy.logger.isDebugEnabled()) {
/*  843 */             CglibAopProxy.logger.debug("Method has advice and optimizations are enabled: " + method);
/*      */           }
/*      */           
/*  846 */           int index = ((Integer)this.fixedInterceptorMap.get(key)).intValue();
/*  847 */           return index + this.fixedInterceptorOffset;
/*      */         }
/*      */         
/*  850 */         if (CglibAopProxy.logger.isDebugEnabled()) {
/*  851 */           CglibAopProxy.logger.debug("Unable to apply any optimizations to advised method: " + method);
/*      */         }
/*  853 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  862 */       if ((exposeProxy) || (!isStatic)) {
/*  863 */         return 1;
/*      */       }
/*  865 */       Class<?> returnType = method.getReturnType();
/*  866 */       if (returnType.isAssignableFrom(targetClass)) {
/*  867 */         if (CglibAopProxy.logger.isDebugEnabled()) {
/*  868 */           CglibAopProxy.logger.debug("Method return type is assignable from target type and may therefore return 'this' - using INVOKE_TARGET: " + method);
/*      */         }
/*      */         
/*  871 */         return 1;
/*      */       }
/*      */       
/*  874 */       if (CglibAopProxy.logger.isDebugEnabled()) {
/*  875 */         CglibAopProxy.logger.debug("Method return type ensures 'this' cannot be returned - using DISPATCH_TARGET: " + method);
/*      */       }
/*      */       
/*  878 */       return 3;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean equals(Object other)
/*      */     {
/*  885 */       if (this == other) {
/*  886 */         return true;
/*      */       }
/*  888 */       if (!(other instanceof ProxyCallbackFilter)) {
/*  889 */         return false;
/*      */       }
/*  891 */       ProxyCallbackFilter otherCallbackFilter = (ProxyCallbackFilter)other;
/*  892 */       AdvisedSupport otherAdvised = otherCallbackFilter.advised;
/*  893 */       if ((this.advised == null) || (otherAdvised == null)) {
/*  894 */         return false;
/*      */       }
/*  896 */       if (this.advised.isFrozen() != otherAdvised.isFrozen()) {
/*  897 */         return false;
/*      */       }
/*  899 */       if (this.advised.isExposeProxy() != otherAdvised.isExposeProxy()) {
/*  900 */         return false;
/*      */       }
/*  902 */       if (this.advised.getTargetSource().isStatic() != otherAdvised.getTargetSource().isStatic()) {
/*  903 */         return false;
/*      */       }
/*  905 */       if (!AopProxyUtils.equalsProxiedInterfaces(this.advised, otherAdvised)) {
/*  906 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  910 */       Advisor[] thisAdvisors = this.advised.getAdvisors();
/*  911 */       Advisor[] thatAdvisors = otherAdvised.getAdvisors();
/*  912 */       if (thisAdvisors.length != thatAdvisors.length) {
/*  913 */         return false;
/*      */       }
/*  915 */       for (int i = 0; i < thisAdvisors.length; i++) {
/*  916 */         Advisor thisAdvisor = thisAdvisors[i];
/*  917 */         Advisor thatAdvisor = thatAdvisors[i];
/*  918 */         if (!equalsAdviceClasses(thisAdvisor, thatAdvisor)) {
/*  919 */           return false;
/*      */         }
/*  921 */         if (!equalsPointcuts(thisAdvisor, thatAdvisor)) {
/*  922 */           return false;
/*      */         }
/*      */       }
/*  925 */       return true;
/*      */     }
/*      */     
/*      */     private boolean equalsAdviceClasses(Advisor a, Advisor b) {
/*  929 */       Advice aa = a.getAdvice();
/*  930 */       Advice ba = b.getAdvice();
/*  931 */       if ((aa == null) || (ba == null)) {
/*  932 */         return aa == ba;
/*      */       }
/*  934 */       return aa.getClass() == ba.getClass();
/*      */     }
/*      */     
/*      */ 
/*      */     private boolean equalsPointcuts(Advisor a, Advisor b)
/*      */     {
/*  940 */       if ((a instanceof PointcutAdvisor)) if (!(b instanceof PointcutAdvisor)) break label42; label42: return 
/*      */       
/*  942 */         ObjectUtils.nullSafeEquals(((PointcutAdvisor)a).getPointcut(), ((PointcutAdvisor)b).getPointcut());
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/*  947 */       int hashCode = 0;
/*  948 */       Advisor[] advisors = this.advised.getAdvisors();
/*  949 */       for (Advisor advisor : advisors) {
/*  950 */         Advice advice = advisor.getAdvice();
/*  951 */         if (advice != null) {
/*  952 */           hashCode = 13 * hashCode + advice.getClass().hashCode();
/*      */         }
/*      */       }
/*  955 */       hashCode = 13 * hashCode + (this.advised.isFrozen() ? 1 : 0);
/*  956 */       hashCode = 13 * hashCode + (this.advised.isExposeProxy() ? 1 : 0);
/*  957 */       hashCode = 13 * hashCode + (this.advised.isOptimize() ? 1 : 0);
/*  958 */       hashCode = 13 * hashCode + (this.advised.isOpaque() ? 1 : 0);
/*  959 */       return hashCode;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class ClassLoaderAwareUndeclaredThrowableStrategy
/*      */     extends UndeclaredThrowableStrategy
/*      */   {
/*      */     private final ClassLoader classLoader;
/*      */     
/*      */ 
/*      */ 
/*      */     public ClassLoaderAwareUndeclaredThrowableStrategy(ClassLoader classLoader)
/*      */     {
/*  974 */       super();
/*  975 */       this.classLoader = classLoader;
/*      */     }
/*      */     
/*      */     public byte[] generate(ClassGenerator cg) throws Exception
/*      */     {
/*  980 */       if (this.classLoader == null) {
/*  981 */         return super.generate(cg);
/*      */       }
/*      */       
/*  984 */       Thread currentThread = Thread.currentThread();
/*      */       try
/*      */       {
/*  987 */         threadContextClassLoader = currentThread.getContextClassLoader();
/*      */       }
/*      */       catch (Throwable ex) {
/*      */         ClassLoader threadContextClassLoader;
/*  991 */         return super.generate(cg);
/*      */       }
/*      */       ClassLoader threadContextClassLoader;
/*  994 */       boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
/*  995 */       if (overrideClassLoader) {
/*  996 */         currentThread.setContextClassLoader(this.classLoader);
/*      */       }
/*      */       try {
/*  999 */         return super.generate(cg);
/*      */       }
/*      */       finally {
/* 1002 */         if (overrideClassLoader)
/*      */         {
/* 1004 */           currentThread.setContextClassLoader(threadContextClassLoader);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\CglibAopProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */