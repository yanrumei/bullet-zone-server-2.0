/*     */ package org.apache.tomcat.util.collections;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ManagedConcurrentWeakHashMap<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements ConcurrentMap<K, V>
/*     */ {
/*  44 */   private final ConcurrentMap<Key, V> map = new ConcurrentHashMap();
/*  45 */   private final ReferenceQueue<Object> queue = new ReferenceQueue();
/*     */   
/*     */ 
/*     */ 
/*     */   public void maintain()
/*     */   {
/*     */     Key key;
/*     */     
/*  53 */     while ((key = (Key)this.queue.poll()) != null)
/*  54 */       if (!key.isDead())
/*     */       {
/*     */ 
/*     */ 
/*  58 */         key.ackDeath();
/*  59 */         this.map.remove(key);
/*     */       }
/*     */   }
/*     */   
/*     */   private static class Key extends WeakReference<Object> {
/*     */     private final int hash;
/*     */     private boolean dead;
/*     */     
/*     */     public Key(Object key, ReferenceQueue<Object> queue) {
/*  68 */       super(queue);
/*  69 */       this.hash = key.hashCode();
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  74 */       return this.hash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/*  79 */       if (this == obj) {
/*  80 */         return true;
/*     */       }
/*  82 */       if (this.dead)
/*     */       {
/*     */ 
/*  85 */         return false;
/*     */       }
/*  87 */       if (!(obj instanceof Reference)) {
/*  88 */         return false;
/*     */       }
/*  90 */       Object oA = get();
/*  91 */       Object oB = ((Reference)obj).get();
/*  92 */       if (oA == oB) {
/*  93 */         return true;
/*     */       }
/*  95 */       if ((oA == null) || (oB == null)) {
/*  96 */         return false;
/*     */       }
/*  98 */       return oA.equals(oB);
/*     */     }
/*     */     
/*     */     public void ackDeath() {
/* 102 */       this.dead = true;
/*     */     }
/*     */     
/*     */     public boolean isDead() {
/* 106 */       return this.dead;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Key createStoreKey(Object key)
/*     */   {
/* 115 */     return new Key(key, this.queue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Key createLookupKey(Object key)
/*     */   {
/* 123 */     return new Key(key, null);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 128 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 133 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value)
/*     */   {
/* 138 */     if (value == null) {
/* 139 */       return false;
/*     */     }
/* 141 */     return this.map.containsValue(value);
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key)
/*     */   {
/* 146 */     if (key == null) {
/* 147 */       return false;
/*     */     }
/* 149 */     return this.map.containsKey(createLookupKey(key));
/*     */   }
/*     */   
/*     */   public V get(Object key)
/*     */   {
/* 154 */     if (key == null) {
/* 155 */       return null;
/*     */     }
/* 157 */     return (V)this.map.get(createLookupKey(key));
/*     */   }
/*     */   
/*     */   public V put(K key, V value)
/*     */   {
/* 162 */     Objects.requireNonNull(value);
/* 163 */     return (V)this.map.put(createStoreKey(key), value);
/*     */   }
/*     */   
/*     */   public V remove(Object key)
/*     */   {
/* 168 */     return (V)this.map.remove(createLookupKey(key));
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 173 */     this.map.clear();
/*     */     
/*     */ 
/* 176 */     maintain();
/*     */   }
/*     */   
/*     */   public V putIfAbsent(K key, V value)
/*     */   {
/* 181 */     Objects.requireNonNull(value);
/* 182 */     Key storeKey = createStoreKey(key);
/* 183 */     V oldValue = this.map.putIfAbsent(storeKey, value);
/* 184 */     if (oldValue != null) {
/* 185 */       storeKey.ackDeath();
/*     */     }
/* 187 */     return oldValue;
/*     */   }
/*     */   
/*     */   public boolean remove(Object key, Object value)
/*     */   {
/* 192 */     if (value == null) {
/* 193 */       return false;
/*     */     }
/* 195 */     return this.map.remove(createLookupKey(key), value);
/*     */   }
/*     */   
/*     */   public boolean replace(K key, V oldValue, V newValue)
/*     */   {
/* 200 */     Objects.requireNonNull(newValue);
/* 201 */     return this.map.replace(createLookupKey(key), oldValue, newValue);
/*     */   }
/*     */   
/*     */   public V replace(K key, V value)
/*     */   {
/* 206 */     Objects.requireNonNull(value);
/* 207 */     return (V)this.map.replace(createLookupKey(key), value);
/*     */   }
/*     */   
/*     */   public Collection<V> values()
/*     */   {
/* 212 */     return this.map.values();
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet()
/*     */   {
/* 217 */     new AbstractSet()
/*     */     {
/*     */       public boolean isEmpty() {
/* 220 */         return ManagedConcurrentWeakHashMap.this.map.isEmpty();
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/* 225 */         return ManagedConcurrentWeakHashMap.this.map.size();
/*     */       }
/*     */       
/*     */       public Iterator<Map.Entry<K, V>> iterator()
/*     */       {
/* 230 */         new Iterator() {
/* 231 */           private final Iterator<Map.Entry<ManagedConcurrentWeakHashMap.Key, V>> it = ManagedConcurrentWeakHashMap.this.map
/* 232 */             .entrySet().iterator();
/*     */           
/*     */           public boolean hasNext()
/*     */           {
/* 236 */             return this.it.hasNext();
/*     */           }
/*     */           
/*     */           public Map.Entry<K, V> next()
/*     */           {
/* 241 */             new Map.Entry() {
/* 242 */               private final Map.Entry<ManagedConcurrentWeakHashMap.Key, V> en = (Map.Entry)ManagedConcurrentWeakHashMap.1.1.this.it.next();
/*     */               
/*     */ 
/*     */               public K getKey()
/*     */               {
/* 247 */                 return (K)((ManagedConcurrentWeakHashMap.Key)this.en.getKey()).get();
/*     */               }
/*     */               
/*     */               public V getValue()
/*     */               {
/* 252 */                 return (V)this.en.getValue();
/*     */               }
/*     */               
/*     */               public V setValue(V value)
/*     */               {
/* 257 */                 Objects.requireNonNull(value);
/* 258 */                 return (V)this.en.setValue(value);
/*     */               }
/*     */             };
/*     */           }
/*     */           
/*     */           public void remove()
/*     */           {
/* 265 */             this.it.remove();
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\collections\ManagedConcurrentWeakHashMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */