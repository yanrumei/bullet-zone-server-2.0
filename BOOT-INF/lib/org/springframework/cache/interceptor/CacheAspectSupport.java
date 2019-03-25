/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.Cache.ValueRetrievalException;
/*     */ import org.springframework.cache.Cache.ValueWrapper;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.expression.AnnotatedElementKey;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class CacheAspectSupport
/*     */   extends AbstractCacheInvoker
/*     */   implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton
/*     */ {
/*  82 */   private static Class<?> javaUtilOptionalClass = null;
/*     */   
/*     */   static
/*     */   {
/*     */     try {
/*  87 */       javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", CacheAspectSupport.class.getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  94 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  96 */   private final Map<CacheOperationCacheKey, CacheOperationMetadata> metadataCache = new ConcurrentHashMap(1024);
/*     */   
/*     */ 
/*  99 */   private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();
/*     */   
/*     */   private CacheOperationSource cacheOperationSource;
/*     */   
/* 103 */   private KeyGenerator keyGenerator = new SimpleKeyGenerator();
/*     */   
/*     */   private CacheResolver cacheResolver;
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/* 109 */   private boolean initialized = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheOperationSources(CacheOperationSource... cacheOperationSources)
/*     */   {
/* 118 */     Assert.notEmpty(cacheOperationSources, "At least 1 CacheOperationSource needs to be specified");
/* 119 */     this.cacheOperationSource = (cacheOperationSources.length > 1 ? new CompositeCacheOperationSource(cacheOperationSources) : cacheOperationSources[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CacheOperationSource getCacheOperationSource()
/*     */   {
/* 127 */     return this.cacheOperationSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setKeyGenerator(KeyGenerator keyGenerator)
/*     */   {
/* 136 */     this.keyGenerator = keyGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public KeyGenerator getKeyGenerator()
/*     */   {
/* 143 */     return this.keyGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheResolver(CacheResolver cacheResolver)
/*     */   {
/* 155 */     this.cacheResolver = cacheResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CacheResolver getCacheResolver()
/*     */   {
/* 162 */     return this.cacheResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheManager(CacheManager cacheManager)
/*     */   {
/* 172 */     this.cacheResolver = new SimpleCacheResolver(cacheManager);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/* 182 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/* 190 */     this.beanFactory = applicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 196 */     Assert.state(getCacheOperationSource() != null, "The 'cacheOperationSources' property is required: If there are no cacheable methods, then don't use a cache aspect.");
/*     */     
/* 198 */     Assert.state(getErrorHandler() != null, "The 'errorHandler' property is required");
/*     */   }
/*     */   
/*     */   public void afterSingletonsInstantiated()
/*     */   {
/* 203 */     if (getCacheResolver() == null) {
/*     */       try
/*     */       {
/* 206 */         setCacheManager((CacheManager)this.beanFactory.getBean(CacheManager.class));
/*     */       }
/*     */       catch (NoUniqueBeanDefinitionException ex) {
/* 209 */         throw new IllegalStateException("No CacheResolver specified, and no unique bean of type CacheManager found. Mark one as primary (or give it the name 'cacheManager') or declare a specific CacheManager to use, that serves as the default one.");
/*     */ 
/*     */       }
/*     */       catch (NoSuchBeanDefinitionException ex)
/*     */       {
/* 214 */         throw new IllegalStateException("No CacheResolver specified, and no bean of type CacheManager found. Register a CacheManager bean or remove the @EnableCaching annotation from your configuration.");
/*     */       }
/*     */     }
/*     */     
/* 218 */     this.initialized = true;
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
/*     */   protected String methodIdentification(Method method, Class<?> targetClass)
/*     */   {
/* 232 */     Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/* 233 */     return ClassUtils.getQualifiedMethodName(specificMethod);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Collection<? extends Cache> getCaches(CacheOperationInvocationContext<CacheOperation> context, CacheResolver cacheResolver)
/*     */   {
/* 239 */     Collection<? extends Cache> caches = cacheResolver.resolveCaches(context);
/* 240 */     if (caches.isEmpty())
/*     */     {
/* 242 */       throw new IllegalStateException("No cache could be resolved for '" + context.getOperation() + "' using resolver '" + cacheResolver + "'. At least one cache should be provided per cache operation.");
/*     */     }
/*     */     
/* 245 */     return caches;
/*     */   }
/*     */   
/*     */ 
/*     */   protected CacheOperationContext getOperationContext(CacheOperation operation, Method method, Object[] args, Object target, Class<?> targetClass)
/*     */   {
/* 251 */     CacheOperationMetadata metadata = getCacheOperationMetadata(operation, method, targetClass);
/* 252 */     return new CacheOperationContext(metadata, args, target);
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
/*     */   protected CacheOperationMetadata getCacheOperationMetadata(CacheOperation operation, Method method, Class<?> targetClass)
/*     */   {
/* 267 */     CacheOperationCacheKey cacheKey = new CacheOperationCacheKey(operation, method, targetClass, null);
/* 268 */     CacheOperationMetadata metadata = (CacheOperationMetadata)this.metadataCache.get(cacheKey);
/* 269 */     if (metadata == null) { KeyGenerator operationKeyGenerator;
/*     */       KeyGenerator operationKeyGenerator;
/* 271 */       if (StringUtils.hasText(operation.getKeyGenerator())) {
/* 272 */         operationKeyGenerator = (KeyGenerator)getBean(operation.getKeyGenerator(), KeyGenerator.class);
/*     */       }
/*     */       else
/* 275 */         operationKeyGenerator = getKeyGenerator();
/*     */       CacheResolver operationCacheResolver;
/*     */       CacheResolver operationCacheResolver;
/* 278 */       if (StringUtils.hasText(operation.getCacheResolver())) {
/* 279 */         operationCacheResolver = (CacheResolver)getBean(operation.getCacheResolver(), CacheResolver.class);
/*     */       } else { CacheResolver operationCacheResolver;
/* 281 */         if (StringUtils.hasText(operation.getCacheManager())) {
/* 282 */           CacheManager cacheManager = (CacheManager)getBean(operation.getCacheManager(), CacheManager.class);
/* 283 */           operationCacheResolver = new SimpleCacheResolver(cacheManager);
/*     */         }
/*     */         else {
/* 286 */           operationCacheResolver = getCacheResolver();
/*     */         } }
/* 288 */       metadata = new CacheOperationMetadata(operation, method, targetClass, operationKeyGenerator, operationCacheResolver);
/*     */       
/* 290 */       this.metadataCache.put(cacheKey, metadata);
/*     */     }
/* 292 */     return metadata;
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
/*     */   protected <T> T getBean(String beanName, Class<T> expectedType)
/*     */   {
/* 307 */     return (T)BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.beanFactory, expectedType, beanName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void clearMetadataCache()
/*     */   {
/* 314 */     this.metadataCache.clear();
/* 315 */     this.evaluator.clear();
/*     */   }
/*     */   
/*     */   protected Object execute(CacheOperationInvoker invoker, Object target, Method method, Object[] args)
/*     */   {
/* 320 */     if (this.initialized) {
/* 321 */       Class<?> targetClass = getTargetClass(target);
/* 322 */       Collection<CacheOperation> operations = getCacheOperationSource().getCacheOperations(method, targetClass);
/* 323 */       if (!CollectionUtils.isEmpty(operations)) {
/* 324 */         return execute(invoker, method, new CacheOperationContexts(operations, method, args, target, targetClass));
/*     */       }
/*     */     }
/*     */     
/* 328 */     return invoker.invoke();
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
/*     */   protected Object invokeOperation(CacheOperationInvoker invoker)
/*     */   {
/* 342 */     return invoker.invoke();
/*     */   }
/*     */   
/*     */   private Class<?> getTargetClass(Object target) {
/* 346 */     Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
/* 347 */     if ((targetClass == null) && (target != null)) {
/* 348 */       targetClass = target.getClass();
/*     */     }
/* 350 */     return targetClass;
/*     */   }
/*     */   
/*     */   private Object execute(final CacheOperationInvoker invoker, Method method, CacheOperationContexts contexts)
/*     */   {
/* 355 */     if (contexts.isSynchronized()) {
/* 356 */       CacheOperationContext context = (CacheOperationContext)contexts.get(CacheableOperation.class).iterator().next();
/* 357 */       if (isConditionPassing(context, CacheOperationExpressionEvaluator.NO_RESULT)) {
/* 358 */         Object key = generateKey(context, CacheOperationExpressionEvaluator.NO_RESULT);
/* 359 */         Cache cache = (Cache)context.getCaches().iterator().next();
/*     */         try {
/* 361 */           wrapCacheValue(method, cache.get(key, new Callable()
/*     */           {
/*     */             public Object call() throws Exception {
/* 364 */               return CacheAspectSupport.this.unwrapReturnValue(CacheAspectSupport.this.invokeOperation(invoker));
/*     */             }
/*     */             
/*     */           }));
/*     */         }
/*     */         catch (Cache.ValueRetrievalException ex)
/*     */         {
/* 371 */           throw ((CacheOperationInvoker.ThrowableWrapper)ex.getCause());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 376 */       return invokeOperation(invoker);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 382 */     processCacheEvicts(contexts.get(CacheEvictOperation.class), true, CacheOperationExpressionEvaluator.NO_RESULT);
/*     */     
/*     */ 
/*     */ 
/* 386 */     Cache.ValueWrapper cacheHit = findCachedItem(contexts.get(CacheableOperation.class));
/*     */     
/*     */ 
/* 389 */     List<CachePutRequest> cachePutRequests = new LinkedList();
/* 390 */     if (cacheHit == null) {
/* 391 */       collectPutRequests(contexts.get(CacheableOperation.class), CacheOperationExpressionEvaluator.NO_RESULT, cachePutRequests);
/*     */     }
/*     */     
/*     */     Object returnValue;
/*     */     
/*     */     Object returnValue;
/*     */     Object cacheValue;
/* 398 */     if ((cacheHit != null) && (cachePutRequests.isEmpty()) && (!hasCachePut(contexts)))
/*     */     {
/* 400 */       Object cacheValue = cacheHit.get();
/* 401 */       returnValue = wrapCacheValue(method, cacheValue);
/*     */     }
/*     */     else
/*     */     {
/* 405 */       returnValue = invokeOperation(invoker);
/* 406 */       cacheValue = unwrapReturnValue(returnValue);
/*     */     }
/*     */     
/*     */ 
/* 410 */     collectPutRequests(contexts.get(CachePutOperation.class), cacheValue, cachePutRequests);
/*     */     
/*     */ 
/* 413 */     for (CachePutRequest cachePutRequest : cachePutRequests) {
/* 414 */       cachePutRequest.apply(cacheValue);
/*     */     }
/*     */     
/*     */ 
/* 418 */     processCacheEvicts(contexts.get(CacheEvictOperation.class), false, cacheValue);
/*     */     
/* 420 */     return returnValue;
/*     */   }
/*     */   
/*     */   private Object wrapCacheValue(Method method, Object cacheValue) {
/* 424 */     if ((method.getReturnType() == javaUtilOptionalClass) && ((cacheValue == null) || 
/* 425 */       (cacheValue.getClass() != javaUtilOptionalClass))) {
/* 426 */       return OptionalUnwrapper.wrap(cacheValue);
/*     */     }
/* 428 */     return cacheValue;
/*     */   }
/*     */   
/*     */   private Object unwrapReturnValue(Object returnValue) {
/* 432 */     if ((returnValue != null) && (returnValue.getClass() == javaUtilOptionalClass)) {
/* 433 */       return OptionalUnwrapper.unwrap(returnValue);
/*     */     }
/* 435 */     return returnValue;
/*     */   }
/*     */   
/*     */   private boolean hasCachePut(CacheOperationContexts contexts)
/*     */   {
/* 440 */     Collection<CacheOperationContext> cachePutContexts = contexts.get(CachePutOperation.class);
/* 441 */     Collection<CacheOperationContext> excluded = new ArrayList();
/* 442 */     for (CacheOperationContext context : cachePutContexts) {
/*     */       try {
/* 444 */         if (!context.isConditionPassing(CacheOperationExpressionEvaluator.RESULT_UNAVAILABLE)) {
/* 445 */           excluded.add(context);
/*     */         }
/*     */       }
/*     */       catch (VariableNotAvailableException localVariableNotAvailableException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 453 */     return cachePutContexts.size() != excluded.size();
/*     */   }
/*     */   
/*     */   private void processCacheEvicts(Collection<CacheOperationContext> contexts, boolean beforeInvocation, Object result) {
/* 457 */     for (CacheOperationContext context : contexts) {
/* 458 */       CacheEvictOperation operation = (CacheEvictOperation)CacheOperationContext.access$200(context).operation;
/* 459 */       if ((beforeInvocation == operation.isBeforeInvocation()) && (isConditionPassing(context, result))) {
/* 460 */         performCacheEvict(context, operation, result);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void performCacheEvict(CacheOperationContext context, CacheEvictOperation operation, Object result) {
/* 466 */     Object key = null;
/* 467 */     for (Cache cache : context.getCaches()) {
/* 468 */       if (operation.isCacheWide()) {
/* 469 */         logInvalidating(context, operation, null);
/* 470 */         doClear(cache);
/*     */       }
/*     */       else {
/* 473 */         if (key == null) {
/* 474 */           key = context.generateKey(result);
/*     */         }
/* 476 */         logInvalidating(context, operation, key);
/* 477 */         doEvict(cache, key);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void logInvalidating(CacheOperationContext context, CacheEvictOperation operation, Object key) {
/* 483 */     if (this.logger.isTraceEnabled()) {
/* 484 */       this.logger.trace("Invalidating " + (key != null ? "cache key [" + key + "]" : "entire cache") + " for operation " + operation + " on method " + 
/* 485 */         CacheOperationContext.access$200(context).method);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Cache.ValueWrapper findCachedItem(Collection<CacheOperationContext> contexts)
/*     */   {
/* 496 */     Object result = CacheOperationExpressionEvaluator.NO_RESULT;
/* 497 */     for (CacheOperationContext context : contexts) {
/* 498 */       if (isConditionPassing(context, result)) {
/* 499 */         Object key = generateKey(context, result);
/* 500 */         Cache.ValueWrapper cached = findInCaches(context, key);
/* 501 */         if (cached != null) {
/* 502 */           return cached;
/*     */         }
/*     */         
/* 505 */         if (this.logger.isTraceEnabled()) {
/* 506 */           this.logger.trace("No cache entry for key '" + key + "' in cache(s) " + context.getCacheNames());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 511 */     return null;
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
/*     */   private void collectPutRequests(Collection<CacheOperationContext> contexts, Object result, Collection<CachePutRequest> putRequests)
/*     */   {
/* 524 */     for (CacheOperationContext context : contexts) {
/* 525 */       if (isConditionPassing(context, result)) {
/* 526 */         Object key = generateKey(context, result);
/* 527 */         putRequests.add(new CachePutRequest(context, key));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Cache.ValueWrapper findInCaches(CacheOperationContext context, Object key) {
/* 533 */     for (Cache cache : context.getCaches()) {
/* 534 */       Cache.ValueWrapper wrapper = doGet(cache, key);
/* 535 */       if (wrapper != null) {
/* 536 */         if (this.logger.isTraceEnabled()) {
/* 537 */           this.logger.trace("Cache entry for key '" + key + "' found in cache '" + cache.getName() + "'");
/*     */         }
/* 539 */         return wrapper;
/*     */       }
/*     */     }
/* 542 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isConditionPassing(CacheOperationContext context, Object result) {
/* 546 */     boolean passing = context.isConditionPassing(result);
/* 547 */     if ((!passing) && (this.logger.isTraceEnabled())) {
/* 548 */       this.logger.trace("Cache condition failed on method " + CacheOperationContext.access$200(context).method + " for operation " + 
/* 549 */         CacheOperationContext.access$200(context).operation);
/*     */     }
/* 551 */     return passing;
/*     */   }
/*     */   
/*     */   private Object generateKey(CacheOperationContext context, Object result) {
/* 555 */     Object key = context.generateKey(result);
/* 556 */     if (key == null)
/*     */     {
/* 558 */       throw new IllegalArgumentException("Null key returned for cache operation (maybe you are using named params on classes without debug info?) " + CacheOperationContext.access$200(context).operation);
/*     */     }
/* 560 */     if (this.logger.isTraceEnabled()) {
/* 561 */       this.logger.trace("Computed cache key '" + key + "' for operation " + CacheOperationContext.access$200(context).operation);
/*     */     }
/* 563 */     return key;
/*     */   }
/*     */   
/*     */ 
/*     */   private class CacheOperationContexts
/*     */   {
/* 569 */     private final MultiValueMap<Class<? extends CacheOperation>, CacheAspectSupport.CacheOperationContext> contexts = new LinkedMultiValueMap();
/*     */     
/*     */ 
/*     */     private final boolean sync;
/*     */     
/*     */ 
/*     */     public CacheOperationContexts(Method operations, Object[] method, Object args, Class<?> target)
/*     */     {
/* 577 */       for (CacheOperation operation : operations) {
/* 578 */         this.contexts.add(operation.getClass(), CacheAspectSupport.this.getOperationContext(operation, method, args, target, targetClass));
/*     */       }
/* 580 */       this.sync = determineSyncFlag(method);
/*     */     }
/*     */     
/*     */     public Collection<CacheAspectSupport.CacheOperationContext> get(Class<? extends CacheOperation> operationClass) {
/* 584 */       Collection<CacheAspectSupport.CacheOperationContext> result = (Collection)this.contexts.get(operationClass);
/* 585 */       return result != null ? result : Collections.emptyList();
/*     */     }
/*     */     
/*     */     public boolean isSynchronized() {
/* 589 */       return this.sync;
/*     */     }
/*     */     
/*     */     private boolean determineSyncFlag(Method method) {
/* 593 */       List<CacheAspectSupport.CacheOperationContext> cacheOperationContexts = (List)this.contexts.get(CacheableOperation.class);
/* 594 */       if (cacheOperationContexts == null) {
/* 595 */         return false;
/*     */       }
/* 597 */       boolean syncEnabled = false;
/* 598 */       for (CacheAspectSupport.CacheOperationContext cacheOperationContext : cacheOperationContexts) {
/* 599 */         if (((CacheableOperation)cacheOperationContext.getOperation()).isSync()) {
/* 600 */           syncEnabled = true;
/* 601 */           break;
/*     */         }
/*     */       }
/* 604 */       if (syncEnabled) {
/* 605 */         if (this.contexts.size() > 1) {
/* 606 */           throw new IllegalStateException("@Cacheable(sync=true) cannot be combined with other cache operations on '" + method + "'");
/*     */         }
/* 608 */         if (cacheOperationContexts.size() > 1) {
/* 609 */           throw new IllegalStateException("Only one @Cacheable(sync=true) entry is allowed on '" + method + "'");
/*     */         }
/* 611 */         CacheAspectSupport.CacheOperationContext cacheOperationContext = (CacheAspectSupport.CacheOperationContext)cacheOperationContexts.iterator().next();
/* 612 */         CacheableOperation operation = (CacheableOperation)cacheOperationContext.getOperation();
/* 613 */         if (cacheOperationContext.getCaches().size() > 1) {
/* 614 */           throw new IllegalStateException("@Cacheable(sync=true) only allows a single cache on '" + operation + "'");
/*     */         }
/* 616 */         if (StringUtils.hasText(operation.getUnless())) {
/* 617 */           throw new IllegalStateException("@Cacheable(sync=true) does not support unless attribute on '" + operation + "'");
/*     */         }
/* 619 */         return true;
/*     */       }
/* 621 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class CacheOperationMetadata
/*     */   {
/*     */     private final CacheOperation operation;
/*     */     
/*     */ 
/*     */     private final Method method;
/*     */     
/*     */ 
/*     */     private final Class<?> targetClass;
/*     */     
/*     */ 
/*     */     private final KeyGenerator keyGenerator;
/*     */     
/*     */     private final CacheResolver cacheResolver;
/*     */     
/*     */ 
/*     */     public CacheOperationMetadata(CacheOperation operation, Method method, Class<?> targetClass, KeyGenerator keyGenerator, CacheResolver cacheResolver)
/*     */     {
/* 645 */       this.operation = operation;
/* 646 */       this.method = method;
/* 647 */       this.targetClass = targetClass;
/* 648 */       this.keyGenerator = keyGenerator;
/* 649 */       this.cacheResolver = cacheResolver;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected class CacheOperationContext
/*     */     implements CacheOperationInvocationContext<CacheOperation>
/*     */   {
/*     */     private final CacheAspectSupport.CacheOperationMetadata metadata;
/*     */     
/*     */     private final Object[] args;
/*     */     
/*     */     private final Object target;
/*     */     
/*     */     private final Collection<? extends Cache> caches;
/*     */     private final Collection<String> cacheNames;
/*     */     private final AnnotatedElementKey methodCacheKey;
/*     */     
/*     */     public CacheOperationContext(CacheAspectSupport.CacheOperationMetadata metadata, Object[] args, Object target)
/*     */     {
/* 669 */       this.metadata = metadata;
/* 670 */       this.args = extractArgs(CacheAspectSupport.CacheOperationMetadata.access$400(metadata), args);
/* 671 */       this.target = target;
/* 672 */       this.caches = CacheAspectSupport.this.getCaches(this, CacheAspectSupport.CacheOperationMetadata.access$500(metadata));
/* 673 */       this.cacheNames = createCacheNames(this.caches);
/* 674 */       this.methodCacheKey = new AnnotatedElementKey(CacheAspectSupport.CacheOperationMetadata.access$400(metadata), CacheAspectSupport.CacheOperationMetadata.access$600(metadata));
/*     */     }
/*     */     
/*     */     public CacheOperation getOperation()
/*     */     {
/* 679 */       return CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata);
/*     */     }
/*     */     
/*     */     public Object getTarget()
/*     */     {
/* 684 */       return this.target;
/*     */     }
/*     */     
/*     */     public Method getMethod()
/*     */     {
/* 689 */       return CacheAspectSupport.CacheOperationMetadata.access$400(this.metadata);
/*     */     }
/*     */     
/*     */     public Object[] getArgs()
/*     */     {
/* 694 */       return this.args;
/*     */     }
/*     */     
/*     */     private Object[] extractArgs(Method method, Object[] args) {
/* 698 */       if (!method.isVarArgs()) {
/* 699 */         return args;
/*     */       }
/* 701 */       Object[] varArgs = ObjectUtils.toObjectArray(args[(args.length - 1)]);
/* 702 */       Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
/* 703 */       System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
/* 704 */       System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
/* 705 */       return combinedArgs;
/*     */     }
/*     */     
/*     */     protected boolean isConditionPassing(Object result) {
/* 709 */       if (StringUtils.hasText(CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata).getCondition())) {
/* 710 */         EvaluationContext evaluationContext = createEvaluationContext(result);
/* 711 */         return CacheAspectSupport.this.evaluator.condition(CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata).getCondition(), this.methodCacheKey, evaluationContext);
/*     */       }
/*     */       
/* 714 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean canPutToCache(Object value) {
/* 718 */       String unless = "";
/* 719 */       if ((CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata) instanceof CacheableOperation)) {
/* 720 */         unless = ((CacheableOperation)CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata)).getUnless();
/*     */       }
/* 722 */       else if ((CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata) instanceof CachePutOperation)) {
/* 723 */         unless = ((CachePutOperation)CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata)).getUnless();
/*     */       }
/* 725 */       if (StringUtils.hasText(unless)) {
/* 726 */         EvaluationContext evaluationContext = createEvaluationContext(value);
/* 727 */         return !CacheAspectSupport.this.evaluator.unless(unless, this.methodCacheKey, evaluationContext);
/*     */       }
/* 729 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Object generateKey(Object result)
/*     */     {
/* 737 */       if (StringUtils.hasText(CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata).getKey())) {
/* 738 */         EvaluationContext evaluationContext = createEvaluationContext(result);
/* 739 */         return CacheAspectSupport.this.evaluator.key(CacheAspectSupport.CacheOperationMetadata.access$300(this.metadata).getKey(), this.methodCacheKey, evaluationContext);
/*     */       }
/* 741 */       return CacheAspectSupport.CacheOperationMetadata.access$800(this.metadata).generate(this.target, CacheAspectSupport.CacheOperationMetadata.access$400(this.metadata), this.args);
/*     */     }
/*     */     
/*     */     private EvaluationContext createEvaluationContext(Object result) {
/* 745 */       return CacheAspectSupport.this.evaluator.createEvaluationContext(this.caches, CacheAspectSupport.CacheOperationMetadata.access$400(this.metadata), this.args, this.target, 
/* 746 */         CacheAspectSupport.CacheOperationMetadata.access$600(this.metadata), result, CacheAspectSupport.this.beanFactory);
/*     */     }
/*     */     
/*     */     protected Collection<? extends Cache> getCaches() {
/* 750 */       return this.caches;
/*     */     }
/*     */     
/*     */     protected Collection<String> getCacheNames() {
/* 754 */       return this.cacheNames;
/*     */     }
/*     */     
/*     */     private Collection<String> createCacheNames(Collection<? extends Cache> caches) {
/* 758 */       Collection<String> names = new ArrayList();
/* 759 */       for (Cache cache : caches) {
/* 760 */         names.add(cache.getName());
/*     */       }
/* 762 */       return names;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class CachePutRequest
/*     */   {
/*     */     private final CacheAspectSupport.CacheOperationContext context;
/*     */     private final Object key;
/*     */     
/*     */     public CachePutRequest(CacheAspectSupport.CacheOperationContext context, Object key)
/*     */     {
/* 774 */       this.context = context;
/* 775 */       this.key = key;
/*     */     }
/*     */     
/*     */     public void apply(Object result) {
/* 779 */       if (this.context.canPutToCache(result)) {
/* 780 */         for (Cache cache : this.context.getCaches()) {
/* 781 */           CacheAspectSupport.this.doPut(cache, this.key, result);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class CacheOperationCacheKey
/*     */     implements Comparable<CacheOperationCacheKey>
/*     */   {
/*     */     private final CacheOperation cacheOperation;
/*     */     private final AnnotatedElementKey methodCacheKey;
/*     */     
/*     */     private CacheOperationCacheKey(CacheOperation cacheOperation, Method method, Class<?> targetClass)
/*     */     {
/* 795 */       this.cacheOperation = cacheOperation;
/* 796 */       this.methodCacheKey = new AnnotatedElementKey(method, targetClass);
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 801 */       if (this == other) {
/* 802 */         return true;
/*     */       }
/* 804 */       if (!(other instanceof CacheOperationCacheKey)) {
/* 805 */         return false;
/*     */       }
/* 807 */       CacheOperationCacheKey otherKey = (CacheOperationCacheKey)other;
/* 808 */       return (this.cacheOperation.equals(otherKey.cacheOperation)) && 
/* 809 */         (this.methodCacheKey.equals(otherKey.methodCacheKey));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 814 */       return this.cacheOperation.hashCode() * 31 + this.methodCacheKey.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 819 */       return this.cacheOperation + " on " + this.methodCacheKey;
/*     */     }
/*     */     
/*     */     public int compareTo(CacheOperationCacheKey other)
/*     */     {
/* 824 */       int result = this.cacheOperation.getName().compareTo(other.cacheOperation.getName());
/* 825 */       if (result == 0) {
/* 826 */         result = this.methodCacheKey.compareTo(other.methodCacheKey);
/*     */       }
/* 828 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @UsesJava8
/*     */   private static class OptionalUnwrapper
/*     */   {
/*     */     public static Object unwrap(Object optionalObject)
/*     */     {
/* 840 */       Optional<?> optional = (Optional)optionalObject;
/* 841 */       if (!optional.isPresent()) {
/* 842 */         return null;
/*     */       }
/* 844 */       Object result = optional.get();
/* 845 */       Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
/* 846 */       return result;
/*     */     }
/*     */     
/*     */     public static Object wrap(Object value) {
/* 850 */       return Optional.ofNullable(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheAspectSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */