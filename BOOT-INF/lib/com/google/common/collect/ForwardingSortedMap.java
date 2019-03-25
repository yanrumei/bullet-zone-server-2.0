/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingSortedMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements SortedMap<K, V>
/*     */ {
/*     */   protected abstract SortedMap<K, V> delegate();
/*     */   
/*     */   public Comparator<? super K> comparator()
/*     */   {
/*  70 */     return delegate().comparator();
/*     */   }
/*     */   
/*     */   public K firstKey()
/*     */   {
/*  75 */     return (K)delegate().firstKey();
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey)
/*     */   {
/*  80 */     return delegate().headMap(toKey);
/*     */   }
/*     */   
/*     */   public K lastKey()
/*     */   {
/*  85 */     return (K)delegate().lastKey();
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey)
/*     */   {
/*  90 */     return delegate().subMap(fromKey, toKey);
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey)
/*     */   {
/*  95 */     return delegate().tailMap(fromKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   protected class StandardKeySet
/*     */     extends Maps.SortedKeySet<K, V>
/*     */   {
/*     */     public StandardKeySet()
/*     */     {
/* 109 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private int unsafeCompare(Object k1, Object k2)
/*     */   {
/* 116 */     Comparator<? super K> comparator = comparator();
/* 117 */     if (comparator == null) {
/* 118 */       return ((Comparable)k1).compareTo(k2);
/*     */     }
/* 120 */     return comparator.compare(k1, k2);
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
/*     */   @Beta
/*     */   protected boolean standardContainsKey(@Nullable Object key)
/*     */   {
/*     */     try
/*     */     {
/* 138 */       SortedMap<Object, V> self = this;
/* 139 */       Object ceilingKey = self.tailMap(key).firstKey();
/* 140 */       return unsafeCompare(ceilingKey, key) == 0;
/*     */     } catch (ClassCastException e) {
/* 142 */       return false;
/*     */     } catch (NoSuchElementException e) {
/* 144 */       return false;
/*     */     } catch (NullPointerException e) {}
/* 146 */     return false;
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
/*     */   @Beta
/*     */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey)
/*     */   {
/* 160 */     Preconditions.checkArgument(unsafeCompare(fromKey, toKey) <= 0, "fromKey must be <= toKey");
/* 161 */     return tailMap(fromKey).headMap(toKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingSortedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */