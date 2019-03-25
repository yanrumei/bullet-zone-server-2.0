/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public final class ParameterMap<K, V>
/*     */   implements Map<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*     */   private final Map<K, V> delegatedMap;
/*     */   private final Map<K, V> unmodifiableDelegatedMap;
/*     */   
/*     */   public ParameterMap()
/*     */   {
/*  54 */     this.delegatedMap = new LinkedHashMap();
/*  55 */     this.unmodifiableDelegatedMap = Collections.unmodifiableMap(this.delegatedMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParameterMap(int initialCapacity)
/*     */   {
/*  66 */     this.delegatedMap = new LinkedHashMap(initialCapacity);
/*  67 */     this.unmodifiableDelegatedMap = Collections.unmodifiableMap(this.delegatedMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParameterMap(int initialCapacity, float loadFactor)
/*     */   {
/*  79 */     this.delegatedMap = new LinkedHashMap(initialCapacity, loadFactor);
/*  80 */     this.unmodifiableDelegatedMap = Collections.unmodifiableMap(this.delegatedMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParameterMap(Map<K, V> map)
/*     */   {
/*  90 */     this.delegatedMap = new LinkedHashMap(map);
/*  91 */     this.unmodifiableDelegatedMap = Collections.unmodifiableMap(this.delegatedMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private boolean locked = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLocked()
/*     */   {
/* 105 */     return this.locked;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocked(boolean locked)
/*     */   {
/* 115 */     this.locked = locked;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.util");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 132 */     checkLocked();
/* 133 */     this.delegatedMap.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public V put(K key, V value)
/*     */   {
/* 144 */     checkLocked();
/* 145 */     return (V)this.delegatedMap.put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putAll(Map<? extends K, ? extends V> map)
/*     */   {
/* 156 */     checkLocked();
/* 157 */     this.delegatedMap.putAll(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public V remove(Object key)
/*     */   {
/* 168 */     checkLocked();
/* 169 */     return (V)this.delegatedMap.remove(key);
/*     */   }
/*     */   
/*     */   private void checkLocked()
/*     */   {
/* 174 */     if (this.locked) {
/* 175 */       throw new IllegalStateException(sm.getString("parameterMap.locked"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/* 182 */     return this.delegatedMap.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 188 */     return this.delegatedMap.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsKey(Object key)
/*     */   {
/* 194 */     return this.delegatedMap.containsKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsValue(Object value)
/*     */   {
/* 200 */     return this.delegatedMap.containsValue(value);
/*     */   }
/*     */   
/*     */ 
/*     */   public V get(Object key)
/*     */   {
/* 206 */     return (V)this.delegatedMap.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<K> keySet()
/*     */   {
/* 218 */     if (this.locked) {
/* 219 */       return this.unmodifiableDelegatedMap.keySet();
/*     */     }
/*     */     
/* 222 */     return this.delegatedMap.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<V> values()
/*     */   {
/* 234 */     if (this.locked) {
/* 235 */       return this.unmodifiableDelegatedMap.values();
/*     */     }
/*     */     
/* 238 */     return this.delegatedMap.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<Map.Entry<K, V>> entrySet()
/*     */   {
/* 250 */     if (this.locked) {
/* 251 */       return this.unmodifiableDelegatedMap.entrySet();
/*     */     }
/*     */     
/* 254 */     return this.delegatedMap.entrySet();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\ParameterMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */