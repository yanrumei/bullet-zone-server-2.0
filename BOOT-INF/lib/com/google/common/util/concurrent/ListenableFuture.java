package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@GwtCompatible
public abstract interface ListenableFuture<V>
  extends Future<V>
{
  public abstract void addListener(Runnable paramRunnable, Executor paramExecutor);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ListenableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */