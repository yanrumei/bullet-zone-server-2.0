/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ public abstract class ForwardingIterator<T>
/*    */   extends ForwardingObject
/*    */   implements Iterator<T>
/*    */ {
/*    */   protected abstract Iterator<T> delegate();
/*    */   
/*    */   public boolean hasNext()
/*    */   {
/* 50 */     return delegate().hasNext();
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public T next()
/*    */   {
/* 56 */     return (T)delegate().next();
/*    */   }
/*    */   
/*    */   public void remove()
/*    */   {
/* 61 */     delegate().remove();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */