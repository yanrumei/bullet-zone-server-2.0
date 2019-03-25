package com.fasterxml.jackson.databind.jsonFormatVisitors;

import java.util.Set;

public abstract interface JsonValueFormatVisitor
{
  public abstract void format(JsonValueFormat paramJsonValueFormat);
  
  public abstract void enumTypes(Set<String> paramSet);
  
  public static class Base
    implements JsonValueFormatVisitor
  {
    public void format(JsonValueFormat format) {}
    
    public void enumTypes(Set<String> enums) {}
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonValueFormatVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */