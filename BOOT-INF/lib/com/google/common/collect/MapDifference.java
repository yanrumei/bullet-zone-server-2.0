package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface MapDifference<K, V>
{
  public abstract boolean areEqual();
  
  public abstract Map<K, V> entriesOnlyOnLeft();
  
  public abstract Map<K, V> entriesOnlyOnRight();
  
  public abstract Map<K, V> entriesInCommon();
  
  public abstract Map<K, ValueDifference<V>> entriesDiffering();
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public abstract int hashCode();
  
  public static abstract interface ValueDifference<V>
  {
    public abstract V leftValue();
    
    public abstract V rightValue();
    
    public abstract boolean equals(@Nullable Object paramObject);
    
    public abstract int hashCode();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\MapDifference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */