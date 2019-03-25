/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
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
/*    */ final class FilteredKeyListMultimap<K, V>
/*    */   extends FilteredKeyMultimap<K, V>
/*    */   implements ListMultimap<K, V>
/*    */ {
/*    */   FilteredKeyListMultimap(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate)
/*    */   {
/* 33 */     super(unfiltered, keyPredicate);
/*    */   }
/*    */   
/*    */   public ListMultimap<K, V> unfiltered()
/*    */   {
/* 38 */     return (ListMultimap)super.unfiltered();
/*    */   }
/*    */   
/*    */   public List<V> get(K key)
/*    */   {
/* 43 */     return (List)super.get(key);
/*    */   }
/*    */   
/*    */   public List<V> removeAll(@Nullable Object key)
/*    */   {
/* 48 */     return (List)super.removeAll(key);
/*    */   }
/*    */   
/*    */   public List<V> replaceValues(K key, Iterable<? extends V> values)
/*    */   {
/* 53 */     return (List)super.replaceValues(key, values);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\FilteredKeyListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */