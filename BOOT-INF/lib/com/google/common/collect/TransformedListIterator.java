/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.ListIterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ abstract class TransformedListIterator<F, T>
/*    */   extends TransformedIterator<F, T>
/*    */   implements ListIterator<T>
/*    */ {
/*    */   TransformedListIterator(ListIterator<? extends F> backingIterator)
/*    */   {
/* 34 */     super(backingIterator);
/*    */   }
/*    */   
/*    */   private ListIterator<? extends F> backingIterator() {
/* 38 */     return Iterators.cast(this.backingIterator);
/*    */   }
/*    */   
/*    */   public final boolean hasPrevious()
/*    */   {
/* 43 */     return backingIterator().hasPrevious();
/*    */   }
/*    */   
/*    */   public final T previous()
/*    */   {
/* 48 */     return (T)transform(backingIterator().previous());
/*    */   }
/*    */   
/*    */   public final int nextIndex()
/*    */   {
/* 53 */     return backingIterator().nextIndex();
/*    */   }
/*    */   
/*    */   public final int previousIndex()
/*    */   {
/* 58 */     return backingIterator().previousIndex();
/*    */   }
/*    */   
/*    */   public void set(T element)
/*    */   {
/* 63 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void add(T element)
/*    */   {
/* 68 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\TransformedListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */