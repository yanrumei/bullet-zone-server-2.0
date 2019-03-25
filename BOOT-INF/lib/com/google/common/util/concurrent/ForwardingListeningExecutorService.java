/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.Callable;
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
/*    */ @GwtIncompatible
/*    */ public abstract class ForwardingListeningExecutorService
/*    */   extends ForwardingExecutorService
/*    */   implements ListeningExecutorService
/*    */ {
/*    */   protected abstract ListeningExecutorService delegate();
/*    */   
/*    */   public <T> ListenableFuture<T> submit(Callable<T> task)
/*    */   {
/* 42 */     return delegate().submit(task);
/*    */   }
/*    */   
/*    */   public ListenableFuture<?> submit(Runnable task)
/*    */   {
/* 47 */     return delegate().submit(task);
/*    */   }
/*    */   
/*    */   public <T> ListenableFuture<T> submit(Runnable task, T result)
/*    */   {
/* 52 */     return delegate().submit(task, result);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ForwardingListeningExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */