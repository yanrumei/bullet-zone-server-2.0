/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Range<C extends Comparable>
/*     */   extends RangeGwtSerializationDependencies
/*     */   implements Predicate<C>, Serializable
/*     */ {
/* 122 */   private static final Function<Range, Cut> LOWER_BOUND_FN = new Function()
/*     */   {
/*     */     public Cut apply(Range range)
/*     */     {
/* 126 */       return range.lowerBound;
/*     */     }
/*     */   };
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn()
/*     */   {
/* 132 */     return LOWER_BOUND_FN;
/*     */   }
/*     */   
/* 135 */   private static final Function<Range, Cut> UPPER_BOUND_FN = new Function()
/*     */   {
/*     */     public Cut apply(Range range)
/*     */     {
/* 139 */       return range.upperBound;
/*     */     }
/*     */   };
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn()
/*     */   {
/* 145 */     return UPPER_BOUND_FN;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Ordering<Range<C>> rangeLexOrdering() {
/* 149 */     return RangeLexOrdering.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
/* 153 */     return new Range(lowerBound, upperBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> open(C lower, C upper)
/*     */   {
/* 165 */     return create(Cut.aboveValue(lower), Cut.belowValue(upper));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> closed(C lower, C upper)
/*     */   {
/* 177 */     return create(Cut.belowValue(lower), Cut.aboveValue(upper));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper)
/*     */   {
/* 189 */     return create(Cut.belowValue(lower), Cut.belowValue(upper));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper)
/*     */   {
/* 201 */     return create(Cut.aboveValue(lower), Cut.aboveValue(upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType)
/*     */   {
/* 215 */     Preconditions.checkNotNull(lowerType);
/* 216 */     Preconditions.checkNotNull(upperType);
/*     */     
/*     */ 
/* 219 */     Cut<C> lowerBound = lowerType == BoundType.OPEN ? Cut.aboveValue(lower) : Cut.belowValue(lower);
/*     */     
/* 221 */     Cut<C> upperBound = upperType == BoundType.OPEN ? Cut.belowValue(upper) : Cut.aboveValue(upper);
/* 222 */     return create(lowerBound, upperBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> lessThan(C endpoint)
/*     */   {
/* 232 */     return create(Cut.belowAll(), Cut.belowValue(endpoint));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> atMost(C endpoint)
/*     */   {
/* 242 */     return create(Cut.belowAll(), Cut.aboveValue(endpoint));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType)
/*     */   {
/* 252 */     switch (boundType) {
/*     */     case OPEN: 
/* 254 */       return lessThan(endpoint);
/*     */     case CLOSED: 
/* 256 */       return atMost(endpoint);
/*     */     }
/* 258 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint)
/*     */   {
/* 269 */     return create(Cut.aboveValue(endpoint), Cut.aboveAll());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> atLeast(C endpoint)
/*     */   {
/* 279 */     return create(Cut.belowValue(endpoint), Cut.aboveAll());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType)
/*     */   {
/* 289 */     switch (boundType) {
/*     */     case OPEN: 
/* 291 */       return greaterThan(endpoint);
/*     */     case CLOSED: 
/* 293 */       return atLeast(endpoint);
/*     */     }
/* 295 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/* 299 */   private static final Range<Comparable> ALL = new Range(
/* 300 */     Cut.belowAll(), Cut.aboveAll());
/*     */   
/*     */   final Cut<C> lowerBound;
/*     */   
/*     */   final Cut<C> upperBound;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> all()
/*     */   {
/* 309 */     return ALL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> singleton(C value)
/*     */   {
/* 320 */     return closed(value, value);
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
/*     */   public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values)
/*     */   {
/* 335 */     Preconditions.checkNotNull(values);
/* 336 */     if ((values instanceof ContiguousSet)) {
/* 337 */       return ((ContiguousSet)values).range();
/*     */     }
/* 339 */     Iterator<C> valueIterator = values.iterator();
/* 340 */     C min = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 341 */     C max = min;
/* 342 */     while (valueIterator.hasNext()) {
/* 343 */       C value = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 344 */       min = (Comparable)Ordering.natural().min(min, value);
/* 345 */       max = (Comparable)Ordering.natural().max(max, value);
/*     */     }
/* 347 */     return closed(min, max);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Range(Cut<C> lowerBound, Cut<C> upperBound)
/*     */   {
/* 354 */     this.lowerBound = ((Cut)Preconditions.checkNotNull(lowerBound));
/* 355 */     this.upperBound = ((Cut)Preconditions.checkNotNull(upperBound));
/* 356 */     if ((lowerBound.compareTo(upperBound) > 0) || 
/* 357 */       (lowerBound == Cut.aboveAll()) || 
/* 358 */       (upperBound == Cut.belowAll())) {
/* 359 */       throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasLowerBound()
/*     */   {
/* 367 */     return this.lowerBound != Cut.belowAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public C lowerEndpoint()
/*     */   {
/* 377 */     return this.lowerBound.endpoint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BoundType lowerBoundType()
/*     */   {
/* 388 */     return this.lowerBound.typeAsLowerBound();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasUpperBound()
/*     */   {
/* 395 */     return this.upperBound != Cut.aboveAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public C upperEndpoint()
/*     */   {
/* 405 */     return this.upperBound.endpoint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BoundType upperBoundType()
/*     */   {
/* 416 */     return this.upperBound.typeAsUpperBound();
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
/*     */   public boolean isEmpty()
/*     */   {
/* 429 */     return this.lowerBound.equals(this.upperBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(C value)
/*     */   {
/* 438 */     Preconditions.checkNotNull(value);
/*     */     
/* 440 */     return (this.lowerBound.isLessThan(value)) && (!this.upperBound.isLessThan(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean apply(C input)
/*     */   {
/* 450 */     return contains(input);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsAll(Iterable<? extends C> values)
/*     */   {
/* 458 */     if (Iterables.isEmpty(values)) {
/* 459 */       return true;
/*     */     }
/*     */     
/*     */     SortedSet<? extends C> set;
/* 463 */     if ((values instanceof SortedSet)) {
/* 464 */       set = cast(values);
/* 465 */       Comparator<?> comparator = set.comparator();
/* 466 */       if ((Ordering.natural().equals(comparator)) || (comparator == null)) {
/* 467 */         return (contains((Comparable)set.first())) && (contains((Comparable)set.last()));
/*     */       }
/*     */     }
/*     */     
/* 471 */     for (C value : values) {
/* 472 */       if (!contains(value)) {
/* 473 */         return false;
/*     */       }
/*     */     }
/* 476 */     return true;
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
/*     */   public boolean encloses(Range<C> other)
/*     */   {
/* 504 */     return (this.lowerBound.compareTo(other.lowerBound) <= 0) && 
/* 505 */       (this.upperBound.compareTo(other.upperBound) >= 0);
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
/*     */   public boolean isConnected(Range<C> other)
/*     */   {
/* 533 */     return (this.lowerBound.compareTo(other.upperBound) <= 0) && 
/* 534 */       (other.lowerBound.compareTo(this.upperBound) <= 0);
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
/*     */   public Range<C> intersection(Range<C> connectedRange)
/*     */   {
/* 554 */     int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
/* 555 */     int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
/* 556 */     if ((lowerCmp >= 0) && (upperCmp <= 0))
/* 557 */       return this;
/* 558 */     if ((lowerCmp <= 0) && (upperCmp >= 0)) {
/* 559 */       return connectedRange;
/*     */     }
/* 561 */     Cut<C> newLower = lowerCmp >= 0 ? this.lowerBound : connectedRange.lowerBound;
/* 562 */     Cut<C> newUpper = upperCmp <= 0 ? this.upperBound : connectedRange.upperBound;
/* 563 */     return create(newLower, newUpper);
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
/*     */   public Range<C> span(Range<C> other)
/*     */   {
/* 579 */     int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
/* 580 */     int upperCmp = this.upperBound.compareTo(other.upperBound);
/* 581 */     if ((lowerCmp <= 0) && (upperCmp >= 0))
/* 582 */       return this;
/* 583 */     if ((lowerCmp >= 0) && (upperCmp <= 0)) {
/* 584 */       return other;
/*     */     }
/* 586 */     Cut<C> newLower = lowerCmp <= 0 ? this.lowerBound : other.lowerBound;
/* 587 */     Cut<C> newUpper = upperCmp >= 0 ? this.upperBound : other.upperBound;
/* 588 */     return create(newLower, newUpper);
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
/*     */   public Range<C> canonical(DiscreteDomain<C> domain)
/*     */   {
/* 617 */     Preconditions.checkNotNull(domain);
/* 618 */     Cut<C> lower = this.lowerBound.canonical(domain);
/* 619 */     Cut<C> upper = this.upperBound.canonical(domain);
/* 620 */     return (lower == this.lowerBound) && (upper == this.upperBound) ? this : create(lower, upper);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(@Nullable Object object)
/*     */   {
/* 632 */     if ((object instanceof Range)) {
/* 633 */       Range<?> other = (Range)object;
/* 634 */       return (this.lowerBound.equals(other.lowerBound)) && (this.upperBound.equals(other.upperBound));
/*     */     }
/* 636 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 642 */     return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 651 */     return toString(this.lowerBound, this.upperBound);
/*     */   }
/*     */   
/*     */   private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
/* 655 */     StringBuilder sb = new StringBuilder(16);
/* 656 */     lowerBound.describeAsLowerBound(sb);
/* 657 */     sb.append("..");
/* 658 */     upperBound.describeAsUpperBound(sb);
/* 659 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static <T> SortedSet<T> cast(Iterable<T> iterable)
/*     */   {
/* 666 */     return (SortedSet)iterable;
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 670 */     if (equals(ALL)) {
/* 671 */       return all();
/*     */     }
/* 673 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   static int compareOrThrow(Comparable left, Comparable right)
/*     */   {
/* 679 */     return left.compareTo(right);
/*     */   }
/*     */   
/*     */   private static class RangeLexOrdering
/*     */     extends Ordering<Range<?>>
/*     */     implements Serializable
/*     */   {
/* 686 */     static final Ordering<Range<?>> INSTANCE = new RangeLexOrdering();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int compare(Range<?> left, Range<?> right) {
/* 690 */       return 
/*     */       
/*     */ 
/* 693 */         ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */