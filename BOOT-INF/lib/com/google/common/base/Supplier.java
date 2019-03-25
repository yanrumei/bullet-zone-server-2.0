package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@FunctionalInterface
@GwtCompatible
public abstract interface Supplier<T>
  extends java.util.function.Supplier<T>
{
  @CanIgnoreReturnValue
  public abstract T get();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Supplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */