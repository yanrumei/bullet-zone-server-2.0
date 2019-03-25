package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface BiMap<K, V>
  extends Map<K, V>
{
  @Nullable
  @CanIgnoreReturnValue
  public abstract V put(@Nullable K paramK, @Nullable V paramV);
  
  @Nullable
  @CanIgnoreReturnValue
  public abstract V forcePut(@Nullable K paramK, @Nullable V paramV);
  
  public abstract void putAll(Map<? extends K, ? extends V> paramMap);
  
  public abstract Set<V> values();
  
  public abstract BiMap<V, K> inverse();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\BiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */