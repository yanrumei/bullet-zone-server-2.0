/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingMultimap<K, V>
/*     */   extends ForwardingObject
/*     */   implements Multimap<K, V>
/*     */ {
/*     */   protected abstract Multimap<K, V> delegate();
/*     */   
/*     */   public Map<K, Collection<V>> asMap()
/*     */   {
/*  50 */     return delegate().asMap();
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  55 */     delegate().clear();
/*     */   }
/*     */   
/*     */   public boolean containsEntry(@Nullable Object key, @Nullable Object value)
/*     */   {
/*  60 */     return delegate().containsEntry(key, value);
/*     */   }
/*     */   
/*     */   public boolean containsKey(@Nullable Object key)
/*     */   {
/*  65 */     return delegate().containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(@Nullable Object value)
/*     */   {
/*  70 */     return delegate().containsValue(value);
/*     */   }
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries()
/*     */   {
/*  75 */     return delegate().entries();
/*     */   }
/*     */   
/*     */   public Collection<V> get(@Nullable K key)
/*     */   {
/*  80 */     return delegate().get(key);
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/*  85 */     return delegate().isEmpty();
/*     */   }
/*     */   
/*     */   public Multiset<K> keys()
/*     */   {
/*  90 */     return delegate().keys();
/*     */   }
/*     */   
/*     */   public Set<K> keySet()
/*     */   {
/*  95 */     return delegate().keySet();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value)
/*     */   {
/* 101 */     return delegate().put(key, value);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(K key, Iterable<? extends V> values)
/*     */   {
/* 107 */     return delegate().putAll(key, values);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap)
/*     */   {
/* 113 */     return delegate().putAll(multimap);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(@Nullable Object key, @Nullable Object value)
/*     */   {
/* 119 */     return delegate().remove(key, value);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> removeAll(@Nullable Object key)
/*     */   {
/* 125 */     return delegate().removeAll(key);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> replaceValues(K key, Iterable<? extends V> values)
/*     */   {
/* 131 */     return delegate().replaceValues(key, values);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 136 */     return delegate().size();
/*     */   }
/*     */   
/*     */   public Collection<V> values()
/*     */   {
/* 141 */     return delegate().values();
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object)
/*     */   {
/* 146 */     return (object == this) || (delegate().equals(object));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 151 */     return delegate().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */