/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ final class SortedMultisets
/*     */ {
/*     */   static class ElementSet<E>
/*     */     extends Multisets.ElementSet<E>
/*     */     implements SortedSet<E>
/*     */   {
/*     */     @Weak
/*     */     private final SortedMultiset<E> multiset;
/*     */     
/*     */     ElementSet(SortedMultiset<E> multiset)
/*     */     {
/*  50 */       this.multiset = multiset;
/*     */     }
/*     */     
/*     */     final SortedMultiset<E> multiset()
/*     */     {
/*  55 */       return this.multiset;
/*     */     }
/*     */     
/*     */     public Comparator<? super E> comparator()
/*     */     {
/*  60 */       return multiset().comparator();
/*     */     }
/*     */     
/*     */     public SortedSet<E> subSet(E fromElement, E toElement)
/*     */     {
/*  65 */       return multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
/*     */     }
/*     */     
/*     */     public SortedSet<E> headSet(E toElement)
/*     */     {
/*  70 */       return multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
/*     */     }
/*     */     
/*     */     public SortedSet<E> tailSet(E fromElement)
/*     */     {
/*  75 */       return multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
/*     */     }
/*     */     
/*     */     public E first()
/*     */     {
/*  80 */       return (E)SortedMultisets.getElementOrThrow(multiset().firstEntry());
/*     */     }
/*     */     
/*     */     public E last()
/*     */     {
/*  85 */       return (E)SortedMultisets.getElementOrThrow(multiset().lastEntry());
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class NavigableElementSet<E>
/*     */     extends SortedMultisets.ElementSet<E> implements NavigableSet<E>
/*     */   {
/*     */     NavigableElementSet(SortedMultiset<E> multiset)
/*     */     {
/*  95 */       super();
/*     */     }
/*     */     
/*     */     public E lower(E e)
/*     */     {
/* 100 */       return (E)SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.OPEN).lastEntry());
/*     */     }
/*     */     
/*     */     public E floor(E e)
/*     */     {
/* 105 */       return (E)SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.CLOSED).lastEntry());
/*     */     }
/*     */     
/*     */     public E ceiling(E e)
/*     */     {
/* 110 */       return (E)SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.CLOSED).firstEntry());
/*     */     }
/*     */     
/*     */     public E higher(E e)
/*     */     {
/* 115 */       return (E)SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.OPEN).firstEntry());
/*     */     }
/*     */     
/*     */     public NavigableSet<E> descendingSet()
/*     */     {
/* 120 */       return new NavigableElementSet(multiset().descendingMultiset());
/*     */     }
/*     */     
/*     */     public Iterator<E> descendingIterator()
/*     */     {
/* 125 */       return descendingSet().iterator();
/*     */     }
/*     */     
/*     */     public E pollFirst()
/*     */     {
/* 130 */       return (E)SortedMultisets.getElementOrNull(multiset().pollFirstEntry());
/*     */     }
/*     */     
/*     */     public E pollLast()
/*     */     {
/* 135 */       return (E)SortedMultisets.getElementOrNull(multiset().pollLastEntry());
/*     */     }
/*     */     
/*     */ 
/*     */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
/*     */     {
/* 141 */       return new NavigableElementSet(
/* 142 */         multiset()
/* 143 */         .subMultiset(fromElement, 
/* 144 */         BoundType.forBoolean(fromInclusive), toElement, 
/* 145 */         BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */     
/*     */     public NavigableSet<E> headSet(E toElement, boolean inclusive)
/*     */     {
/* 150 */       return new NavigableElementSet(
/* 151 */         multiset().headMultiset(toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
/*     */     {
/* 156 */       return new NavigableElementSet(
/* 157 */         multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */   }
/*     */   
/*     */   private static <E> E getElementOrThrow(Multiset.Entry<E> entry) {
/* 162 */     if (entry == null) {
/* 163 */       throw new NoSuchElementException();
/*     */     }
/* 165 */     return (E)entry.getElement();
/*     */   }
/*     */   
/*     */   private static <E> E getElementOrNull(@Nullable Multiset.Entry<E> entry) {
/* 169 */     return entry == null ? null : entry.getElement();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SortedMultisets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */