/*     */ package org.springframework.cache;
/*     */ 
/*     */ import java.util.concurrent.Callable;
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
/*     */ public abstract interface Cache
/*     */ {
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract Object getNativeCache();
/*     */   
/*     */   public abstract ValueWrapper get(Object paramObject);
/*     */   
/*     */   public abstract <T> T get(Object paramObject, Class<T> paramClass);
/*     */   
/*     */   public abstract <T> T get(Object paramObject, Callable<T> paramCallable);
/*     */   
/*     */   public abstract void put(Object paramObject1, Object paramObject2);
/*     */   
/*     */   public abstract ValueWrapper putIfAbsent(Object paramObject1, Object paramObject2);
/*     */   
/*     */   public abstract void evict(Object paramObject);
/*     */   
/*     */   public abstract void clear();
/*     */   
/*     */   public static class ValueRetrievalException
/*     */     extends RuntimeException
/*     */   {
/*     */     private final Object key;
/*     */     
/*     */     public ValueRetrievalException(Object key, Callable<?> loader, Throwable ex)
/*     */     {
/* 168 */       super(ex);
/* 169 */       this.key = key;
/*     */     }
/*     */     
/*     */     public Object getKey() {
/* 173 */       return this.key;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface ValueWrapper
/*     */   {
/*     */     public abstract Object get();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\Cache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */