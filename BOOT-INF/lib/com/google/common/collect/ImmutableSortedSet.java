/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators.AbstractSpliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collector;
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
/*     */ @GwtCompatible(serializable=true, emulated=true)
/*     */ public abstract class ImmutableSortedSet<E>
/*     */   extends ImmutableSortedSetFauxverideShim<E>
/*     */   implements NavigableSet<E>, SortedIterable<E>
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1301;
/*     */   final transient Comparator<? super E> comparator;
/*     */   @LazyInit
/*     */   @GwtIncompatible
/*     */   transient ImmutableSortedSet<E> descendingSet;
/*     */   
/*     */   @Beta
/*     */   public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator)
/*     */   {
/*  82 */     return CollectCollectors.toImmutableSortedSet(comparator);
/*     */   }
/*     */   
/*     */   static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
/*  86 */     if (Ordering.natural().equals(comparator)) {
/*  87 */       return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */     }
/*  89 */     return new RegularImmutableSortedSet(ImmutableList.of(), comparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> ImmutableSortedSet<E> of()
/*     */   {
/*  97 */     return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element)
/*     */   {
/* 104 */     return new RegularImmutableSortedSet(ImmutableList.of(element), Ordering.natural());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2)
/*     */   {
/* 116 */     return construct(Ordering.natural(), 2, new Comparable[] { e1, e2 });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3)
/*     */   {
/* 128 */     return construct(Ordering.natural(), 3, new Comparable[] { e1, e2, e3 });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4)
/*     */   {
/* 140 */     return construct(Ordering.natural(), 4, new Comparable[] { e1, e2, e3, e4 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5)
/*     */   {
/* 153 */     return construct(Ordering.natural(), 5, new Comparable[] { e1, e2, e3, e4, e5 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining)
/*     */   {
/* 167 */     Comparable[] contents = new Comparable[6 + remaining.length];
/* 168 */     contents[0] = e1;
/* 169 */     contents[1] = e2;
/* 170 */     contents[2] = e3;
/* 171 */     contents[3] = e4;
/* 172 */     contents[4] = e5;
/* 173 */     contents[5] = e6;
/* 174 */     System.arraycopy(remaining, 0, contents, 6, remaining.length);
/* 175 */     return construct(Ordering.natural(), contents.length, (Comparable[])contents);
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements)
/*     */   {
/* 189 */     return construct(Ordering.natural(), elements.length, (Object[])elements.clone());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements)
/*     */   {
/* 221 */     Ordering<E> naturalOrder = Ordering.natural();
/* 222 */     return copyOf(naturalOrder, elements);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements)
/*     */   {
/* 258 */     Ordering<E> naturalOrder = Ordering.natural();
/* 259 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements)
/*     */   {
/* 277 */     Ordering<E> naturalOrder = Ordering.natural();
/* 278 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements)
/*     */   {
/* 292 */     return new Builder(comparator).addAll(elements).build();
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements)
/*     */   {
/* 310 */     Preconditions.checkNotNull(comparator);
/* 311 */     boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
/*     */     
/* 313 */     if ((hasSameComparator) && ((elements instanceof ImmutableSortedSet)))
/*     */     {
/* 315 */       ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
/* 316 */       if (!original.isPartialView()) {
/* 317 */         return original;
/*     */       }
/*     */     }
/*     */     
/* 321 */     E[] array = (Object[])Iterables.toArray(elements);
/* 322 */     return construct(comparator, array.length, array);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements)
/*     */   {
/* 345 */     return copyOf(comparator, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet)
/*     */   {
/* 366 */     Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
/* 367 */     ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
/* 368 */     if (list.isEmpty()) {
/* 369 */       return emptySet(comparator);
/*     */     }
/* 371 */     return new RegularImmutableSortedSet(list, comparator);
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
/*     */   static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents)
/*     */   {
/* 389 */     if (n == 0) {
/* 390 */       return emptySet(comparator);
/*     */     }
/* 392 */     ObjectArrays.checkElementsNotNull(contents, n);
/* 393 */     Arrays.sort(contents, 0, n, comparator);
/* 394 */     int uniques = 1;
/* 395 */     for (int i = 1; i < n; i++) {
/* 396 */       E cur = contents[i];
/* 397 */       E prev = contents[(uniques - 1)];
/* 398 */       if (comparator.compare(cur, prev) != 0) {
/* 399 */         contents[(uniques++)] = cur;
/*     */       }
/*     */     }
/* 402 */     Arrays.fill(contents, uniques, n, null);
/* 403 */     return new RegularImmutableSortedSet(
/* 404 */       ImmutableList.asImmutableList(contents, uniques), comparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator)
/*     */   {
/* 416 */     return new Builder(comparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<?>> Builder<E> reverseOrder()
/*     */   {
/* 424 */     return new Builder(Collections.reverseOrder());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<?>> Builder<E> naturalOrder()
/*     */   {
/* 435 */     return new Builder(Ordering.natural());
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
/*     */   public static final class Builder<E>
/*     */     extends ImmutableSet.Builder<E>
/*     */   {
/*     */     private final Comparator<? super E> comparator;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder(Comparator<? super E> comparator)
/*     */     {
/* 463 */       this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E element)
/*     */     {
/* 479 */       super.add(element);
/* 480 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements)
/*     */     {
/* 494 */       super.add(elements);
/* 495 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements)
/*     */     {
/* 509 */       super.addAll(elements);
/* 510 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements)
/*     */     {
/* 524 */       super.addAll(elements);
/* 525 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(ImmutableCollection.ArrayBasedBuilder<E> builder)
/*     */     {
/* 531 */       super.combine(builder);
/* 532 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableSortedSet<E> build()
/*     */     {
/* 542 */       E[] contentsArray = (Object[])this.contents;
/* 543 */       ImmutableSortedSet<E> result = ImmutableSortedSet.construct(this.comparator, this.size, contentsArray);
/* 544 */       this.size = result.size();
/* 545 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   int unsafeCompare(Object a, Object b) {
/* 550 */     return unsafeCompare(this.comparator, a, b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static int unsafeCompare(Comparator<?> comparator, Object a, Object b)
/*     */   {
/* 558 */     Comparator<Object> unsafeComparator = comparator;
/* 559 */     return unsafeComparator.compare(a, b);
/*     */   }
/*     */   
/*     */ 
/*     */   ImmutableSortedSet(Comparator<? super E> comparator)
/*     */   {
/* 565 */     this.comparator = comparator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Comparator<? super E> comparator()
/*     */   {
/* 577 */     return this.comparator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableSortedSet<E> headSet(E toElement)
/*     */   {
/* 596 */     return headSet(toElement, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive)
/*     */   {
/* 605 */     return headSetImpl(Preconditions.checkNotNull(toElement), inclusive);
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
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, E toElement)
/*     */   {
/* 623 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
/*     */   {
/* 633 */     Preconditions.checkNotNull(fromElement);
/* 634 */     Preconditions.checkNotNull(toElement);
/* 635 */     Preconditions.checkArgument(this.comparator.compare(fromElement, toElement) <= 0);
/* 636 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
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
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement)
/*     */   {
/* 652 */     return tailSet(fromElement, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive)
/*     */   {
/* 661 */     return tailSetImpl(Preconditions.checkNotNull(fromElement), inclusive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */   abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
/*     */   
/*     */ 
/*     */ 
/*     */   abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public E lower(E e)
/*     */   {
/* 681 */     return (E)Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public E floor(E e)
/*     */   {
/* 690 */     return (E)Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public E ceiling(E e)
/*     */   {
/* 699 */     return (E)Iterables.getFirst(tailSet(e, true), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public E higher(E e)
/*     */   {
/* 708 */     return (E)Iterables.getFirst(tailSet(e, false), null);
/*     */   }
/*     */   
/*     */   public E first()
/*     */   {
/* 713 */     return (E)iterator().next();
/*     */   }
/*     */   
/*     */   public E last()
/*     */   {
/* 718 */     return (E)descendingIterator().next();
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollFirst()
/*     */   {
/* 733 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollLast()
/*     */   {
/* 748 */     throw new UnsupportedOperationException();
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
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> descendingSet()
/*     */   {
/* 762 */     ImmutableSortedSet<E> result = this.descendingSet;
/* 763 */     if (result == null) {
/* 764 */       result = this.descendingSet = createDescendingSet();
/* 765 */       result.descendingSet = this;
/*     */     }
/* 767 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   abstract ImmutableSortedSet<E> createDescendingSet();
/*     */   
/*     */ 
/*     */   public Spliterator<E> spliterator()
/*     */   {
/* 778 */     new Spliterators.AbstractSpliterator(
/* 779 */       size(), 1365)
/*     */       {
/* 780 */         final UnmodifiableIterator<E> iterator = ImmutableSortedSet.this.iterator();
/*     */         
/*     */         public boolean tryAdvance(Consumer<? super E> action)
/*     */         {
/* 784 */           if (this.iterator.hasNext()) {
/* 785 */             action.accept(this.iterator.next());
/* 786 */             return true;
/*     */           }
/* 788 */           return false;
/*     */         }
/*     */         
/*     */ 
/*     */         public Comparator<? super E> getComparator()
/*     */         {
/* 794 */           return ImmutableSortedSet.this.comparator;
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @GwtIncompatible
/*     */     public abstract UnmodifiableIterator<E> descendingIterator();
/*     */     
/*     */ 
/*     */ 
/*     */     abstract int indexOf(@Nullable Object paramObject);
/*     */     
/*     */ 
/*     */ 
/*     */     private static class SerializedForm<E>
/*     */       implements Serializable
/*     */     {
/*     */       final Comparator<? super E> comparator;
/*     */       
/*     */       final Object[] elements;
/*     */       
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/*     */ 
/*     */       public SerializedForm(Comparator<? super E> comparator, Object[] elements)
/*     */       {
/* 822 */         this.comparator = comparator;
/* 823 */         this.elements = elements;
/*     */       }
/*     */       
/*     */       Object readResolve()
/*     */       {
/* 828 */         return new ImmutableSortedSet.Builder(this.comparator).add((Object[])this.elements).build();
/*     */       }
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream stream)
/*     */       throws InvalidObjectException
/*     */     {
/* 835 */       throw new InvalidObjectException("Use SerializedForm");
/*     */     }
/*     */     
/*     */     Object writeReplace()
/*     */     {
/* 840 */       return new SerializedForm(this.comparator, toArray());
/*     */     }
/*     */   }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */