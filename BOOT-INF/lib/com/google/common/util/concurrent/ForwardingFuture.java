/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ForwardingObject;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
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
/*    */ @CanIgnoreReturnValue
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingFuture<V>
/*    */   extends ForwardingObject
/*    */   implements Future<V>
/*    */ {
/*    */   protected abstract Future<? extends V> delegate();
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning)
/*    */   {
/* 47 */     return delegate().cancel(mayInterruptIfRunning);
/*    */   }
/*    */   
/*    */   public boolean isCancelled()
/*    */   {
/* 52 */     return delegate().isCancelled();
/*    */   }
/*    */   
/*    */   public boolean isDone()
/*    */   {
/* 57 */     return delegate().isDone();
/*    */   }
/*    */   
/*    */   public V get() throws InterruptedException, ExecutionException
/*    */   {
/* 62 */     return (V)delegate().get();
/*    */   }
/*    */   
/*    */   public V get(long timeout, TimeUnit unit)
/*    */     throws InterruptedException, ExecutionException, TimeoutException
/*    */   {
/* 68 */     return (V)delegate().get(timeout, unit);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static abstract class SimpleForwardingFuture<V>
/*    */     extends ForwardingFuture<V>
/*    */   {
/*    */     private final Future<V> delegate;
/*    */     
/*    */ 
/*    */ 
/*    */     protected SimpleForwardingFuture(Future<V> delegate)
/*    */     {
/* 82 */       this.delegate = ((Future)Preconditions.checkNotNull(delegate));
/*    */     }
/*    */     
/*    */     protected final Future<V> delegate()
/*    */     {
/* 87 */       return this.delegate;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ForwardingFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */