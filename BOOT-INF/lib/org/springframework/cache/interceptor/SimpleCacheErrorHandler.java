/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import org.springframework.cache.Cache;
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
/*    */ public class SimpleCacheErrorHandler
/*    */   implements CacheErrorHandler
/*    */ {
/*    */   public void handleCacheGetError(RuntimeException exception, Cache cache, Object key)
/*    */   {
/* 32 */     throw exception;
/*    */   }
/*    */   
/*    */   public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value)
/*    */   {
/* 37 */     throw exception;
/*    */   }
/*    */   
/*    */   public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key)
/*    */   {
/* 42 */     throw exception;
/*    */   }
/*    */   
/*    */   public void handleCacheClearError(RuntimeException exception, Cache cache)
/*    */   {
/* 47 */     throw exception;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\SimpleCacheErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */