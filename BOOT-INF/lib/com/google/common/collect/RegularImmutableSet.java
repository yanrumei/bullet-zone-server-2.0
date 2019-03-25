/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ @GwtCompatible(serializable=true, emulated=true)
/*     */ final class RegularImmutableSet<E>
/*     */   extends ImmutableSet<E>
/*     */ {
/*  33 */   static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet(new Object[0], 0, null, 0);
/*     */   
/*     */   private final transient Object[] elements;
/*     */   
/*     */   @VisibleForTesting
/*     */   final transient Object[] table;
/*     */   private final transient int mask;
/*     */   private final transient int hashCode;
/*     */   
/*     */   RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask)
/*     */   {
/*  44 */     this.elements = elements;
/*  45 */     this.table = table;
/*  46 */     this.mask = mask;
/*  47 */     this.hashCode = hashCode;
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Object target)
/*     */   {
/*  52 */     Object[] table = this.table;
/*  53 */     if ((target == null) || (table == null)) {
/*  54 */       return false;
/*     */     }
/*  56 */     for (int i = Hashing.smearedHash(target);; i++) {
/*  57 */       i &= this.mask;
/*  58 */       Object candidate = table[i];
/*  59 */       if (candidate == null)
/*  60 */         return false;
/*  61 */       if (candidate.equals(target)) {
/*  62 */         return true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  69 */     return this.elements.length;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator()
/*     */   {
/*  74 */     return Iterators.forArray(this.elements);
/*     */   }
/*     */   
/*     */   public Spliterator<E> spliterator()
/*     */   {
/*  79 */     return Spliterators.spliterator(this.elements, 1297);
/*     */   }
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset)
/*     */   {
/*  84 */     System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
/*  85 */     return offset + this.elements.length;
/*     */   }
/*     */   
/*     */   ImmutableList<E> createAsList()
/*     */   {
/*  90 */     return this.table == null ? ImmutableList.of() : new RegularImmutableAsList(this, this.elements);
/*     */   }
/*     */   
/*     */   boolean isPartialView()
/*     */   {
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 100 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   boolean isHashCodeFast()
/*     */   {
/* 105 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\RegularImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */