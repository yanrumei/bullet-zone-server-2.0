/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public class TreeRangeSet<C extends Comparable<?>>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*     */   @VisibleForTesting
/*     */   final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */   private transient Set<Range<C>> asRanges;
/*     */   private transient Set<Range<C>> asDescendingSetOfRanges;
/*     */   private transient RangeSet<C> complement;
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create()
/*     */   {
/*  52 */     return new TreeRangeSet(new TreeMap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet)
/*     */   {
/*  59 */     TreeRangeSet<C> result = create();
/*  60 */     result.addAll(rangeSet);
/*  61 */     return result;
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
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(Iterable<Range<C>> ranges)
/*     */   {
/*  74 */     TreeRangeSet<C> result = create();
/*  75 */     result.addAll(ranges);
/*  76 */     return result;
/*     */   }
/*     */   
/*     */   private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
/*  80 */     this.rangesByLowerBound = rangesByLowerCut;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<Range<C>> asRanges()
/*     */   {
/*  88 */     Set<Range<C>> result = this.asRanges;
/*  89 */     return result == null ? (this.asRanges = new AsRanges(this.rangesByLowerBound.values())) : result;
/*     */   }
/*     */   
/*     */   public Set<Range<C>> asDescendingSetOfRanges()
/*     */   {
/*  94 */     Set<Range<C>> result = this.asDescendingSetOfRanges;
/*  95 */     return result == null ? 
/*  96 */       (this.asDescendingSetOfRanges = new AsRanges(this.rangesByLowerBound.descendingMap().values())) : result;
/*     */   }
/*     */   
/*     */   final class AsRanges extends ForwardingCollection<Range<C>> implements Set<Range<C>>
/*     */   {
/*     */     final Collection<Range<C>> delegate;
/*     */     
/*     */     AsRanges()
/*     */     {
/* 105 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     protected Collection<Range<C>> delegate()
/*     */     {
/* 110 */       return this.delegate;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 115 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object o)
/*     */     {
/* 120 */       return Sets.equalsImpl(this, o);
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Range<C> rangeContaining(C value)
/*     */   {
/* 127 */     Preconditions.checkNotNull(value);
/* 128 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(Cut.belowValue(value));
/* 129 */     if ((floorEntry != null) && (((Range)floorEntry.getValue()).contains(value))) {
/* 130 */       return (Range)floorEntry.getValue();
/*     */     }
/*     */     
/* 133 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean intersects(Range<C> range)
/*     */   {
/* 139 */     Preconditions.checkNotNull(range);
/* 140 */     Map.Entry<Cut<C>, Range<C>> ceilingEntry = this.rangesByLowerBound.ceilingEntry(range.lowerBound);
/* 141 */     if ((ceilingEntry != null) && 
/* 142 */       (((Range)ceilingEntry.getValue()).isConnected(range)) && 
/* 143 */       (!((Range)ceilingEntry.getValue()).intersection(range).isEmpty())) {
/* 144 */       return true;
/*     */     }
/* 146 */     Map.Entry<Cut<C>, Range<C>> priorEntry = this.rangesByLowerBound.lowerEntry(range.lowerBound);
/* 147 */     return (priorEntry != null) && 
/* 148 */       (((Range)priorEntry.getValue()).isConnected(range)) && 
/* 149 */       (!((Range)priorEntry.getValue()).intersection(range).isEmpty());
/*     */   }
/*     */   
/*     */   public boolean encloses(Range<C> range)
/*     */   {
/* 154 */     Preconditions.checkNotNull(range);
/* 155 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 156 */     return (floorEntry != null) && (((Range)floorEntry.getValue()).encloses(range));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Range<C> rangeEnclosing(Range<C> range) {
/* 161 */     Preconditions.checkNotNull(range);
/* 162 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 163 */     return (floorEntry != null) && (((Range)floorEntry.getValue()).encloses(range)) ? 
/* 164 */       (Range)floorEntry.getValue() : null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Range<C> span()
/*     */   {
/* 170 */     Map.Entry<Cut<C>, Range<C>> firstEntry = this.rangesByLowerBound.firstEntry();
/* 171 */     Map.Entry<Cut<C>, Range<C>> lastEntry = this.rangesByLowerBound.lastEntry();
/* 172 */     if (firstEntry == null) {
/* 173 */       throw new NoSuchElementException();
/*     */     }
/* 175 */     return Range.create(((Range)firstEntry.getValue()).lowerBound, ((Range)lastEntry.getValue()).upperBound);
/*     */   }
/*     */   
/*     */   public void add(Range<C> rangeToAdd)
/*     */   {
/* 180 */     Preconditions.checkNotNull(rangeToAdd);
/*     */     
/* 182 */     if (rangeToAdd.isEmpty()) {
/* 183 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 188 */     Cut<C> lbToAdd = rangeToAdd.lowerBound;
/* 189 */     Cut<C> ubToAdd = rangeToAdd.upperBound;
/*     */     
/* 191 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(lbToAdd);
/* 192 */     if (entryBelowLB != null)
/*     */     {
/* 194 */       Range<C> rangeBelowLB = (Range)entryBelowLB.getValue();
/* 195 */       if (rangeBelowLB.upperBound.compareTo(lbToAdd) >= 0)
/*     */       {
/* 197 */         if (rangeBelowLB.upperBound.compareTo(ubToAdd) >= 0)
/*     */         {
/* 199 */           ubToAdd = rangeBelowLB.upperBound;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 205 */         lbToAdd = rangeBelowLB.lowerBound;
/*     */       }
/*     */     }
/*     */     
/* 209 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(ubToAdd);
/* 210 */     if (entryBelowUB != null)
/*     */     {
/* 212 */       Range<C> rangeBelowUB = (Range)entryBelowUB.getValue();
/* 213 */       if (rangeBelowUB.upperBound.compareTo(ubToAdd) >= 0)
/*     */       {
/* 215 */         ubToAdd = rangeBelowUB.upperBound;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 220 */     this.rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
/*     */     
/* 222 */     replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
/*     */   }
/*     */   
/*     */   public void remove(Range<C> rangeToRemove)
/*     */   {
/* 227 */     Preconditions.checkNotNull(rangeToRemove);
/*     */     
/* 229 */     if (rangeToRemove.isEmpty()) {
/* 230 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 236 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 237 */     if (entryBelowLB != null)
/*     */     {
/* 239 */       Range<C> rangeBelowLB = (Range)entryBelowLB.getValue();
/* 240 */       if (rangeBelowLB.upperBound.compareTo(rangeToRemove.lowerBound) >= 0)
/*     */       {
/* 242 */         if ((rangeToRemove.hasUpperBound()) && 
/* 243 */           (rangeBelowLB.upperBound.compareTo(rangeToRemove.upperBound) >= 0))
/*     */         {
/* 245 */           replaceRangeWithSameLowerBound(
/* 246 */             Range.create(rangeToRemove.upperBound, rangeBelowLB.upperBound));
/*     */         }
/* 248 */         replaceRangeWithSameLowerBound(
/* 249 */           Range.create(rangeBelowLB.lowerBound, rangeToRemove.lowerBound));
/*     */       }
/*     */     }
/*     */     
/* 253 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(rangeToRemove.upperBound);
/* 254 */     if (entryBelowUB != null)
/*     */     {
/* 256 */       Range<C> rangeBelowUB = (Range)entryBelowUB.getValue();
/* 257 */       if ((rangeToRemove.hasUpperBound()) && 
/* 258 */         (rangeBelowUB.upperBound.compareTo(rangeToRemove.upperBound) >= 0))
/*     */       {
/* 260 */         replaceRangeWithSameLowerBound(
/* 261 */           Range.create(rangeToRemove.upperBound, rangeBelowUB.upperBound));
/*     */       }
/*     */     }
/*     */     
/* 265 */     this.rangesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */   
/*     */   private void replaceRangeWithSameLowerBound(Range<C> range) {
/* 269 */     if (range.isEmpty()) {
/* 270 */       this.rangesByLowerBound.remove(range.lowerBound);
/*     */     } else {
/* 272 */       this.rangesByLowerBound.put(range.lowerBound, range);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RangeSet<C> complement()
/*     */   {
/* 280 */     RangeSet<C> result = this.complement;
/* 281 */     return result == null ? (this.complement = new Complement()) : result;
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static final class RangesByUpperBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */     
/*     */     private final Range<Cut<C>> upperBoundWindow;
/*     */     
/*     */ 
/*     */     RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound)
/*     */     {
/* 296 */       this.rangesByLowerBound = rangesByLowerBound;
/* 297 */       this.upperBoundWindow = Range.all();
/*     */     }
/*     */     
/*     */     private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, Range<Cut<C>> upperBoundWindow)
/*     */     {
/* 302 */       this.rangesByLowerBound = rangesByLowerBound;
/* 303 */       this.upperBoundWindow = upperBoundWindow;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 307 */       if (window.isConnected(this.upperBoundWindow)) {
/* 308 */         return new RangesByUpperBound(this.rangesByLowerBound, window.intersection(this.upperBoundWindow));
/*     */       }
/* 310 */       return ImmutableSortedMap.of();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive)
/*     */     {
/* 317 */       return subMap(
/* 318 */         Range.range(fromKey, 
/* 319 */         BoundType.forBoolean(fromInclusive), toKey, 
/* 320 */         BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive)
/*     */     {
/* 325 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive)
/*     */     {
/* 330 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator()
/*     */     {
/* 335 */       return Ordering.natural();
/*     */     }
/*     */     
/*     */     public boolean containsKey(@Nullable Object key)
/*     */     {
/* 340 */       return get(key) != null;
/*     */     }
/*     */     
/*     */     public Range<C> get(@Nullable Object key)
/*     */     {
/* 345 */       if ((key instanceof Cut)) {
/*     */         try
/*     */         {
/* 348 */           Cut<C> cut = (Cut)key;
/* 349 */           if (!this.upperBoundWindow.contains(cut)) {
/* 350 */             return null;
/*     */           }
/* 352 */           Map.Entry<Cut<C>, Range<C>> candidate = this.rangesByLowerBound.lowerEntry(cut);
/* 353 */           if ((candidate != null) && (((Range)candidate.getValue()).upperBound.equals(cut))) {
/* 354 */             return (Range)candidate.getValue();
/*     */           }
/*     */         } catch (ClassCastException e) {
/* 357 */           return null;
/*     */         }
/*     */       }
/* 360 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator()
/*     */     {
/*     */       Iterator<Range<C>> backingItr;
/*     */       
/*     */       final Iterator<Range<C>> backingItr;
/*     */       
/* 370 */       if (!this.upperBoundWindow.hasLowerBound()) {
/* 371 */         backingItr = this.rangesByLowerBound.values().iterator();
/*     */       }
/*     */       else {
/* 374 */         Map.Entry<Cut<C>, Range<C>> lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
/* 375 */         Iterator<Range<C>> backingItr; if (lowerEntry == null) {
/* 376 */           backingItr = this.rangesByLowerBound.values().iterator(); } else { Iterator<Range<C>> backingItr;
/* 377 */           if (this.upperBoundWindow.lowerBound.isLessThan(((Range)lowerEntry.getValue()).upperBound)) {
/* 378 */             backingItr = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */ 
/* 384 */             backingItr = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator(); }
/*     */         }
/*     */       }
/* 387 */       new AbstractIterator()
/*     */       {
/*     */         protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 390 */           if (!backingItr.hasNext()) {
/* 391 */             return (Map.Entry)endOfData();
/*     */           }
/* 393 */           Range<C> range = (Range)backingItr.next();
/* 394 */           if (TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound)) {
/* 395 */             return (Map.Entry)endOfData();
/*     */           }
/* 397 */           return Maps.immutableEntry(range.upperBound, range);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator()
/*     */     {
/*     */       Collection<Range<C>> candidates;
/*     */       Collection<Range<C>> candidates;
/* 406 */       if (this.upperBoundWindow.hasUpperBound())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 411 */         candidates = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
/*     */       } else {
/* 413 */         candidates = this.rangesByLowerBound.descendingMap().values();
/*     */       }
/* 415 */       final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator(candidates.iterator());
/* 416 */       if ((backingItr.hasNext()) && 
/* 417 */         (this.upperBoundWindow.upperBound.isLessThan(((Range)backingItr.peek()).upperBound))) {
/* 418 */         backingItr.next();
/*     */       }
/* 420 */       new AbstractIterator()
/*     */       {
/*     */         protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 423 */           if (!backingItr.hasNext()) {
/* 424 */             return (Map.Entry)endOfData();
/*     */           }
/* 426 */           Range<C> range = (Range)backingItr.next();
/* 427 */           return TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound) ? 
/* 428 */             Maps.immutableEntry(range.upperBound, range) : 
/* 429 */             (Map.Entry)endOfData();
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 436 */       if (this.upperBoundWindow.equals(Range.all())) {
/* 437 */         return this.rangesByLowerBound.size();
/*     */       }
/* 439 */       return Iterators.size(entryIterator());
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/* 444 */       return 
/*     */       
/* 446 */         !entryIterator().hasNext() ? true : this.upperBoundWindow.equals(Range.all()) ? this.rangesByLowerBound.isEmpty() : false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ComplementRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
/*     */     
/*     */     private final Range<Cut<C>> complementLowerBoundWindow;
/*     */     
/*     */ 
/*     */     ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound)
/*     */     {
/* 463 */       this(positiveRangesByLowerBound, Range.all());
/*     */     }
/*     */     
/*     */     private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, Range<Cut<C>> window)
/*     */     {
/* 468 */       this.positiveRangesByLowerBound = positiveRangesByLowerBound;
/* 469 */       this.positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound(positiveRangesByLowerBound);
/* 470 */       this.complementLowerBoundWindow = window;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
/* 474 */       if (!this.complementLowerBoundWindow.isConnected(subWindow)) {
/* 475 */         return ImmutableSortedMap.of();
/*     */       }
/* 477 */       subWindow = subWindow.intersection(this.complementLowerBoundWindow);
/* 478 */       return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, subWindow);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive)
/*     */     {
/* 485 */       return subMap(
/* 486 */         Range.range(fromKey, 
/* 487 */         BoundType.forBoolean(fromInclusive), toKey, 
/* 488 */         BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive)
/*     */     {
/* 493 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive)
/*     */     {
/* 498 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator()
/*     */     {
/* 503 */       return Ordering.natural();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator()
/*     */     {
/*     */       Collection<Range<C>> positiveRanges;
/*     */       
/*     */ 
/*     */ 
/*     */       Collection<Range<C>> positiveRanges;
/*     */       
/*     */ 
/* 518 */       if (this.complementLowerBoundWindow.hasLowerBound())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 524 */         positiveRanges = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values();
/*     */       } else {
/* 526 */         positiveRanges = this.positiveRangesByUpperBound.values();
/*     */       }
/*     */       
/* 529 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRanges.iterator());
/*     */       Cut<C> firstComplementRangeLowerBound;
/* 531 */       if ((this.complementLowerBoundWindow.contains(Cut.belowAll())) && (
/* 532 */         (!positiveItr.hasNext()) || (((Range)positiveItr.peek()).lowerBound != Cut.belowAll()))) {
/* 533 */         firstComplementRangeLowerBound = Cut.belowAll(); } else { Cut<C> firstComplementRangeLowerBound;
/* 534 */         if (positiveItr.hasNext()) {
/* 535 */           firstComplementRangeLowerBound = ((Range)positiveItr.next()).upperBound;
/*     */         } else
/* 537 */           return Iterators.emptyIterator(); }
/*     */       final Cut<C> firstComplementRangeLowerBound;
/* 539 */       new AbstractIterator() {
/* 540 */         Cut<C> nextComplementRangeLowerBound = firstComplementRangeLowerBound;
/*     */         
/*     */         protected Map.Entry<Cut<C>, Range<C>> computeNext()
/*     */         {
/* 544 */           if ((TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound)) || 
/* 545 */             (this.nextComplementRangeLowerBound == Cut.aboveAll())) {
/* 546 */             return (Map.Entry)endOfData();
/*     */           }
/*     */           Range<C> negativeRange;
/* 549 */           if (positiveItr.hasNext()) {
/* 550 */             Range<C> positiveRange = (Range)positiveItr.next();
/* 551 */             Range<C> negativeRange = Range.create(this.nextComplementRangeLowerBound, positiveRange.lowerBound);
/* 552 */             this.nextComplementRangeLowerBound = positiveRange.upperBound;
/*     */           } else {
/* 554 */             negativeRange = Range.create(this.nextComplementRangeLowerBound, Cut.aboveAll());
/* 555 */             this.nextComplementRangeLowerBound = Cut.aboveAll();
/*     */           }
/* 557 */           return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */         }
/*     */       };
/*     */     }
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
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator()
/*     */     {
/* 575 */       Cut<C> startingPoint = this.complementLowerBoundWindow.hasUpperBound() ? (Cut)this.complementLowerBoundWindow.upperEndpoint() : Cut.aboveAll();
/*     */       
/*     */ 
/* 578 */       boolean inclusive = (this.complementLowerBoundWindow.hasUpperBound()) && (this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED);
/*     */       
/* 580 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(this.positiveRangesByUpperBound
/*     */       
/* 582 */         .headMap(startingPoint, inclusive)
/* 583 */         .descendingMap()
/* 584 */         .values()
/* 585 */         .iterator());
/*     */       Cut<C> cut;
/* 587 */       Cut<C> cut; if (positiveItr.hasNext())
/*     */       {
/*     */ 
/*     */ 
/* 591 */         cut = ((Range)positiveItr.peek()).upperBound == Cut.aboveAll() ? ((Range)positiveItr.next()).lowerBound : (Cut)this.positiveRangesByLowerBound.higherKey(((Range)positiveItr.peek()).upperBound);
/* 592 */       } else { if ((!this.complementLowerBoundWindow.contains(Cut.belowAll())) || 
/* 593 */           (this.positiveRangesByLowerBound.containsKey(Cut.belowAll()))) {
/* 594 */           return Iterators.emptyIterator();
/*     */         }
/* 596 */         cut = (Cut)this.positiveRangesByLowerBound.higherKey(Cut.belowAll());
/*     */       }
/*     */       
/* 599 */       final Cut<C> firstComplementRangeUpperBound = (Cut)MoreObjects.firstNonNull(cut, Cut.aboveAll());
/* 600 */       new AbstractIterator() {
/* 601 */         Cut<C> nextComplementRangeUpperBound = firstComplementRangeUpperBound;
/*     */         
/*     */         protected Map.Entry<Cut<C>, Range<C>> computeNext()
/*     */         {
/* 605 */           if (this.nextComplementRangeUpperBound == Cut.belowAll())
/* 606 */             return (Map.Entry)endOfData();
/* 607 */           if (positiveItr.hasNext()) {
/* 608 */             Range<C> positiveRange = (Range)positiveItr.next();
/*     */             
/* 610 */             Range<C> negativeRange = Range.create(positiveRange.upperBound, this.nextComplementRangeUpperBound);
/* 611 */             this.nextComplementRangeUpperBound = positiveRange.lowerBound;
/* 612 */             if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(negativeRange.lowerBound)) {
/* 613 */               return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */             }
/* 615 */           } else if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
/* 616 */             Range<C> negativeRange = Range.create(Cut.belowAll(), this.nextComplementRangeUpperBound);
/* 617 */             this.nextComplementRangeUpperBound = Cut.belowAll();
/* 618 */             return Maps.immutableEntry(Cut.belowAll(), negativeRange);
/*     */           }
/* 620 */           return (Map.Entry)endOfData();
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 627 */       return Iterators.size(entryIterator());
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Range<C> get(Object key)
/*     */     {
/* 633 */       if ((key instanceof Cut)) {
/*     */         try
/*     */         {
/* 636 */           Cut<C> cut = (Cut)key;
/*     */           
/* 638 */           Map.Entry<Cut<C>, Range<C>> firstEntry = tailMap(cut, true).firstEntry();
/* 639 */           if ((firstEntry != null) && (((Cut)firstEntry.getKey()).equals(cut))) {
/* 640 */             return (Range)firstEntry.getValue();
/*     */           }
/*     */         } catch (ClassCastException e) {
/* 643 */           return null;
/*     */         }
/*     */       }
/* 646 */       return null;
/*     */     }
/*     */     
/*     */     public boolean containsKey(Object key)
/*     */     {
/* 651 */       return get(key) != null;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Complement extends TreeRangeSet<C> {
/*     */     Complement() {
/* 657 */       super(null);
/*     */     }
/*     */     
/*     */     public void add(Range<C> rangeToAdd)
/*     */     {
/* 662 */       TreeRangeSet.this.remove(rangeToAdd);
/*     */     }
/*     */     
/*     */     public void remove(Range<C> rangeToRemove)
/*     */     {
/* 667 */       TreeRangeSet.this.add(rangeToRemove);
/*     */     }
/*     */     
/*     */     public boolean contains(C value)
/*     */     {
/* 672 */       return !TreeRangeSet.this.contains(value);
/*     */     }
/*     */     
/*     */     public RangeSet<C> complement()
/*     */     {
/* 677 */       return TreeRangeSet.this;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final Range<Cut<C>> lowerBoundWindow;
/*     */     
/*     */ 
/*     */     private final Range<C> restriction;
/*     */     
/*     */ 
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */     
/*     */ 
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
/*     */     
/*     */ 
/*     */ 
/*     */     private SubRangeSetRangesByLowerBound(Range<Cut<C>> lowerBoundWindow, Range<C> restriction, NavigableMap<Cut<C>, Range<C>> rangesByLowerBound)
/*     */     {
/* 702 */       this.lowerBoundWindow = ((Range)Preconditions.checkNotNull(lowerBoundWindow));
/* 703 */       this.restriction = ((Range)Preconditions.checkNotNull(restriction));
/* 704 */       this.rangesByLowerBound = ((NavigableMap)Preconditions.checkNotNull(rangesByLowerBound));
/* 705 */       this.rangesByUpperBound = new TreeRangeSet.RangesByUpperBound(rangesByLowerBound);
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 709 */       if (!window.isConnected(this.lowerBoundWindow)) {
/* 710 */         return ImmutableSortedMap.of();
/*     */       }
/* 712 */       return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow
/* 713 */         .intersection(window), this.restriction, this.rangesByLowerBound);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive)
/*     */     {
/* 720 */       return subMap(
/* 721 */         Range.range(fromKey, 
/*     */         
/* 723 */         BoundType.forBoolean(fromInclusive), toKey, 
/*     */         
/* 725 */         BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive)
/*     */     {
/* 730 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive)
/*     */     {
/* 735 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator()
/*     */     {
/* 740 */       return Ordering.natural();
/*     */     }
/*     */     
/*     */     public boolean containsKey(@Nullable Object key)
/*     */     {
/* 745 */       return get(key) != null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Range<C> get(@Nullable Object key)
/*     */     {
/* 751 */       if ((key instanceof Cut)) {
/*     */         try
/*     */         {
/* 754 */           Cut<C> cut = (Cut)key;
/* 755 */           if ((!this.lowerBoundWindow.contains(cut)) || 
/* 756 */             (cut.compareTo(this.restriction.lowerBound) < 0) || 
/* 757 */             (cut.compareTo(this.restriction.upperBound) >= 0))
/* 758 */             return null;
/* 759 */           if (cut.equals(this.restriction.lowerBound))
/*     */           {
/* 761 */             Range<C> candidate = (Range)Maps.valueOrNull(this.rangesByLowerBound.floorEntry(cut));
/* 762 */             if ((candidate != null) && (candidate.upperBound.compareTo(this.restriction.lowerBound) > 0)) {
/* 763 */               return candidate.intersection(this.restriction);
/*     */             }
/*     */           } else {
/* 766 */             Range<C> result = (Range)this.rangesByLowerBound.get(cut);
/* 767 */             if (result != null) {
/* 768 */               return result.intersection(this.restriction);
/*     */             }
/*     */           }
/*     */         } catch (ClassCastException e) {
/* 772 */           return null;
/*     */         }
/*     */       }
/* 775 */       return null;
/*     */     }
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator()
/*     */     {
/* 780 */       if (this.restriction.isEmpty()) {
/* 781 */         return Iterators.emptyIterator();
/*     */       }
/*     */       
/* 784 */       if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound))
/* 785 */         return Iterators.emptyIterator();
/* 786 */       Iterator<Range<C>> completeRangeItr; final Iterator<Range<C>> completeRangeItr; if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound))
/*     */       {
/*     */ 
/* 789 */         completeRangeItr = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 798 */         completeRangeItr = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values().iterator();
/*     */       }
/*     */       
/*     */ 
/* 802 */       final Cut<Cut<C>> upperBoundOnLowerBounds = (Cut)Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/* 803 */       new AbstractIterator()
/*     */       {
/*     */         protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 806 */           if (!completeRangeItr.hasNext()) {
/* 807 */             return (Map.Entry)endOfData();
/*     */           }
/* 809 */           Range<C> nextRange = (Range)completeRangeItr.next();
/* 810 */           if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound)) {
/* 811 */             return (Map.Entry)endOfData();
/*     */           }
/* 813 */           nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 814 */           return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator()
/*     */     {
/* 822 */       if (this.restriction.isEmpty()) {
/* 823 */         return Iterators.emptyIterator();
/*     */       }
/*     */       
/*     */ 
/* 827 */       Cut<Cut<C>> upperBoundOnLowerBounds = (Cut)Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 835 */       final Iterator<Range<C>> completeRangeItr = this.rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED).descendingMap().values().iterator();
/* 836 */       new AbstractIterator()
/*     */       {
/*     */         protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 839 */           if (!completeRangeItr.hasNext()) {
/* 840 */             return (Map.Entry)endOfData();
/*     */           }
/* 842 */           Range<C> nextRange = (Range)completeRangeItr.next();
/* 843 */           if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo(nextRange.upperBound) >= 0) {
/* 844 */             return (Map.Entry)endOfData();
/*     */           }
/* 846 */           nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 847 */           if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(nextRange.lowerBound)) {
/* 848 */             return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */           }
/* 850 */           return (Map.Entry)endOfData();
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */     public int size()
/*     */     {
/* 858 */       return Iterators.size(entryIterator());
/*     */     }
/*     */   }
/*     */   
/*     */   public RangeSet<C> subRangeSet(Range<C> view)
/*     */   {
/* 864 */     return view.equals(Range.all()) ? this : new SubRangeSet(view);
/*     */   }
/*     */   
/*     */   private final class SubRangeSet extends TreeRangeSet<C> {
/*     */     private final Range<C> restriction;
/*     */     
/*     */     SubRangeSet() {
/* 871 */       super(
/*     */       
/* 873 */         null);
/* 874 */       this.restriction = restriction;
/*     */     }
/*     */     
/*     */     public boolean encloses(Range<C> range)
/*     */     {
/* 879 */       if ((!this.restriction.isEmpty()) && (this.restriction.encloses(range))) {
/* 880 */         Range<C> enclosing = TreeRangeSet.this.rangeEnclosing(range);
/* 881 */         return (enclosing != null) && (!enclosing.intersection(this.restriction).isEmpty());
/*     */       }
/* 883 */       return false;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Range<C> rangeContaining(C value)
/*     */     {
/* 889 */       if (!this.restriction.contains(value)) {
/* 890 */         return null;
/*     */       }
/* 892 */       Range<C> result = TreeRangeSet.this.rangeContaining(value);
/* 893 */       return result == null ? null : result.intersection(this.restriction);
/*     */     }
/*     */     
/*     */     public void add(Range<C> rangeToAdd)
/*     */     {
/* 898 */       Preconditions.checkArgument(
/* 899 */         this.restriction.encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", rangeToAdd, this.restriction);
/*     */       
/*     */ 
/*     */ 
/* 903 */       super.add(rangeToAdd);
/*     */     }
/*     */     
/*     */     public void remove(Range<C> rangeToRemove)
/*     */     {
/* 908 */       if (rangeToRemove.isConnected(this.restriction)) {
/* 909 */         TreeRangeSet.this.remove(rangeToRemove.intersection(this.restriction));
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean contains(C value)
/*     */     {
/* 915 */       return (this.restriction.contains(value)) && (TreeRangeSet.this.contains(value));
/*     */     }
/*     */     
/*     */     public void clear()
/*     */     {
/* 920 */       TreeRangeSet.this.remove(this.restriction);
/*     */     }
/*     */     
/*     */     public RangeSet<C> subRangeSet(Range<C> view)
/*     */     {
/* 925 */       if (view.encloses(this.restriction))
/* 926 */         return this;
/* 927 */       if (view.isConnected(this.restriction)) {
/* 928 */         return new SubRangeSet(this, this.restriction.intersection(view));
/*     */       }
/* 930 */       return ImmutableRangeSet.of();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\TreeRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */