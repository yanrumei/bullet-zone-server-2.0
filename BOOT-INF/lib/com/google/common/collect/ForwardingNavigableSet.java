/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingNavigableSet<E>
/*     */   extends ForwardingSortedSet<E>
/*     */   implements NavigableSet<E>
/*     */ {
/*     */   protected abstract NavigableSet<E> delegate();
/*     */   
/*     */   public E lower(E e)
/*     */   {
/*  63 */     return (E)delegate().lower(e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardLower(E e)
/*     */   {
/*  72 */     return (E)Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */   
/*     */   public E floor(E e)
/*     */   {
/*  77 */     return (E)delegate().floor(e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardFloor(E e)
/*     */   {
/*  86 */     return (E)Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */   
/*     */   public E ceiling(E e)
/*     */   {
/*  91 */     return (E)delegate().ceiling(e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardCeiling(E e)
/*     */   {
/* 100 */     return (E)Iterators.getNext(tailSet(e, true).iterator(), null);
/*     */   }
/*     */   
/*     */   public E higher(E e)
/*     */   {
/* 105 */     return (E)delegate().higher(e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardHigher(E e)
/*     */   {
/* 114 */     return (E)Iterators.getNext(tailSet(e, false).iterator(), null);
/*     */   }
/*     */   
/*     */   public E pollFirst()
/*     */   {
/* 119 */     return (E)delegate().pollFirst();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardPollFirst()
/*     */   {
/* 128 */     return (E)Iterators.pollNext(iterator());
/*     */   }
/*     */   
/*     */   public E pollLast()
/*     */   {
/* 133 */     return (E)delegate().pollLast();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardPollLast()
/*     */   {
/* 142 */     return (E)Iterators.pollNext(descendingIterator());
/*     */   }
/*     */   
/*     */   protected E standardFirst() {
/* 146 */     return (E)iterator().next();
/*     */   }
/*     */   
/*     */   protected E standardLast() {
/* 150 */     return (E)descendingIterator().next();
/*     */   }
/*     */   
/*     */   public NavigableSet<E> descendingSet()
/*     */   {
/* 155 */     return delegate().descendingSet();
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
/*     */   protected class StandardDescendingSet
/*     */     extends Sets.DescendingSet<E>
/*     */   {
/*     */     public StandardDescendingSet()
/*     */     {
/* 171 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator<E> descendingIterator()
/*     */   {
/* 177 */     return delegate().descendingIterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
/*     */   {
/* 183 */     return delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   protected NavigableSet<E> standardSubSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
/*     */   {
/* 194 */     return tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SortedSet<E> standardSubSet(E fromElement, E toElement)
/*     */   {
/* 205 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */   
/*     */   public NavigableSet<E> headSet(E toElement, boolean inclusive)
/*     */   {
/* 210 */     return delegate().headSet(toElement, inclusive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SortedSet<E> standardHeadSet(E toElement)
/*     */   {
/* 220 */     return headSet(toElement, false);
/*     */   }
/*     */   
/*     */   public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
/*     */   {
/* 225 */     return delegate().tailSet(fromElement, inclusive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SortedSet<E> standardTailSet(E fromElement)
/*     */   {
/* 235 */     return tailSet(fromElement, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingNavigableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */