package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;

@FunctionalInterface
@GwtCompatible
public abstract interface Function<F, T>
  extends java.util.function.Function<F, T>
{
  @Nullable
  @CanIgnoreReturnValue
  public abstract T apply(@Nullable F paramF);
  
  public abstract boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */