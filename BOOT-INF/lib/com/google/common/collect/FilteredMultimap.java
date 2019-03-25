package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import java.util.Map.Entry;

@GwtCompatible
abstract interface FilteredMultimap<K, V>
  extends Multimap<K, V>
{
  public abstract Multimap<K, V> unfiltered();
  
  public abstract Predicate<? super Map.Entry<K, V>> entryPredicate();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\FilteredMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */