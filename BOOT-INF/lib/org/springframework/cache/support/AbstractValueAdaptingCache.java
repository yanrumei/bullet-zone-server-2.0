/*     */ package org.springframework.cache.support;
/*     */ 
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.Cache.ValueWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractValueAdaptingCache
/*     */   implements Cache
/*     */ {
/*     */   private final boolean allowNullValues;
/*     */   
/*     */   protected AbstractValueAdaptingCache(boolean allowNullValues)
/*     */   {
/*  43 */     this.allowNullValues = allowNullValues;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isAllowNullValues()
/*     */   {
/*  51 */     return this.allowNullValues;
/*     */   }
/*     */   
/*     */   public Cache.ValueWrapper get(Object key)
/*     */   {
/*  56 */     Object value = lookup(key);
/*  57 */     return toValueWrapper(value);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T get(Object key, Class<T> type)
/*     */   {
/*  63 */     Object value = fromStoreValue(lookup(key));
/*  64 */     if ((value != null) && (type != null) && (!type.isInstance(value))) {
/*  65 */       throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
/*     */     }
/*  67 */     return (T)value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Object lookup(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object fromStoreValue(Object storeValue)
/*     */   {
/*  85 */     if ((this.allowNullValues) && (storeValue == NullValue.INSTANCE)) {
/*  86 */       return null;
/*     */     }
/*  88 */     return storeValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object toStoreValue(Object userValue)
/*     */   {
/*  98 */     if ((this.allowNullValues) && (userValue == null)) {
/*  99 */       return NullValue.INSTANCE;
/*     */     }
/* 101 */     return userValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Cache.ValueWrapper toValueWrapper(Object storeValue)
/*     */   {
/* 112 */     return storeValue != null ? new SimpleValueWrapper(fromStoreValue(storeValue)) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\support\AbstractValueAdaptingCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */