/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ abstract class AbstractSortedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   @GwtTransient
/*     */   final Comparator<? super E> comparator;
/*     */   private transient SortedMultiset<E> descendingMultiset;
/*     */   
/*     */   AbstractSortedMultiset()
/*     */   {
/*  42 */     this(Ordering.natural());
/*     */   }
/*     */   
/*     */   AbstractSortedMultiset(Comparator<? super E> comparator) {
/*  46 */     this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
/*     */   }
/*     */   
/*     */   public NavigableSet<E> elementSet()
/*     */   {
/*  51 */     return (NavigableSet)super.elementSet();
/*     */   }
/*     */   
/*     */   NavigableSet<E> createElementSet()
/*     */   {
/*  56 */     return new SortedMultisets.NavigableElementSet(this);
/*     */   }
/*     */   
/*     */   public Comparator<? super E> comparator()
/*     */   {
/*  61 */     return this.comparator;
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> firstEntry()
/*     */   {
/*  66 */     Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
/*  67 */     return entryIterator.hasNext() ? (Multiset.Entry)entryIterator.next() : null;
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> lastEntry()
/*     */   {
/*  72 */     Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
/*  73 */     return entryIterator.hasNext() ? (Multiset.Entry)entryIterator.next() : null;
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry()
/*     */   {
/*  78 */     Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
/*  79 */     if (entryIterator.hasNext()) {
/*  80 */       Multiset.Entry<E> result = (Multiset.Entry)entryIterator.next();
/*  81 */       result = Multisets.immutableEntry(result.getElement(), result.getCount());
/*  82 */       entryIterator.remove();
/*  83 */       return result;
/*     */     }
/*  85 */     return null;
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry()
/*     */   {
/*  90 */     Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
/*  91 */     if (entryIterator.hasNext()) {
/*  92 */       Multiset.Entry<E> result = (Multiset.Entry)entryIterator.next();
/*  93 */       result = Multisets.immutableEntry(result.getElement(), result.getCount());
/*  94 */       entryIterator.remove();
/*  95 */       return result;
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMultiset<E> subMultiset(@Nullable E fromElement, BoundType fromBoundType, @Nullable E toElement, BoundType toBoundType)
/*     */   {
/* 107 */     Preconditions.checkNotNull(fromBoundType);
/* 108 */     Preconditions.checkNotNull(toBoundType);
/* 109 */     return tailMultiset(fromElement, fromBoundType).headMultiset(toElement, toBoundType);
/*     */   }
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> descendingEntryIterator();
/*     */   
/*     */   Iterator<E> descendingIterator() {
/* 115 */     return Multisets.iteratorImpl(descendingMultiset());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SortedMultiset<E> descendingMultiset()
/*     */   {
/* 122 */     SortedMultiset<E> result = this.descendingMultiset;
/* 123 */     return result == null ? (this.descendingMultiset = createDescendingMultiset()) : result;
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
/*     */   SortedMultiset<E> createDescendingMultiset()
/*     */   {
/* 144 */     new DescendingMultiset()
/*     */     {
/*     */       SortedMultiset<E> forwardMultiset()
/*     */       {
/* 131 */         return AbstractSortedMultiset.this;
/*     */       }
/*     */       
/*     */       Iterator<Multiset.Entry<E>> entryIterator()
/*     */       {
/* 136 */         return AbstractSortedMultiset.this.descendingEntryIterator();
/*     */       }
/*     */       
/*     */       public Iterator<E> iterator()
/*     */       {
/* 141 */         return AbstractSortedMultiset.this.descendingIterator();
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\AbstractSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */