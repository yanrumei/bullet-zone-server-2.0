/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated=true)
/*     */ public abstract class ContiguousSet<C extends Comparable>
/*     */   extends ImmutableSortedSet<C>
/*     */ {
/*     */   final DiscreteDomain<C> domain;
/*     */   
/*     */   public static <C extends Comparable> ContiguousSet<C> create(Range<C> range, DiscreteDomain<C> domain)
/*     */   {
/*  51 */     Preconditions.checkNotNull(range);
/*  52 */     Preconditions.checkNotNull(domain);
/*  53 */     Range<C> effectiveRange = range;
/*     */     try {
/*  55 */       if (!range.hasLowerBound()) {
/*  56 */         effectiveRange = effectiveRange.intersection(Range.atLeast(domain.minValue()));
/*     */       }
/*  58 */       if (!range.hasUpperBound()) {
/*  59 */         effectiveRange = effectiveRange.intersection(Range.atMost(domain.maxValue()));
/*     */       }
/*     */     } catch (NoSuchElementException e) {
/*  62 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  68 */     boolean empty = (effectiveRange.isEmpty()) || (Range.compareOrThrow(range.lowerBound
/*  69 */       .leastValueAbove(domain), range.upperBound
/*  70 */       .greatestValueBelow(domain)) > 0);
/*     */     
/*     */ 
/*  73 */     return empty ? new EmptyContiguousSet(domain) : new RegularContiguousSet(effectiveRange, domain);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   ContiguousSet(DiscreteDomain<C> domain)
/*     */   {
/*  81 */     super(Ordering.natural());
/*  82 */     this.domain = domain;
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> headSet(C toElement)
/*     */   {
/*  87 */     return headSetImpl((Comparable)Preconditions.checkNotNull(toElement), false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> headSet(C toElement, boolean inclusive)
/*     */   {
/*  96 */     return headSetImpl((Comparable)Preconditions.checkNotNull(toElement), inclusive);
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> subSet(C fromElement, C toElement)
/*     */   {
/* 101 */     Preconditions.checkNotNull(fromElement);
/* 102 */     Preconditions.checkNotNull(toElement);
/* 103 */     Preconditions.checkArgument(comparator().compare(fromElement, toElement) <= 0);
/* 104 */     return subSetImpl(fromElement, true, toElement, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> subSet(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
/*     */   {
/* 114 */     Preconditions.checkNotNull(fromElement);
/* 115 */     Preconditions.checkNotNull(toElement);
/* 116 */     Preconditions.checkArgument(comparator().compare(fromElement, toElement) <= 0);
/* 117 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> tailSet(C fromElement)
/*     */   {
/* 122 */     return tailSetImpl((Comparable)Preconditions.checkNotNull(fromElement), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> tailSet(C fromElement, boolean inclusive)
/*     */   {
/* 131 */     return tailSetImpl((Comparable)Preconditions.checkNotNull(fromElement), inclusive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract ContiguousSet<C> headSetImpl(C paramC, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract ContiguousSet<C> subSetImpl(C paramC1, boolean paramBoolean1, C paramC2, boolean paramBoolean2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract ContiguousSet<C> tailSetImpl(C paramC, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ContiguousSet<C> intersection(ContiguousSet<C> paramContiguousSet);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Range<C> range();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Range<C> range(BoundType paramBoundType1, BoundType paramBoundType2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   ImmutableSortedSet<C> createDescendingSet()
/*     */   {
/* 180 */     return new DescendingImmutableSortedSet(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 186 */     return range().toString();
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
/*     */   public static <E> ImmutableSortedSet.Builder<E> builder()
/*     */   {
/* 199 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */