/*     */ package org.hibernate.validator.internal.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class CollectionHelper
/*     */ {
/*     */   public static <K, V> HashMap<K, V> newHashMap()
/*     */   {
/*  33 */     return new HashMap();
/*     */   }
/*     */   
/*     */   public static <K, V> HashMap<K, V> newHashMap(int size) {
/*  37 */     return new HashMap(size);
/*     */   }
/*     */   
/*     */   public static <K, V> HashMap<K, V> newHashMap(Map<K, V> map) {
/*  41 */     return new HashMap(map);
/*     */   }
/*     */   
/*     */   public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
/*  45 */     return new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */   public static <T> HashSet<T> newHashSet() {
/*  49 */     return new HashSet();
/*     */   }
/*     */   
/*     */   public static <T> HashSet<T> newHashSet(int size) {
/*  53 */     return new HashSet(size);
/*     */   }
/*     */   
/*     */   public static <T> HashSet<T> newHashSet(Collection<? extends T> c) {
/*  57 */     return new HashSet(c);
/*     */   }
/*     */   
/*     */   public static <T> HashSet<T> newHashSet(Collection<? extends T> s1, Collection<? extends T> s2) {
/*  61 */     HashSet<T> set = newHashSet(s1);
/*  62 */     set.addAll(s2);
/*  63 */     return set;
/*     */   }
/*     */   
/*     */   public static <T> HashSet<T> newHashSet(Iterable<? extends T> iterable) {
/*  67 */     HashSet<T> set = newHashSet();
/*  68 */     for (T t : iterable) {
/*  69 */       set.add(t);
/*     */     }
/*  71 */     return set;
/*     */   }
/*     */   
/*     */   public static <T> ArrayList<T> newArrayList() {
/*  75 */     return new ArrayList();
/*     */   }
/*     */   
/*     */   public static <T> ArrayList<T> newArrayList(int size) {
/*  79 */     return new ArrayList(size);
/*     */   }
/*     */   
/*     */   public static <T> ArrayList<T> newArrayList(Iterable<T>... iterables) {
/*  83 */     ArrayList<T> resultList = newArrayList();
/*  84 */     for (Iterable<T> oneIterable : iterables) {
/*  85 */       for (T oneElement : oneIterable) {
/*  86 */         resultList.add(oneElement);
/*     */       }
/*     */     }
/*  89 */     return resultList;
/*     */   }
/*     */   
/*     */   public static <T> Set<T> asSet(T... ts) {
/*  93 */     return new HashSet(Arrays.asList(ts));
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
/*     */   public static <K, V> Map<K, List<V>> partition(List<V> list, Partitioner<K, V> partitioner)
/*     */   {
/* 109 */     if (list == null) {
/* 110 */       return Collections.emptyMap();
/*     */     }
/*     */     
/* 113 */     Map<K, List<V>> theValue = newHashMap();
/*     */     
/* 115 */     for (V v : list) {
/* 116 */       K key = partitioner.getPartition(v);
/*     */       
/* 118 */       List<V> partition = (List)theValue.get(key);
/* 119 */       if (partition == null) {
/* 120 */         partition = newArrayList();
/* 121 */         theValue.put(key, partition);
/*     */       }
/*     */       
/* 124 */       partition.add(v);
/*     */     }
/*     */     
/* 127 */     return theValue;
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
/*     */   public static <K, V> Map<K, Set<V>> partition(Set<V> set, Partitioner<K, V> partitioner)
/*     */   {
/* 143 */     if (set == null) {
/* 144 */       return Collections.emptyMap();
/*     */     }
/*     */     
/* 147 */     Map<K, Set<V>> theValue = newHashMap();
/*     */     
/* 149 */     for (V v : set) {
/* 150 */       K key = partitioner.getPartition(v);
/*     */       
/* 152 */       Set<V> partition = (Set)theValue.get(key);
/* 153 */       if (partition == null) {
/* 154 */         partition = newHashSet();
/* 155 */         theValue.put(key, partition);
/*     */       }
/*     */       
/* 158 */       partition.add(v);
/*     */     }
/*     */     
/* 161 */     return theValue;
/*     */   }
/*     */   
/*     */   public static abstract interface Partitioner<K, V>
/*     */   {
/*     */     public abstract K getPartition(V paramV);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\CollectionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */