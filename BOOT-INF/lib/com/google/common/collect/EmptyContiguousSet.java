/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ final class EmptyContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   EmptyContiguousSet(DiscreteDomain<C> domain)
/*     */   {
/*  32 */     super(domain);
/*     */   }
/*     */   
/*     */   public C first()
/*     */   {
/*  37 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public C last()
/*     */   {
/*  42 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  47 */     return 0;
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other)
/*     */   {
/*  52 */     return this;
/*     */   }
/*     */   
/*     */   public Range<C> range()
/*     */   {
/*  57 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType)
/*     */   {
/*  62 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive)
/*     */   {
/*  67 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
/*     */   {
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   ContiguousSet<C> tailSetImpl(C fromElement, boolean fromInclusive)
/*     */   {
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public boolean contains(Object object)
/*     */   {
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   int indexOf(Object target)
/*     */   {
/*  89 */     return -1;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<C> iterator()
/*     */   {
/*  94 */     return Iterators.emptyIterator();
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<C> descendingIterator()
/*     */   {
/* 100 */     return Iterators.emptyIterator();
/*     */   }
/*     */   
/*     */   boolean isPartialView()
/*     */   {
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public ImmutableList<C> asList()
/*     */   {
/* 115 */     return ImmutableList.of();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 120 */     return "[]";
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object)
/*     */   {
/* 125 */     if ((object instanceof Set)) {
/* 126 */       Set<?> that = (Set)object;
/* 127 */       return that.isEmpty();
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   boolean isHashCodeFast()
/*     */   {
/* 135 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 140 */     return 0;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     private final DiscreteDomain<C> domain;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 148 */     private SerializedForm(DiscreteDomain<C> domain) { this.domain = domain; }
/*     */     
/*     */     private Object readResolve()
/*     */     {
/* 152 */       return new EmptyContiguousSet(this.domain);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   Object writeReplace()
/*     */   {
/* 161 */     return new SerializedForm(this.domain, null);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   ImmutableSortedSet<C> createDescendingSet() {
/* 166 */     return ImmutableSortedSet.emptySet(Ordering.natural().reverse());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\EmptyContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */