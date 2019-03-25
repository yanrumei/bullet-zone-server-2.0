/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
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
/*    */ @GwtCompatible(emulated=true)
/*    */ abstract class HashMultimapGwtSerializationDependencies<K, V>
/*    */   extends AbstractSetMultimap<K, V>
/*    */ {
/*    */   HashMultimapGwtSerializationDependencies(Map<K, Collection<V>> map)
/*    */   {
/* 35 */     super(map);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\HashMultimapGwtSerializationDependencies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */