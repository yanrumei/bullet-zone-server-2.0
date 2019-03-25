/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.PriorityQueue;
/*      */ import java.util.Queue;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated=true)
/*      */ public final class Iterators
/*      */ {
/*      */   static <T> UnmodifiableIterator<T> emptyIterator()
/*      */   {
/*   79 */     return emptyListIterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static <T> UnmodifiableListIterator<T> emptyListIterator()
/*      */   {
/*   91 */     return ArrayItr.EMPTY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static enum EmptyModifiableIterator
/*      */     implements Iterator<Object>
/*      */   {
/*   99 */     INSTANCE;
/*      */     
/*      */     private EmptyModifiableIterator() {}
/*      */     
/*  103 */     public boolean hasNext() { return false; }
/*      */     
/*      */ 
/*      */     public Object next()
/*      */     {
/*  108 */       throw new NoSuchElementException();
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/*  113 */       CollectPreconditions.checkRemove(false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static <T> Iterator<T> emptyModifiableIterator()
/*      */   {
/*  126 */     return EmptyModifiableIterator.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(Iterator<? extends T> iterator)
/*      */   {
/*  132 */     Preconditions.checkNotNull(iterator);
/*  133 */     if ((iterator instanceof UnmodifiableIterator))
/*      */     {
/*  135 */       UnmodifiableIterator<T> result = (UnmodifiableIterator)iterator;
/*  136 */       return result;
/*      */     }
/*  138 */     new UnmodifiableIterator()
/*      */     {
/*      */       public boolean hasNext() {
/*  141 */         return this.val$iterator.hasNext();
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  146 */         return (T)this.val$iterator.next();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator)
/*      */   {
/*  159 */     return (UnmodifiableIterator)Preconditions.checkNotNull(iterator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int size(Iterator<?> iterator)
/*      */   {
/*  168 */     long count = 0L;
/*  169 */     while (iterator.hasNext()) {
/*  170 */       iterator.next();
/*  171 */       count += 1L;
/*      */     }
/*  173 */     return Ints.saturatedCast(count);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean contains(Iterator<?> iterator, @Nullable Object element)
/*      */   {
/*  180 */     if (element == null) {
/*  181 */       do { if (!iterator.hasNext()) break;
/*  182 */       } while (iterator.next() != null);
/*  183 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  187 */     while (iterator.hasNext()) {
/*  188 */       if (element.equals(iterator.next())) {
/*  189 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  193 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove)
/*      */   {
/*  207 */     Preconditions.checkNotNull(elementsToRemove);
/*  208 */     boolean result = false;
/*  209 */     while (removeFrom.hasNext()) {
/*  210 */       if (elementsToRemove.contains(removeFrom.next())) {
/*  211 */         removeFrom.remove();
/*  212 */         result = true;
/*      */       }
/*      */     }
/*  215 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate)
/*      */   {
/*  231 */     Preconditions.checkNotNull(predicate);
/*  232 */     boolean modified = false;
/*  233 */     while (removeFrom.hasNext()) {
/*  234 */       if (predicate.apply(removeFrom.next())) {
/*  235 */         removeFrom.remove();
/*  236 */         modified = true;
/*      */       }
/*      */     }
/*  239 */     return modified;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain)
/*      */   {
/*  253 */     Preconditions.checkNotNull(elementsToRetain);
/*  254 */     boolean result = false;
/*  255 */     while (removeFrom.hasNext()) {
/*  256 */       if (!elementsToRetain.contains(removeFrom.next())) {
/*  257 */         removeFrom.remove();
/*  258 */         result = true;
/*      */       }
/*      */     }
/*  261 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2)
/*      */   {
/*  275 */     while (iterator1.hasNext()) {
/*  276 */       if (!iterator2.hasNext()) {
/*  277 */         return false;
/*      */       }
/*  279 */       Object o1 = iterator1.next();
/*  280 */       Object o2 = iterator2.next();
/*  281 */       if (!Objects.equal(o1, o2)) {
/*  282 */         return false;
/*      */       }
/*      */     }
/*  285 */     return !iterator2.hasNext();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toString(Iterator<?> iterator)
/*      */   {
/*  294 */     StringBuilder sb = new StringBuilder().append('[');
/*  295 */     boolean first = true;
/*  296 */     while (iterator.hasNext()) {
/*  297 */       if (!first) {
/*  298 */         sb.append(", ");
/*      */       }
/*  300 */       first = false;
/*  301 */       sb.append(iterator.next());
/*      */     }
/*  303 */     return ']';
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T getOnlyElement(Iterator<T> iterator)
/*      */   {
/*  315 */     T first = iterator.next();
/*  316 */     if (!iterator.hasNext()) {
/*  317 */       return first;
/*      */     }
/*      */     
/*  320 */     StringBuilder sb = new StringBuilder().append("expected one element but was: <").append(first);
/*  321 */     for (int i = 0; (i < 4) && (iterator.hasNext()); i++) {
/*  322 */       sb.append(", ").append(iterator.next());
/*      */     }
/*  324 */     if (iterator.hasNext()) {
/*  325 */       sb.append(", ...");
/*      */     }
/*  327 */     sb.append('>');
/*      */     
/*  329 */     throw new IllegalArgumentException(sb.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Nullable
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T getOnlyElement(Iterator<? extends T> iterator, @Nullable T defaultValue)
/*      */   {
/*  342 */     return (T)(iterator.hasNext() ? getOnlyElement(iterator) : defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type)
/*      */   {
/*  356 */     List<T> list = Lists.newArrayList(iterator);
/*  357 */     return Iterables.toArray(list, type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator)
/*      */   {
/*  370 */     Preconditions.checkNotNull(addTo);
/*  371 */     Preconditions.checkNotNull(iterator);
/*  372 */     boolean wasModified = false;
/*  373 */     while (iterator.hasNext()) {
/*  374 */       wasModified |= addTo.add(iterator.next());
/*      */     }
/*  376 */     return wasModified;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int frequency(Iterator<?> iterator, @Nullable Object element)
/*      */   {
/*  387 */     return size(filter(iterator, Predicates.equalTo(element)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> cycle(Iterable<T> iterable)
/*      */   {
/*  405 */     Preconditions.checkNotNull(iterable);
/*  406 */     new Iterator() {
/*  407 */       Iterator<T> iterator = Iterators.emptyModifiableIterator();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public boolean hasNext()
/*      */       {
/*  420 */         return (this.iterator.hasNext()) || (this.val$iterable.iterator().hasNext());
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  425 */         if (!this.iterator.hasNext()) {
/*  426 */           this.iterator = this.val$iterable.iterator();
/*  427 */           if (!this.iterator.hasNext()) {
/*  428 */             throw new NoSuchElementException();
/*      */           }
/*      */         }
/*  431 */         return (T)this.iterator.next();
/*      */       }
/*      */       
/*      */       public void remove()
/*      */       {
/*  436 */         this.iterator.remove();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   public static <T> Iterator<T> cycle(T... elements)
/*      */   {
/*  456 */     return cycle(Lists.newArrayList(elements));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b)
/*      */   {
/*  468 */     Preconditions.checkNotNull(a);
/*  469 */     Preconditions.checkNotNull(b);
/*  470 */     return concat(new ConsumingQueueIterator(new Iterator[] { a, b }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c)
/*      */   {
/*  484 */     Preconditions.checkNotNull(a);
/*  485 */     Preconditions.checkNotNull(b);
/*  486 */     Preconditions.checkNotNull(c);
/*  487 */     return concat(new ConsumingQueueIterator(new Iterator[] { a, b, c }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d)
/*      */   {
/*  504 */     Preconditions.checkNotNull(a);
/*  505 */     Preconditions.checkNotNull(b);
/*  506 */     Preconditions.checkNotNull(c);
/*  507 */     Preconditions.checkNotNull(d);
/*  508 */     return concat(new ConsumingQueueIterator(new Iterator[] { a, b, c, d }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T>... inputs)
/*      */   {
/*  522 */     for (Iterator<? extends T> input : (Iterator[])Preconditions.checkNotNull(inputs)) {
/*  523 */       Preconditions.checkNotNull(input);
/*      */     }
/*  525 */     return concat(new ConsumingQueueIterator(inputs));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> inputs)
/*      */   {
/*  538 */     return new ConcatenatedIterator(inputs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size)
/*      */   {
/*  557 */     return partitionImpl(iterator, size, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size)
/*      */   {
/*  577 */     return partitionImpl(iterator, size, true);
/*      */   }
/*      */   
/*      */   private static <T> UnmodifiableIterator<List<T>> partitionImpl(Iterator<T> iterator, final int size, final boolean pad)
/*      */   {
/*  582 */     Preconditions.checkNotNull(iterator);
/*  583 */     Preconditions.checkArgument(size > 0);
/*  584 */     new UnmodifiableIterator()
/*      */     {
/*      */       public boolean hasNext() {
/*  587 */         return this.val$iterator.hasNext();
/*      */       }
/*      */       
/*      */       public List<T> next()
/*      */       {
/*  592 */         if (!hasNext()) {
/*  593 */           throw new NoSuchElementException();
/*      */         }
/*  595 */         Object[] array = new Object[size];
/*  596 */         for (int count = 0; 
/*  597 */             (count < size) && (this.val$iterator.hasNext()); count++) {
/*  598 */           array[count] = this.val$iterator.next();
/*      */         }
/*  600 */         for (int i = count; i < size; i++) {
/*  601 */           array[i] = null;
/*      */         }
/*      */         
/*      */ 
/*  605 */         List<T> list = Collections.unmodifiableList(Arrays.asList(array));
/*  606 */         return (pad) || (count == size) ? list : list.subList(0, count);
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> UnmodifiableIterator<T> filter(Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue)
/*      */   {
/*  617 */     Preconditions.checkNotNull(unfiltered);
/*  618 */     Preconditions.checkNotNull(retainIfTrue);
/*  619 */     new AbstractIterator()
/*      */     {
/*      */       protected T computeNext() {
/*  622 */         while (this.val$unfiltered.hasNext()) {
/*  623 */           T element = this.val$unfiltered.next();
/*  624 */           if (retainIfTrue.apply(element)) {
/*  625 */             return element;
/*      */           }
/*      */         }
/*  628 */         return (T)endOfData();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> desiredType)
/*      */   {
/*  640 */     return filter(unfiltered, Predicates.instanceOf(desiredType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  648 */     return indexOf(iterator, predicate) != -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  657 */     Preconditions.checkNotNull(predicate);
/*  658 */     while (iterator.hasNext()) {
/*  659 */       T element = iterator.next();
/*  660 */       if (!predicate.apply(element)) {
/*  661 */         return false;
/*      */       }
/*      */     }
/*  664 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  679 */     return (T)filter(iterator, predicate).next();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Nullable
/*      */   public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @Nullable T defaultValue)
/*      */   {
/*  695 */     return (T)getNext(filter(iterator, predicate), defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  712 */     UnmodifiableIterator<T> filteredIterator = filter(iterator, predicate);
/*  713 */     return filteredIterator.hasNext() ? Optional.of(filteredIterator.next()) : Optional.absent();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  733 */     Preconditions.checkNotNull(predicate, "predicate");
/*  734 */     for (int i = 0; iterator.hasNext(); i++) {
/*  735 */       T current = iterator.next();
/*  736 */       if (predicate.apply(current)) {
/*  737 */         return i;
/*      */       }
/*      */     }
/*  740 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function)
/*      */   {
/*  753 */     Preconditions.checkNotNull(function);
/*  754 */     new TransformedIterator(fromIterator)
/*      */     {
/*      */       T transform(F from) {
/*  757 */         return (T)function.apply(from);
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> T get(Iterator<T> iterator, int position)
/*      */   {
/*  773 */     checkNonnegative(position);
/*  774 */     int skipped = advance(iterator, position);
/*  775 */     if (!iterator.hasNext()) {
/*  776 */       throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  783 */     return (T)iterator.next();
/*      */   }
/*      */   
/*      */   static void checkNonnegative(int position) {
/*  787 */     if (position < 0) {
/*  788 */       throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Nullable
/*      */   public static <T> T get(Iterator<? extends T> iterator, int position, @Nullable T defaultValue)
/*      */   {
/*  809 */     checkNonnegative(position);
/*  810 */     advance(iterator, position);
/*  811 */     return (T)getNext(iterator, defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Nullable
/*      */   public static <T> T getNext(Iterator<? extends T> iterator, @Nullable T defaultValue)
/*      */   {
/*  825 */     return (T)(iterator.hasNext() ? iterator.next() : defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> T getLast(Iterator<T> iterator)
/*      */   {
/*      */     for (;;)
/*      */     {
/*  836 */       T current = iterator.next();
/*  837 */       if (!iterator.hasNext()) {
/*  838 */         return current;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Nullable
/*      */   public static <T> T getLast(Iterator<? extends T> iterator, @Nullable T defaultValue)
/*      */   {
/*  853 */     return (T)(iterator.hasNext() ? getLast(iterator) : defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int advance(Iterator<?> iterator, int numberToAdvance)
/*      */   {
/*  865 */     Preconditions.checkNotNull(iterator);
/*  866 */     Preconditions.checkArgument(numberToAdvance >= 0, "numberToAdvance must be nonnegative");
/*      */     
/*      */ 
/*  869 */     for (int i = 0; (i < numberToAdvance) && (iterator.hasNext()); i++) {
/*  870 */       iterator.next();
/*      */     }
/*  872 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> limit(final Iterator<T> iterator, int limitSize)
/*      */   {
/*  887 */     Preconditions.checkNotNull(iterator);
/*  888 */     Preconditions.checkArgument(limitSize >= 0, "limit is negative");
/*  889 */     new Iterator()
/*      */     {
/*      */       private int count;
/*      */       
/*      */       public boolean hasNext() {
/*  894 */         return (this.count < this.val$limitSize) && (iterator.hasNext());
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  899 */         if (!hasNext()) {
/*  900 */           throw new NoSuchElementException();
/*      */         }
/*  902 */         this.count += 1;
/*  903 */         return (T)iterator.next();
/*      */       }
/*      */       
/*      */       public void remove()
/*      */       {
/*  908 */         iterator.remove();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> consumingIterator(Iterator<T> iterator)
/*      */   {
/*  927 */     Preconditions.checkNotNull(iterator);
/*  928 */     new UnmodifiableIterator()
/*      */     {
/*      */       public boolean hasNext() {
/*  931 */         return this.val$iterator.hasNext();
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  936 */         T next = this.val$iterator.next();
/*  937 */         this.val$iterator.remove();
/*  938 */         return next;
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/*  943 */         return "Iterators.consumingIterator(...)";
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Nullable
/*      */   static <T> T pollNext(Iterator<T> iterator)
/*      */   {
/*  954 */     if (iterator.hasNext()) {
/*  955 */       T result = iterator.next();
/*  956 */       iterator.remove();
/*  957 */       return result;
/*      */     }
/*  959 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void clear(Iterator<?> iterator)
/*      */   {
/*  969 */     Preconditions.checkNotNull(iterator);
/*  970 */     while (iterator.hasNext()) {
/*  971 */       iterator.next();
/*  972 */       iterator.remove();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   public static <T> UnmodifiableIterator<T> forArray(T... array)
/*      */   {
/*  991 */     return forArray(array, 0, array.length, 0);
/*      */   }
/*      */   
/*      */   private static final class ArrayItr<T> extends AbstractIndexedListIterator<T> {
/*  995 */     static final UnmodifiableListIterator<Object> EMPTY = new ArrayItr(new Object[0], 0, 0, 0);
/*      */     
/*      */     private final T[] array;
/*      */     private final int offset;
/*      */     
/*      */     ArrayItr(T[] array, int offset, int length, int index)
/*      */     {
/* 1002 */       super(index);
/* 1003 */       this.array = array;
/* 1004 */       this.offset = offset;
/*      */     }
/*      */     
/*      */     protected T get(int index)
/*      */     {
/* 1009 */       return (T)this.array[(this.offset + index)];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static <T> UnmodifiableListIterator<T> forArray(T[] array, int offset, int length, int index)
/*      */   {
/* 1022 */     Preconditions.checkArgument(length >= 0);
/* 1023 */     int end = offset + length;
/*      */     
/*      */ 
/* 1026 */     Preconditions.checkPositionIndexes(offset, end, array.length);
/* 1027 */     Preconditions.checkPositionIndex(index, length);
/* 1028 */     if (length == 0) {
/* 1029 */       return emptyListIterator();
/*      */     }
/* 1031 */     return new ArrayItr(array, offset, length, index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable T value)
/*      */   {
/* 1041 */     new UnmodifiableIterator()
/*      */     {
/*      */       boolean done;
/*      */       
/*      */       public boolean hasNext() {
/* 1046 */         return !this.done;
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/* 1051 */         if (this.done) {
/* 1052 */           throw new NoSuchElementException();
/*      */         }
/* 1054 */         this.done = true;
/* 1055 */         return (T)this.val$value;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> UnmodifiableIterator<T> forEnumeration(Enumeration<T> enumeration)
/*      */   {
/* 1069 */     Preconditions.checkNotNull(enumeration);
/* 1070 */     new UnmodifiableIterator()
/*      */     {
/*      */       public boolean hasNext() {
/* 1073 */         return this.val$enumeration.hasMoreElements();
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/* 1078 */         return (T)this.val$enumeration.nextElement();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Enumeration<T> asEnumeration(Iterator<T> iterator)
/*      */   {
/* 1091 */     Preconditions.checkNotNull(iterator);
/* 1092 */     new Enumeration()
/*      */     {
/*      */       public boolean hasMoreElements() {
/* 1095 */         return this.val$iterator.hasNext();
/*      */       }
/*      */       
/*      */       public T nextElement()
/*      */       {
/* 1100 */         return (T)this.val$iterator.next();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */   private static class PeekingImpl<E>
/*      */     implements PeekingIterator<E>
/*      */   {
/*      */     private final Iterator<? extends E> iterator;
/*      */     private boolean hasPeeked;
/*      */     private E peekedElement;
/*      */     
/*      */     public PeekingImpl(Iterator<? extends E> iterator)
/*      */     {
/* 1115 */       this.iterator = ((Iterator)Preconditions.checkNotNull(iterator));
/*      */     }
/*      */     
/*      */     public boolean hasNext()
/*      */     {
/* 1120 */       return (this.hasPeeked) || (this.iterator.hasNext());
/*      */     }
/*      */     
/*      */     public E next()
/*      */     {
/* 1125 */       if (!this.hasPeeked) {
/* 1126 */         return (E)this.iterator.next();
/*      */       }
/* 1128 */       E result = this.peekedElement;
/* 1129 */       this.hasPeeked = false;
/* 1130 */       this.peekedElement = null;
/* 1131 */       return result;
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/* 1136 */       Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
/* 1137 */       this.iterator.remove();
/*      */     }
/*      */     
/*      */     public E peek()
/*      */     {
/* 1142 */       if (!this.hasPeeked) {
/* 1143 */         this.peekedElement = this.iterator.next();
/* 1144 */         this.hasPeeked = true;
/*      */       }
/* 1146 */       return (E)this.peekedElement;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator)
/*      */   {
/* 1189 */     if ((iterator instanceof PeekingImpl))
/*      */     {
/*      */ 
/*      */ 
/* 1193 */       PeekingImpl<T> peeking = (PeekingImpl)iterator;
/* 1194 */       return peeking;
/*      */     }
/* 1196 */     return new PeekingImpl(iterator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator)
/*      */   {
/* 1207 */     return (PeekingIterator)Preconditions.checkNotNull(iterator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator)
/*      */   {
/* 1226 */     Preconditions.checkNotNull(iterators, "iterators");
/* 1227 */     Preconditions.checkNotNull(comparator, "comparator");
/*      */     
/* 1229 */     return new MergingIterator(iterators, comparator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class MergingIterator<T>
/*      */     extends UnmodifiableIterator<T>
/*      */   {
/*      */     final Queue<PeekingIterator<T>> queue;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator)
/*      */     {
/* 1249 */       Comparator<PeekingIterator<T>> heapComparator = new Comparator()
/*      */       {
/*      */         public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2)
/*      */         {
/* 1253 */           return itemComparator.compare(o1.peek(), o2.peek());
/*      */         }
/*      */         
/* 1256 */       };
/* 1257 */       this.queue = new PriorityQueue(2, heapComparator);
/*      */       
/* 1259 */       for (Iterator<? extends T> iterator : iterators) {
/* 1260 */         if (iterator.hasNext()) {
/* 1261 */           this.queue.add(Iterators.peekingIterator(iterator));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean hasNext()
/*      */     {
/* 1268 */       return !this.queue.isEmpty();
/*      */     }
/*      */     
/*      */     public T next()
/*      */     {
/* 1273 */       PeekingIterator<T> nextIter = (PeekingIterator)this.queue.remove();
/* 1274 */       T next = nextIter.next();
/* 1275 */       if (nextIter.hasNext()) {
/* 1276 */         this.queue.add(nextIter);
/*      */       }
/* 1278 */       return next;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ConcatenatedIterator<T> extends MultitransformedIterator<Iterator<? extends T>, T>
/*      */   {
/*      */     public ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> iterators)
/*      */     {
/* 1286 */       super();
/*      */     }
/*      */     
/*      */     Iterator<? extends T> transform(Iterator<? extends T> iterator)
/*      */     {
/* 1291 */       return iterator;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private static <T> Iterator<Iterator<? extends T>> getComponentIterators(Iterator<? extends Iterator<? extends T>> iterators)
/*      */     {
/* 1300 */       new MultitransformedIterator(iterators)
/*      */       {
/*      */         Iterator<? extends Iterator<? extends T>> transform(Iterator<? extends T> iterator) {
/* 1303 */           if ((iterator instanceof Iterators.ConcatenatedIterator)) {
/* 1304 */             Iterators.ConcatenatedIterator<? extends T> concatIterator = (Iterators.ConcatenatedIterator)iterator;
/*      */             
/* 1306 */             if (!concatIterator.current.hasNext()) {
/* 1307 */               return Iterators.ConcatenatedIterator.getComponentIterators(concatIterator.backingIterator);
/*      */             }
/*      */           }
/* 1310 */           return Iterators.singletonIterator(iterator);
/*      */         }
/*      */       };
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static <T> ListIterator<T> cast(Iterator<T> iterator)
/*      */   {
/* 1320 */     return (ListIterator)iterator;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\Iterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */