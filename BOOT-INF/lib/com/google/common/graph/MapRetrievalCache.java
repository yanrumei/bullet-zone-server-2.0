/*     */ package com.google.common.graph;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MapRetrievalCache<K, V>
/*     */   extends MapIteratorCache<K, V>
/*     */ {
/*     */   @Nullable
/*     */   private transient CacheEntry<K, V> cacheEntry1;
/*     */   @Nullable
/*     */   private transient CacheEntry<K, V> cacheEntry2;
/*     */   
/*     */   MapRetrievalCache(Map<K, V> backingMap)
/*     */   {
/*  33 */     super(backingMap);
/*     */   }
/*     */   
/*     */ 
/*     */   public V get(@Nullable Object key)
/*     */   {
/*  39 */     V value = getIfCached(key);
/*  40 */     if (value != null) {
/*  41 */       return value;
/*     */     }
/*     */     
/*  44 */     value = getWithoutCaching(key);
/*  45 */     if (value != null) {
/*  46 */       addToCache(key, value);
/*     */     }
/*  48 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected V getIfCached(@Nullable Object key)
/*     */   {
/*  55 */     V value = super.getIfCached(key);
/*  56 */     if (value != null) {
/*  57 */       return value;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */     CacheEntry<K, V> entry = this.cacheEntry1;
/*  66 */     if ((entry != null) && (entry.key == key)) {
/*  67 */       return (V)entry.value;
/*     */     }
/*  69 */     entry = this.cacheEntry2;
/*  70 */     if ((entry != null) && (entry.key == key))
/*     */     {
/*     */ 
/*  73 */       addToCache(entry);
/*  74 */       return (V)entry.value;
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   protected void clearCache()
/*     */   {
/*  81 */     super.clearCache();
/*  82 */     this.cacheEntry1 = null;
/*  83 */     this.cacheEntry2 = null;
/*     */   }
/*     */   
/*     */   private void addToCache(K key, V value) {
/*  87 */     addToCache(new CacheEntry(key, value));
/*     */   }
/*     */   
/*     */   private void addToCache(CacheEntry<K, V> entry)
/*     */   {
/*  92 */     this.cacheEntry2 = this.cacheEntry1;
/*  93 */     this.cacheEntry1 = entry;
/*     */   }
/*     */   
/*     */   private static final class CacheEntry<K, V> {
/*     */     final K key;
/*     */     final V value;
/*     */     
/*     */     CacheEntry(K key, V value) {
/* 101 */       this.key = key;
/* 102 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\MapRetrievalCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */