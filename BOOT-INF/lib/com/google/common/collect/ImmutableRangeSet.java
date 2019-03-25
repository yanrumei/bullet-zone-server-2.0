/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ImmutableRangeSet<C extends Comparable>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*  50 */   private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(
/*  51 */     ImmutableList.of());
/*     */   
/*  53 */   private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(
/*  54 */     ImmutableList.of(Range.all()));
/*     */   private final transient ImmutableList<Range<C>> ranges;
/*     */   @LazyInit
/*     */   private transient ImmutableRangeSet<C> complement;
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of()
/*     */   {
/*  61 */     return EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static <C extends Comparable> ImmutableRangeSet<C> all()
/*     */   {
/*  69 */     return ALL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range)
/*     */   {
/*  77 */     Preconditions.checkNotNull(range);
/*  78 */     if (range.isEmpty())
/*  79 */       return of();
/*  80 */     if (range.equals(Range.all())) {
/*  81 */       return all();
/*     */     }
/*  83 */     return new ImmutableRangeSet(ImmutableList.of(range));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet)
/*     */   {
/*  91 */     Preconditions.checkNotNull(rangeSet);
/*  92 */     if (rangeSet.isEmpty())
/*  93 */       return of();
/*  94 */     if (rangeSet.encloses(Range.all())) {
/*  95 */       return all();
/*     */     }
/*     */     
/*  98 */     if ((rangeSet instanceof ImmutableRangeSet)) {
/*  99 */       ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet)rangeSet;
/* 100 */       if (!immutableRangeSet.isPartialView()) {
/* 101 */         return immutableRangeSet;
/*     */       }
/*     */     }
/* 104 */     return new ImmutableRangeSet(ImmutableList.copyOf(rangeSet.asRanges()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(Iterable<Range<C>> ranges)
/*     */   {
/* 116 */     return copyOf(TreeRangeSet.create(ranges));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(Iterable<Range<C>> ranges)
/*     */   {
/* 128 */     return new Builder().addAll(ranges).build();
/*     */   }
/*     */   
/*     */   ImmutableRangeSet(ImmutableList<Range<C>> ranges) {
/* 132 */     this.ranges = ranges;
/*     */   }
/*     */   
/*     */   private ImmutableRangeSet(ImmutableList<Range<C>> ranges, ImmutableRangeSet<C> complement) {
/* 136 */     this.ranges = ranges;
/* 137 */     this.complement = complement;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean intersects(Range<C> otherRange)
/*     */   {
/* 145 */     int ceilingIndex = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 147 */       Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */       
/* 149 */       Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */ 
/* 152 */     if ((ceilingIndex < this.ranges.size()) && 
/* 153 */       (((Range)this.ranges.get(ceilingIndex)).isConnected(otherRange)) && 
/* 154 */       (!((Range)this.ranges.get(ceilingIndex)).intersection(otherRange).isEmpty())) {
/* 155 */       return true;
/*     */     }
/* 157 */     return (ceilingIndex > 0) && 
/* 158 */       (((Range)this.ranges.get(ceilingIndex - 1)).isConnected(otherRange)) && 
/* 159 */       (!((Range)this.ranges.get(ceilingIndex - 1)).intersection(otherRange).isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean encloses(Range<C> otherRange)
/*     */   {
/* 165 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 167 */       Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */       
/* 169 */       Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/*     */ 
/* 172 */     return (index != -1) && (((Range)this.ranges.get(index)).encloses(otherRange));
/*     */   }
/*     */   
/*     */ 
/*     */   public Range<C> rangeContaining(C value)
/*     */   {
/* 178 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 180 */       Range.lowerBoundFn(), 
/* 181 */       Cut.belowValue(value), 
/* 182 */       Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/*     */ 
/* 185 */     if (index != -1) {
/* 186 */       Range<C> range = (Range)this.ranges.get(index);
/* 187 */       return range.contains(value) ? range : null;
/*     */     }
/* 189 */     return null;
/*     */   }
/*     */   
/*     */   public Range<C> span()
/*     */   {
/* 194 */     if (this.ranges.isEmpty()) {
/* 195 */       throw new NoSuchElementException();
/*     */     }
/* 197 */     return Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 202 */     return this.ranges.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void add(Range<C> range)
/*     */   {
/* 214 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void addAll(RangeSet<C> other)
/*     */   {
/* 226 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void addAll(Iterable<Range<C>> other)
/*     */   {
/* 238 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void remove(Range<C> range)
/*     */   {
/* 250 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void removeAll(RangeSet<C> other)
/*     */   {
/* 262 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void removeAll(Iterable<Range<C>> other)
/*     */   {
/* 274 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ImmutableSet<Range<C>> asRanges()
/*     */   {
/* 279 */     if (this.ranges.isEmpty()) {
/* 280 */       return ImmutableSet.of();
/*     */     }
/* 282 */     return new RegularImmutableSortedSet(this.ranges, Range.rangeLexOrdering());
/*     */   }
/*     */   
/*     */   public ImmutableSet<Range<C>> asDescendingSetOfRanges()
/*     */   {
/* 287 */     if (this.ranges.isEmpty()) {
/* 288 */       return ImmutableSet.of();
/*     */     }
/* 290 */     return new RegularImmutableSortedSet(this.ranges
/* 291 */       .reverse(), Range.rangeLexOrdering().reverse());
/*     */   }
/*     */   
/*     */ 
/*     */   private final class ComplementRanges
/*     */     extends ImmutableList<Range<C>>
/*     */   {
/*     */     private final boolean positiveBoundedBelow;
/*     */     
/*     */     private final boolean positiveBoundedAbove;
/*     */     
/*     */     private final int size;
/*     */     
/*     */ 
/*     */     ComplementRanges()
/*     */     {
/* 307 */       this.positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
/* 308 */       this.positiveBoundedAbove = ((Range)Iterables.getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
/*     */       
/* 310 */       int size = ImmutableRangeSet.this.ranges.size() - 1;
/* 311 */       if (this.positiveBoundedBelow) {
/* 312 */         size++;
/*     */       }
/* 314 */       if (this.positiveBoundedAbove) {
/* 315 */         size++;
/*     */       }
/* 317 */       this.size = size;
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 322 */       return this.size;
/*     */     }
/*     */     
/*     */     public Range<C> get(int index)
/*     */     {
/* 327 */       Preconditions.checkElementIndex(index, this.size);
/*     */       Cut<C> lowerBound;
/*     */       Cut<C> lowerBound;
/* 330 */       if (this.positiveBoundedBelow) {
/* 331 */         lowerBound = index == 0 ? Cut.belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound;
/*     */       } else {
/* 333 */         lowerBound = ((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
/*     */       }
/*     */       Cut<C> upperBound;
/*     */       Cut<C> upperBound;
/* 337 */       if ((this.positiveBoundedAbove) && (index == this.size - 1)) {
/* 338 */         upperBound = Cut.aboveAll();
/*     */       } else {
/* 340 */         upperBound = ((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
/*     */       }
/*     */       
/* 343 */       return Range.create(lowerBound, upperBound);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 348 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public ImmutableRangeSet<C> complement()
/*     */   {
/* 354 */     ImmutableRangeSet<C> result = this.complement;
/* 355 */     if (result != null)
/* 356 */       return result;
/* 357 */     if (this.ranges.isEmpty())
/* 358 */       return this.complement = all();
/* 359 */     if ((this.ranges.size() == 1) && (((Range)this.ranges.get(0)).equals(Range.all()))) {
/* 360 */       return this.complement = of();
/*     */     }
/* 362 */     ImmutableList<Range<C>> complementRanges = new ComplementRanges();
/* 363 */     result = this.complement = new ImmutableRangeSet(complementRanges, this);
/*     */     
/* 365 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableRangeSet<C> union(RangeSet<C> other)
/*     */   {
/* 377 */     return unionOf(Iterables.concat(asRanges(), other.asRanges()));
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
/*     */   public ImmutableRangeSet<C> intersection(RangeSet<C> other)
/*     */   {
/* 390 */     RangeSet<C> copy = TreeRangeSet.create(this);
/* 391 */     copy.removeAll(other.complement());
/* 392 */     return copyOf(copy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableRangeSet<C> difference(RangeSet<C> other)
/*     */   {
/* 404 */     RangeSet<C> copy = TreeRangeSet.create(this);
/* 405 */     copy.removeAll(other);
/* 406 */     return copyOf(copy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ImmutableList<Range<C>> intersectRanges(final Range<C> range)
/*     */   {
/* 414 */     if ((this.ranges.isEmpty()) || (range.isEmpty()))
/* 415 */       return ImmutableList.of();
/* 416 */     if (range.encloses(span())) {
/* 417 */       return this.ranges;
/*     */     }
/*     */     int fromIndex;
/*     */     final int fromIndex;
/* 421 */     if (range.hasLowerBound())
/*     */     {
/* 423 */       fromIndex = SortedLists.binarySearch(this.ranges, 
/*     */       
/* 425 */         Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 430 */       fromIndex = 0;
/*     */     }
/*     */     int toIndex;
/*     */     int toIndex;
/* 434 */     if (range.hasUpperBound())
/*     */     {
/* 436 */       toIndex = SortedLists.binarySearch(this.ranges, 
/*     */       
/* 438 */         Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 443 */       toIndex = this.ranges.size();
/*     */     }
/* 445 */     final int length = toIndex - fromIndex;
/* 446 */     if (length == 0) {
/* 447 */       return ImmutableList.of();
/*     */     }
/* 449 */     new ImmutableList()
/*     */     {
/*     */       public int size() {
/* 452 */         return length;
/*     */       }
/*     */       
/*     */       public Range<C> get(int index)
/*     */       {
/* 457 */         Preconditions.checkElementIndex(index, length);
/* 458 */         if ((index == 0) || (index == length - 1)) {
/* 459 */           return ((Range)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range);
/*     */         }
/* 461 */         return (Range)ImmutableRangeSet.this.ranges.get(index + fromIndex);
/*     */       }
/*     */       
/*     */ 
/*     */       boolean isPartialView()
/*     */       {
/* 467 */         return true;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableRangeSet<C> subRangeSet(Range<C> range)
/*     */   {
/* 478 */     if (!isEmpty()) {
/* 479 */       Range<C> span = span();
/* 480 */       if (range.encloses(span))
/* 481 */         return this;
/* 482 */       if (range.isConnected(span)) {
/* 483 */         return new ImmutableRangeSet(intersectRanges(range));
/*     */       }
/*     */     }
/* 486 */     return of();
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
/*     */   public ImmutableSortedSet<C> asSet(DiscreteDomain<C> domain)
/*     */   {
/* 509 */     Preconditions.checkNotNull(domain);
/* 510 */     if (isEmpty()) {
/* 511 */       return ImmutableSortedSet.of();
/*     */     }
/* 513 */     Range<C> span = span().canonical(domain);
/* 514 */     if (!span.hasLowerBound())
/*     */     {
/*     */ 
/* 517 */       throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
/*     */     }
/* 519 */     if (!span.hasUpperBound()) {
/*     */       try {
/* 521 */         domain.maxValue();
/*     */       } catch (NoSuchElementException e) {
/* 523 */         throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 528 */     return new AsSet(domain);
/*     */   }
/*     */   
/*     */   private final class AsSet extends ImmutableSortedSet<C> {
/*     */     private final DiscreteDomain<C> domain;
/*     */     private transient Integer size;
/*     */     
/* 535 */     AsSet() { super();
/* 536 */       this.domain = domain;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int size()
/*     */     {
/* 544 */       Integer result = this.size;
/* 545 */       if (result == null) {
/* 546 */         long total = 0L;
/* 547 */         for (UnmodifiableIterator localUnmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); localUnmodifiableIterator.hasNext();) { Range<C> range = (Range)localUnmodifiableIterator.next();
/* 548 */           total += ContiguousSet.create(range, this.domain).size();
/* 549 */           if (total >= 2147483647L) {
/*     */             break;
/*     */           }
/*     */         }
/* 553 */         result = this.size = Integer.valueOf(Ints.saturatedCast(total));
/*     */       }
/* 555 */       return result.intValue();
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<C> iterator()
/*     */     {
/* 560 */       new AbstractIterator() {
/* 561 */         final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();
/* 562 */         Iterator<C> elemItr = Iterators.emptyIterator();
/*     */         
/*     */         protected C computeNext()
/*     */         {
/* 566 */           while (!this.elemItr.hasNext()) {
/* 567 */             if (this.rangeItr.hasNext()) {
/* 568 */               this.elemItr = ContiguousSet.create((Range)this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).iterator();
/*     */             } else {
/* 570 */               return (Comparable)endOfData();
/*     */             }
/*     */           }
/* 573 */           return (Comparable)this.elemItr.next();
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     @GwtIncompatible("NavigableSet")
/*     */     public UnmodifiableIterator<C> descendingIterator()
/*     */     {
/* 581 */       new AbstractIterator() {
/* 582 */         final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
/* 583 */         Iterator<C> elemItr = Iterators.emptyIterator();
/*     */         
/*     */         protected C computeNext()
/*     */         {
/* 587 */           while (!this.elemItr.hasNext()) {
/* 588 */             if (this.rangeItr.hasNext()) {
/* 589 */               this.elemItr = ContiguousSet.create((Range)this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).descendingIterator();
/*     */             } else {
/* 591 */               return (Comparable)endOfData();
/*     */             }
/*     */           }
/* 594 */           return (Comparable)this.elemItr.next();
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> subSet(Range<C> range) {
/* 600 */       return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive)
/*     */     {
/* 605 */       return subSet(Range.upTo(toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */ 
/*     */     ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
/*     */     {
/* 611 */       if ((!fromInclusive) && (!toInclusive) && (Range.compareOrThrow(fromElement, toElement) == 0)) {
/* 612 */         return ImmutableSortedSet.of();
/*     */       }
/* 614 */       return subSet(
/* 615 */         Range.range(fromElement, 
/* 616 */         BoundType.forBoolean(fromInclusive), toElement, 
/* 617 */         BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive)
/*     */     {
/* 622 */       return subSet(Range.downTo(fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public boolean contains(@Nullable Object o)
/*     */     {
/* 627 */       if (o == null) {
/* 628 */         return false;
/*     */       }
/*     */       try
/*     */       {
/* 632 */         C c = (Comparable)o;
/* 633 */         return ImmutableRangeSet.this.contains(c);
/*     */       } catch (ClassCastException e) {}
/* 635 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     int indexOf(Object target)
/*     */     {
/* 641 */       if (contains(target))
/*     */       {
/* 643 */         C c = (Comparable)target;
/* 644 */         long total = 0L;
/* 645 */         for (UnmodifiableIterator localUnmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); localUnmodifiableIterator.hasNext();) { Range<C> range = (Range)localUnmodifiableIterator.next();
/* 646 */           if (range.contains(c)) {
/* 647 */             return Ints.saturatedCast(total + ContiguousSet.create(range, this.domain).indexOf(c));
/*     */           }
/* 649 */           total += ContiguousSet.create(range, this.domain).size();
/*     */         }
/*     */         
/* 652 */         throw new AssertionError("impossible");
/*     */       }
/* 654 */       return -1;
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> createDescendingSet()
/*     */     {
/* 659 */       return new DescendingImmutableSortedSet(this);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 664 */       return ImmutableRangeSet.this.ranges.isPartialView();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 669 */       return ImmutableRangeSet.this.ranges.toString();
/*     */     }
/*     */     
/*     */     Object writeReplace()
/*     */     {
/* 674 */       return new ImmutableRangeSet.AsSetSerializedForm(ImmutableRangeSet.this.ranges, this.domain);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     AsSetSerializedForm(ImmutableList<Range<C>> ranges, DiscreteDomain<C> domain) {
/* 683 */       this.ranges = ranges;
/* 684 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 688 */       return new ImmutableRangeSet(this.ranges).asSet(this.domain);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isPartialView()
/*     */   {
/* 699 */     return this.ranges.isPartialView();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Builder<C> builder()
/*     */   {
/* 706 */     return new Builder();
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Builder<C extends Comparable<?>>
/*     */   {
/*     */     private final List<Range<C>> ranges;
/*     */     
/*     */     public Builder()
/*     */     {
/* 716 */       this.ranges = Lists.newArrayList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> add(Range<C> range)
/*     */     {
/* 729 */       Preconditions.checkArgument(!range.isEmpty(), "range must not be empty, but was %s", range);
/* 730 */       this.ranges.add(range);
/* 731 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(RangeSet<C> ranges)
/*     */     {
/* 741 */       return addAll(ranges.asRanges());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(Iterable<Range<C>> ranges)
/*     */     {
/* 753 */       for (Range<C> range : ranges) {
/* 754 */         add(range);
/*     */       }
/* 756 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableRangeSet<C> build()
/*     */     {
/* 766 */       ImmutableList.Builder<Range<C>> mergedRangesBuilder = new ImmutableList.Builder(this.ranges.size());
/* 767 */       Collections.sort(this.ranges, Range.rangeLexOrdering());
/* 768 */       PeekingIterator<Range<C>> peekingItr = Iterators.peekingIterator(this.ranges.iterator());
/* 769 */       while (peekingItr.hasNext()) {
/* 770 */         Range<C> range = (Range)peekingItr.next();
/* 771 */         while (peekingItr.hasNext()) {
/* 772 */           Range<C> nextRange = (Range)peekingItr.peek();
/* 773 */           if (!range.isConnected(nextRange)) break;
/* 774 */           Preconditions.checkArgument(
/* 775 */             range.intersection(nextRange).isEmpty(), "Overlapping ranges not permitted but found %s overlapping %s", range, nextRange);
/*     */           
/*     */ 
/*     */ 
/* 779 */           range = range.span((Range)peekingItr.next());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 784 */         mergedRangesBuilder.add(range);
/*     */       }
/* 786 */       ImmutableList<Range<C>> mergedRanges = mergedRangesBuilder.build();
/* 787 */       if (mergedRanges.isEmpty())
/* 788 */         return ImmutableRangeSet.of();
/* 789 */       if ((mergedRanges.size() == 1) && 
/* 790 */         (((Range)Iterables.getOnlyElement(mergedRanges)).equals(Range.all()))) {
/* 791 */         return ImmutableRangeSet.all();
/*     */       }
/* 793 */       return new ImmutableRangeSet(mergedRanges);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable
/*     */   {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     
/*     */     SerializedForm(ImmutableList<Range<C>> ranges) {
/* 802 */       this.ranges = ranges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 806 */       if (this.ranges.isEmpty())
/* 807 */         return ImmutableRangeSet.of();
/* 808 */       if (this.ranges.equals(ImmutableList.of(Range.all()))) {
/* 809 */         return ImmutableRangeSet.all();
/*     */       }
/* 811 */       return new ImmutableRangeSet(this.ranges);
/*     */     }
/*     */   }
/*     */   
/*     */   Object writeReplace()
/*     */   {
/* 817 */     return new SerializedForm(this.ranges);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ImmutableRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */