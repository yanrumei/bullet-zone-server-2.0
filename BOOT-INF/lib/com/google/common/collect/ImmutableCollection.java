/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Predicate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ImmutableCollection<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1296;
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   public Spliterator<E> spliterator()
/*     */   {
/* 177 */     return Spliterators.spliterator(this, 1296);
/*     */   }
/*     */   
/* 180 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*     */   
/*     */   public final Object[] toArray()
/*     */   {
/* 184 */     int size = size();
/* 185 */     if (size == 0) {
/* 186 */       return EMPTY_ARRAY;
/*     */     }
/* 188 */     Object[] result = new Object[size];
/* 189 */     copyIntoArray(result, 0);
/* 190 */     return result;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <T> T[] toArray(T[] other)
/*     */   {
/* 196 */     Preconditions.checkNotNull(other);
/* 197 */     int size = size();
/* 198 */     if (other.length < size) {
/* 199 */       other = ObjectArrays.newArray(other, size);
/* 200 */     } else if (other.length > size) {
/* 201 */       other[size] = null;
/*     */     }
/* 203 */     copyIntoArray(other, 0);
/* 204 */     return other;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean contains(@Nullable Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean add(E e)
/*     */   {
/* 220 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean remove(Object object)
/*     */   {
/* 233 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean addAll(Collection<? extends E> newElements)
/*     */   {
/* 246 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean removeAll(Collection<?> oldElements)
/*     */   {
/* 259 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean removeIf(Predicate<? super E> filter)
/*     */   {
/* 272 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean retainAll(Collection<?> elementsToKeep)
/*     */   {
/* 284 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void clear()
/*     */   {
/* 296 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableList<E> asList()
/*     */   {
/* 310 */     switch (size()) {
/*     */     case 0: 
/* 312 */       return ImmutableList.of();
/*     */     case 1: 
/* 314 */       return ImmutableList.of(iterator().next());
/*     */     }
/* 316 */     return new RegularImmutableAsList(this, toArray());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract boolean isPartialView();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   int copyIntoArray(Object[] dst, int offset)
/*     */   {
/* 334 */     for (UnmodifiableIterator localUnmodifiableIterator = iterator(); localUnmodifiableIterator.hasNext();) { E e = localUnmodifiableIterator.next();
/* 335 */       dst[(offset++)] = e;
/*     */     }
/* 337 */     return offset;
/*     */   }
/*     */   
/*     */   Object writeReplace()
/*     */   {
/* 342 */     return new ImmutableList.SerializedForm(toArray());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static abstract class Builder<E>
/*     */   {
/*     */     static final int DEFAULT_INITIAL_CAPACITY = 4;
/*     */     
/*     */ 
/*     */     static int expandedCapacity(int oldCapacity, int minCapacity)
/*     */     {
/* 354 */       if (minCapacity < 0) {
/* 355 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 358 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 359 */       if (newCapacity < minCapacity) {
/* 360 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 362 */       if (newCapacity < 0) {
/* 363 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/*     */       
/* 366 */       return newCapacity;
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
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public abstract Builder<E> add(E paramE);
/*     */     
/*     */ 
/*     */ 
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
/* 398 */       for (E element : elements) {
/* 399 */         add(element);
/*     */       }
/* 401 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements)
/*     */     {
/* 418 */       for (E element : elements) {
/* 419 */         add(element);
/*     */       }
/* 421 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements)
/*     */     {
/* 438 */       while (elements.hasNext()) {
/* 439 */         add(elements.next());
/*     */       }
/* 441 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public abstract ImmutableCollection<E> build();
/*     */   }
/*     */   
/*     */ 
/*     */   static abstract class ArrayBasedBuilder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     Object[] contents;
/*     */     
/*     */     int size;
/*     */     
/*     */ 
/*     */     ArrayBasedBuilder(int initialCapacity)
/*     */     {
/* 459 */       CollectPreconditions.checkNonnegative(initialCapacity, "initialCapacity");
/* 460 */       this.contents = new Object[initialCapacity];
/* 461 */       this.size = 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private void ensureCapacity(int minCapacity)
/*     */     {
/* 469 */       if (this.contents.length < minCapacity)
/*     */       {
/* 471 */         this.contents = Arrays.copyOf(this.contents, 
/* 472 */           expandedCapacity(this.contents.length, minCapacity));
/*     */       }
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ArrayBasedBuilder<E> add(E element)
/*     */     {
/* 479 */       Preconditions.checkNotNull(element);
/* 480 */       ensureCapacity(this.size + 1);
/* 481 */       this.contents[(this.size++)] = element;
/* 482 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ImmutableCollection.Builder<E> add(E... elements)
/*     */     {
/* 488 */       ObjectArrays.checkElementsNotNull(elements);
/* 489 */       ensureCapacity(this.size + elements.length);
/* 490 */       System.arraycopy(elements, 0, this.contents, this.size, elements.length);
/* 491 */       this.size += elements.length;
/* 492 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ImmutableCollection.Builder<E> addAll(Iterable<? extends E> elements)
/*     */     {
/* 498 */       if ((elements instanceof Collection)) {
/* 499 */         Collection<?> collection = (Collection)elements;
/* 500 */         ensureCapacity(this.size + collection.size());
/*     */       }
/* 502 */       super.addAll(elements);
/* 503 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     ArrayBasedBuilder<E> combine(ArrayBasedBuilder<E> builder) {
/* 508 */       Preconditions.checkNotNull(builder);
/* 509 */       ensureCapacity(this.size + builder.size);
/* 510 */       System.arraycopy(builder.contents, 0, this.contents, this.size, builder.size);
/* 511 */       this.size += builder.size;
/* 512 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ImmutableCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */