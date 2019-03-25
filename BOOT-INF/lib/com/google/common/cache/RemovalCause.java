package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum RemovalCause
{
  EXPLICIT,  REPLACED,  COLLECTED,  EXPIRED,  SIZE;
  
  private RemovalCause() {}
  
  abstract boolean wasEvicted();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\cache\RemovalCause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */