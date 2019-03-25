/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.MoreObjects.ToStringHelper;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableList.Builder;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ @GwtCompatible(emulated=true)
/*      */ public final class Futures
/*      */   extends GwtFuturesCatchingSpecialization
/*      */ {
/*      */   @Deprecated
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper)
/*      */   {
/*  142 */     return new MappingCheckedFuture((ListenableFuture)Preconditions.checkNotNull(future), mapper);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> immediateFuture(@Nullable V value)
/*      */   {
/*  151 */     if (value == null)
/*      */     {
/*      */ 
/*  154 */       ListenableFuture<V> typedNull = ImmediateFuture.ImmediateSuccessfulFuture.NULL;
/*  155 */       return typedNull;
/*      */     }
/*  157 */     return new ImmediateFuture.ImmediateSuccessfulFuture(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value)
/*      */   {
/*  180 */     return new ImmediateFuture.ImmediateSuccessfulCheckedFuture(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable)
/*      */   {
/*  191 */     Preconditions.checkNotNull(throwable);
/*  192 */     return new ImmediateFuture.ImmediateFailedFuture(throwable);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> immediateCancelledFuture()
/*      */   {
/*  202 */     return new ImmediateFuture.ImmediateCancelledFuture();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception)
/*      */   {
/*  226 */     Preconditions.checkNotNull(exception);
/*  227 */     return new ImmediateFuture.ImmediateFailedCheckedFuture(exception);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback)
/*      */   {
/*  283 */     return AbstractCatchingFuture.create(input, exceptionType, fallback);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor)
/*      */   {
/*  334 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @CanIgnoreReturnValue
/*      */   @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback)
/*      */   {
/*  412 */     return AbstractCatchingFuture.create(input, exceptionType, fallback);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor)
/*      */   {
/*  484 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor)
/*      */   {
/*  506 */     return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function)
/*      */   {
/*  557 */     return AbstractTransformFuture.create(input, function);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor)
/*      */   {
/*  604 */     return AbstractTransformFuture.create(input, function, executor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function)
/*      */   {
/*  651 */     return AbstractTransformFuture.create(input, function);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor)
/*      */   {
/*  692 */     return AbstractTransformFuture.create(input, function, executor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <I, O> Future<O> lazyTransform(Future<I> input, final Function<? super I, ? extends O> function)
/*      */   {
/*  718 */     Preconditions.checkNotNull(input);
/*  719 */     Preconditions.checkNotNull(function);
/*  720 */     new Future()
/*      */     {
/*      */       public boolean cancel(boolean mayInterruptIfRunning)
/*      */       {
/*  724 */         return this.val$input.cancel(mayInterruptIfRunning);
/*      */       }
/*      */       
/*      */       public boolean isCancelled()
/*      */       {
/*  729 */         return this.val$input.isCancelled();
/*      */       }
/*      */       
/*      */       public boolean isDone()
/*      */       {
/*  734 */         return this.val$input.isDone();
/*      */       }
/*      */       
/*      */       public O get() throws InterruptedException, ExecutionException
/*      */       {
/*  739 */         return (O)applyTransformation(this.val$input.get());
/*      */       }
/*      */       
/*      */       public O get(long timeout, TimeUnit unit)
/*      */         throws InterruptedException, ExecutionException, TimeoutException
/*      */       {
/*  745 */         return (O)applyTransformation(this.val$input.get(timeout, unit));
/*      */       }
/*      */       
/*      */       private O applyTransformation(I input) throws ExecutionException {
/*      */         try {
/*  750 */           return (O)function.apply(input);
/*      */         } catch (Throwable t) {
/*  752 */           throw new ExecutionException(t);
/*      */         }
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested)
/*      */   {
/*  785 */     return transformAsync(nested, DEREFERENCER, 
/*  786 */       MoreExecutors.directExecutor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  792 */   private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction()
/*      */   {
/*      */     public ListenableFuture<Object> apply(ListenableFuture<Object> input)
/*      */     {
/*  796 */       return input;
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures)
/*      */   {
/*  816 */     return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures)
/*      */   {
/*  835 */     return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures)
/*      */   {
/*  846 */     return new FutureCombiner(false, ImmutableList.copyOf(futures), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures)
/*      */   {
/*  857 */     return new FutureCombiner(false, ImmutableList.copyOf(futures), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures)
/*      */   {
/*  869 */     return new FutureCombiner(true, ImmutableList.copyOf(futures), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures)
/*      */   {
/*  881 */     return new FutureCombiner(true, ImmutableList.copyOf(futures), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtCompatible
/*      */   public static final class FutureCombiner<V>
/*      */   {
/*      */     private final boolean allMustSucceed;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final ImmutableList<ListenableFuture<? extends V>> futures;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures)
/*      */     {
/*  919 */       this.allMustSucceed = allMustSucceed;
/*  920 */       this.futures = futures;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor)
/*      */     {
/*  938 */       return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     @Deprecated
/*      */     public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner)
/*      */     {
/*  953 */       return callAsync(combiner, MoreExecutors.directExecutor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     @CanIgnoreReturnValue
/*      */     public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor)
/*      */     {
/*  972 */       return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public <C> ListenableFuture<C> call(Callable<C> combiner)
/*      */     {
/*  988 */       return call(combiner, MoreExecutors.directExecutor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future)
/*      */   {
/* 1006 */     if (future.isDone()) {
/* 1007 */       return future;
/*      */     }
/* 1009 */     return new NonCancellationPropagatingFuture(future);
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class NonCancellationPropagatingFuture<V>
/*      */     extends AbstractFuture.TrustedFuture<V>
/*      */   {
/*      */     NonCancellationPropagatingFuture(final ListenableFuture<V> delegate)
/*      */     {
/* 1018 */       delegate.addListener(new Runnable()
/*      */       {
/*      */ 
/*      */         public void run()
/*      */         {
/*      */ 
/* 1024 */           Futures.NonCancellationPropagatingFuture.this.setFuture(delegate);
/*      */         }
/*      */         
/* 1027 */       }, MoreExecutors.directExecutor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures)
/*      */   {
/* 1048 */     return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures)
/*      */   {
/* 1067 */     return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures)
/*      */   {
/* 1093 */     ImmutableList<ListenableFuture<? extends T>> copy = ImmutableList.copyOf(futures);
/* 1094 */     ImmutableList.Builder<SettableFuture<T>> delegatesBuilder = ImmutableList.builder();
/* 1095 */     for (int i = 0; i < copy.size(); i++) {
/* 1096 */       delegatesBuilder.add(SettableFuture.create());
/*      */     }
/* 1098 */     final ImmutableList<SettableFuture<T>> delegates = delegatesBuilder.build();
/*      */     
/* 1100 */     AtomicInteger delegateIndex = new AtomicInteger();
/* 1101 */     for (UnmodifiableIterator localUnmodifiableIterator = copy.iterator(); localUnmodifiableIterator.hasNext();) { final ListenableFuture<? extends T> future = (ListenableFuture)localUnmodifiableIterator.next();
/* 1102 */       future.addListener(new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/* 1106 */           for (int i = this.val$delegateIndex.get(); i < delegates.size(); i++) {
/* 1107 */             if (((SettableFuture)delegates.get(i)).setFuture(future))
/*      */             {
/* 1109 */               this.val$delegateIndex.set(i + 1);
/* 1110 */               return;
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         }
/*      */         
/* 1117 */       }, MoreExecutors.directExecutor());
/*      */     }
/*      */     
/*      */ 
/* 1121 */     Object delegatesCast = delegates;
/* 1122 */     return (ImmutableList<ListenableFuture<T>>)delegatesCast;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback)
/*      */   {
/* 1167 */     addCallback(future, callback, MoreExecutors.directExecutor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor)
/*      */   {
/* 1208 */     Preconditions.checkNotNull(callback);
/* 1209 */     future.addListener(new CallbackListener(future, callback), executor);
/*      */   }
/*      */   
/*      */   private static final class CallbackListener<V> implements Runnable
/*      */   {
/*      */     final Future<V> future;
/*      */     final FutureCallback<? super V> callback;
/*      */     
/*      */     CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
/* 1218 */       this.future = future;
/* 1219 */       this.callback = callback;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*      */       try
/*      */       {
/* 1226 */         value = Futures.getDone(this.future);
/*      */       } catch (ExecutionException e) { V value;
/* 1228 */         this.callback.onFailure(e.getCause());
/* 1229 */         return;
/*      */       } catch (RuntimeException e) {
/* 1231 */         this.callback.onFailure(e);
/* 1232 */         return;
/*      */       } catch (Error e) {
/* 1234 */         this.callback.onFailure(e); return;
/*      */       }
/*      */       V value;
/* 1237 */       this.callback.onSuccess(value);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1242 */       return MoreObjects.toStringHelper(this).addValue(this.callback).toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getDone(Future<V> future)
/*      */     throws ExecutionException
/*      */   {
/* 1278 */     Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
/* 1279 */     return (V)Uninterruptibles.getUninterruptibly(future);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass)
/*      */     throws Exception
/*      */   {
/* 1327 */     return (V)FuturesGetChecked.getChecked(future, exceptionClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit)
/*      */     throws Exception
/*      */   {
/* 1376 */     return (V)FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V> V getUnchecked(Future<V> future)
/*      */   {
/* 1415 */     Preconditions.checkNotNull(future);
/*      */     try {
/* 1417 */       return (V)Uninterruptibles.getUninterruptibly(future);
/*      */     } catch (ExecutionException e) {
/* 1419 */       wrapAndThrowUnchecked(e.getCause());
/* 1420 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static void wrapAndThrowUnchecked(Throwable cause) {
/* 1426 */     if ((cause instanceof Error)) {
/* 1427 */       throw new ExecutionError((Error)cause);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1434 */     throw new UncheckedExecutionException(cause);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   private static class MappingCheckedFuture<V, X extends Exception>
/*      */     extends AbstractCheckedFuture<V, X>
/*      */   {
/*      */     final Function<? super Exception, X> mapper;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper)
/*      */     {
/* 1459 */       super();
/*      */       
/* 1461 */       this.mapper = ((Function)Preconditions.checkNotNull(mapper));
/*      */     }
/*      */     
/*      */     protected X mapException(Exception e)
/*      */     {
/* 1466 */       return (Exception)this.mapper.apply(e);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\Futures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */