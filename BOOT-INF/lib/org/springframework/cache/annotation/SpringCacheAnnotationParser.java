/*     */ package org.springframework.cache.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.springframework.cache.interceptor.CacheEvictOperation;
/*     */ import org.springframework.cache.interceptor.CacheEvictOperation.Builder;
/*     */ import org.springframework.cache.interceptor.CacheOperation;
/*     */ import org.springframework.cache.interceptor.CacheOperation.Builder;
/*     */ import org.springframework.cache.interceptor.CachePutOperation;
/*     */ import org.springframework.cache.interceptor.CachePutOperation.Builder;
/*     */ import org.springframework.cache.interceptor.CacheableOperation;
/*     */ import org.springframework.cache.interceptor.CacheableOperation.Builder;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
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
/*     */ public class SpringCacheAnnotationParser
/*     */   implements CacheAnnotationParser, Serializable
/*     */ {
/*     */   public Collection<CacheOperation> parseCacheAnnotations(Class<?> type)
/*     */   {
/*  51 */     DefaultCacheConfig defaultConfig = getDefaultCacheConfig(type);
/*  52 */     return parseCacheAnnotations(defaultConfig, type);
/*     */   }
/*     */   
/*     */   public Collection<CacheOperation> parseCacheAnnotations(Method method)
/*     */   {
/*  57 */     DefaultCacheConfig defaultConfig = getDefaultCacheConfig(method.getDeclaringClass());
/*  58 */     return parseCacheAnnotations(defaultConfig, method);
/*     */   }
/*     */   
/*     */   protected Collection<CacheOperation> parseCacheAnnotations(DefaultCacheConfig cachingConfig, AnnotatedElement ae) {
/*  62 */     Collection<CacheOperation> ops = null;
/*     */     
/*  64 */     Collection<Cacheable> cacheables = AnnotatedElementUtils.getAllMergedAnnotations(ae, Cacheable.class);
/*  65 */     Iterator localIterator; if (!cacheables.isEmpty()) {
/*  66 */       ops = lazyInit(ops);
/*  67 */       for (localIterator = cacheables.iterator(); localIterator.hasNext();) { cacheable = (Cacheable)localIterator.next();
/*  68 */         ops.add(parseCacheableAnnotation(ae, cachingConfig, cacheable));
/*     */       } }
/*     */     Cacheable cacheable;
/*  71 */     Object evicts = AnnotatedElementUtils.getAllMergedAnnotations(ae, CacheEvict.class);
/*  72 */     if (!((Collection)evicts).isEmpty()) {
/*  73 */       ops = lazyInit(ops);
/*  74 */       for (cacheable = ((Collection)evicts).iterator(); cacheable.hasNext();) { evict = (CacheEvict)cacheable.next();
/*  75 */         ops.add(parseEvictAnnotation(ae, cachingConfig, evict));
/*     */       } }
/*     */     CacheEvict evict;
/*  78 */     Collection<CachePut> puts = AnnotatedElementUtils.getAllMergedAnnotations(ae, CachePut.class);
/*  79 */     if (!puts.isEmpty()) {
/*  80 */       ops = lazyInit(ops);
/*  81 */       for (evict = puts.iterator(); evict.hasNext();) { put = (CachePut)evict.next();
/*  82 */         ops.add(parsePutAnnotation(ae, cachingConfig, put));
/*     */       } }
/*     */     CachePut put;
/*  85 */     Collection<Caching> cachings = AnnotatedElementUtils.getAllMergedAnnotations(ae, Caching.class);
/*  86 */     if (!cachings.isEmpty()) {
/*  87 */       ops = lazyInit(ops);
/*  88 */       for (Caching caching : cachings) {
/*  89 */         Collection<CacheOperation> cachingOps = parseCachingAnnotation(ae, cachingConfig, caching);
/*  90 */         if (cachingOps != null) {
/*  91 */           ops.addAll(cachingOps);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  96 */     return ops;
/*     */   }
/*     */   
/*     */   private <T extends Annotation> Collection<CacheOperation> lazyInit(Collection<CacheOperation> ops) {
/* 100 */     return ops != null ? ops : new ArrayList(1);
/*     */   }
/*     */   
/*     */   CacheableOperation parseCacheableAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, Cacheable cacheable) {
/* 104 */     CacheableOperation.Builder builder = new CacheableOperation.Builder();
/*     */     
/* 106 */     builder.setName(ae.toString());
/* 107 */     builder.setCacheNames(cacheable.cacheNames());
/* 108 */     builder.setCondition(cacheable.condition());
/* 109 */     builder.setUnless(cacheable.unless());
/* 110 */     builder.setKey(cacheable.key());
/* 111 */     builder.setKeyGenerator(cacheable.keyGenerator());
/* 112 */     builder.setCacheManager(cacheable.cacheManager());
/* 113 */     builder.setCacheResolver(cacheable.cacheResolver());
/* 114 */     builder.setSync(cacheable.sync());
/*     */     
/* 116 */     defaultConfig.applyDefault(builder);
/* 117 */     CacheableOperation op = builder.build();
/* 118 */     validateCacheOperation(ae, op);
/*     */     
/* 120 */     return op;
/*     */   }
/*     */   
/*     */   CacheEvictOperation parseEvictAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, CacheEvict cacheEvict) {
/* 124 */     CacheEvictOperation.Builder builder = new CacheEvictOperation.Builder();
/*     */     
/* 126 */     builder.setName(ae.toString());
/* 127 */     builder.setCacheNames(cacheEvict.cacheNames());
/* 128 */     builder.setCondition(cacheEvict.condition());
/* 129 */     builder.setKey(cacheEvict.key());
/* 130 */     builder.setKeyGenerator(cacheEvict.keyGenerator());
/* 131 */     builder.setCacheManager(cacheEvict.cacheManager());
/* 132 */     builder.setCacheResolver(cacheEvict.cacheResolver());
/* 133 */     builder.setCacheWide(cacheEvict.allEntries());
/* 134 */     builder.setBeforeInvocation(cacheEvict.beforeInvocation());
/*     */     
/* 136 */     defaultConfig.applyDefault(builder);
/* 137 */     CacheEvictOperation op = builder.build();
/* 138 */     validateCacheOperation(ae, op);
/*     */     
/* 140 */     return op;
/*     */   }
/*     */   
/*     */   CacheOperation parsePutAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, CachePut cachePut) {
/* 144 */     CachePutOperation.Builder builder = new CachePutOperation.Builder();
/*     */     
/* 146 */     builder.setName(ae.toString());
/* 147 */     builder.setCacheNames(cachePut.cacheNames());
/* 148 */     builder.setCondition(cachePut.condition());
/* 149 */     builder.setUnless(cachePut.unless());
/* 150 */     builder.setKey(cachePut.key());
/* 151 */     builder.setKeyGenerator(cachePut.keyGenerator());
/* 152 */     builder.setCacheManager(cachePut.cacheManager());
/* 153 */     builder.setCacheResolver(cachePut.cacheResolver());
/*     */     
/* 155 */     defaultConfig.applyDefault(builder);
/* 156 */     CachePutOperation op = builder.build();
/* 157 */     validateCacheOperation(ae, op);
/*     */     
/* 159 */     return op;
/*     */   }
/*     */   
/*     */   Collection<CacheOperation> parseCachingAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, Caching caching) {
/* 163 */     Collection<CacheOperation> ops = null;
/*     */     
/* 165 */     Cacheable[] cacheables = caching.cacheable();
/* 166 */     Object localObject; Cacheable cacheable; if (!ObjectUtils.isEmpty(cacheables)) {
/* 167 */       ops = lazyInit(ops);
/* 168 */       Cacheable[] arrayOfCacheable1 = cacheables;int i = arrayOfCacheable1.length; for (localObject = 0; localObject < i; localObject++) { cacheable = arrayOfCacheable1[localObject];
/* 169 */         ops.add(parseCacheableAnnotation(ae, defaultConfig, cacheable));
/*     */       }
/*     */     }
/* 172 */     CacheEvict[] cacheEvicts = caching.evict();
/* 173 */     CacheEvict cacheEvict; if (!ObjectUtils.isEmpty(cacheEvicts)) {
/* 174 */       ops = lazyInit(ops);
/* 175 */       CacheEvict[] arrayOfCacheEvict1 = cacheEvicts;localObject = arrayOfCacheEvict1.length; for (cacheable = 0; cacheable < localObject; cacheable++) { cacheEvict = arrayOfCacheEvict1[cacheable];
/* 176 */         ops.add(parseEvictAnnotation(ae, defaultConfig, cacheEvict));
/*     */       }
/*     */     }
/* 179 */     CachePut[] cachePuts = caching.put();
/* 180 */     if (!ObjectUtils.isEmpty(cachePuts)) {
/* 181 */       ops = lazyInit(ops);
/* 182 */       localObject = cachePuts;cacheable = localObject.length; for (cacheEvict = 0; cacheEvict < cacheable; cacheEvict++) { CachePut cachePut = localObject[cacheEvict];
/* 183 */         ops.add(parsePutAnnotation(ae, defaultConfig, cachePut));
/*     */       }
/*     */     }
/*     */     
/* 187 */     return ops;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   DefaultCacheConfig getDefaultCacheConfig(Class<?> target)
/*     */   {
/* 196 */     CacheConfig annotation = (CacheConfig)AnnotatedElementUtils.getMergedAnnotation(target, CacheConfig.class);
/* 197 */     if (annotation != null) {
/* 198 */       return new DefaultCacheConfig(annotation.cacheNames(), annotation.keyGenerator(), annotation
/* 199 */         .cacheManager(), annotation.cacheResolver(), null);
/*     */     }
/* 201 */     return new DefaultCacheConfig();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void validateCacheOperation(AnnotatedElement ae, CacheOperation operation)
/*     */   {
/* 213 */     if ((StringUtils.hasText(operation.getKey())) && (StringUtils.hasText(operation.getKeyGenerator())))
/*     */     {
/* 215 */       throw new IllegalStateException("Invalid cache annotation configuration on '" + ae.toString() + "'. Both 'key' and 'keyGenerator' attributes have been set. These attributes are mutually exclusive: either set the SpEL expression used tocompute the key at runtime or set the name of the KeyGenerator bean to use.");
/*     */     }
/*     */     
/*     */ 
/* 219 */     if ((StringUtils.hasText(operation.getCacheManager())) && (StringUtils.hasText(operation.getCacheResolver())))
/*     */     {
/* 221 */       throw new IllegalStateException("Invalid cache annotation configuration on '" + ae.toString() + "'. Both 'cacheManager' and 'cacheResolver' attributes have been set. These attributes are mutually exclusive: the cache manager is used to configure adefault cache resolver if none is set. If a cache resolver is set, the cache managerwon't be used.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 230 */     return (this == other) || ((other instanceof SpringCacheAnnotationParser));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 235 */     return SpringCacheAnnotationParser.class.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class DefaultCacheConfig
/*     */   {
/*     */     private final String[] cacheNames;
/*     */     
/*     */     private final String keyGenerator;
/*     */     
/*     */     private final String cacheManager;
/*     */     
/*     */     private final String cacheResolver;
/*     */     
/*     */ 
/*     */     public DefaultCacheConfig()
/*     */     {
/* 253 */       this(null, null, null, null);
/*     */     }
/*     */     
/*     */     private DefaultCacheConfig(String[] cacheNames, String keyGenerator, String cacheManager, String cacheResolver) {
/* 257 */       this.cacheNames = cacheNames;
/* 258 */       this.keyGenerator = keyGenerator;
/* 259 */       this.cacheManager = cacheManager;
/* 260 */       this.cacheResolver = cacheResolver;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void applyDefault(CacheOperation.Builder builder)
/*     */     {
/* 268 */       if ((builder.getCacheNames().isEmpty()) && (this.cacheNames != null)) {
/* 269 */         builder.setCacheNames(this.cacheNames);
/*     */       }
/* 271 */       if ((!StringUtils.hasText(builder.getKey())) && (!StringUtils.hasText(builder.getKeyGenerator())) && 
/* 272 */         (StringUtils.hasText(this.keyGenerator))) {
/* 273 */         builder.setKeyGenerator(this.keyGenerator);
/*     */       }
/*     */       
/* 276 */       if ((!StringUtils.hasText(builder.getCacheManager())) && (!StringUtils.hasText(builder.getCacheResolver())))
/*     */       {
/*     */ 
/* 279 */         if (StringUtils.hasText(this.cacheResolver)) {
/* 280 */           builder.setCacheResolver(this.cacheResolver);
/*     */         }
/* 282 */         else if (StringUtils.hasText(this.cacheManager)) {
/* 283 */           builder.setCacheManager(this.cacheManager);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\SpringCacheAnnotationParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */