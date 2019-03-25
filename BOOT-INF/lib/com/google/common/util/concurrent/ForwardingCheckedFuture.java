/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ @Deprecated
/*    */ @Beta
/*    */ @GwtIncompatible
/*    */ public abstract class ForwardingCheckedFuture<V, X extends Exception>
/*    */   extends ForwardingListenableFuture<V>
/*    */   implements CheckedFuture<V, X>
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   public V checkedGet()
/*    */     throws Exception
/*    */   {
/* 53 */     return (V)delegate().checkedGet();
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public V checkedGet(long timeout, TimeUnit unit) throws TimeoutException, Exception
/*    */   {
/* 59 */     return (V)delegate().checkedGet(timeout, unit);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract CheckedFuture<V, X> delegate();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   @Beta
/*    */   public static abstract class SimpleForwardingCheckedFuture<V, X extends Exception>
/*    */     extends ForwardingCheckedFuture<V, X>
/*    */   {
/*    */     private final CheckedFuture<V, X> delegate;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     protected SimpleForwardingCheckedFuture(CheckedFuture<V, X> delegate)
/*    */     {
/* 87 */       this.delegate = ((CheckedFuture)Preconditions.checkNotNull(delegate));
/*    */     }
/*    */     
/*    */     protected final CheckedFuture<V, X> delegate()
/*    */     {
/* 92 */       return this.delegate;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ForwardingCheckedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */