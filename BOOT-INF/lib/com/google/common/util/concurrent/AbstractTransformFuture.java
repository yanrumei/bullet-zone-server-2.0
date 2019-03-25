/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractTransformFuture<I, O, F, T>
/*     */   extends AbstractFuture.TrustedFuture<O>
/*     */   implements Runnable
/*     */ {
/*     */   @Nullable
/*     */   ListenableFuture<? extends I> inputFuture;
/*     */   @Nullable
/*     */   F function;
/*     */   
/*     */   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function)
/*     */   {
/*  39 */     AsyncTransformFuture<I, O> output = new AsyncTransformFuture(input, function);
/*  40 */     input.addListener(output, MoreExecutors.directExecutor());
/*  41 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor)
/*     */   {
/*  48 */     Preconditions.checkNotNull(executor);
/*  49 */     AsyncTransformFuture<I, O> output = new AsyncTransformFuture(input, function);
/*  50 */     input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
/*  51 */     return output;
/*     */   }
/*     */   
/*     */   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function)
/*     */   {
/*  56 */     Preconditions.checkNotNull(function);
/*  57 */     TransformFuture<I, O> output = new TransformFuture(input, function);
/*  58 */     input.addListener(output, MoreExecutors.directExecutor());
/*  59 */     return output;
/*     */   }
/*     */   
/*     */   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor)
/*     */   {
/*  64 */     Preconditions.checkNotNull(function);
/*  65 */     TransformFuture<I, O> output = new TransformFuture(input, function);
/*  66 */     input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
/*  67 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   AbstractTransformFuture(ListenableFuture<? extends I> inputFuture, F function)
/*     */   {
/*  78 */     this.inputFuture = ((ListenableFuture)Preconditions.checkNotNull(inputFuture));
/*  79 */     this.function = Preconditions.checkNotNull(function);
/*     */   }
/*     */   
/*     */   public final void run()
/*     */   {
/*  84 */     ListenableFuture<? extends I> localInputFuture = this.inputFuture;
/*  85 */     F localFunction = this.function;
/*  86 */     if ((isCancelled() | localInputFuture == null | localFunction == null)) {
/*  87 */       return;
/*     */     }
/*  89 */     this.inputFuture = null;
/*  90 */     this.function = null;
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
/*     */     try
/*     */     {
/* 103 */       sourceResult = Futures.getDone(localInputFuture);
/*     */     }
/*     */     catch (CancellationException e)
/*     */     {
/*     */       I sourceResult;
/* 108 */       cancel(false);
/* 109 */       return;
/*     */     }
/*     */     catch (ExecutionException e) {
/* 112 */       setException(e.getCause());
/* 113 */       return;
/*     */     }
/*     */     catch (RuntimeException e) {
/* 116 */       setException(e);
/* 117 */       return;
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Error e)
/*     */     {
/*     */ 
/* 124 */       setException(e);
/* 125 */       return;
/*     */     }
/*     */     try
/*     */     {
/*     */       I sourceResult;
/* 130 */       transformResult = doTransform(localFunction, sourceResult);
/*     */     } catch (UndeclaredThrowableException e) {
/*     */       T transformResult;
/* 133 */       setException(e.getCause());
/* 134 */       return;
/*     */     }
/*     */     catch (Throwable t) {
/* 137 */       setException(t); return;
/*     */     }
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
/*     */     T transformResult;
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
/* 177 */     setResult(transformResult);
/*     */   }
/*     */   
/*     */ 
/*     */   @Nullable
/*     */   @ForOverride
/*     */   abstract T doTransform(F paramF, @Nullable I paramI)
/*     */     throws Exception;
/*     */   
/*     */   @ForOverride
/*     */   abstract void setResult(@Nullable T paramT);
/*     */   
/*     */   protected final void afterDone()
/*     */   {
/* 191 */     maybePropagateCancellation(this.inputFuture);
/* 192 */     this.inputFuture = null;
/* 193 */     this.function = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class AsyncTransformFuture<I, O>
/*     */     extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>>
/*     */   {
/*     */     AsyncTransformFuture(ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function)
/*     */     {
/* 205 */       super(function);
/*     */     }
/*     */     
/*     */     ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> function, @Nullable I input)
/*     */       throws Exception
/*     */     {
/* 211 */       ListenableFuture<? extends O> outputFuture = function.apply(input);
/* 212 */       Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
/*     */       
/*     */ 
/*     */ 
/* 216 */       return outputFuture;
/*     */     }
/*     */     
/*     */     void setResult(ListenableFuture<? extends O> result)
/*     */     {
/* 221 */       setFuture(result);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class TransformFuture<I, O>
/*     */     extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O>
/*     */   {
/*     */     TransformFuture(ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function)
/*     */     {
/* 233 */       super(function);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     O doTransform(Function<? super I, ? extends O> function, @Nullable I input)
/*     */     {
/* 239 */       return (O)function.apply(input);
/*     */     }
/*     */     
/*     */ 
/*     */     void setResult(@Nullable O result)
/*     */     {
/* 245 */       set(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\AbstractTransformFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */