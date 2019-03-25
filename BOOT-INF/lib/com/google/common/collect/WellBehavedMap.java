/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class WellBehavedMap<K, V>
/*    */   extends ForwardingMap<K, V>
/*    */ {
/*    */   private final Map<K, V> delegate;
/*    */   private Set<Map.Entry<K, V>> entrySet;
/*    */   
/*    */   private WellBehavedMap(Map<K, V> delegate)
/*    */   {
/* 42 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static <K, V> WellBehavedMap<K, V> wrap(Map<K, V> delegate)
/*    */   {
/* 52 */     return new WellBehavedMap(delegate);
/*    */   }
/*    */   
/*    */   protected Map<K, V> delegate()
/*    */   {
/* 57 */     return this.delegate;
/*    */   }
/*    */   
/*    */   public Set<Map.Entry<K, V>> entrySet()
/*    */   {
/* 62 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 63 */     if (es != null) {
/* 64 */       return es;
/*    */     }
/* 66 */     return this.entrySet = new EntrySet(null);
/*    */   }
/*    */   
/*    */   private final class EntrySet extends Maps.EntrySet<K, V> {
/*    */     private EntrySet() {}
/*    */     
/*    */     Map<K, V> map() {
/* 73 */       return WellBehavedMap.this;
/*    */     }
/*    */     
/*    */     public Iterator<Map.Entry<K, V>> iterator()
/*    */     {
/* 78 */       new TransformedIterator(WellBehavedMap.this.keySet().iterator())
/*    */       {
/*    */         Map.Entry<K, V> transform(final K key) {
/* 81 */           new AbstractMapEntry()
/*    */           {
/*    */             public K getKey() {
/* 84 */               return (K)key;
/*    */             }
/*    */             
/*    */             public V getValue()
/*    */             {
/* 89 */               return (V)WellBehavedMap.this.get(key);
/*    */             }
/*    */             
/*    */             public V setValue(V value)
/*    */             {
/* 94 */               return (V)WellBehavedMap.this.put(key, value);
/*    */             }
/*    */           };
/*    */         }
/*    */       };
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\WellBehavedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */