/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Iterator;
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
/*    */ abstract class TransformedIterator<F, T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   final Iterator<? extends F> backingIterator;
/*    */   
/*    */   TransformedIterator(Iterator<? extends F> backingIterator)
/*    */   {
/* 36 */     this.backingIterator = ((Iterator)Preconditions.checkNotNull(backingIterator));
/*    */   }
/*    */   
/*    */   abstract T transform(F paramF);
/*    */   
/*    */   public final boolean hasNext()
/*    */   {
/* 43 */     return this.backingIterator.hasNext();
/*    */   }
/*    */   
/*    */   public final T next()
/*    */   {
/* 48 */     return (T)transform(this.backingIterator.next());
/*    */   }
/*    */   
/*    */   public final void remove()
/*    */   {
/* 53 */     this.backingIterator.remove();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\TransformedIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */