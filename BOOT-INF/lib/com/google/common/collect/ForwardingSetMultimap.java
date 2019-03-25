/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingSetMultimap<K, V>
/*    */   extends ForwardingMultimap<K, V>
/*    */   implements SetMultimap<K, V>
/*    */ {
/*    */   protected abstract SetMultimap<K, V> delegate();
/*    */   
/*    */   public Set<Map.Entry<K, V>> entries()
/*    */   {
/* 46 */     return delegate().entries();
/*    */   }
/*    */   
/*    */   public Set<V> get(@Nullable K key)
/*    */   {
/* 51 */     return delegate().get(key);
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public Set<V> removeAll(@Nullable Object key)
/*    */   {
/* 57 */     return delegate().removeAll(key);
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public Set<V> replaceValues(K key, Iterable<? extends V> values)
/*    */   {
/* 63 */     return delegate().replaceValues(key, values);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */