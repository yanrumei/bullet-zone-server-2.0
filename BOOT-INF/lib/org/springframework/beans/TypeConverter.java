package org.springframework.beans;

import java.lang.reflect.Field;
import org.springframework.core.MethodParameter;

public abstract interface TypeConverter
{
  public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass)
    throws TypeMismatchException;
  
  public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, MethodParameter paramMethodParameter)
    throws TypeMismatchException;
  
  public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, Field paramField)
    throws TypeMismatchException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\TypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */