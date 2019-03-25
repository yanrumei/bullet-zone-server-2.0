/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Comparator;
/*    */ import java.util.SortedSet;
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
/*    */ public abstract class ForwardingSortedSetMultimap<K, V>
/*    */   extends ForwardingSetMultimap<K, V>
/*    */   implements SortedSetMultimap<K, V>
/*    */ {
/*    */   protected abstract SortedSetMultimap<K, V> delegate();
/*    */   
/*    */   public SortedSet<V> get(@Nullable K key)
/*    */   {
/* 48 */     return delegate().get(key);
/*    */   }
/*    */   
/*    */   public SortedSet<V> removeAll(@Nullable Object key)
/*    */   {
/* 53 */     return delegate().removeAll(key);
/*    */   }
/*    */   
/*    */   public SortedSet<V> replaceValues(K key, Iterable<? extends V> values)
/*    */   {
/* 58 */     return delegate().replaceValues(key, values);
/*    */   }
/*    */   
/*    */   public Comparator<? super V> valueComparator()
/*    */   {
/* 63 */     return delegate().valueComparator();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingSortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */