package com.fasterxml.jackson.databind.cfg;

public abstract interface ConfigFeature
{
  public abstract boolean enabledByDefault();
  
  public abstract int getMask();
  
  public abstract boolean enabledIn(int paramInt);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\cfg\ConfigFeature.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */