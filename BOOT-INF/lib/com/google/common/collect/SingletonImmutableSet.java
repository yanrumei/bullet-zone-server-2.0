/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class SingletonImmutableSet<E>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   final transient E element;
/*     */   @LazyInit
/*     */   private transient int cachedHashCode;
/*     */   
/*     */   SingletonImmutableSet(E element)
/*     */   {
/*  45 */     this.element = Preconditions.checkNotNull(element);
/*     */   }
/*     */   
/*     */   SingletonImmutableSet(E element, int hashCode)
/*     */   {
/*  50 */     this.element = element;
/*  51 */     this.cachedHashCode = hashCode;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  56 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean contains(Object target)
/*     */   {
/*  61 */     return this.element.equals(target);
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator()
/*     */   {
/*  66 */     return Iterators.singletonIterator(this.element);
/*     */   }
/*     */   
/*     */   ImmutableList<E> createAsList()
/*     */   {
/*  71 */     return ImmutableList.of(this.element);
/*     */   }
/*     */   
/*     */   boolean isPartialView()
/*     */   {
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset)
/*     */   {
/*  81 */     dst[offset] = this.element;
/*  82 */     return offset + 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/*  88 */     int code = this.cachedHashCode;
/*  89 */     if (code == 0) {
/*  90 */       this.cachedHashCode = (code = this.element.hashCode());
/*     */     }
/*  92 */     return code;
/*     */   }
/*     */   
/*     */   boolean isHashCodeFast()
/*     */   {
/*  97 */     return this.cachedHashCode != 0;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 102 */     return '[' + this.element.toString() + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SingletonImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */