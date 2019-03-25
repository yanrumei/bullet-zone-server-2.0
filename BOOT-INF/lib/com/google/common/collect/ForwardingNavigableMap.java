/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingNavigableMap<K, V>
/*     */   extends ForwardingSortedMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   protected abstract NavigableMap<K, V> delegate();
/*     */   
/*     */   public Map.Entry<K, V> lowerEntry(K key)
/*     */   {
/*  69 */     return delegate().lowerEntry(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map.Entry<K, V> standardLowerEntry(K key)
/*     */   {
/*  78 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */   
/*     */   public K lowerKey(K key)
/*     */   {
/*  83 */     return (K)delegate().lowerKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected K standardLowerKey(K key)
/*     */   {
/*  92 */     return (K)Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */   
/*     */   public Map.Entry<K, V> floorEntry(K key)
/*     */   {
/*  97 */     return delegate().floorEntry(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map.Entry<K, V> standardFloorEntry(K key)
/*     */   {
/* 106 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */   
/*     */   public K floorKey(K key)
/*     */   {
/* 111 */     return (K)delegate().floorKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected K standardFloorKey(K key)
/*     */   {
/* 120 */     return (K)Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */   
/*     */   public Map.Entry<K, V> ceilingEntry(K key)
/*     */   {
/* 125 */     return delegate().ceilingEntry(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map.Entry<K, V> standardCeilingEntry(K key)
/*     */   {
/* 134 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */   
/*     */   public K ceilingKey(K key)
/*     */   {
/* 139 */     return (K)delegate().ceilingKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected K standardCeilingKey(K key)
/*     */   {
/* 148 */     return (K)Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */   
/*     */   public Map.Entry<K, V> higherEntry(K key)
/*     */   {
/* 153 */     return delegate().higherEntry(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map.Entry<K, V> standardHigherEntry(K key)
/*     */   {
/* 162 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */   
/*     */   public K higherKey(K key)
/*     */   {
/* 167 */     return (K)delegate().higherKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected K standardHigherKey(K key)
/*     */   {
/* 176 */     return (K)Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */   
/*     */   public Map.Entry<K, V> firstEntry()
/*     */   {
/* 181 */     return delegate().firstEntry();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map.Entry<K, V> standardFirstEntry()
/*     */   {
/* 190 */     return (Map.Entry)Iterables.getFirst(entrySet(), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected K standardFirstKey()
/*     */   {
/* 199 */     Map.Entry<K, V> entry = firstEntry();
/* 200 */     if (entry == null) {
/* 201 */       throw new NoSuchElementException();
/*     */     }
/* 203 */     return (K)entry.getKey();
/*     */   }
/*     */   
/*     */ 
/*     */   public Map.Entry<K, V> lastEntry()
/*     */   {
/* 209 */     return delegate().lastEntry();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map.Entry<K, V> standardLastEntry()
/*     */   {
/* 218 */     return (Map.Entry)Iterables.getFirst(descendingMap().entrySet(), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected K standardLastKey()
/*     */   {
/* 226 */     Map.Entry<K, V> entry = lastEntry();
/* 227 */     if (entry == null) {
/* 228 */       throw new NoSuchElementException();
/*     */     }
/* 230 */     return (K)entry.getKey();
/*     */   }
/*     */   
/*     */ 
/*     */   public Map.Entry<K, V> pollFirstEntry()
/*     */   {
/* 236 */     return delegate().pollFirstEntry();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map.Entry<K, V> standardPollFirstEntry()
/*     */   {
/* 245 */     return (Map.Entry)Iterators.pollNext(entrySet().iterator());
/*     */   }
/*     */   
/*     */   public Map.Entry<K, V> pollLastEntry()
/*     */   {
/* 250 */     return delegate().pollLastEntry();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map.Entry<K, V> standardPollLastEntry()
/*     */   {
/* 259 */     return (Map.Entry)Iterators.pollNext(descendingMap().entrySet().iterator());
/*     */   }
/*     */   
/*     */   public NavigableMap<K, V> descendingMap()
/*     */   {
/* 264 */     return delegate().descendingMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   protected class StandardDescendingMap
/*     */     extends Maps.DescendingMap<K, V>
/*     */   {
/*     */     public StandardDescendingMap() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     NavigableMap<K, V> forward()
/*     */     {
/* 286 */       return ForwardingNavigableMap.this;
/*     */     }
/*     */     
/*     */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
/*     */     {
/* 291 */       forward().replaceAll(function);
/*     */     }
/*     */     
/*     */     protected Iterator<Map.Entry<K, V>> entryIterator()
/*     */     {
/* 296 */       new Iterator() {
/* 297 */         private Map.Entry<K, V> toRemove = null;
/* 298 */         private Map.Entry<K, V> nextOrNull = ForwardingNavigableMap.StandardDescendingMap.this.forward().lastEntry();
/*     */         
/*     */         public boolean hasNext()
/*     */         {
/* 302 */           return this.nextOrNull != null;
/*     */         }
/*     */         
/*     */         public Map.Entry<K, V> next()
/*     */         {
/* 307 */           if (!hasNext()) {
/* 308 */             throw new NoSuchElementException();
/*     */           }
/*     */           try {
/* 311 */             return this.nextOrNull;
/*     */           } finally {
/* 313 */             this.toRemove = this.nextOrNull;
/* 314 */             this.nextOrNull = ForwardingNavigableMap.StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
/*     */           }
/*     */         }
/*     */         
/*     */         public void remove()
/*     */         {
/* 320 */           CollectPreconditions.checkRemove(this.toRemove != null);
/* 321 */           ForwardingNavigableMap.StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
/* 322 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */   public NavigableSet<K> navigableKeySet()
/*     */   {
/* 330 */     return delegate().navigableKeySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   protected class StandardNavigableKeySet
/*     */     extends Maps.NavigableKeySet<K, V>
/*     */   {
/*     */     public StandardNavigableKeySet()
/*     */     {
/* 345 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   public NavigableSet<K> descendingKeySet()
/*     */   {
/* 351 */     return delegate().descendingKeySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   protected NavigableSet<K> standardDescendingKeySet()
/*     */   {
/* 363 */     return descendingMap().navigableKeySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey)
/*     */   {
/* 374 */     return subMap(fromKey, true, toKey, false);
/*     */   }
/*     */   
/*     */   public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
/*     */   {
/* 379 */     return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
/*     */   }
/*     */   
/*     */   public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
/*     */   {
/* 384 */     return delegate().headMap(toKey, inclusive);
/*     */   }
/*     */   
/*     */   public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
/*     */   {
/* 389 */     return delegate().tailMap(fromKey, inclusive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SortedMap<K, V> standardHeadMap(K toKey)
/*     */   {
/* 398 */     return headMap(toKey, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SortedMap<K, V> standardTailMap(K fromKey)
/*     */   {
/* 407 */     return tailMap(fromKey, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingNavigableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */