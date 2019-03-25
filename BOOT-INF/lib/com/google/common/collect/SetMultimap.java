package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface SetMultimap<K, V>
  extends Multimap<K, V>
{
  public abstract Set<V> get(@Nullable K paramK);
  
  @CanIgnoreReturnValue
  public abstract Set<V> removeAll(@Nullable Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract Set<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  public abstract Set<Map.Entry<K, V>> entries();
  
  public abstract Map<K, Collection<V>> asMap();
  
  public abstract boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */