/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.Executor;
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
/*    */ @CanIgnoreReturnValue
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingListenableFuture<V>
/*    */   extends ForwardingFuture<V>
/*    */   implements ListenableFuture<V>
/*    */ {
/*    */   protected abstract ListenableFuture<? extends V> delegate();
/*    */   
/*    */   public void addListener(Runnable listener, Executor exec)
/*    */   {
/* 45 */     delegate().addListener(listener, exec);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static abstract class SimpleForwardingListenableFuture<V>
/*    */     extends ForwardingListenableFuture<V>
/*    */   {
/*    */     private final ListenableFuture<V> delegate;
/*    */     
/*    */ 
/*    */ 
/*    */     protected SimpleForwardingListenableFuture(ListenableFuture<V> delegate)
/*    */     {
/* 60 */       this.delegate = ((ListenableFuture)Preconditions.checkNotNull(delegate));
/*    */     }
/*    */     
/*    */     protected final ListenableFuture<V> delegate()
/*    */     {
/* 65 */       return this.delegate;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ForwardingListenableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */