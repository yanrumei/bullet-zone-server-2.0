/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public abstract class CollectionFactory
/*     */ {
/*  56 */   private static final Set<Class<?>> approximableCollectionTypes = new HashSet();
/*     */   
/*  58 */   private static final Set<Class<?>> approximableMapTypes = new HashSet();
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  63 */     approximableCollectionTypes.add(Collection.class);
/*  64 */     approximableCollectionTypes.add(List.class);
/*  65 */     approximableCollectionTypes.add(Set.class);
/*  66 */     approximableCollectionTypes.add(SortedSet.class);
/*  67 */     approximableCollectionTypes.add(NavigableSet.class);
/*  68 */     approximableMapTypes.add(Map.class);
/*  69 */     approximableMapTypes.add(SortedMap.class);
/*  70 */     approximableMapTypes.add(NavigableMap.class);
/*     */     
/*     */ 
/*  73 */     approximableCollectionTypes.add(ArrayList.class);
/*  74 */     approximableCollectionTypes.add(LinkedList.class);
/*  75 */     approximableCollectionTypes.add(HashSet.class);
/*  76 */     approximableCollectionTypes.add(LinkedHashSet.class);
/*  77 */     approximableCollectionTypes.add(TreeSet.class);
/*  78 */     approximableCollectionTypes.add(EnumSet.class);
/*  79 */     approximableMapTypes.add(HashMap.class);
/*  80 */     approximableMapTypes.add(LinkedHashMap.class);
/*  81 */     approximableMapTypes.add(TreeMap.class);
/*  82 */     approximableMapTypes.add(EnumMap.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isApproximableCollectionType(Class<?> collectionType)
/*     */   {
/*  93 */     return (collectionType != null) && (approximableCollectionTypes.contains(collectionType));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Collection<E> createApproximateCollection(Object collection, int capacity)
/*     */   {
/* 118 */     if ((collection instanceof LinkedList)) {
/* 119 */       return new LinkedList();
/*     */     }
/* 121 */     if ((collection instanceof List)) {
/* 122 */       return new ArrayList(capacity);
/*     */     }
/* 124 */     if ((collection instanceof EnumSet))
/*     */     {
/* 126 */       Collection<E> enumSet = EnumSet.copyOf((EnumSet)collection);
/* 127 */       enumSet.clear();
/* 128 */       return enumSet;
/*     */     }
/* 130 */     if ((collection instanceof SortedSet)) {
/* 131 */       return new TreeSet(((SortedSet)collection).comparator());
/*     */     }
/*     */     
/* 134 */     return new LinkedHashSet(capacity);
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
/*     */   public static <E> Collection<E> createCollection(Class<?> collectionType, int capacity)
/*     */   {
/* 149 */     return createCollection(collectionType, null, capacity);
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
/*     */   public static <E> Collection<E> createCollection(Class<?> collectionType, Class<?> elementType, int capacity)
/*     */   {
/* 177 */     Assert.notNull(collectionType, "Collection type must not be null");
/* 178 */     if (collectionType.isInterface()) {
/* 179 */       if ((Set.class == collectionType) || (Collection.class == collectionType)) {
/* 180 */         return new LinkedHashSet(capacity);
/*     */       }
/* 182 */       if (List.class == collectionType) {
/* 183 */         return new ArrayList(capacity);
/*     */       }
/* 185 */       if ((SortedSet.class == collectionType) || (NavigableSet.class == collectionType)) {
/* 186 */         return new TreeSet();
/*     */       }
/*     */       
/* 189 */       throw new IllegalArgumentException("Unsupported Collection interface: " + collectionType.getName());
/*     */     }
/*     */     
/* 192 */     if (EnumSet.class == collectionType) {
/* 193 */       Assert.notNull(elementType, "Cannot create EnumSet for unknown element type");
/*     */       
/* 195 */       return EnumSet.noneOf(asEnumType(elementType));
/*     */     }
/*     */     
/* 198 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/* 199 */       throw new IllegalArgumentException("Unsupported Collection type: " + collectionType.getName());
/*     */     }
/*     */     try {
/* 202 */       return (Collection)collectionType.newInstance();
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/* 206 */       throw new IllegalArgumentException("Could not instantiate Collection type: " + collectionType.getName(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isApproximableMapType(Class<?> mapType)
/*     */   {
/* 218 */     return (mapType != null) && (approximableMapTypes.contains(mapType));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> Map<K, V> createApproximateMap(Object map, int capacity)
/*     */   {
/* 240 */     if ((map instanceof EnumMap)) {
/* 241 */       EnumMap enumMap = new EnumMap((EnumMap)map);
/* 242 */       enumMap.clear();
/* 243 */       return enumMap;
/*     */     }
/* 245 */     if ((map instanceof SortedMap)) {
/* 246 */       return new TreeMap(((SortedMap)map).comparator());
/*     */     }
/*     */     
/* 249 */     return new LinkedHashMap(capacity);
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
/*     */   public static <K, V> Map<K, V> createMap(Class<?> mapType, int capacity)
/*     */   {
/* 264 */     return createMap(mapType, null, capacity);
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
/*     */   public static <K, V> Map<K, V> createMap(Class<?> mapType, Class<?> keyType, int capacity)
/*     */   {
/* 293 */     Assert.notNull(mapType, "Map type must not be null");
/* 294 */     if (mapType.isInterface()) {
/* 295 */       if (Map.class == mapType) {
/* 296 */         return new LinkedHashMap(capacity);
/*     */       }
/* 298 */       if ((SortedMap.class == mapType) || (NavigableMap.class == mapType)) {
/* 299 */         return new TreeMap();
/*     */       }
/* 301 */       if (MultiValueMap.class == mapType) {
/* 302 */         return new LinkedMultiValueMap();
/*     */       }
/*     */       
/* 305 */       throw new IllegalArgumentException("Unsupported Map interface: " + mapType.getName());
/*     */     }
/*     */     
/* 308 */     if (EnumMap.class == mapType) {
/* 309 */       Assert.notNull(keyType, "Cannot create EnumMap for unknown key type");
/* 310 */       return new EnumMap(asEnumType(keyType));
/*     */     }
/*     */     
/* 313 */     if (!Map.class.isAssignableFrom(mapType)) {
/* 314 */       throw new IllegalArgumentException("Unsupported Map type: " + mapType.getName());
/*     */     }
/*     */     try {
/* 317 */       return (Map)mapType.newInstance();
/*     */     }
/*     */     catch (Throwable ex) {
/* 320 */       throw new IllegalArgumentException("Could not instantiate Map type: " + mapType.getName(), ex);
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
/*     */   public static Properties createStringAdaptingProperties()
/*     */   {
/* 333 */     new Properties()
/*     */     {
/*     */       public String getProperty(String key) {
/* 336 */         Object value = get(key);
/* 337 */         return value != null ? value.toString() : null;
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
/*     */   private static Class<? extends Enum> asEnumType(Class<?> enumType)
/*     */   {
/* 350 */     Assert.notNull(enumType, "Enum type must not be null");
/* 351 */     if (!Enum.class.isAssignableFrom(enumType)) {
/* 352 */       throw new IllegalArgumentException("Supplied type is not an enum: " + enumType.getName());
/*     */     }
/* 354 */     return enumType.asSubclass(Enum.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\CollectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */