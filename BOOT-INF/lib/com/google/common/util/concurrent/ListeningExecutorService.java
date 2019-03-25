package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@CanIgnoreReturnValue
@GwtIncompatible
public abstract interface ListeningExecutorService
  extends ExecutorService
{
  public abstract <T> ListenableFuture<T> submit(Callable<T> paramCallable);
  
  public abstract ListenableFuture<?> submit(Runnable paramRunnable);
  
  public abstract <T> ListenableFuture<T> submit(Runnable paramRunnable, T paramT);
  
  public abstract <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection)
    throws InterruptedException;
  
  public abstract <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ListeningExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */