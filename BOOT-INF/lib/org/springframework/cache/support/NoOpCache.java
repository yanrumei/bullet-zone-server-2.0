/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.Cache.ValueRetrievalException;
/*    */ import org.springframework.cache.Cache.ValueWrapper;
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
/*    */ 
/*    */ 
/*    */ public class NoOpCache
/*    */   implements Cache
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public NoOpCache(String name)
/*    */   {
/* 43 */     Assert.notNull(name, "Cache name must not be null");
/* 44 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 50 */     return this.name;
/*    */   }
/*    */   
/*    */   public Object getNativeCache()
/*    */   {
/* 55 */     return null;
/*    */   }
/*    */   
/*    */   public Cache.ValueWrapper get(Object key)
/*    */   {
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public <T> T get(Object key, Class<T> type)
/*    */   {
/* 65 */     return null;
/*    */   }
/*    */   
/*    */   public <T> T get(Object key, Callable<T> valueLoader)
/*    */   {
/*    */     try {
/* 71 */       return (T)valueLoader.call();
/*    */     }
/*    */     catch (Exception ex) {
/* 74 */       throw new Cache.ValueRetrievalException(key, valueLoader, ex);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void put(Object key, Object value) {}
/*    */   
/*    */ 
/*    */   public Cache.ValueWrapper putIfAbsent(Object key, Object value)
/*    */   {
/* 84 */     return null;
/*    */   }
/*    */   
/*    */   public void evict(Object key) {}
/*    */   
/*    */   public void clear() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\support\NoOpCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */