/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MethodInvoker;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ class ConstructorResolver
/*     */ {
/*  73 */   private static final NamedThreadLocal<InjectionPoint> currentInjectionPoint = new NamedThreadLocal("Current injection point");
/*     */   
/*     */ 
/*     */ 
/*     */   private final AbstractAutowireCapableBeanFactory beanFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstructorResolver(AbstractAutowireCapableBeanFactory beanFactory)
/*     */   {
/*  84 */     this.beanFactory = beanFactory;
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
/*     */   public BeanWrapper autowireConstructor(final String beanName, final RootBeanDefinition mbd, Constructor<?>[] chosenCtors, Object[] explicitArgs)
/*     */   {
/* 105 */     BeanWrapperImpl bw = new BeanWrapperImpl();
/* 106 */     this.beanFactory.initBeanWrapper(bw);
/*     */     
/* 108 */     Constructor<?> constructorToUse = null;
/* 109 */     ArgumentsHolder argsHolderToUse = null;
/* 110 */     Object[] argsToUse = null;
/*     */     
/* 112 */     if (explicitArgs != null) {
/* 113 */       argsToUse = explicitArgs;
/*     */     }
/*     */     else {
/* 116 */       Object[] argsToResolve = null;
/* 117 */       synchronized (mbd.constructorArgumentLock) {
/* 118 */         constructorToUse = (Constructor)mbd.resolvedConstructorOrFactoryMethod;
/* 119 */         if ((constructorToUse != null) && (mbd.constructorArgumentsResolved))
/*     */         {
/* 121 */           argsToUse = mbd.resolvedConstructorArguments;
/* 122 */           if (argsToUse == null) {
/* 123 */             argsToResolve = mbd.preparedConstructorArguments;
/*     */           }
/*     */         }
/*     */       }
/* 127 */       if (argsToResolve != null) {
/* 128 */         argsToUse = resolvePreparedArguments(beanName, mbd, bw, constructorToUse, argsToResolve);
/*     */       }
/*     */     }
/*     */     
/* 132 */     if (constructorToUse == null)
/*     */     {
/*     */ 
/* 135 */       boolean autowiring = (chosenCtors != null) || (mbd.getResolvedAutowireMode() == 3);
/* 136 */       ConstructorArgumentValues resolvedValues = null;
/*     */       int minNrOfArgs;
/*     */       int minNrOfArgs;
/* 139 */       if (explicitArgs != null) {
/* 140 */         minNrOfArgs = explicitArgs.length;
/*     */       }
/*     */       else {
/* 143 */         ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
/* 144 */         resolvedValues = new ConstructorArgumentValues();
/* 145 */         minNrOfArgs = resolveConstructorArguments(beanName, mbd, bw, cargs, resolvedValues);
/*     */       }
/*     */       
/*     */ 
/* 149 */       Constructor<?>[] candidates = chosenCtors;
/* 150 */       if (candidates == null) {
/* 151 */         Class<?> beanClass = mbd.getBeanClass();
/*     */         try
/*     */         {
/* 154 */           candidates = mbd.isNonPublicAccessAllowed() ? beanClass.getDeclaredConstructors() : beanClass.getConstructors();
/*     */ 
/*     */         }
/*     */         catch (Throwable ex)
/*     */         {
/* 159 */           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Resolution of declared constructors on bean Class [" + beanClass.getName() + "] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex);
/*     */         }
/*     */       }
/* 162 */       AutowireUtils.sortConstructors(candidates);
/* 163 */       int minTypeDiffWeight = Integer.MAX_VALUE;
/* 164 */       Set<Constructor<?>> ambiguousConstructors = null;
/* 165 */       LinkedList<UnsatisfiedDependencyException> causes = null;
/*     */       
/* 167 */       for (Constructor<?> candidate : candidates) {
/* 168 */         Class<?>[] paramTypes = candidate.getParameterTypes();
/*     */         
/* 170 */         if ((constructorToUse != null) && (argsToUse.length > paramTypes.length)) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 175 */         if (paramTypes.length >= minNrOfArgs)
/*     */         {
/*     */           ArgumentsHolder argsHolder;
/*     */           
/*     */ 
/* 180 */           if (resolvedValues != null) {
/*     */             try {
/* 182 */               String[] paramNames = ConstructorPropertiesChecker.evaluate(candidate, paramTypes.length);
/* 183 */               if (paramNames == null) {
/* 184 */                 ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
/* 185 */                 if (pnd != null) {
/* 186 */                   paramNames = pnd.getParameterNames(candidate);
/*     */                 }
/*     */               }
/* 189 */               argsHolder = createArgumentArray(beanName, mbd, resolvedValues, bw, paramTypes, paramNames, 
/* 190 */                 getUserDeclaredConstructor(candidate), autowiring);
/*     */             } catch (UnsatisfiedDependencyException ex) {
/*     */               ArgumentsHolder argsHolder;
/* 193 */               if (this.beanFactory.logger.isTraceEnabled()) {
/* 194 */                 this.beanFactory.logger.trace("Ignoring constructor [" + candidate + "] of bean '" + beanName + "': " + ex);
/*     */               }
/*     */               
/*     */ 
/* 198 */               if (causes == null) {
/* 199 */                 causes = new LinkedList();
/*     */               }
/* 201 */               causes.add(ex);
/* 202 */               continue;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 207 */             if (paramTypes.length != explicitArgs.length) {
/*     */               continue;
/*     */             }
/* 210 */             argsHolder = new ArgumentsHolder(explicitArgs);
/*     */           }
/*     */           
/*     */ 
/* 214 */           int typeDiffWeight = mbd.isLenientConstructorResolution() ? argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes);
/*     */           
/* 216 */           if (typeDiffWeight < minTypeDiffWeight) {
/* 217 */             constructorToUse = candidate;
/* 218 */             argsHolderToUse = argsHolder;
/* 219 */             argsToUse = argsHolder.arguments;
/* 220 */             minTypeDiffWeight = typeDiffWeight;
/* 221 */             ambiguousConstructors = null;
/*     */           }
/* 223 */           else if ((constructorToUse != null) && (typeDiffWeight == minTypeDiffWeight)) {
/* 224 */             if (ambiguousConstructors == null) {
/* 225 */               ambiguousConstructors = new LinkedHashSet();
/* 226 */               ambiguousConstructors.add(constructorToUse);
/*     */             }
/* 228 */             ambiguousConstructors.add(candidate);
/*     */           }
/*     */         }
/*     */       }
/* 232 */       if (constructorToUse == null) {
/* 233 */         if (causes != null) {
/* 234 */           UnsatisfiedDependencyException ex = (UnsatisfiedDependencyException)causes.removeLast();
/* 235 */           for (Exception cause : causes) {
/* 236 */             this.beanFactory.onSuppressedException(cause);
/*     */           }
/* 238 */           throw ex;
/*     */         }
/* 240 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Could not resolve matching constructor (hint: specify index/type/name arguments for simple parameters to avoid type ambiguities)");
/*     */       }
/*     */       
/*     */ 
/* 244 */       if ((ambiguousConstructors != null) && (!mbd.isLenientConstructorResolution())) {
/* 245 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Ambiguous constructor matches found in bean '" + beanName + "' (hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " + ambiguousConstructors);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 251 */       if (explicitArgs == null) {
/* 252 */         argsHolderToUse.storeCache(mbd, constructorToUse);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/*     */       Object beanInstance;
/*     */       Object beanInstance;
/* 259 */       if (System.getSecurityManager() != null) {
/* 260 */         final Constructor<?> ctorToUse = constructorToUse;
/* 261 */         final Object[] argumentsToUse = argsToUse;
/* 262 */         beanInstance = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 265 */             return ConstructorResolver.this.beanFactory.getInstantiationStrategy().instantiate(mbd, beanName, 
/* 266 */               ConstructorResolver.this.beanFactory, ctorToUse, argumentsToUse);
/*     */           }
/* 268 */         }, this.beanFactory.getAccessControlContext());
/*     */       }
/*     */       else {
/* 271 */         beanInstance = this.beanFactory.getInstantiationStrategy().instantiate(mbd, beanName, this.beanFactory, constructorToUse, argsToUse);
/*     */       }
/*     */       
/*     */ 
/* 275 */       bw.setBeanInstance(beanInstance);
/* 276 */       return bw;
/*     */     }
/*     */     catch (Throwable ex) {
/* 279 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean instantiation via constructor failed", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resolveFactoryMethodIfPossible(RootBeanDefinition mbd)
/*     */   {
/*     */     boolean isStatic;
/*     */     
/*     */ 
/*     */     boolean isStatic;
/*     */     
/* 292 */     if (mbd.getFactoryBeanName() != null) {
/* 293 */       Class<?> factoryClass = this.beanFactory.getType(mbd.getFactoryBeanName());
/* 294 */       isStatic = false;
/*     */     }
/*     */     else {
/* 297 */       factoryClass = mbd.getBeanClass();
/* 298 */       isStatic = true;
/*     */     }
/* 300 */     Class<?> factoryClass = ClassUtils.getUserClass(factoryClass);
/*     */     
/* 302 */     Method[] candidates = getCandidateMethods(factoryClass, mbd);
/* 303 */     Method uniqueCandidate = null;
/* 304 */     for (Method candidate : candidates) {
/* 305 */       if ((Modifier.isStatic(candidate.getModifiers()) == isStatic) && (mbd.isFactoryMethod(candidate))) {
/* 306 */         if (uniqueCandidate == null) {
/* 307 */           uniqueCandidate = candidate;
/*     */         }
/* 309 */         else if (!Arrays.equals(uniqueCandidate.getParameterTypes(), candidate.getParameterTypes())) {
/* 310 */           uniqueCandidate = null;
/* 311 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 315 */     synchronized (mbd.constructorArgumentLock) {
/* 316 */       mbd.resolvedConstructorOrFactoryMethod = uniqueCandidate;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Method[] getCandidateMethods(final Class<?> factoryClass, final RootBeanDefinition mbd)
/*     */   {
/* 326 */     if (System.getSecurityManager() != null) {
/* 327 */       (Method[])AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Method[] run() {
/* 330 */           return mbd.isNonPublicAccessAllowed() ? 
/* 331 */             ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 336 */     return mbd.isNonPublicAccessAllowed() ? 
/* 337 */       ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods();
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
/*     */   public BeanWrapper instantiateUsingFactoryMethod(final String beanName, final RootBeanDefinition mbd, Object[] explicitArgs)
/*     */   {
/* 359 */     BeanWrapperImpl bw = new BeanWrapperImpl();
/* 360 */     this.beanFactory.initBeanWrapper(bw);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 366 */     String factoryBeanName = mbd.getFactoryBeanName();
/* 367 */     boolean isStatic; Object factoryBean; Class<?> factoryClass; boolean isStatic; if (factoryBeanName != null) {
/* 368 */       if (factoryBeanName.equals(beanName)) {
/* 369 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "factory-bean reference points back to the same bean definition");
/*     */       }
/*     */       
/* 372 */       Object factoryBean = this.beanFactory.getBean(factoryBeanName);
/* 373 */       if (factoryBean == null) {
/* 374 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "factory-bean '" + factoryBeanName + "' (or a BeanPostProcessor involved) returned null");
/*     */       }
/*     */       
/* 377 */       if ((mbd.isSingleton()) && (this.beanFactory.containsSingleton(beanName))) {
/* 378 */         throw new IllegalStateException("About-to-be-created singleton instance implicitly appeared through the creation of the factory bean that its bean definition points to");
/*     */       }
/*     */       
/* 381 */       Class<?> factoryClass = factoryBean.getClass();
/* 382 */       isStatic = false;
/*     */     }
/*     */     else
/*     */     {
/* 386 */       if (!mbd.hasBeanClass()) {
/* 387 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "bean definition declares neither a bean class nor a factory-bean reference");
/*     */       }
/*     */       
/* 390 */       factoryBean = null;
/* 391 */       factoryClass = mbd.getBeanClass();
/* 392 */       isStatic = true;
/*     */     }
/*     */     
/* 395 */     Method factoryMethodToUse = null;
/* 396 */     ArgumentsHolder argsHolderToUse = null;
/* 397 */     Object[] argsToUse = null;
/*     */     
/* 399 */     if (explicitArgs != null) {
/* 400 */       argsToUse = explicitArgs;
/*     */     }
/*     */     else {
/* 403 */       Object[] argsToResolve = null;
/* 404 */       synchronized (mbd.constructorArgumentLock) {
/* 405 */         factoryMethodToUse = (Method)mbd.resolvedConstructorOrFactoryMethod;
/* 406 */         if ((factoryMethodToUse != null) && (mbd.constructorArgumentsResolved))
/*     */         {
/* 408 */           argsToUse = mbd.resolvedConstructorArguments;
/* 409 */           if (argsToUse == null) {
/* 410 */             argsToResolve = mbd.preparedConstructorArguments;
/*     */           }
/*     */         }
/*     */       }
/* 414 */       if (argsToResolve != null) {
/* 415 */         argsToUse = resolvePreparedArguments(beanName, mbd, bw, factoryMethodToUse, argsToResolve);
/*     */       }
/*     */     }
/*     */     
/* 419 */     if ((factoryMethodToUse == null) || (argsToUse == null))
/*     */     {
/*     */ 
/* 422 */       factoryClass = ClassUtils.getUserClass(factoryClass);
/*     */       
/* 424 */       Method[] rawCandidates = getCandidateMethods(factoryClass, mbd);
/* 425 */       List<Method> candidateSet = new ArrayList();
/* 426 */       for (Method candidate : rawCandidates) {
/* 427 */         if ((Modifier.isStatic(candidate.getModifiers()) == isStatic) && (mbd.isFactoryMethod(candidate))) {
/* 428 */           candidateSet.add(candidate);
/*     */         }
/*     */       }
/* 431 */       Method[] candidates = (Method[])candidateSet.toArray(new Method[candidateSet.size()]);
/* 432 */       AutowireUtils.sortFactoryMethods(candidates);
/*     */       
/* 434 */       ConstructorArgumentValues resolvedValues = null;
/* 435 */       boolean autowiring = mbd.getResolvedAutowireMode() == 3;
/* 436 */       int minTypeDiffWeight = Integer.MAX_VALUE;
/* 437 */       Set<Method> ambiguousFactoryMethods = null;
/*     */       int minNrOfArgs;
/*     */       int minNrOfArgs;
/* 440 */       if (explicitArgs != null) {
/* 441 */         minNrOfArgs = explicitArgs.length;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 446 */         ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
/* 447 */         resolvedValues = new ConstructorArgumentValues();
/* 448 */         minNrOfArgs = resolveConstructorArguments(beanName, mbd, bw, cargs, resolvedValues);
/*     */       }
/*     */       
/* 451 */       LinkedList<UnsatisfiedDependencyException> causes = null;
/*     */       Method candidate;
/* 453 */       for (candidate : candidates) {
/* 454 */         Class<?>[] paramTypes = candidate.getParameterTypes();
/*     */         
/* 456 */         if (paramTypes.length >= minNrOfArgs)
/*     */         {
/*     */           ArgumentsHolder argsHolder;
/* 459 */           if (resolvedValues != null)
/*     */           {
/*     */             try {
/* 462 */               String[] paramNames = null;
/* 463 */               ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
/* 464 */               if (pnd != null) {
/* 465 */                 paramNames = pnd.getParameterNames(candidate);
/*     */               }
/* 467 */               argsHolder = createArgumentArray(beanName, mbd, resolvedValues, bw, paramTypes, paramNames, candidate, autowiring);
/*     */             }
/*     */             catch (UnsatisfiedDependencyException ex) {
/*     */               ArgumentsHolder argsHolder;
/* 471 */               if (this.beanFactory.logger.isTraceEnabled()) {
/* 472 */                 this.beanFactory.logger.trace("Ignoring factory method [" + candidate + "] of bean '" + beanName + "': " + ex);
/*     */               }
/*     */               
/*     */ 
/* 476 */               if (causes == null) {
/* 477 */                 causes = new LinkedList();
/*     */               }
/* 479 */               causes.add(ex);
/* 480 */               continue;
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 486 */             if (paramTypes.length != explicitArgs.length) {
/*     */               continue;
/*     */             }
/* 489 */             argsHolder = new ArgumentsHolder(explicitArgs);
/*     */           }
/*     */           
/*     */ 
/* 493 */           int typeDiffWeight = mbd.isLenientConstructorResolution() ? argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes);
/*     */           
/* 495 */           if (typeDiffWeight < minTypeDiffWeight) {
/* 496 */             factoryMethodToUse = candidate;
/* 497 */             argsHolderToUse = argsHolder;
/* 498 */             argsToUse = argsHolder.arguments;
/* 499 */             minTypeDiffWeight = typeDiffWeight;
/* 500 */             ambiguousFactoryMethods = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 507 */           else if ((factoryMethodToUse != null) && (typeDiffWeight == minTypeDiffWeight) && 
/* 508 */             (!mbd.isLenientConstructorResolution()) && 
/* 509 */             (paramTypes.length == factoryMethodToUse.getParameterTypes().length) && 
/* 510 */             (!Arrays.equals(paramTypes, factoryMethodToUse.getParameterTypes()))) {
/* 511 */             if (ambiguousFactoryMethods == null) {
/* 512 */               ambiguousFactoryMethods = new LinkedHashSet();
/* 513 */               ambiguousFactoryMethods.add(factoryMethodToUse);
/*     */             }
/* 515 */             ambiguousFactoryMethods.add(candidate);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 520 */       if (factoryMethodToUse == null) { Object localObject2;
/* 521 */         Exception cause; if (causes != null) {
/* 522 */           UnsatisfiedDependencyException ex = (UnsatisfiedDependencyException)causes.removeLast();
/* 523 */           for (localObject2 = causes.iterator(); ((Iterator)localObject2).hasNext();) { cause = (Exception)((Iterator)localObject2).next();
/* 524 */             this.beanFactory.onSuppressedException(cause);
/*     */           }
/* 526 */           throw ex;
/*     */         }
/* 528 */         Object argTypes = new ArrayList(minNrOfArgs);
/* 529 */         Object localObject3; if (explicitArgs != null) {
/* 530 */           localObject2 = explicitArgs;localObject3 = localObject2.length; for (candidate = 0; candidate < localObject3; candidate++) { Object arg = localObject2[candidate];
/* 531 */             ((List)argTypes).add(arg != null ? arg.getClass().getSimpleName() : "null");
/*     */           }
/*     */         }
/*     */         else {
/* 535 */           Object valueHolders = new LinkedHashSet(resolvedValues.getArgumentCount());
/* 536 */           ((Set)valueHolders).addAll(resolvedValues.getIndexedArgumentValues().values());
/* 537 */           ((Set)valueHolders).addAll(resolvedValues.getGenericArgumentValues());
/* 538 */           for (localObject3 = ((Set)valueHolders).iterator(); ((Iterator)localObject3).hasNext();) { ConstructorArgumentValues.ValueHolder value = (ConstructorArgumentValues.ValueHolder)((Iterator)localObject3).next();
/*     */             
/* 540 */             String argType = value.getValue() != null ? value.getValue().getClass().getSimpleName() : value.getType() != null ? ClassUtils.getShortName(value.getType()) : "null";
/* 541 */             ((List)argTypes).add(argType);
/*     */           }
/*     */         }
/* 544 */         String argDesc = StringUtils.collectionToCommaDelimitedString((Collection)argTypes);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 549 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "No matching factory method found: " + (mbd.getFactoryBeanName() != null ? "factory bean '" + mbd.getFactoryBeanName() + "'; " : "") + "factory method '" + mbd.getFactoryMethodName() + "(" + argDesc + ")'. Check that a method with the specified name " + (minNrOfArgs > 0 ? "and arguments " : "") + "exists and that it is " + (isStatic ? "static" : "non-static") + ".");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 555 */       if (Void.TYPE == factoryMethodToUse.getReturnType())
/*     */       {
/* 557 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid factory method '" + mbd.getFactoryMethodName() + "': needs to have a non-void return type!");
/*     */       }
/*     */       
/* 560 */       if (ambiguousFactoryMethods != null) {
/* 561 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Ambiguous factory method matches found in bean '" + beanName + "' (hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " + ambiguousFactoryMethods);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 567 */       if ((explicitArgs == null) && (argsHolderToUse != null)) {
/* 568 */         argsHolderToUse.storeCache(mbd, factoryMethodToUse);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/*     */       Object beanInstance;
/*     */       Object beanInstance;
/* 575 */       if (System.getSecurityManager() != null) {
/* 576 */         final Object fb = factoryBean;
/* 577 */         final Method factoryMethod = factoryMethodToUse;
/* 578 */         final Object[] args = argsToUse;
/* 579 */         beanInstance = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 582 */             return ConstructorResolver.this.beanFactory.getInstantiationStrategy().instantiate(mbd, beanName, 
/* 583 */               ConstructorResolver.this.beanFactory, fb, factoryMethod, args);
/*     */           }
/* 585 */         }, this.beanFactory.getAccessControlContext());
/*     */       }
/*     */       else {
/* 588 */         beanInstance = this.beanFactory.getInstantiationStrategy().instantiate(mbd, beanName, this.beanFactory, factoryBean, factoryMethodToUse, argsToUse);
/*     */       }
/*     */       
/*     */ 
/* 592 */       if (beanInstance == null) {
/* 593 */         return null;
/*     */       }
/* 595 */       bw.setBeanInstance(beanInstance);
/* 596 */       return bw;
/*     */     }
/*     */     catch (Throwable ex) {
/* 599 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean instantiation via factory method failed", ex);
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
/*     */   private int resolveConstructorArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw, ConstructorArgumentValues cargs, ConstructorArgumentValues resolvedValues)
/*     */   {
/* 612 */     TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
/* 613 */     TypeConverter converter = customConverter != null ? customConverter : bw;
/* 614 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
/*     */     
/*     */ 
/* 617 */     int minNrOfArgs = cargs.getArgumentCount();
/*     */     
/* 619 */     for (Map.Entry<Integer, ConstructorArgumentValues.ValueHolder> entry : cargs.getIndexedArgumentValues().entrySet()) {
/* 620 */       int index = ((Integer)entry.getKey()).intValue();
/* 621 */       if (index < 0) {
/* 622 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid constructor argument index: " + index);
/*     */       }
/*     */       
/* 625 */       if (index > minNrOfArgs) {
/* 626 */         minNrOfArgs = index + 1;
/*     */       }
/* 628 */       ConstructorArgumentValues.ValueHolder valueHolder = (ConstructorArgumentValues.ValueHolder)entry.getValue();
/* 629 */       if (valueHolder.isConverted()) {
/* 630 */         resolvedValues.addIndexedArgumentValue(index, valueHolder);
/*     */       }
/*     */       else
/*     */       {
/* 634 */         Object resolvedValue = valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
/*     */         
/* 636 */         ConstructorArgumentValues.ValueHolder resolvedValueHolder = new ConstructorArgumentValues.ValueHolder(resolvedValue, valueHolder.getType(), valueHolder.getName());
/* 637 */         resolvedValueHolder.setSource(valueHolder);
/* 638 */         resolvedValues.addIndexedArgumentValue(index, resolvedValueHolder);
/*     */       }
/*     */     }
/*     */     
/* 642 */     for (ConstructorArgumentValues.ValueHolder valueHolder : cargs.getGenericArgumentValues()) {
/* 643 */       if (valueHolder.isConverted()) {
/* 644 */         resolvedValues.addGenericArgumentValue(valueHolder);
/*     */       }
/*     */       else
/*     */       {
/* 648 */         Object resolvedValue = valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
/*     */         
/* 650 */         ConstructorArgumentValues.ValueHolder resolvedValueHolder = new ConstructorArgumentValues.ValueHolder(resolvedValue, valueHolder.getType(), valueHolder.getName());
/* 651 */         resolvedValueHolder.setSource(valueHolder);
/* 652 */         resolvedValues.addGenericArgumentValue(resolvedValueHolder);
/*     */       }
/*     */     }
/*     */     
/* 656 */     return minNrOfArgs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ArgumentsHolder createArgumentArray(String beanName, RootBeanDefinition mbd, ConstructorArgumentValues resolvedValues, BeanWrapper bw, Class<?>[] paramTypes, String[] paramNames, Object methodOrCtor, boolean autowiring)
/*     */     throws UnsatisfiedDependencyException
/*     */   {
/* 668 */     TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
/* 669 */     TypeConverter converter = customConverter != null ? customConverter : bw;
/*     */     
/* 671 */     ArgumentsHolder args = new ArgumentsHolder(paramTypes.length);
/* 672 */     Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet(paramTypes.length);
/*     */     
/* 674 */     Set<String> autowiredBeanNames = new LinkedHashSet(4);
/*     */     
/* 676 */     for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
/* 677 */       Class<?> paramType = paramTypes[paramIndex];
/* 678 */       String paramName = paramNames != null ? paramNames[paramIndex] : "";
/*     */       
/*     */ 
/* 681 */       ConstructorArgumentValues.ValueHolder valueHolder = resolvedValues.getArgumentValue(paramIndex, paramType, paramName, usedValueHolders);
/*     */       
/*     */ 
/*     */ 
/* 685 */       if ((valueHolder == null) && ((!autowiring) || (paramTypes.length == resolvedValues.getArgumentCount()))) {
/* 686 */         valueHolder = resolvedValues.getGenericArgumentValue(null, null, usedValueHolders);
/*     */       }
/* 688 */       if (valueHolder != null)
/*     */       {
/*     */ 
/* 691 */         usedValueHolders.add(valueHolder);
/* 692 */         Object originalValue = valueHolder.getValue();
/*     */         
/* 694 */         if (valueHolder.isConverted()) {
/* 695 */           Object convertedValue = valueHolder.getConvertedValue();
/* 696 */           args.preparedArguments[paramIndex] = convertedValue;
/*     */         }
/*     */         else
/*     */         {
/* 700 */           ConstructorArgumentValues.ValueHolder sourceHolder = (ConstructorArgumentValues.ValueHolder)valueHolder.getSource();
/* 701 */           Object sourceValue = sourceHolder.getValue();
/* 702 */           MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, paramIndex);
/*     */           try {
/* 704 */             Object convertedValue = converter.convertIfNecessary(originalValue, paramType, methodParam);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 714 */             args.resolveNecessary = true;
/* 715 */             args.preparedArguments[paramIndex] = sourceValue;
/*     */ 
/*     */ 
/*     */           }
/*     */           catch (TypeMismatchException ex)
/*     */           {
/*     */ 
/*     */ 
/* 723 */             throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam), "Could not convert argument value of type [" + ObjectUtils.nullSafeClassName(valueHolder.getValue()) + "] to required type [" + paramType.getName() + "]: " + ex.getMessage());
/*     */           } }
/*     */         Object convertedValue;
/* 726 */         args.arguments[paramIndex] = convertedValue;
/* 727 */         args.rawArguments[paramIndex] = originalValue;
/*     */       }
/*     */       else {
/* 730 */         MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, paramIndex);
/*     */         
/*     */ 
/* 733 */         if (!autowiring)
/*     */         {
/*     */ 
/* 736 */           throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam), "Ambiguous argument values for parameter of type [" + paramType.getName() + "] - did you specify the correct bean references as arguments?");
/*     */         }
/*     */         
/*     */         try
/*     */         {
/* 741 */           Object autowiredArgument = resolveAutowiredArgument(methodParam, beanName, autowiredBeanNames, converter);
/* 742 */           args.rawArguments[paramIndex] = autowiredArgument;
/* 743 */           args.arguments[paramIndex] = autowiredArgument;
/* 744 */           args.preparedArguments[paramIndex] = new AutowiredArgumentMarker(null);
/* 745 */           args.resolveNecessary = true;
/*     */         }
/*     */         catch (BeansException ex)
/*     */         {
/* 749 */           throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam), ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 754 */     for (String autowiredBeanName : autowiredBeanNames) {
/* 755 */       this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
/* 756 */       if (this.beanFactory.logger.isDebugEnabled()) {
/* 757 */         this.beanFactory.logger.debug("Autowiring by type from bean name '" + beanName + "' via " + ((methodOrCtor instanceof Constructor) ? "constructor" : "factory method") + " to bean named '" + autowiredBeanName + "'");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 763 */     return args;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object[] resolvePreparedArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw, Member methodOrCtor, Object[] argsToResolve)
/*     */   {
/* 772 */     TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
/* 773 */     TypeConverter converter = customConverter != null ? customConverter : bw;
/* 774 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
/*     */     
/*     */ 
/* 777 */     Class<?>[] paramTypes = (methodOrCtor instanceof Method) ? ((Method)methodOrCtor).getParameterTypes() : ((Constructor)methodOrCtor).getParameterTypes();
/*     */     
/* 779 */     Object[] resolvedArgs = new Object[argsToResolve.length];
/* 780 */     for (int argIndex = 0; argIndex < argsToResolve.length; argIndex++) {
/* 781 */       Object argValue = argsToResolve[argIndex];
/* 782 */       MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, argIndex);
/* 783 */       GenericTypeResolver.resolveParameterType(methodParam, methodOrCtor.getDeclaringClass());
/* 784 */       if ((argValue instanceof AutowiredArgumentMarker)) {
/* 785 */         argValue = resolveAutowiredArgument(methodParam, beanName, null, converter);
/*     */       }
/* 787 */       else if ((argValue instanceof BeanMetadataElement)) {
/* 788 */         argValue = valueResolver.resolveValueIfNecessary("constructor argument", argValue);
/*     */       }
/* 790 */       else if ((argValue instanceof String)) {
/* 791 */         argValue = this.beanFactory.evaluateBeanDefinitionString((String)argValue, mbd);
/*     */       }
/* 793 */       Class<?> paramType = paramTypes[argIndex];
/*     */       try {
/* 795 */         resolvedArgs[argIndex] = converter.convertIfNecessary(argValue, paramType, methodParam);
/*     */ 
/*     */       }
/*     */       catch (TypeMismatchException ex)
/*     */       {
/*     */ 
/* 801 */         throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam), "Could not convert argument value of type [" + ObjectUtils.nullSafeClassName(argValue) + "] to required type [" + paramType.getName() + "]: " + ex.getMessage());
/*     */       }
/*     */     }
/* 804 */     return resolvedArgs;
/*     */   }
/*     */   
/*     */   protected Constructor<?> getUserDeclaredConstructor(Constructor<?> constructor) {
/* 808 */     Class<?> declaringClass = constructor.getDeclaringClass();
/* 809 */     Class<?> userClass = ClassUtils.getUserClass(declaringClass);
/* 810 */     if (userClass != declaringClass) {
/*     */       try {
/* 812 */         return userClass.getDeclaredConstructor(constructor.getParameterTypes());
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 819 */     return constructor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object resolveAutowiredArgument(MethodParameter param, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*     */   {
/* 828 */     if (InjectionPoint.class.isAssignableFrom(param.getParameterType())) {
/* 829 */       InjectionPoint injectionPoint = (InjectionPoint)currentInjectionPoint.get();
/* 830 */       if (injectionPoint == null) {
/* 831 */         throw new IllegalStateException("No current InjectionPoint available for " + param);
/*     */       }
/* 833 */       return injectionPoint;
/*     */     }
/* 835 */     return this.beanFactory.resolveDependency(new DependencyDescriptor(param, true), beanName, autowiredBeanNames, typeConverter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static InjectionPoint setCurrentInjectionPoint(InjectionPoint injectionPoint)
/*     */   {
/* 842 */     InjectionPoint old = (InjectionPoint)currentInjectionPoint.get();
/* 843 */     if (injectionPoint != null) {
/* 844 */       currentInjectionPoint.set(injectionPoint);
/*     */     }
/*     */     else {
/* 847 */       currentInjectionPoint.remove();
/*     */     }
/* 849 */     return old;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ArgumentsHolder
/*     */   {
/*     */     public final Object[] rawArguments;
/*     */     
/*     */ 
/*     */     public final Object[] arguments;
/*     */     
/*     */ 
/*     */     public final Object[] preparedArguments;
/*     */     
/* 864 */     public boolean resolveNecessary = false;
/*     */     
/*     */     public ArgumentsHolder(int size) {
/* 867 */       this.rawArguments = new Object[size];
/* 868 */       this.arguments = new Object[size];
/* 869 */       this.preparedArguments = new Object[size];
/*     */     }
/*     */     
/*     */     public ArgumentsHolder(Object[] args) {
/* 873 */       this.rawArguments = args;
/* 874 */       this.arguments = args;
/* 875 */       this.preparedArguments = args;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getTypeDifferenceWeight(Class<?>[] paramTypes)
/*     */     {
/* 883 */       int typeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.arguments);
/* 884 */       int rawTypeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.rawArguments) - 1024;
/* 885 */       return rawTypeDiffWeight < typeDiffWeight ? rawTypeDiffWeight : typeDiffWeight;
/*     */     }
/*     */     
/*     */     public int getAssignabilityWeight(Class<?>[] paramTypes) {
/* 889 */       for (int i = 0; i < paramTypes.length; i++) {
/* 890 */         if (!ClassUtils.isAssignableValue(paramTypes[i], this.arguments[i])) {
/* 891 */           return Integer.MAX_VALUE;
/*     */         }
/*     */       }
/* 894 */       for (int i = 0; i < paramTypes.length; i++) {
/* 895 */         if (!ClassUtils.isAssignableValue(paramTypes[i], this.rawArguments[i])) {
/* 896 */           return 2147483135;
/*     */         }
/*     */       }
/* 899 */       return 2147482623;
/*     */     }
/*     */     
/*     */     public void storeCache(RootBeanDefinition mbd, Object constructorOrFactoryMethod) {
/* 903 */       synchronized (mbd.constructorArgumentLock) {
/* 904 */         mbd.resolvedConstructorOrFactoryMethod = constructorOrFactoryMethod;
/* 905 */         mbd.constructorArgumentsResolved = true;
/* 906 */         if (this.resolveNecessary) {
/* 907 */           mbd.preparedConstructorArguments = this.preparedArguments;
/*     */         }
/*     */         else {
/* 910 */           mbd.resolvedConstructorArguments = this.arguments;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class AutowiredArgumentMarker {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ConstructorPropertiesChecker
/*     */   {
/*     */     public static String[] evaluate(Constructor<?> candidate, int paramCount)
/*     */     {
/* 930 */       ConstructorProperties cp = (ConstructorProperties)candidate.getAnnotation(ConstructorProperties.class);
/* 931 */       if (cp != null) {
/* 932 */         String[] names = cp.value();
/* 933 */         if (names.length != paramCount) {
/* 934 */           throw new IllegalStateException("Constructor annotated with @ConstructorProperties but not corresponding to actual number of parameters (" + paramCount + "): " + candidate);
/*     */         }
/*     */         
/* 937 */         return names;
/*     */       }
/*     */       
/* 940 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\ConstructorResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */