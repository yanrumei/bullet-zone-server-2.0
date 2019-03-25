/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ public abstract class ForwardingListMultimap<K, V>
/*    */   extends ForwardingMultimap<K, V>
/*    */   implements ListMultimap<K, V>
/*    */ {
/*    */   protected abstract ListMultimap<K, V> delegate();
/*    */   
/*    */   public List<V> get(@Nullable K key)
/*    */   {
/* 48 */     return delegate().get(key);
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public List<V> removeAll(@Nullable Object key)
/*    */   {
/* 54 */     return delegate().removeAll(key);
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public List<V> replaceValues(K key, Iterable<? extends V> values)
/*    */   {
/* 60 */     return delegate().replaceValues(key, values);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */