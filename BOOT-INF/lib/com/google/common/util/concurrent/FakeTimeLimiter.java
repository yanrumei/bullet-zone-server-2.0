/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @Beta
/*     */ @CanIgnoreReturnValue
/*     */ @GwtIncompatible
/*     */ public final class FakeTimeLimiter
/*     */   implements TimeLimiter
/*     */ {
/*     */   public <T> T newProxy(T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit)
/*     */   {
/*  43 */     Preconditions.checkNotNull(target);
/*  44 */     Preconditions.checkNotNull(interfaceType);
/*  45 */     Preconditions.checkNotNull(timeoutUnit);
/*  46 */     return target;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible)
/*     */     throws Exception
/*     */   {
/*  54 */     Preconditions.checkNotNull(timeoutUnit);
/*  55 */     return (T)callable.call();
/*     */   }
/*     */   
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
/*     */     throws ExecutionException
/*     */   {
/*  61 */     Preconditions.checkNotNull(callable);
/*  62 */     Preconditions.checkNotNull(timeoutUnit);
/*     */     try {
/*  64 */       return (T)callable.call();
/*     */     } catch (RuntimeException e) {
/*  66 */       throw new UncheckedExecutionException(e);
/*     */     } catch (Exception e) {
/*  68 */       throw new ExecutionException(e);
/*     */     } catch (Error e) {
/*  70 */       throw new ExecutionError(e);
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*  74 */       throw new ExecutionException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
/*     */     throws ExecutionException
/*     */   {
/*  81 */     return (T)callWithTimeout(callable, timeoutDuration, timeoutUnit);
/*     */   }
/*     */   
/*     */   public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit)
/*     */   {
/*  86 */     Preconditions.checkNotNull(runnable);
/*  87 */     Preconditions.checkNotNull(timeoutUnit);
/*     */     try {
/*  89 */       runnable.run();
/*     */     } catch (RuntimeException e) {
/*  91 */       throw new UncheckedExecutionException(e);
/*     */     } catch (Error e) {
/*  93 */       throw new ExecutionError(e);
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*  97 */       throw new UncheckedExecutionException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void runUninterruptiblyWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit)
/*     */   {
/* 104 */     runWithTimeout(runnable, timeoutDuration, timeoutUnit);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\FakeTimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */