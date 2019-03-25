package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@GwtCompatible
public abstract interface LoadingCache<K, V>
  extends Cache<K, V>, Function<K, V>
{
  public abstract V get(K paramK)
    throws ExecutionException;
  
  public abstract V getUnchecked(K paramK);
  
  public abstract ImmutableMap<K, V> getAll(Iterable<? extends K> paramIterable)
    throws ExecutionException;
  
  @Deprecated
  public abstract V apply(K paramK);
  
  public abstract void refresh(K paramK);
  
  public abstract ConcurrentMap<K, V> asMap();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\cache\LoadingCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */