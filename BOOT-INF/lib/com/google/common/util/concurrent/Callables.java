/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class Callables
/*     */ {
/*     */   public static <T> Callable<T> returning(@Nullable T value)
/*     */   {
/*  40 */     new Callable()
/*     */     {
/*     */       public T call() {
/*  43 */         return (T)this.val$value;
/*     */       }
/*     */     };
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> callable, ListeningExecutorService listeningExecutorService)
/*     */   {
/*  61 */     Preconditions.checkNotNull(callable);
/*  62 */     Preconditions.checkNotNull(listeningExecutorService);
/*  63 */     new AsyncCallable()
/*     */     {
/*     */       public ListenableFuture<T> call() throws Exception {
/*  66 */         return this.val$listeningExecutorService.submit(callable);
/*     */       }
/*     */     };
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
/*     */   @GwtIncompatible
/*     */   static <T> Callable<T> threadRenaming(final Callable<T> callable, Supplier<String> nameSupplier)
/*     */   {
/*  83 */     Preconditions.checkNotNull(nameSupplier);
/*  84 */     Preconditions.checkNotNull(callable);
/*  85 */     new Callable()
/*     */     {
/*     */       public T call() throws Exception {
/*  88 */         Thread currentThread = Thread.currentThread();
/*  89 */         String oldName = currentThread.getName();
/*  90 */         boolean restoreName = Callables.trySetName((String)this.val$nameSupplier.get(), currentThread);
/*     */         try { boolean bool1;
/*  92 */           return (T)callable.call();
/*     */         } finally {
/*  94 */           if (restoreName) {
/*  95 */             boolean bool2 = Callables.trySetName(oldName, currentThread);
/*     */           }
/*     */         }
/*     */       }
/*     */     };
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
/*     */   @GwtIncompatible
/*     */   static Runnable threadRenaming(final Runnable task, Supplier<String> nameSupplier)
/*     */   {
/* 113 */     Preconditions.checkNotNull(nameSupplier);
/* 114 */     Preconditions.checkNotNull(task);
/* 115 */     new Runnable()
/*     */     {
/*     */       public void run() {
/* 118 */         Thread currentThread = Thread.currentThread();
/* 119 */         String oldName = currentThread.getName();
/* 120 */         boolean restoreName = Callables.trySetName((String)this.val$nameSupplier.get(), currentThread);
/*     */         try {
/* 122 */           task.run();
/*     */         } finally { boolean bool1;
/* 124 */           if (restoreName) {
/* 125 */             boolean bool2 = Callables.trySetName(oldName, currentThread);
/*     */           }
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   private static boolean trySetName(String threadName, Thread currentThread)
/*     */   {
/*     */     try
/*     */     {
/* 139 */       currentThread.setName(threadName);
/* 140 */       return true;
/*     */     } catch (SecurityException e) {}
/* 142 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\Callables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */