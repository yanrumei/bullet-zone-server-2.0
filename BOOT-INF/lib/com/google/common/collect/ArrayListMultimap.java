/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable=true, emulated=true)
/*     */ public final class ArrayListMultimap<K, V>
/*     */   extends ArrayListMultimapGwtSerializationDependencies<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 3;
/*     */   @VisibleForTesting
/*     */   transient int expectedValuesPerKey;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ArrayListMultimap<K, V> create()
/*     */   {
/*  80 */     return new ArrayListMultimap();
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
/*     */ 
/*     */   public static <K, V> ArrayListMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey)
/*     */   {
/*  96 */     return new ArrayListMultimap(expectedKeys, expectedValuesPerKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ArrayListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap)
/*     */   {
/* 108 */     return new ArrayListMultimap(multimap);
/*     */   }
/*     */   
/*     */   private ArrayListMultimap() {
/* 112 */     super(new HashMap());
/* 113 */     this.expectedValuesPerKey = 3;
/*     */   }
/*     */   
/*     */   private ArrayListMultimap(int expectedKeys, int expectedValuesPerKey) {
/* 117 */     super(Maps.newHashMapWithExpectedSize(expectedKeys));
/* 118 */     CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 119 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private ArrayListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 123 */     this(multimap
/* 124 */       .keySet().size(), (multimap instanceof ArrayListMultimap) ? ((ArrayListMultimap)multimap).expectedValuesPerKey : 3);
/*     */     
/*     */ 
/*     */ 
/* 128 */     putAll(multimap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   List<V> createCollection()
/*     */   {
/* 137 */     return new ArrayList(this.expectedValuesPerKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void trimToSize()
/*     */   {
/* 150 */     for (Collection<V> collection : backingMap().values()) {
/* 151 */       ArrayList<V> arrayList = (ArrayList)collection;
/* 152 */       arrayList.trimToSize();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream)
/*     */     throws IOException
/*     */   {
/* 163 */     stream.defaultWriteObject();
/* 164 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 169 */     stream.defaultReadObject();
/* 170 */     this.expectedValuesPerKey = 3;
/* 171 */     int distinctKeys = Serialization.readCount(stream);
/* 172 */     Map<K, Collection<V>> map = Maps.newHashMap();
/* 173 */     setMap(map);
/* 174 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ArrayListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */