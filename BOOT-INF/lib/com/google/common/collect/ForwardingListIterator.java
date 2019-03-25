/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ public abstract class ForwardingListIterator<E>
/*    */   extends ForwardingIterator<E>
/*    */   implements ListIterator<E>
/*    */ {
/*    */   protected abstract ListIterator<E> delegate();
/*    */   
/*    */   public void add(E element)
/*    */   {
/* 51 */     delegate().add(element);
/*    */   }
/*    */   
/*    */   public boolean hasPrevious()
/*    */   {
/* 56 */     return delegate().hasPrevious();
/*    */   }
/*    */   
/*    */   public int nextIndex()
/*    */   {
/* 61 */     return delegate().nextIndex();
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public E previous()
/*    */   {
/* 67 */     return (E)delegate().previous();
/*    */   }
/*    */   
/*    */   public int previousIndex()
/*    */   {
/* 72 */     return delegate().previousIndex();
/*    */   }
/*    */   
/*    */   public void set(E element)
/*    */   {
/* 77 */     delegate().set(element);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */