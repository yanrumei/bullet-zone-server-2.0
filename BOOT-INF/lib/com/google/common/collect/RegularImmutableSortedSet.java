/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible(serializable=true, emulated=true)
/*     */ final class RegularImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*  44 */   static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new RegularImmutableSortedSet(
/*  45 */     ImmutableList.of(), Ordering.natural());
/*     */   private final transient ImmutableList<E> elements;
/*     */   
/*     */   RegularImmutableSortedSet(ImmutableList<E> elements, Comparator<? super E> comparator)
/*     */   {
/*  50 */     super(comparator);
/*  51 */     this.elements = elements;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator()
/*     */   {
/*  56 */     return this.elements.iterator();
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<E> descendingIterator()
/*     */   {
/*  62 */     return this.elements.reverse().iterator();
/*     */   }
/*     */   
/*     */   public Spliterator<E> spliterator()
/*     */   {
/*  67 */     return asList().spliterator();
/*     */   }
/*     */   
/*     */   public void forEach(Consumer<? super E> action)
/*     */   {
/*  72 */     this.elements.forEach(action);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  77 */     return this.elements.size();
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Object o)
/*     */   {
/*     */     try {
/*  83 */       return (o != null) && (unsafeBinarySearch(o) >= 0);
/*     */     } catch (ClassCastException e) {}
/*  85 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsAll(Collection<?> targets)
/*     */   {
/*  95 */     if ((targets instanceof Multiset)) {
/*  96 */       targets = ((Multiset)targets).elementSet();
/*     */     }
/*  98 */     if ((!SortedIterables.hasSameComparator(comparator(), targets)) || (targets.size() <= 1)) {
/*  99 */       return super.containsAll(targets);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */     Iterator<E> thisIterator = iterator();
/*     */     
/* 108 */     Iterator<?> thatIterator = targets.iterator();
/*     */     
/*     */ 
/* 111 */     if (!thisIterator.hasNext()) {
/* 112 */       return false;
/*     */     }
/*     */     
/* 115 */     Object target = thatIterator.next();
/* 116 */     E current = thisIterator.next();
/*     */     try {
/*     */       for (;;) {
/* 119 */         int cmp = unsafeCompare(current, target);
/*     */         
/* 121 */         if (cmp < 0) {
/* 122 */           if (!thisIterator.hasNext()) {
/* 123 */             return false;
/*     */           }
/* 125 */           current = thisIterator.next();
/* 126 */         } else if (cmp == 0) {
/* 127 */           if (!thatIterator.hasNext()) {
/* 128 */             return true;
/*     */           }
/* 130 */           target = thatIterator.next();
/*     */         }
/* 132 */         else if (cmp > 0) {
/* 133 */           return false;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 139 */       return false;
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 137 */       return false;
/*     */     }
/*     */     catch (ClassCastException e) {}
/*     */   }
/*     */   
/*     */   private int unsafeBinarySearch(Object key) throws ClassCastException
/*     */   {
/* 144 */     return Collections.binarySearch(this.elements, key, unsafeComparator());
/*     */   }
/*     */   
/*     */   boolean isPartialView()
/*     */   {
/* 149 */     return this.elements.isPartialView();
/*     */   }
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset)
/*     */   {
/* 154 */     return this.elements.copyIntoArray(dst, offset);
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object)
/*     */   {
/* 159 */     if (object == this) {
/* 160 */       return true;
/*     */     }
/* 162 */     if (!(object instanceof Set)) {
/* 163 */       return false;
/*     */     }
/*     */     
/* 166 */     Set<?> that = (Set)object;
/* 167 */     if (size() != that.size())
/* 168 */       return false;
/* 169 */     if (isEmpty()) {
/* 170 */       return true;
/*     */     }
/*     */     
/* 173 */     if (SortedIterables.hasSameComparator(this.comparator, that)) {
/* 174 */       Iterator<?> otherIterator = that.iterator();
/*     */       try {
/* 176 */         Iterator<E> iterator = iterator();
/* 177 */         while (iterator.hasNext()) {
/* 178 */           Object element = iterator.next();
/* 179 */           Object otherElement = otherIterator.next();
/* 180 */           if ((otherElement == null) || (unsafeCompare(element, otherElement) != 0)) {
/* 181 */             return false;
/*     */           }
/*     */         }
/* 184 */         return true;
/*     */       } catch (ClassCastException e) {
/* 186 */         return false;
/*     */       } catch (NoSuchElementException e) {
/* 188 */         return false;
/*     */       }
/*     */     }
/* 191 */     return containsAll(that);
/*     */   }
/*     */   
/*     */   public E first()
/*     */   {
/* 196 */     if (isEmpty()) {
/* 197 */       throw new NoSuchElementException();
/*     */     }
/* 199 */     return (E)this.elements.get(0);
/*     */   }
/*     */   
/*     */   public E last()
/*     */   {
/* 204 */     if (isEmpty()) {
/* 205 */       throw new NoSuchElementException();
/*     */     }
/* 207 */     return (E)this.elements.get(size() - 1);
/*     */   }
/*     */   
/*     */   public E lower(E element)
/*     */   {
/* 212 */     int index = headIndex(element, false) - 1;
/* 213 */     return index == -1 ? null : this.elements.get(index);
/*     */   }
/*     */   
/*     */   public E floor(E element)
/*     */   {
/* 218 */     int index = headIndex(element, true) - 1;
/* 219 */     return index == -1 ? null : this.elements.get(index);
/*     */   }
/*     */   
/*     */   public E ceiling(E element)
/*     */   {
/* 224 */     int index = tailIndex(element, true);
/* 225 */     return index == size() ? null : this.elements.get(index);
/*     */   }
/*     */   
/*     */   public E higher(E element)
/*     */   {
/* 230 */     int index = tailIndex(element, false);
/* 231 */     return index == size() ? null : this.elements.get(index);
/*     */   }
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive)
/*     */   {
/* 236 */     return getSubSet(0, headIndex(toElement, inclusive));
/*     */   }
/*     */   
/*     */   int headIndex(E toElement, boolean inclusive) {
/* 240 */     int index = Collections.binarySearch(this.elements, Preconditions.checkNotNull(toElement), comparator());
/* 241 */     if (index >= 0) {
/* 242 */       return inclusive ? index + 1 : index;
/*     */     }
/* 244 */     return index ^ 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
/*     */   {
/* 251 */     return tailSetImpl(fromElement, fromInclusive).headSetImpl(toElement, toInclusive);
/*     */   }
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive)
/*     */   {
/* 256 */     return getSubSet(tailIndex(fromElement, inclusive), size());
/*     */   }
/*     */   
/*     */   int tailIndex(E fromElement, boolean inclusive) {
/* 260 */     int index = Collections.binarySearch(this.elements, Preconditions.checkNotNull(fromElement), comparator());
/* 261 */     if (index >= 0) {
/* 262 */       return inclusive ? index : index + 1;
/*     */     }
/* 264 */     return index ^ 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Comparator<Object> unsafeComparator()
/*     */   {
/* 273 */     return this.comparator;
/*     */   }
/*     */   
/*     */   RegularImmutableSortedSet<E> getSubSet(int newFromIndex, int newToIndex) {
/* 277 */     if ((newFromIndex == 0) && (newToIndex == size()))
/* 278 */       return this;
/* 279 */     if (newFromIndex < newToIndex) {
/* 280 */       return new RegularImmutableSortedSet(this.elements
/* 281 */         .subList(newFromIndex, newToIndex), this.comparator);
/*     */     }
/* 283 */     return emptySet(this.comparator);
/*     */   }
/*     */   
/*     */ 
/*     */   int indexOf(@Nullable Object target)
/*     */   {
/* 289 */     if (target == null) {
/* 290 */       return -1;
/*     */     }
/*     */     try
/*     */     {
/* 294 */       position = Collections.binarySearch(this.elements, target, unsafeComparator());
/*     */     } catch (ClassCastException e) { int position;
/* 296 */       return -1; }
/*     */     int position;
/* 298 */     return position >= 0 ? position : -1;
/*     */   }
/*     */   
/*     */   ImmutableList<E> createAsList()
/*     */   {
/* 303 */     return size() <= 1 ? this.elements : new ImmutableSortedAsList(this, this.elements);
/*     */   }
/*     */   
/*     */   ImmutableSortedSet<E> createDescendingSet()
/*     */   {
/* 308 */     Comparator<? super E> reversedOrder = Collections.reverseOrder(this.comparator);
/* 309 */     return isEmpty() ? 
/* 310 */       emptySet(reversedOrder) : new RegularImmutableSortedSet(this.elements
/* 311 */       .reverse(), reversedOrder);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\RegularImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */