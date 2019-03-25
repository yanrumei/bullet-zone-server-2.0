package com.fasterxml.jackson.annotation;

public abstract interface ObjectIdResolver
{
  public abstract void bindItem(ObjectIdGenerator.IdKey paramIdKey, Object paramObject);
  
  public abstract Object resolveId(ObjectIdGenerator.IdKey paramIdKey);
  
  public abstract ObjectIdResolver newForDeserialization(Object paramObject);
  
  public abstract boolean canUseFor(ObjectIdResolver paramObjectIdResolver);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-annotations-2.8.0.jar!\com\fasterxml\jackson\annotation\ObjectIdResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */