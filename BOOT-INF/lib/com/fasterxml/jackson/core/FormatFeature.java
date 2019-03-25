package com.fasterxml.jackson.core;

public abstract interface FormatFeature
{
  public abstract boolean enabledByDefault();
  
  public abstract int getMask();
  
  public abstract boolean enabledIn(int paramInt);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\FormatFeature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */