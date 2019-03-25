/*     */ package org.springframework.cache.concurrent;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.cache.Cache.ValueRetrievalException;
/*     */ import org.springframework.cache.Cache.ValueWrapper;
/*     */ import org.springframework.cache.support.AbstractValueAdaptingCache;
/*     */ import org.springframework.core.serializer.support.SerializationDelegate;
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
/*     */ public class ConcurrentMapCache
/*     */   extends AbstractValueAdaptingCache
/*     */ {
/*     */   private final String name;
/*     */   private final ConcurrentMap<Object, Object> store;
/*     */   private final SerializationDelegate serialization;
/*     */   
/*     */   public ConcurrentMapCache(String name)
/*     */   {
/*  62 */     this(name, new ConcurrentHashMap(256), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConcurrentMapCache(String name, boolean allowNullValues)
/*     */   {
/*  72 */     this(name, new ConcurrentHashMap(256), allowNullValues);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues)
/*     */   {
/*  84 */     this(name, store, allowNullValues, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues, SerializationDelegate serialization)
/*     */   {
/* 103 */     super(allowNullValues);
/* 104 */     Assert.notNull(name, "Name must not be null");
/* 105 */     Assert.notNull(store, "Store must not be null");
/* 106 */     this.name = name;
/* 107 */     this.store = store;
/* 108 */     this.serialization = serialization;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isStoreByValue()
/*     */   {
/* 119 */     return this.serialization != null;
/*     */   }
/*     */   
/*     */   public final String getName()
/*     */   {
/* 124 */     return this.name;
/*     */   }
/*     */   
/*     */   public final ConcurrentMap<Object, Object> getNativeCache()
/*     */   {
/* 129 */     return this.store;
/*     */   }
/*     */   
/*     */   protected Object lookup(Object key)
/*     */   {
/* 134 */     return this.store.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T get(Object key, Callable<T> valueLoader)
/*     */   {
/* 140 */     if (this.store.containsKey(key)) {
/* 141 */       return (T)get(key).get();
/*     */     }
/*     */     
/* 144 */     synchronized (this.store) {
/* 145 */       if (this.store.containsKey(key)) {
/* 146 */         return (T)get(key).get();
/*     */       }
/*     */       try
/*     */       {
/* 150 */         value = valueLoader.call();
/*     */       } catch (Throwable ex) {
/*     */         T value;
/* 153 */         throw new Cache.ValueRetrievalException(key, valueLoader, ex); }
/*     */       T value;
/* 155 */       put(key, value);
/* 156 */       return value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void put(Object key, Object value)
/*     */   {
/* 163 */     this.store.put(key, toStoreValue(value));
/*     */   }
/*     */   
/*     */   public Cache.ValueWrapper putIfAbsent(Object key, Object value)
/*     */   {
/* 168 */     Object existing = this.store.putIfAbsent(key, toStoreValue(value));
/* 169 */     return toValueWrapper(existing);
/*     */   }
/*     */   
/*     */   public void evict(Object key)
/*     */   {
/* 174 */     this.store.remove(key);
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 179 */     this.store.clear();
/*     */   }
/*     */   
/*     */   protected Object toStoreValue(Object userValue)
/*     */   {
/* 184 */     Object storeValue = super.toStoreValue(userValue);
/* 185 */     if (this.serialization != null) {
/*     */       try {
/* 187 */         return serializeValue(storeValue);
/*     */       }
/*     */       catch (Throwable ex) {
/* 190 */         throw new IllegalArgumentException("Failed to serialize cache value '" + userValue + "'. Does it implement Serializable?", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 195 */     return storeValue;
/*     */   }
/*     */   
/*     */   private Object serializeValue(Object storeValue) throws IOException
/*     */   {
/* 200 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     try {
/* 202 */       this.serialization.serialize(storeValue, out);
/* 203 */       return out.toByteArray();
/*     */     }
/*     */     finally {
/* 206 */       out.close();
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object fromStoreValue(Object storeValue)
/*     */   {
/* 212 */     if (this.serialization != null) {
/*     */       try {
/* 214 */         return super.fromStoreValue(deserializeValue(storeValue));
/*     */       }
/*     */       catch (Throwable ex) {
/* 217 */         throw new IllegalArgumentException("Failed to deserialize cache value '" + storeValue + "'", ex);
/*     */       }
/*     */     }
/*     */     
/* 221 */     return super.fromStoreValue(storeValue);
/*     */   }
/*     */   
/*     */   private Object deserializeValue(Object storeValue)
/*     */     throws IOException
/*     */   {
/* 227 */     ByteArrayInputStream in = new ByteArrayInputStream((byte[])storeValue);
/*     */     try {
/* 229 */       return this.serialization.deserialize(in);
/*     */     }
/*     */     finally {
/* 232 */       in.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\concurrent\ConcurrentMapCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */