package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.SortedMap;

@GwtCompatible
public abstract interface SortedMapDifference<K, V>
  extends MapDifference<K, V>
{
  public abstract SortedMap<K, V> entriesOnlyOnLeft();
  
  public abstract SortedMap<K, V> entriesOnlyOnRight();
  
  public abstract SortedMap<K, V> entriesInCommon();
  
  public abstract SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SortedMapDifference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */