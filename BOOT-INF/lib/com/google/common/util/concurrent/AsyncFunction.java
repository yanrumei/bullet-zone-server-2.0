package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@FunctionalInterface
@GwtCompatible
public abstract interface AsyncFunction<I, O>
{
  public abstract ListenableFuture<O> apply(@Nullable I paramI)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\AsyncFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */