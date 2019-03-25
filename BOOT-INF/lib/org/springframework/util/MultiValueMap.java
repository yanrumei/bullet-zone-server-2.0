package org.springframework.util;

import java.util.List;
import java.util.Map;

public abstract interface MultiValueMap<K, V>
  extends Map<K, List<V>>
{
  public abstract V getFirst(K paramK);
  
  public abstract void add(K paramK, V paramV);
  
  public abstract void set(K paramK, V paramV);
  
  public abstract void setAll(Map<K, V> paramMap);
  
  public abstract Map<K, V> toSingleValueMap();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\MultiValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */