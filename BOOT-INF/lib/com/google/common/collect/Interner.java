package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@Beta
@GwtIncompatible
public abstract interface Interner<E>
{
  @CanIgnoreReturnValue
  public abstract E intern(E paramE);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\Interner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */