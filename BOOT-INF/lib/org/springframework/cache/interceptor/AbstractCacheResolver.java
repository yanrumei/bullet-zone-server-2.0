/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCacheResolver
/*    */   implements CacheResolver, InitializingBean
/*    */ {
/*    */   private CacheManager cacheManager;
/*    */   
/*    */   protected AbstractCacheResolver() {}
/*    */   
/*    */   protected AbstractCacheResolver(CacheManager cacheManager)
/*    */   {
/* 45 */     this.cacheManager = cacheManager;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setCacheManager(CacheManager cacheManager)
/*    */   {
/* 53 */     this.cacheManager = cacheManager;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public CacheManager getCacheManager()
/*    */   {
/* 60 */     return this.cacheManager;
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 65 */     Assert.notNull(this.cacheManager, "CacheManager is required");
/*    */   }
/*    */   
/*    */ 
/*    */   public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context)
/*    */   {
/* 71 */     Collection<String> cacheNames = getCacheNames(context);
/* 72 */     if (cacheNames == null) {
/* 73 */       return Collections.emptyList();
/*    */     }
/*    */     
/* 76 */     Collection<Cache> result = new ArrayList();
/* 77 */     for (String cacheName : cacheNames) {
/* 78 */       Cache cache = getCacheManager().getCache(cacheName);
/* 79 */       if (cache == null)
/*    */       {
/* 81 */         throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for " + context.getOperation());
/*    */       }
/* 83 */       result.add(cache);
/*    */     }
/* 85 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract Collection<String> getCacheNames(CacheOperationInvocationContext<?> paramCacheOperationInvocationContext);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\AbstractCacheResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */