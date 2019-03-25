/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractCheckedFuture<V, X extends Exception>
/*     */   extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*     */   implements CheckedFuture<V, X>
/*     */ {
/*     */   protected AbstractCheckedFuture(ListenableFuture<V> delegate)
/*     */   {
/*  50 */     super(delegate);
/*     */   }
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
/*     */   protected abstract X mapException(Exception paramException);
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
/*     */   @CanIgnoreReturnValue
/*     */   public V checkedGet()
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/*  85 */       return (V)get();
/*     */     } catch (InterruptedException e) {
/*  87 */       Thread.currentThread().interrupt();
/*  88 */       throw mapException(e);
/*     */     } catch (CancellationException e) {
/*  90 */       throw mapException(e);
/*     */     } catch (ExecutionException e) {
/*  92 */       throw mapException(e);
/*     */     }
/*     */   }
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
/*     */   @CanIgnoreReturnValue
/*     */   public V checkedGet(long timeout, TimeUnit unit)
/*     */     throws TimeoutException, Exception
/*     */   {
/*     */     try
/*     */     {
/* 113 */       return (V)get(timeout, unit);
/*     */     } catch (InterruptedException e) {
/* 115 */       Thread.currentThread().interrupt();
/* 116 */       throw mapException(e);
/*     */     } catch (CancellationException e) {
/* 118 */       throw mapException(e);
/*     */     } catch (ExecutionException e) {
/* 120 */       throw mapException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\AbstractCheckedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */