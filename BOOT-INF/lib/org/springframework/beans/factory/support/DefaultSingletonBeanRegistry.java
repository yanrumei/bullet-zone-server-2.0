/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanCreationNotAllowedException;
/*     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.SingletonBeanRegistry;
/*     */ import org.springframework.core.SimpleAliasRegistry;
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
/*     */ public class DefaultSingletonBeanRegistry
/*     */   extends SimpleAliasRegistry
/*     */   implements SingletonBeanRegistry
/*     */ {
/*  79 */   protected static final Object NULL_OBJECT = new Object();
/*     */   
/*     */ 
/*     */ 
/*  83 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*  86 */   private final Map<String, Object> singletonObjects = new ConcurrentHashMap(256);
/*     */   
/*     */ 
/*  89 */   private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap(16);
/*     */   
/*     */ 
/*  92 */   private final Map<String, Object> earlySingletonObjects = new HashMap(16);
/*     */   
/*     */ 
/*  95 */   private final Set<String> registeredSingletons = new LinkedHashSet(256);
/*     */   
/*     */ 
/*     */ 
/*  99 */   private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap(16));
/*     */   
/*     */ 
/*     */ 
/* 103 */   private final Set<String> inCreationCheckExclusions = Collections.newSetFromMap(new ConcurrentHashMap(16));
/*     */   
/*     */ 
/*     */   private Set<Exception> suppressedExceptions;
/*     */   
/*     */ 
/* 109 */   private boolean singletonsCurrentlyInDestruction = false;
/*     */   
/*     */ 
/* 112 */   private final Map<String, Object> disposableBeans = new LinkedHashMap();
/*     */   
/*     */ 
/* 115 */   private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap(16);
/*     */   
/*     */ 
/* 118 */   private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/* 121 */   private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap(64);
/*     */   
/*     */   public void registerSingleton(String beanName, Object singletonObject)
/*     */     throws IllegalStateException
/*     */   {
/* 126 */     Assert.notNull(beanName, "'beanName' must not be null");
/* 127 */     synchronized (this.singletonObjects) {
/* 128 */       Object oldObject = this.singletonObjects.get(beanName);
/* 129 */       if (oldObject != null) {
/* 130 */         throw new IllegalStateException("Could not register object [" + singletonObject + "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
/*     */       }
/*     */       
/* 133 */       addSingleton(beanName, singletonObject);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addSingleton(String beanName, Object singletonObject)
/*     */   {
/* 144 */     synchronized (this.singletonObjects) {
/* 145 */       this.singletonObjects.put(beanName, singletonObject != null ? singletonObject : NULL_OBJECT);
/* 146 */       this.singletonFactories.remove(beanName);
/* 147 */       this.earlySingletonObjects.remove(beanName);
/* 148 */       this.registeredSingletons.add(beanName);
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
/*     */   protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory)
/*     */   {
/* 161 */     Assert.notNull(singletonFactory, "Singleton factory must not be null");
/* 162 */     synchronized (this.singletonObjects) {
/* 163 */       if (!this.singletonObjects.containsKey(beanName)) {
/* 164 */         this.singletonFactories.put(beanName, singletonFactory);
/* 165 */         this.earlySingletonObjects.remove(beanName);
/* 166 */         this.registeredSingletons.add(beanName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getSingleton(String beanName)
/*     */   {
/* 173 */     return getSingleton(beanName, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getSingleton(String beanName, boolean allowEarlyReference)
/*     */   {
/* 185 */     Object singletonObject = this.singletonObjects.get(beanName);
/* 186 */     if ((singletonObject == null) && (isSingletonCurrentlyInCreation(beanName))) {
/* 187 */       synchronized (this.singletonObjects) {
/* 188 */         singletonObject = this.earlySingletonObjects.get(beanName);
/* 189 */         if ((singletonObject == null) && (allowEarlyReference)) {
/* 190 */           ObjectFactory<?> singletonFactory = (ObjectFactory)this.singletonFactories.get(beanName);
/* 191 */           if (singletonFactory != null) {
/* 192 */             singletonObject = singletonFactory.getObject();
/* 193 */             this.earlySingletonObjects.put(beanName, singletonObject);
/* 194 */             this.singletonFactories.remove(beanName);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 199 */     return singletonObject != NULL_OBJECT ? singletonObject : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory)
/*     */   {
/* 211 */     Assert.notNull(beanName, "'beanName' must not be null");
/* 212 */     synchronized (this.singletonObjects) {
/* 213 */       Object singletonObject = this.singletonObjects.get(beanName);
/* 214 */       if (singletonObject == null) {
/* 215 */         if (this.singletonsCurrentlyInDestruction) {
/* 216 */           throw new BeanCreationNotAllowedException(beanName, "Singleton bean creation not allowed while singletons of this factory are in destruction (Do not request a bean from a BeanFactory in a destroy method implementation!)");
/*     */         }
/*     */         
/*     */ 
/* 220 */         if (this.logger.isDebugEnabled()) {
/* 221 */           this.logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
/*     */         }
/* 223 */         beforeSingletonCreation(beanName);
/* 224 */         boolean newSingleton = false;
/* 225 */         boolean recordSuppressedExceptions = this.suppressedExceptions == null;
/* 226 */         if (recordSuppressedExceptions) {
/* 227 */           this.suppressedExceptions = new LinkedHashSet();
/*     */         }
/*     */         try {
/* 230 */           singletonObject = singletonFactory.getObject();
/* 231 */           newSingleton = true;
/*     */ 
/*     */         }
/*     */         catch (IllegalStateException ex)
/*     */         {
/* 236 */           singletonObject = this.singletonObjects.get(beanName);
/* 237 */           if (singletonObject == null) {
/* 238 */             throw ex;
/*     */           }
/*     */         }
/*     */         catch (BeanCreationException ex) {
/* 242 */           if (recordSuppressedExceptions) {
/* 243 */             for (Exception suppressedException : this.suppressedExceptions) {
/* 244 */               ex.addRelatedCause(suppressedException);
/*     */             }
/*     */           }
/* 247 */           throw ex;
/*     */         }
/*     */         finally {
/* 250 */           if (recordSuppressedExceptions) {
/* 251 */             this.suppressedExceptions = null;
/*     */           }
/* 253 */           afterSingletonCreation(beanName);
/*     */         }
/* 255 */         if (newSingleton) {
/* 256 */           addSingleton(beanName, singletonObject);
/*     */         }
/*     */       }
/* 259 */       return singletonObject != NULL_OBJECT ? singletonObject : null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onSuppressedException(Exception ex)
/*     */   {
/* 269 */     synchronized (this.singletonObjects) {
/* 270 */       if (this.suppressedExceptions != null) {
/* 271 */         this.suppressedExceptions.add(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeSingleton(String beanName)
/*     */   {
/* 283 */     synchronized (this.singletonObjects) {
/* 284 */       this.singletonObjects.remove(beanName);
/* 285 */       this.singletonFactories.remove(beanName);
/* 286 */       this.earlySingletonObjects.remove(beanName);
/* 287 */       this.registeredSingletons.remove(beanName);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean containsSingleton(String beanName)
/*     */   {
/* 293 */     return this.singletonObjects.containsKey(beanName);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String[] getSingletonNames()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	org/springframework/beans/factory/support/DefaultSingletonBeanRegistry:singletonObjects	Ljava/util/Map;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 14	org/springframework/beans/factory/support/DefaultSingletonBeanRegistry:registeredSingletons	Ljava/util/Set;
/*     */     //   11: invokestatic 68	org/springframework/util/StringUtils:toStringArray	(Ljava/util/Collection;)[Ljava/lang/String;
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: areturn
/*     */     //   17: astore_2
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: aload_2
/*     */     //   21: athrow
/*     */     // Line number table:
/*     */     //   Java source line #298	-> byte code offset #0
/*     */     //   Java source line #299	-> byte code offset #7
/*     */     //   Java source line #300	-> byte code offset #17
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	this	DefaultSingletonBeanRegistry
/*     */     //   5	14	1	Ljava/lang/Object;	Object
/*     */     //   17	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	16	17	finally
/*     */     //   17	20	17	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int getSingletonCount()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	org/springframework/beans/factory/support/DefaultSingletonBeanRegistry:singletonObjects	Ljava/util/Map;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 14	org/springframework/beans/factory/support/DefaultSingletonBeanRegistry:registeredSingletons	Ljava/util/Set;
/*     */     //   11: invokeinterface 69 1 0
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: ireturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #305	-> byte code offset #0
/*     */     //   Java source line #306	-> byte code offset #7
/*     */     //   Java source line #307	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	this	DefaultSingletonBeanRegistry
/*     */     //   5	16	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	18	19	finally
/*     */     //   19	22	19	finally
/*     */   }
/*     */   
/*     */   public void setCurrentlyInCreation(String beanName, boolean inCreation)
/*     */   {
/* 312 */     Assert.notNull(beanName, "Bean name must not be null");
/* 313 */     if (!inCreation) {
/* 314 */       this.inCreationCheckExclusions.add(beanName);
/*     */     }
/*     */     else {
/* 317 */       this.inCreationCheckExclusions.remove(beanName);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isCurrentlyInCreation(String beanName) {
/* 322 */     Assert.notNull(beanName, "Bean name must not be null");
/* 323 */     return (!this.inCreationCheckExclusions.contains(beanName)) && (isActuallyInCreation(beanName));
/*     */   }
/*     */   
/*     */   protected boolean isActuallyInCreation(String beanName) {
/* 327 */     return isSingletonCurrentlyInCreation(beanName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSingletonCurrentlyInCreation(String beanName)
/*     */   {
/* 336 */     return this.singletonsCurrentlyInCreation.contains(beanName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void beforeSingletonCreation(String beanName)
/*     */   {
/* 346 */     if ((!this.inCreationCheckExclusions.contains(beanName)) && (!this.singletonsCurrentlyInCreation.add(beanName))) {
/* 347 */       throw new BeanCurrentlyInCreationException(beanName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void afterSingletonCreation(String beanName)
/*     */   {
/* 358 */     if ((!this.inCreationCheckExclusions.contains(beanName)) && (!this.singletonsCurrentlyInCreation.remove(beanName))) {
/* 359 */       throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
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
/*     */   public void registerDisposableBean(String beanName, DisposableBean bean)
/*     */   {
/* 374 */     synchronized (this.disposableBeans) {
/* 375 */       this.disposableBeans.put(beanName, bean);
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
/*     */   public void registerContainedBean(String containedBeanName, String containingBeanName)
/*     */   {
/* 390 */     Set<String> containedBeans = (Set)this.containedBeanMap.get(containingBeanName);
/* 391 */     if ((containedBeans != null) && (containedBeans.contains(containedBeanName))) {
/* 392 */       return;
/*     */     }
/*     */     
/*     */ 
/* 396 */     synchronized (this.containedBeanMap) {
/* 397 */       containedBeans = (Set)this.containedBeanMap.get(containingBeanName);
/* 398 */       if (containedBeans == null) {
/* 399 */         containedBeans = new LinkedHashSet(8);
/* 400 */         this.containedBeanMap.put(containingBeanName, containedBeans);
/*     */       }
/* 402 */       containedBeans.add(containedBeanName);
/*     */     }
/* 404 */     registerDependentBean(containedBeanName, containingBeanName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerDependentBean(String beanName, String dependentBeanName)
/*     */   {
/* 415 */     String canonicalName = canonicalName(beanName);
/* 416 */     Set<String> dependentBeans = (Set)this.dependentBeanMap.get(canonicalName);
/* 417 */     if ((dependentBeans != null) && (dependentBeans.contains(dependentBeanName))) {
/* 418 */       return;
/*     */     }
/*     */     
/*     */ 
/* 422 */     synchronized (this.dependentBeanMap) {
/* 423 */       dependentBeans = (Set)this.dependentBeanMap.get(canonicalName);
/* 424 */       if (dependentBeans == null) {
/* 425 */         dependentBeans = new LinkedHashSet(8);
/* 426 */         this.dependentBeanMap.put(canonicalName, dependentBeans);
/*     */       }
/* 428 */       dependentBeans.add(dependentBeanName);
/*     */     }
/* 430 */     synchronized (this.dependenciesForBeanMap) {
/* 431 */       Object dependenciesForBean = (Set)this.dependenciesForBeanMap.get(dependentBeanName);
/* 432 */       if (dependenciesForBean == null) {
/* 433 */         dependenciesForBean = new LinkedHashSet(8);
/* 434 */         this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
/*     */       }
/* 436 */       ((Set)dependenciesForBean).add(canonicalName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isDependent(String beanName, String dependentBeanName)
/*     */   {
/* 448 */     return isDependent(beanName, dependentBeanName, null);
/*     */   }
/*     */   
/*     */   private boolean isDependent(String beanName, String dependentBeanName, Set<String> alreadySeen) {
/* 452 */     if ((alreadySeen != null) && (alreadySeen.contains(beanName))) {
/* 453 */       return false;
/*     */     }
/* 455 */     String canonicalName = canonicalName(beanName);
/* 456 */     Set<String> dependentBeans = (Set)this.dependentBeanMap.get(canonicalName);
/* 457 */     if (dependentBeans == null) {
/* 458 */       return false;
/*     */     }
/* 460 */     if (dependentBeans.contains(dependentBeanName)) {
/* 461 */       return true;
/*     */     }
/* 463 */     for (String transitiveDependency : dependentBeans) {
/* 464 */       if (alreadySeen == null) {
/* 465 */         alreadySeen = new HashSet();
/*     */       }
/* 467 */       alreadySeen.add(beanName);
/* 468 */       if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
/* 469 */         return true;
/*     */       }
/*     */     }
/* 472 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasDependentBean(String beanName)
/*     */   {
/* 480 */     return this.dependentBeanMap.containsKey(beanName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getDependentBeans(String beanName)
/*     */   {
/* 489 */     Set<String> dependentBeans = (Set)this.dependentBeanMap.get(beanName);
/* 490 */     if (dependentBeans == null) {
/* 491 */       return new String[0];
/*     */     }
/* 493 */     return StringUtils.toStringArray(dependentBeans);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getDependenciesForBean(String beanName)
/*     */   {
/* 503 */     Set<String> dependenciesForBean = (Set)this.dependenciesForBeanMap.get(beanName);
/* 504 */     if (dependenciesForBean == null) {
/* 505 */       return new String[0];
/*     */     }
/* 507 */     return (String[])dependenciesForBean.toArray(new String[dependenciesForBean.size()]);
/*     */   }
/*     */   
/*     */   public void destroySingletons() {
/* 511 */     if (this.logger.isDebugEnabled()) {
/* 512 */       this.logger.debug("Destroying singletons in " + this);
/*     */     }
/* 514 */     synchronized (this.singletonObjects) {
/* 515 */       this.singletonsCurrentlyInDestruction = true;
/*     */     }
/*     */     
/*     */     String[] disposableBeanNames;
/* 519 */     synchronized (this.disposableBeans) {
/* 520 */       disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet()); }
/*     */     String[] disposableBeanNames;
/* 522 */     for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
/* 523 */       destroySingleton(disposableBeanNames[i]);
/*     */     }
/*     */     
/* 526 */     this.containedBeanMap.clear();
/* 527 */     this.dependentBeanMap.clear();
/* 528 */     this.dependenciesForBeanMap.clear();
/*     */     
/* 530 */     synchronized (this.singletonObjects) {
/* 531 */       this.singletonObjects.clear();
/* 532 */       this.singletonFactories.clear();
/* 533 */       this.earlySingletonObjects.clear();
/* 534 */       this.registeredSingletons.clear();
/* 535 */       this.singletonsCurrentlyInDestruction = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroySingleton(String beanName)
/*     */   {
/* 547 */     removeSingleton(beanName);
/*     */     
/*     */     DisposableBean disposableBean;
/*     */     
/* 551 */     synchronized (this.disposableBeans) {
/* 552 */       disposableBean = (DisposableBean)this.disposableBeans.remove(beanName); }
/*     */     DisposableBean disposableBean;
/* 554 */     destroyBean(beanName, disposableBean);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void destroyBean(String beanName, DisposableBean bean)
/*     */   {
/* 565 */     Set<String> dependencies = (Set)this.dependentBeanMap.remove(beanName);
/* 566 */     Iterator localIterator; if (dependencies != null) {
/* 567 */       if (this.logger.isDebugEnabled()) {
/* 568 */         this.logger.debug("Retrieved dependent beans for bean '" + beanName + "': " + dependencies);
/*     */       }
/* 570 */       for (localIterator = dependencies.iterator(); localIterator.hasNext();) { dependentBeanName = (String)localIterator.next();
/* 571 */         destroySingleton(dependentBeanName);
/*     */       }
/*     */     }
/*     */     
/*     */     String dependentBeanName;
/* 576 */     if (bean != null) {
/*     */       try {
/* 578 */         bean.destroy();
/*     */       }
/*     */       catch (Throwable ex) {
/* 581 */         this.logger.error("Destroy method on bean with name '" + beanName + "' threw an exception", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 586 */     Object containedBeans = (Set)this.containedBeanMap.remove(beanName);
/* 587 */     if (containedBeans != null) {
/* 588 */       for (String containedBeanName : (Set)containedBeans) {
/* 589 */         destroySingleton(containedBeanName);
/*     */       }
/*     */     }
/*     */     
/*     */     Iterator<Map.Entry<String, Set<String>>> it;
/* 594 */     synchronized (this.dependentBeanMap) {
/* 595 */       for (it = this.dependentBeanMap.entrySet().iterator(); it.hasNext();) {
/* 596 */         Map.Entry<String, Set<String>> entry = (Map.Entry)it.next();
/* 597 */         Set<String> dependenciesToClean = (Set)entry.getValue();
/* 598 */         dependenciesToClean.remove(beanName);
/* 599 */         if (dependenciesToClean.isEmpty()) {
/* 600 */           it.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 606 */     this.dependenciesForBeanMap.remove(beanName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object getSingletonMutex()
/*     */   {
/* 617 */     return this.singletonObjects;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\DefaultSingletonBeanRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */