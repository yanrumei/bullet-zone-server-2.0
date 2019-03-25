/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public class ImmutableRangeMap<K extends Comparable<?>, V>
/*     */   implements RangeMap<K, V>, Serializable
/*     */ {
/*  45 */   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(
/*     */   
/*  47 */     ImmutableList.of(), ImmutableList.of());
/*     */   private final transient ImmutableList<Range<K>> ranges;
/*     */   private final transient ImmutableList<V> values;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of()
/*     */   {
/*  54 */     return EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value)
/*     */   {
/*  61 */     return new ImmutableRangeMap(ImmutableList.of(range), ImmutableList.of(value));
/*     */   }
/*     */   
/*     */ 
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap)
/*     */   {
/*  67 */     if ((rangeMap instanceof ImmutableRangeMap)) {
/*  68 */       return (ImmutableRangeMap)rangeMap;
/*     */     }
/*  70 */     Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
/*  71 */     ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder(map.size());
/*  72 */     ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder(map.size());
/*  73 */     for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
/*  74 */       rangesBuilder.add(entry.getKey());
/*  75 */       valuesBuilder.add(entry.getValue());
/*     */     }
/*  77 */     return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> builder()
/*     */   {
/*  84 */     return new Builder();
/*     */   }
/*     */   
/*     */ 
/*     */   public static final class Builder<K extends Comparable<?>, V>
/*     */   {
/*     */     private final List<Map.Entry<Range<K>, V>> entries;
/*     */     
/*     */     public Builder()
/*     */     {
/*  94 */       this.entries = Lists.newArrayList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Range<K> range, V value)
/*     */     {
/* 104 */       Preconditions.checkNotNull(range);
/* 105 */       Preconditions.checkNotNull(value);
/* 106 */       Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
/* 107 */       this.entries.add(Maps.immutableEntry(range, value));
/* 108 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap)
/*     */     {
/* 116 */       for (Map.Entry<Range<K>, ? extends V> entry : rangeMap.asMapOfRanges().entrySet()) {
/* 117 */         put((Range)entry.getKey(), entry.getValue());
/*     */       }
/* 119 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableRangeMap<K, V> build()
/*     */     {
/* 129 */       Collections.sort(this.entries, Range.rangeLexOrdering().onKeys());
/*     */       
/* 131 */       ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder(this.entries.size());
/* 132 */       ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder(this.entries.size());
/* 133 */       for (int i = 0; i < this.entries.size(); i++) {
/* 134 */         Range<K> range = (Range)((Map.Entry)this.entries.get(i)).getKey();
/* 135 */         if (i > 0) {
/* 136 */           Range<K> prevRange = (Range)((Map.Entry)this.entries.get(i - 1)).getKey();
/* 137 */           if ((range.isConnected(prevRange)) && (!range.intersection(prevRange).isEmpty())) {
/* 138 */             throw new IllegalArgumentException("Overlapping ranges: range " + prevRange + " overlaps with entry " + range);
/*     */           }
/*     */         }
/*     */         
/* 142 */         rangesBuilder.add(range);
/* 143 */         valuesBuilder.add(((Map.Entry)this.entries.get(i)).getValue());
/*     */       }
/* 145 */       return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values)
/*     */   {
/* 153 */     this.ranges = ranges;
/* 154 */     this.values = values;
/*     */   }
/*     */   
/*     */ 
/*     */   @Nullable
/*     */   public V get(K key)
/*     */   {
/* 161 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 163 */       Range.lowerBoundFn(), 
/* 164 */       Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/*     */ 
/* 167 */     if (index == -1) {
/* 168 */       return null;
/*     */     }
/* 170 */     Range<K> range = (Range)this.ranges.get(index);
/* 171 */     return (V)(range.contains(key) ? this.values.get(index) : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   public Map.Entry<Range<K>, V> getEntry(K key)
/*     */   {
/* 179 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 181 */       Range.lowerBoundFn(), 
/* 182 */       Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/*     */ 
/* 185 */     if (index == -1) {
/* 186 */       return null;
/*     */     }
/* 188 */     Range<K> range = (Range)this.ranges.get(index);
/* 189 */     return range.contains(key) ? Maps.immutableEntry(range, this.values.get(index)) : null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Range<K> span()
/*     */   {
/* 195 */     if (this.ranges.isEmpty()) {
/* 196 */       throw new NoSuchElementException();
/*     */     }
/* 198 */     Range<K> firstRange = (Range)this.ranges.get(0);
/* 199 */     Range<K> lastRange = (Range)this.ranges.get(this.ranges.size() - 1);
/* 200 */     return Range.create(firstRange.lowerBound, lastRange.upperBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void put(Range<K> range, V value)
/*     */   {
/* 212 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void putCoalescing(Range<K> range, V value)
/*     */   {
/* 224 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void putAll(RangeMap<K, V> rangeMap)
/*     */   {
/* 236 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void clear()
/*     */   {
/* 248 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void remove(Range<K> range)
/*     */   {
/* 260 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asMapOfRanges()
/*     */   {
/* 265 */     if (this.ranges.isEmpty()) {
/* 266 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 269 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet(this.ranges, Range.rangeLexOrdering());
/* 270 */     return new ImmutableSortedMap(rangeSet, this.values);
/*     */   }
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asDescendingMapOfRanges()
/*     */   {
/* 275 */     if (this.ranges.isEmpty()) {
/* 276 */       return ImmutableMap.of();
/*     */     }
/*     */     
/*     */ 
/* 280 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet(this.ranges.reverse(), Range.rangeLexOrdering().reverse());
/* 281 */     return new ImmutableSortedMap(rangeSet, this.values.reverse());
/*     */   }
/*     */   
/*     */   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range)
/*     */   {
/* 286 */     if (((Range)Preconditions.checkNotNull(range)).isEmpty())
/* 287 */       return of();
/* 288 */     if ((this.ranges.isEmpty()) || (range.encloses(span()))) {
/* 289 */       return this;
/*     */     }
/*     */     
/* 292 */     int lowerIndex = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 294 */       Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 299 */     int upperIndex = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 301 */       Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */ 
/*     */ 
/* 305 */     if (lowerIndex >= upperIndex) {
/* 306 */       return of();
/*     */     }
/* 308 */     final int off = lowerIndex;
/* 309 */     final int len = upperIndex - lowerIndex;
/* 310 */     ImmutableList<Range<K>> subRanges = new ImmutableList()
/*     */     {
/*     */       public int size()
/*     */       {
/* 314 */         return len;
/*     */       }
/*     */       
/*     */       public Range<K> get(int index)
/*     */       {
/* 319 */         Preconditions.checkElementIndex(index, len);
/* 320 */         if ((index == 0) || (index == len - 1)) {
/* 321 */           return ((Range)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
/*     */         }
/* 323 */         return (Range)ImmutableRangeMap.this.ranges.get(index + off);
/*     */       }
/*     */       
/*     */ 
/*     */       boolean isPartialView()
/*     */       {
/* 329 */         return true;
/*     */       }
/* 331 */     };
/* 332 */     final ImmutableRangeMap<K, V> outer = this;
/* 333 */     new ImmutableRangeMap(subRanges, this.values.subList(lowerIndex, upperIndex))
/*     */     {
/*     */       public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 336 */         if (range.isConnected(subRange)) {
/* 337 */           return outer.subRangeMap(subRange.intersection(range));
/*     */         }
/* 339 */         return ImmutableRangeMap.of();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 347 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object o)
/*     */   {
/* 352 */     if ((o instanceof RangeMap)) {
/* 353 */       RangeMap<?, ?> rangeMap = (RangeMap)o;
/* 354 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     }
/* 356 */     return false;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 361 */     return asMapOfRanges().toString();
/*     */   }
/*     */   
/*     */ 
/*     */   private static class SerializedForm<K extends Comparable<?>, V>
/*     */     implements Serializable
/*     */   {
/*     */     private final ImmutableMap<Range<K>, V> mapOfRanges;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges)
/*     */     {
/* 373 */       this.mapOfRanges = mapOfRanges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 377 */       if (this.mapOfRanges.isEmpty()) {
/* 378 */         return ImmutableRangeMap.of();
/*     */       }
/* 380 */       return createRangeMap();
/*     */     }
/*     */     
/*     */     Object createRangeMap()
/*     */     {
/* 385 */       ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder();
/* 386 */       for (UnmodifiableIterator localUnmodifiableIterator = this.mapOfRanges.entrySet().iterator(); localUnmodifiableIterator.hasNext();) { Map.Entry<Range<K>, V> entry = (Map.Entry)localUnmodifiableIterator.next();
/* 387 */         builder.put((Range)entry.getKey(), entry.getValue());
/*     */       }
/* 389 */       return builder.build();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 396 */     return new SerializedForm(asMapOfRanges());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ImmutableRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */