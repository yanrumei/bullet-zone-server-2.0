/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.collect.ForwardingObject;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
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
/*     */ @CanIgnoreReturnValue
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingExecutorService
/*     */   extends ForwardingObject
/*     */   implements ExecutorService
/*     */ {
/*     */   protected abstract ExecutorService delegate();
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/*  49 */     return delegate().awaitTermination(timeout, unit);
/*     */   }
/*     */   
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
/*     */     throws InterruptedException
/*     */   {
/*  55 */     return delegate().invokeAll(tasks);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/*  62 */     return delegate().invokeAll(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
/*     */     throws InterruptedException, ExecutionException
/*     */   {
/*  68 */     return (T)delegate().invokeAny(tasks);
/*     */   }
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/*  74 */     return (T)delegate().invokeAny(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/*  79 */     return delegate().isShutdown();
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/*  84 */     return delegate().isTerminated();
/*     */   }
/*     */   
/*     */   public void shutdown()
/*     */   {
/*  89 */     delegate().shutdown();
/*     */   }
/*     */   
/*     */   public List<Runnable> shutdownNow()
/*     */   {
/*  94 */     return delegate().shutdownNow();
/*     */   }
/*     */   
/*     */   public void execute(Runnable command)
/*     */   {
/*  99 */     delegate().execute(command);
/*     */   }
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 103 */     return delegate().submit(task);
/*     */   }
/*     */   
/*     */   public Future<?> submit(Runnable task)
/*     */   {
/* 108 */     return delegate().submit(task);
/*     */   }
/*     */   
/*     */   public <T> Future<T> submit(Runnable task, T result)
/*     */   {
/* 113 */     return delegate().submit(task, result);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ForwardingExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */