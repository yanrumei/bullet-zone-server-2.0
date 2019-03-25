package com.fasterxml.jackson.databind.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract interface Converter<IN, OUT>
{
  public abstract OUT convert(IN paramIN);
  
  public abstract JavaType getInputType(TypeFactory paramTypeFactory);
  
  public abstract JavaType getOutputType(TypeFactory paramTypeFactory);
  
  public static abstract class None
    implements Converter<Object, Object>
  {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\Converter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */