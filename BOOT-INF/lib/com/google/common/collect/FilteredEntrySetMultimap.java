/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class FilteredEntrySetMultimap<K, V>
/*    */   extends FilteredEntryMultimap<K, V>
/*    */   implements FilteredSetMultimap<K, V>
/*    */ {
/*    */   FilteredEntrySetMultimap(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate)
/*    */   {
/* 34 */     super(unfiltered, predicate);
/*    */   }
/*    */   
/*    */   public SetMultimap<K, V> unfiltered()
/*    */   {
/* 39 */     return (SetMultimap)this.unfiltered;
/*    */   }
/*    */   
/*    */   public Set<V> get(K key)
/*    */   {
/* 44 */     return (Set)super.get(key);
/*    */   }
/*    */   
/*    */   public Set<V> removeAll(Object key)
/*    */   {
/* 49 */     return (Set)super.removeAll(key);
/*    */   }
/*    */   
/*    */   public Set<V> replaceValues(K key, Iterable<? extends V> values)
/*    */   {
/* 54 */     return (Set)super.replaceValues(key, values);
/*    */   }
/*    */   
/*    */   Set<Map.Entry<K, V>> createEntries()
/*    */   {
/* 59 */     return Sets.filter(unfiltered().entries(), entryPredicate());
/*    */   }
/*    */   
/*    */   public Set<Map.Entry<K, V>> entries()
/*    */   {
/* 64 */     return (Set)super.entries();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\FilteredEntrySetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */