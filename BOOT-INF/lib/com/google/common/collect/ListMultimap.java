package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface ListMultimap<K, V>
  extends Multimap<K, V>
{
  public abstract List<V> get(@Nullable K paramK);
  
  @CanIgnoreReturnValue
  public abstract List<V> removeAll(@Nullable Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract List<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  public abstract Map<K, Collection<V>> asMap();
  
  public abstract boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */