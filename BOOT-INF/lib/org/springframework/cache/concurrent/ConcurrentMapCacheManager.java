/*     */ package org.springframework.cache.concurrent;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.core.serializer.support.SerializationDelegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConcurrentMapCacheManager
/*     */   implements CacheManager, BeanClassLoaderAware
/*     */ {
/*  51 */   private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(16);
/*     */   
/*  53 */   private boolean dynamic = true;
/*     */   
/*  55 */   private boolean allowNullValues = true;
/*     */   
/*  57 */   private boolean storeByValue = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private SerializationDelegate serialization;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConcurrentMapCacheManager() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConcurrentMapCacheManager(String... cacheNames)
/*     */   {
/*  74 */     setCacheNames(Arrays.asList(cacheNames));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheNames(Collection<String> cacheNames)
/*     */   {
/*  86 */     if (cacheNames != null) {
/*  87 */       for (String name : cacheNames) {
/*  88 */         this.cacheMap.put(name, createConcurrentMapCache(name));
/*     */       }
/*  90 */       this.dynamic = false;
/*     */     }
/*     */     else {
/*  93 */       this.dynamic = true;
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
/*     */   public void setAllowNullValues(boolean allowNullValues)
/*     */   {
/* 106 */     if (allowNullValues != this.allowNullValues) {
/* 107 */       this.allowNullValues = allowNullValues;
/*     */       
/* 109 */       recreateCaches();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAllowNullValues()
/*     */   {
/* 118 */     return this.allowNullValues;
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
/*     */   public void setStoreByValue(boolean storeByValue)
/*     */   {
/* 131 */     if (storeByValue != this.storeByValue) {
/* 132 */       this.storeByValue = storeByValue;
/*     */       
/* 134 */       recreateCaches();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isStoreByValue()
/*     */   {
/* 145 */     return this.storeByValue;
/*     */   }
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/* 150 */     this.serialization = new SerializationDelegate(classLoader);
/*     */     
/* 152 */     if (isStoreByValue()) {
/* 153 */       recreateCaches();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<String> getCacheNames()
/*     */   {
/* 160 */     return Collections.unmodifiableSet(this.cacheMap.keySet());
/*     */   }
/*     */   
/*     */   public Cache getCache(String name)
/*     */   {
/* 165 */     Cache cache = (Cache)this.cacheMap.get(name);
/* 166 */     if ((cache == null) && (this.dynamic)) {
/* 167 */       synchronized (this.cacheMap) {
/* 168 */         cache = (Cache)this.cacheMap.get(name);
/* 169 */         if (cache == null) {
/* 170 */           cache = createConcurrentMapCache(name);
/* 171 */           this.cacheMap.put(name, cache);
/*     */         }
/*     */       }
/*     */     }
/* 175 */     return cache;
/*     */   }
/*     */   
/*     */   private void recreateCaches() {
/* 179 */     for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
/* 180 */       entry.setValue(createConcurrentMapCache((String)entry.getKey()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Cache createConcurrentMapCache(String name)
/*     */   {
/* 190 */     SerializationDelegate actualSerialization = isStoreByValue() ? this.serialization : null;
/* 191 */     return new ConcurrentMapCache(name, new ConcurrentHashMap(256), 
/* 192 */       isAllowNullValues(), actualSerialization);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\concurrent\ConcurrentMapCacheManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */