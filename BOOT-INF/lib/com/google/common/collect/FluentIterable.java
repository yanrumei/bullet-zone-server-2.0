/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.stream.Stream;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public abstract class FluentIterable<E>
/*     */   implements Iterable<E>
/*     */ {
/*     */   private final Optional<Iterable<E>> iterableDelegate;
/*     */   
/*     */   protected FluentIterable()
/*     */   {
/* 118 */     this.iterableDelegate = Optional.absent();
/*     */   }
/*     */   
/*     */   FluentIterable(Iterable<E> iterable) {
/* 122 */     Preconditions.checkNotNull(iterable);
/* 123 */     this.iterableDelegate = Optional.fromNullable(this != iterable ? iterable : null);
/*     */   }
/*     */   
/*     */   private Iterable<E> getDelegate() {
/* 127 */     return (Iterable)this.iterableDelegate.or(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> FluentIterable<E> from(final Iterable<E> iterable)
/*     */   {
/* 138 */     (iterable instanceof FluentIterable) ? (FluentIterable)iterable : new FluentIterable(iterable)
/*     */     {
/*     */ 
/*     */       public Iterator<E> iterator()
/*     */       {
/* 143 */         return iterable.iterator();
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
/*     */   @Beta
/*     */   public static <E> FluentIterable<E> from(E[] elements)
/*     */   {
/* 160 */     return from(Arrays.asList(elements));
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
/*     */   public static <E> FluentIterable<E> from(FluentIterable<E> iterable)
/*     */   {
/* 173 */     return (FluentIterable)Preconditions.checkNotNull(iterable);
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b)
/*     */   {
/* 190 */     return concat(ImmutableList.of(a, b));
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c)
/*     */   {
/* 209 */     return concat(ImmutableList.of(a, b, c));
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d)
/*     */   {
/* 232 */     return concat(ImmutableList.of(a, b, c, d));
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T>... inputs)
/*     */   {
/* 252 */     return concat(ImmutableList.copyOf(inputs));
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends Iterable<? extends T>> inputs)
/*     */   {
/* 272 */     Preconditions.checkNotNull(inputs);
/* 273 */     new FluentIterable()
/*     */     {
/*     */       public Iterator<T> iterator() {
/* 276 */         return Iterators.concat(Iterables.transform(this.val$inputs, Iterables.toIterator()).iterator());
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
/*     */   @Beta
/*     */   public static <E> FluentIterable<E> of()
/*     */   {
/* 290 */     return from(ImmutableList.of());
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static <E> FluentIterable<E> of(E[] elements)
/*     */   {
/* 308 */     return from(Lists.newArrayList(elements));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static <E> FluentIterable<E> of(@Nullable E element, E... elements)
/*     */   {
/* 321 */     return from(Lists.asList(element, elements));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 333 */     return Iterables.toString(getDelegate());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int size()
/*     */   {
/* 342 */     return Iterables.size(getDelegate());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean contains(@Nullable Object target)
/*     */   {
/* 352 */     return Iterables.contains(getDelegate(), target);
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
/*     */   public final FluentIterable<E> cycle()
/*     */   {
/* 373 */     return from(Iterables.cycle(getDelegate()));
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
/*     */   @Beta
/*     */   public final FluentIterable<E> append(Iterable<? extends E> other)
/*     */   {
/* 389 */     return from(concat(getDelegate(), other));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public final FluentIterable<E> append(E... elements)
/*     */   {
/* 402 */     return from(concat(getDelegate(), Arrays.asList(elements)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final FluentIterable<E> filter(Predicate<? super E> predicate)
/*     */   {
/* 412 */     return from(Iterables.filter(getDelegate(), predicate));
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
/*     */   @GwtIncompatible
/*     */   public final <T> FluentIterable<T> filter(Class<T> type)
/*     */   {
/* 430 */     return from(Iterables.filter(getDelegate(), type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean anyMatch(Predicate<? super E> predicate)
/*     */   {
/* 439 */     return Iterables.any(getDelegate(), predicate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean allMatch(Predicate<? super E> predicate)
/*     */   {
/* 449 */     return Iterables.all(getDelegate(), predicate);
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
/*     */   public final Optional<E> firstMatch(Predicate<? super E> predicate)
/*     */   {
/* 462 */     return Iterables.tryFind(getDelegate(), predicate);
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
/*     */   public final <T> FluentIterable<T> transform(Function<? super E, T> function)
/*     */   {
/* 476 */     return from(Iterables.transform(getDelegate(), function));
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
/*     */   public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function)
/*     */   {
/* 494 */     return from(concat(transform(function)));
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
/*     */   public final Optional<E> first()
/*     */   {
/* 508 */     Iterator<E> iterator = getDelegate().iterator();
/* 509 */     return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.absent();
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
/*     */   public final Optional<E> last()
/*     */   {
/* 527 */     Iterable<E> iterable = getDelegate();
/* 528 */     if ((iterable instanceof List)) {
/* 529 */       List<E> list = (List)iterable;
/* 530 */       if (list.isEmpty()) {
/* 531 */         return Optional.absent();
/*     */       }
/* 533 */       return Optional.of(list.get(list.size() - 1));
/*     */     }
/* 535 */     Iterator<E> iterator = iterable.iterator();
/* 536 */     if (!iterator.hasNext()) {
/* 537 */       return Optional.absent();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 544 */     if ((iterable instanceof SortedSet)) {
/* 545 */       SortedSet<E> sortedSet = (SortedSet)iterable;
/* 546 */       return Optional.of(sortedSet.last());
/*     */     }
/*     */     for (;;)
/*     */     {
/* 550 */       E current = iterator.next();
/* 551 */       if (!iterator.hasNext()) {
/* 552 */         return Optional.of(current);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final FluentIterable<E> skip(int numberToSkip)
/*     */   {
/* 575 */     return from(Iterables.skip(getDelegate(), numberToSkip));
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
/*     */   public final FluentIterable<E> limit(int maxSize)
/*     */   {
/* 590 */     return from(Iterables.limit(getDelegate(), maxSize));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isEmpty()
/*     */   {
/* 599 */     return !getDelegate().iterator().hasNext();
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
/*     */   public final ImmutableList<E> toList()
/*     */   {
/* 613 */     return ImmutableList.copyOf(getDelegate());
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
/*     */   public final ImmutableList<E> toSortedList(Comparator<? super E> comparator)
/*     */   {
/* 629 */     return Ordering.from(comparator).immutableSortedCopy(getDelegate());
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
/*     */   public final ImmutableSet<E> toSet()
/*     */   {
/* 643 */     return ImmutableSet.copyOf(getDelegate());
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
/*     */   public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator)
/*     */   {
/* 660 */     return ImmutableSortedSet.copyOf(comparator, getDelegate());
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
/*     */   public final ImmutableMultiset<E> toMultiset()
/*     */   {
/* 674 */     return ImmutableMultiset.copyOf(getDelegate());
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
/*     */   public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> valueFunction)
/*     */   {
/* 694 */     return Maps.toMap(getDelegate(), valueFunction);
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
/*     */   public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> keyFunction)
/*     */   {
/* 716 */     return Multimaps.index(getDelegate(), keyFunction);
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
/*     */   public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> keyFunction)
/*     */   {
/* 750 */     return Maps.uniqueIndex(getDelegate(), keyFunction);
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
/*     */   @GwtIncompatible
/*     */   public final E[] toArray(Class<E> type)
/*     */   {
/* 767 */     return Iterables.toArray(getDelegate(), type);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final <C extends Collection<? super E>> C copyInto(C collection)
/*     */   {
/* 783 */     Preconditions.checkNotNull(collection);
/* 784 */     Iterable<E> iterable = getDelegate();
/* 785 */     if ((iterable instanceof Collection)) {
/* 786 */       collection.addAll(Collections2.cast(iterable));
/*     */     } else {
/* 788 */       for (E item : iterable) {
/* 789 */         collection.add(item);
/*     */       }
/*     */     }
/* 792 */     return collection;
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
/*     */   @Beta
/*     */   public final String join(Joiner joiner)
/*     */   {
/* 807 */     return joiner.join(this);
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
/*     */   public final E get(int position)
/*     */   {
/* 824 */     return (E)Iterables.get(getDelegate(), position);
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
/*     */   public final Stream<E> stream()
/*     */   {
/* 838 */     return Streams.stream(getDelegate());
/*     */   }
/*     */   
/*     */ 
/*     */   private static class FromIterableFunction<E>
/*     */     implements Function<Iterable<E>, FluentIterable<E>>
/*     */   {
/*     */     public FluentIterable<E> apply(Iterable<E> fromObject)
/*     */     {
/* 847 */       return FluentIterable.from(fromObject);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\FluentIterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */