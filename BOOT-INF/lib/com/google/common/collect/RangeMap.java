package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible
public abstract interface RangeMap<K extends Comparable, V>
{
  @Nullable
  public abstract V get(K paramK);
  
  @Nullable
  public abstract Map.Entry<Range<K>, V> getEntry(K paramK);
  
  public abstract Range<K> span();
  
  public abstract void put(Range<K> paramRange, V paramV);
  
  public abstract void putCoalescing(Range<K> paramRange, V paramV);
  
  public abstract void putAll(RangeMap<K, V> paramRangeMap);
  
  public abstract void clear();
  
  public abstract void remove(Range<K> paramRange);
  
  public abstract Map<Range<K>, V> asMapOfRanges();
  
  public abstract Map<Range<K>, V> asDescendingMapOfRanges();
  
  public abstract RangeMap<K, V> subRangeMap(Range<K> paramRange);
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public abstract int hashCode();
  
  public abstract String toString();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\RangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */