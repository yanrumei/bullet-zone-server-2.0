/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.SortedMap;
/*    */ import java.util.SortedSet;
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
/*    */ abstract class AbstractSortedKeySortedSetMultimap<K, V>
/*    */   extends AbstractSortedSetMultimap<K, V>
/*    */ {
/*    */   AbstractSortedKeySortedSetMultimap(SortedMap<K, Collection<V>> map)
/*    */   {
/* 37 */     super(map);
/*    */   }
/*    */   
/*    */   public SortedMap<K, Collection<V>> asMap()
/*    */   {
/* 42 */     return (SortedMap)super.asMap();
/*    */   }
/*    */   
/*    */   SortedMap<K, Collection<V>> backingMap()
/*    */   {
/* 47 */     return (SortedMap)super.backingMap();
/*    */   }
/*    */   
/*    */   public SortedSet<K> keySet()
/*    */   {
/* 52 */     return (SortedSet)super.keySet();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\AbstractSortedKeySortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */