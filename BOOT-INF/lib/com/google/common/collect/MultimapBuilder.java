/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public abstract class MultimapBuilder<K0, V0>
/*     */ {
/*     */   private static final int DEFAULT_EXPECTED_KEYS = 8;
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys()
/*     */   {
/*  85 */     return hashKeys(8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys(int expectedKeys)
/*     */   {
/*  95 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/*  96 */     new MultimapBuilderWithKeys()
/*     */     {
/*     */       <K, V> Map<K, Collection<V>> createMap() {
/*  99 */         return Maps.newHashMapWithExpectedSize(this.val$expectedKeys);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys()
/*     */   {
/* 113 */     return linkedHashKeys(8);
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
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys(int expectedKeys)
/*     */   {
/* 126 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/* 127 */     new MultimapBuilderWithKeys()
/*     */     {
/*     */       <K, V> Map<K, Collection<V>> createMap() {
/* 130 */         return Maps.newLinkedHashMapWithExpectedSize(this.val$expectedKeys);
/*     */       }
/*     */     };
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
/*     */   public static MultimapBuilderWithKeys<Comparable> treeKeys()
/*     */   {
/* 147 */     return treeKeys(Ordering.natural());
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
/*     */ 
/*     */   public static <K0> MultimapBuilderWithKeys<K0> treeKeys(Comparator<K0> comparator)
/*     */   {
/* 164 */     Preconditions.checkNotNull(comparator);
/* 165 */     new MultimapBuilderWithKeys()
/*     */     {
/*     */       <K extends K0, V> Map<K, Collection<V>> createMap() {
/* 168 */         return new TreeMap(this.val$comparator);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(Class<K0> keyClass)
/*     */   {
/* 178 */     Preconditions.checkNotNull(keyClass);
/* 179 */     new MultimapBuilderWithKeys()
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       <K extends K0, V> Map<K, Collection<V>> createMap() {
/* 185 */         return new EnumMap(this.val$keyClass); }
/*     */     };
/*     */   }
/*     */   
/*     */   public abstract <K extends K0, V extends V0> Multimap<K, V> build();
/*     */   
/*     */   private static final class ArrayListSupplier<V> implements Supplier<List<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/* 194 */     ArrayListSupplier(int expectedValuesPerKey) { this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey"); }
/*     */     
/*     */ 
/*     */     public List<V> get()
/*     */     {
/* 199 */       return new ArrayList(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum LinkedListSupplier implements Supplier<List<Object>> {
/* 204 */     INSTANCE;
/*     */     
/*     */     private LinkedListSupplier() {}
/*     */     
/*     */     public static <V> Supplier<List<V>> instance() {
/* 209 */       Supplier<List<V>> result = INSTANCE;
/* 210 */       return result;
/*     */     }
/*     */     
/*     */     public List<Object> get()
/*     */     {
/* 215 */       return new LinkedList();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     HashSetSupplier(int expectedValuesPerKey) {
/* 223 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */     
/*     */     public Set<V> get()
/*     */     {
/* 228 */       return Sets.newHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LinkedHashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     LinkedHashSetSupplier(int expectedValuesPerKey) {
/* 236 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */     
/*     */     public Set<V> get()
/*     */     {
/* 241 */       return Sets.newLinkedHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TreeSetSupplier<V> implements Supplier<SortedSet<V>>, Serializable {
/*     */     private final Comparator<? super V> comparator;
/*     */     
/*     */     TreeSetSupplier(Comparator<? super V> comparator) {
/* 249 */       this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
/*     */     }
/*     */     
/*     */     public SortedSet<V> get()
/*     */     {
/* 254 */       return new TreeSet(this.comparator);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EnumSetSupplier<V extends Enum<V>> implements Supplier<Set<V>>, Serializable
/*     */   {
/*     */     private final Class<V> clazz;
/*     */     
/*     */     EnumSetSupplier(Class<V> clazz) {
/* 263 */       this.clazz = ((Class)Preconditions.checkNotNull(clazz));
/*     */     }
/*     */     
/*     */     public Set<V> get()
/*     */     {
/* 268 */       return EnumSet.noneOf(this.clazz);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class MultimapBuilderWithKeys<K0>
/*     */   {
/*     */     private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract <K extends K0, V> Map<K, Collection<V>> createMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues()
/*     */     {
/* 290 */       return arrayListValues(2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues(final int expectedValuesPerKey)
/*     */     {
/* 300 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 301 */       new MultimapBuilder.ListMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V> ListMultimap<K, V> build() {
/* 304 */           return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 305 */             .createMap(), new MultimapBuilder.ArrayListSupplier(expectedValuesPerKey));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> linkedListValues()
/*     */     {
/* 315 */       new MultimapBuilder.ListMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V> ListMultimap<K, V> build() {
/* 318 */           return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 319 */             .createMap(), MultimapBuilder.LinkedListSupplier.instance());
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues()
/*     */     {
/* 328 */       return hashSetValues(2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues(final int expectedValuesPerKey)
/*     */     {
/* 338 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 339 */       new MultimapBuilder.SetMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V> SetMultimap<K, V> build() {
/* 342 */           return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 343 */             .createMap(), new MultimapBuilder.HashSetSupplier(expectedValuesPerKey));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues()
/*     */     {
/* 353 */       return linkedHashSetValues(2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues(final int expectedValuesPerKey)
/*     */     {
/* 363 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 364 */       new MultimapBuilder.SetMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V> SetMultimap<K, V> build() {
/* 367 */           return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 368 */             .createMap(), new MultimapBuilder.LinkedHashSetSupplier(expectedValuesPerKey));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.SortedSetMultimapBuilder<K0, Comparable> treeSetValues()
/*     */     {
/* 379 */       return treeSetValues(Ordering.natural());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public <V0> MultimapBuilder.SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> comparator)
/*     */     {
/* 389 */       Preconditions.checkNotNull(comparator, "comparator");
/* 390 */       new MultimapBuilder.SortedSetMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
/* 393 */           return Multimaps.newSortedSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 394 */             .createMap(), new MultimapBuilder.TreeSetSupplier(comparator));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public <V0 extends Enum<V0>> MultimapBuilder.SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> valueClass)
/*     */     {
/* 404 */       Preconditions.checkNotNull(valueClass, "valueClass");
/* 405 */       new MultimapBuilder.SetMultimapBuilder()
/*     */       {
/*     */ 
/*     */         public <K extends K0, V extends V0> SetMultimap<K, V> build()
/*     */         {
/*     */ 
/* 411 */           Supplier<Set<V>> factory = new MultimapBuilder.EnumSetSupplier(valueClass);
/* 412 */           return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), factory);
/*     */         }
/*     */       };
/*     */     }
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
/*     */   public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
/*     */   {
/* 429 */     Multimap<K, V> result = build();
/* 430 */     result.putAll(multimap);
/* 431 */     return result;
/*     */   }
/*     */   
/*     */   public static abstract class ListMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     ListMultimapBuilder()
/*     */     {
/* 438 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();
/*     */     
/*     */     public <K extends K0, V extends V0> ListMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
/*     */     {
/* 446 */       return (ListMultimap)super.build(multimap);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class SetMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     SetMultimapBuilder()
/*     */     {
/* 454 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();
/*     */     
/*     */     public <K extends K0, V extends V0> SetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
/*     */     {
/* 462 */       return (SetMultimap)super.build(multimap);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class SortedSetMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder.SetMultimapBuilder<K0, V0>
/*     */   {
/*     */     public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();
/*     */     
/*     */ 
/*     */ 
/*     */     public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
/*     */     {
/* 478 */       return (SortedSetMultimap)super.build(multimap);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\MultimapBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */