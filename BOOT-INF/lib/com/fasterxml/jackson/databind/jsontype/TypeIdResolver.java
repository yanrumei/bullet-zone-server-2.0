package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import java.io.IOException;

public abstract interface TypeIdResolver
{
  public abstract void init(JavaType paramJavaType);
  
  public abstract String idFromValue(Object paramObject);
  
  public abstract String idFromValueAndType(Object paramObject, Class<?> paramClass);
  
  public abstract String idFromBaseType();
  
  public abstract JavaType typeFromId(DatabindContext paramDatabindContext, String paramString)
    throws IOException;
  
  public abstract String getDescForKnownTypeIds();
  
  public abstract JsonTypeInfo.Id getMechanism();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\TypeIdResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */