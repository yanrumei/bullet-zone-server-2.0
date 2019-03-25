/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.function.BiConsumer;
/*     */ import javax.annotation.Nullable;
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
/*     */ final class RegularImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*  42 */   static final ImmutableMap<Object, Object> EMPTY = new RegularImmutableMap((Map.Entry[])ImmutableMap.EMPTY_ENTRY_ARRAY, null, 0);
/*     */   
/*     */   private final transient Map.Entry<K, V>[] entries;
/*     */   
/*     */   private final transient ImmutableMapEntry<K, V>[] table;
/*     */   
/*     */   private final transient int mask;
/*     */   private static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   static <K, V> RegularImmutableMap<K, V> fromEntries(Map.Entry<K, V>... entries)
/*     */   {
/*  54 */     return fromEntryArray(entries.length, entries);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static <K, V> RegularImmutableMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray)
/*     */   {
/*  63 */     Preconditions.checkPositionIndex(n, entryArray.length);
/*  64 */     if (n == 0)
/*  65 */       return (RegularImmutableMap)EMPTY;
/*     */     Map.Entry<K, V>[] entries;
/*     */     Map.Entry<K, V>[] entries;
/*  68 */     if (n == entryArray.length) {
/*  69 */       entries = entryArray;
/*     */     } else {
/*  71 */       entries = ImmutableMapEntry.createEntryArray(n);
/*     */     }
/*  73 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  74 */     ImmutableMapEntry<K, V>[] table = ImmutableMapEntry.createEntryArray(tableSize);
/*  75 */     int mask = tableSize - 1;
/*  76 */     for (int entryIndex = 0; entryIndex < n; entryIndex++) {
/*  77 */       Map.Entry<K, V> entry = entryArray[entryIndex];
/*  78 */       K key = entry.getKey();
/*  79 */       V value = entry.getValue();
/*  80 */       CollectPreconditions.checkEntryNotNull(key, value);
/*  81 */       int tableIndex = Hashing.smear(key.hashCode()) & mask;
/*  82 */       ImmutableMapEntry<K, V> existing = table[tableIndex];
/*     */       ImmutableMapEntry<K, V> newEntry;
/*     */       ImmutableMapEntry<K, V> newEntry;
/*  85 */       if (existing == null)
/*     */       {
/*  87 */         boolean reusable = ((entry instanceof ImmutableMapEntry)) && (((ImmutableMapEntry)entry).isReusable());
/*  88 */         newEntry = reusable ? (ImmutableMapEntry)entry : new ImmutableMapEntry(key, value);
/*     */       }
/*     */       else {
/*  91 */         newEntry = new ImmutableMapEntry.NonTerminalImmutableMapEntry(key, value, existing);
/*     */       }
/*  93 */       table[tableIndex] = newEntry;
/*  94 */       entries[entryIndex] = newEntry;
/*  95 */       checkNoConflictInKeyBucket(key, newEntry, existing);
/*     */     }
/*  97 */     return new RegularImmutableMap(entries, table, mask);
/*     */   }
/*     */   
/*     */   private RegularImmutableMap(Map.Entry<K, V>[] entries, ImmutableMapEntry<K, V>[] table, int mask) {
/* 101 */     this.entries = entries;
/* 102 */     this.table = table;
/* 103 */     this.mask = mask;
/*     */   }
/*     */   
/*     */   static void checkNoConflictInKeyBucket(Object key, Map.Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> keyBucketHead)
/*     */   {
/* 108 */     for (; keyBucketHead != null; keyBucketHead = keyBucketHead.getNextInKeyBucket()) {
/* 109 */       checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
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
/*     */   public V get(@Nullable Object key)
/*     */   {
/* 122 */     return (V)get(key, this.table, this.mask);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   static <V> V get(@Nullable Object key, @Nullable ImmutableMapEntry<?, V>[] keyTable, int mask) {
/* 127 */     if ((key == null) || (keyTable == null)) {
/* 128 */       return null;
/*     */     }
/* 130 */     int index = Hashing.smear(key.hashCode()) & mask;
/* 131 */     for (ImmutableMapEntry<?, V> entry = keyTable[index]; 
/* 132 */         entry != null; 
/* 133 */         entry = entry.getNextInKeyBucket()) {
/* 134 */       Object candidateKey = entry.getKey();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */       if (key.equals(candidateKey)) {
/* 143 */         return (V)entry.getValue();
/*     */       }
/*     */     }
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action)
/*     */   {
/* 151 */     Preconditions.checkNotNull(action);
/* 152 */     for (Map.Entry<K, V> entry : this.entries) {
/* 153 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 159 */     return this.entries.length;
/*     */   }
/*     */   
/*     */   boolean isPartialView()
/*     */   {
/* 164 */     return false;
/*     */   }
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet()
/*     */   {
/* 169 */     return new ImmutableMapEntrySet.RegularEntrySet(this, this.entries);
/*     */   }
/*     */   
/*     */   ImmutableSet<K> createKeySet()
/*     */   {
/* 174 */     return new KeySet(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated=true)
/*     */   private static final class KeySet<K, V> extends ImmutableSet.Indexed<K> {
/*     */     @Weak
/*     */     private final RegularImmutableMap<K, V> map;
/*     */     
/* 182 */     KeySet(RegularImmutableMap<K, V> map) { this.map = map; }
/*     */     
/*     */ 
/*     */     K get(int index)
/*     */     {
/* 187 */       return (K)this.map.entries[index].getKey();
/*     */     }
/*     */     
/*     */     public boolean contains(Object object)
/*     */     {
/* 192 */       return this.map.containsKey(object);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 197 */       return true;
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 202 */       return this.map.size();
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace()
/*     */     {
/* 208 */       return new SerializedForm(this.map);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static class SerializedForm<K> implements Serializable {
/*     */       final ImmutableMap<K, ?> map;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/* 216 */       SerializedForm(ImmutableMap<K, ?> map) { this.map = map; }
/*     */       
/*     */       Object readResolve()
/*     */       {
/* 220 */         return this.map.keySet();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   ImmutableCollection<V> createValues()
/*     */   {
/* 229 */     return new Values(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated=true)
/*     */   private static final class Values<K, V> extends ImmutableList<V> {
/*     */     @Weak
/*     */     final RegularImmutableMap<K, V> map;
/*     */     
/* 237 */     Values(RegularImmutableMap<K, V> map) { this.map = map; }
/*     */     
/*     */ 
/*     */     public V get(int index)
/*     */     {
/* 242 */       return (V)this.map.entries[index].getValue();
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 247 */       return this.map.size();
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 252 */       return true;
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace()
/*     */     {
/* 258 */       return new SerializedForm(this.map);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static class SerializedForm<V> implements Serializable {
/*     */       final ImmutableMap<?, V> map;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/* 266 */       SerializedForm(ImmutableMap<?, V> map) { this.map = map; }
/*     */       
/*     */       Object readResolve()
/*     */       {
/* 270 */         return this.map.values();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\RegularImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */