package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public abstract interface Weigher<K, V>
{
  public abstract int weigh(K paramK, V paramV);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\cache\Weigher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */