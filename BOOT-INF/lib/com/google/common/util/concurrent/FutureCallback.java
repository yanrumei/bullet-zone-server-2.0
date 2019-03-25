package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface FutureCallback<V>
{
  public abstract void onSuccess(@Nullable V paramV);
  
  public abstract void onFailure(Throwable paramThrowable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\FutureCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */