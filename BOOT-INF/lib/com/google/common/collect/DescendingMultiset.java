/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class DescendingMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   private transient Comparator<? super E> comparator;
/*     */   private transient NavigableSet<E> elementSet;
/*     */   private transient Set<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   abstract SortedMultiset<E> forwardMultiset();
/*     */   
/*     */   public Comparator<? super E> comparator()
/*     */   {
/*  40 */     Comparator<? super E> result = this.comparator;
/*  41 */     if (result == null) {
/*  42 */       return this.comparator = Ordering.from(forwardMultiset().comparator()).reverse();
/*     */     }
/*  44 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NavigableSet<E> elementSet()
/*     */   {
/*  51 */     NavigableSet<E> result = this.elementSet;
/*  52 */     if (result == null) {
/*  53 */       return this.elementSet = new SortedMultisets.NavigableElementSet(this);
/*     */     }
/*  55 */     return result;
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry()
/*     */   {
/*  60 */     return forwardMultiset().pollLastEntry();
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry()
/*     */   {
/*  65 */     return forwardMultiset().pollFirstEntry();
/*     */   }
/*     */   
/*     */   public SortedMultiset<E> headMultiset(E toElement, BoundType boundType)
/*     */   {
/*  70 */     return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset();
/*     */   }
/*     */   
/*     */ 
/*     */   public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType)
/*     */   {
/*  76 */     return 
/*     */     
/*  78 */       forwardMultiset().subMultiset(toElement, toBoundType, fromElement, fromBoundType).descendingMultiset();
/*     */   }
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(E fromElement, BoundType boundType)
/*     */   {
/*  83 */     return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset();
/*     */   }
/*     */   
/*     */   protected Multiset<E> delegate()
/*     */   {
/*  88 */     return forwardMultiset();
/*     */   }
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset()
/*     */   {
/*  93 */     return forwardMultiset();
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> firstEntry()
/*     */   {
/*  98 */     return forwardMultiset().lastEntry();
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> lastEntry()
/*     */   {
/* 103 */     return forwardMultiset().firstEntry();
/*     */   }
/*     */   
/*     */ 
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */   
/*     */ 
/*     */   public Set<Multiset.Entry<E>> entrySet()
/*     */   {
/* 112 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 113 */     return result == null ? (this.entrySet = createEntrySet()) : result;
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
/*     */   Set<Multiset.Entry<E>> createEntrySet()
/*     */   {
/* 134 */     new Multisets.EntrySet()
/*     */     {
/*     */       Multiset<E> multiset()
/*     */       {
/* 121 */         return DescendingMultiset.this;
/*     */       }
/*     */       
/*     */       public Iterator<Multiset.Entry<E>> iterator()
/*     */       {
/* 126 */         return DescendingMultiset.this.entryIterator();
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/* 131 */         return DescendingMultiset.this.forwardMultiset().entrySet().size();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 139 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */   
/*     */   public Object[] toArray()
/*     */   {
/* 144 */     return standardToArray();
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] array)
/*     */   {
/* 149 */     return standardToArray(array);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 154 */     return entrySet().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\DescendingMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */