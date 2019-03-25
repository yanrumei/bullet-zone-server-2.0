package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract interface LongAddable
{
  public abstract void increment();
  
  public abstract void add(long paramLong);
  
  public abstract long sum();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\cache\LongAddable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */