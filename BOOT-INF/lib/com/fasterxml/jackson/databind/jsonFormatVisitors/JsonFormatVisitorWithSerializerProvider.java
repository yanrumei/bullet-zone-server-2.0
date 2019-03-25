package com.fasterxml.jackson.databind.jsonFormatVisitors;

import com.fasterxml.jackson.databind.SerializerProvider;

public abstract interface JsonFormatVisitorWithSerializerProvider
{
  public abstract SerializerProvider getProvider();
  
  public abstract void setProvider(SerializerProvider paramSerializerProvider);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonFormatVisitorWithSerializerProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */