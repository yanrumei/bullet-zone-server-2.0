/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.Cache.ValueWrapper;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractCacheInvoker
/*     */ {
/*     */   private CacheErrorHandler errorHandler;
/*     */   
/*     */   protected AbstractCacheInvoker()
/*     */   {
/*  36 */     this(new SimpleCacheErrorHandler());
/*     */   }
/*     */   
/*     */   protected AbstractCacheInvoker(CacheErrorHandler errorHandler) {
/*  40 */     Assert.notNull(errorHandler, "ErrorHandler must not be null");
/*  41 */     this.errorHandler = errorHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setErrorHandler(CacheErrorHandler errorHandler)
/*     */   {
/*  51 */     this.errorHandler = errorHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CacheErrorHandler getErrorHandler()
/*     */   {
/*  58 */     return this.errorHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Cache.ValueWrapper doGet(Cache cache, Object key)
/*     */   {
/*     */     try
/*     */     {
/*  71 */       return cache.get(key);
/*     */     }
/*     */     catch (RuntimeException ex) {
/*  74 */       getErrorHandler().handleCacheGetError(ex, cache, key); }
/*  75 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPut(Cache cache, Object key, Object result)
/*     */   {
/*     */     try
/*     */     {
/*  85 */       cache.put(key, result);
/*     */     }
/*     */     catch (RuntimeException ex) {
/*  88 */       getErrorHandler().handleCachePutError(ex, cache, key, result);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void doEvict(Cache cache, Object key)
/*     */   {
/*     */     try
/*     */     {
/*  98 */       cache.evict(key);
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 101 */       getErrorHandler().handleCacheEvictError(ex, cache, key);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void doClear(Cache cache)
/*     */   {
/*     */     try
/*     */     {
/* 111 */       cache.clear();
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 114 */       getErrorHandler().handleCacheClearError(ex, cache);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\AbstractCacheInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */