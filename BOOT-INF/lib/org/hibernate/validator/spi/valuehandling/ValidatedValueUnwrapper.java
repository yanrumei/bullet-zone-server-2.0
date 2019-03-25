package org.hibernate.validator.spi.valuehandling;

import java.lang.reflect.Type;

public abstract class ValidatedValueUnwrapper<T>
{
  public abstract Object handleValidatedValue(T paramT);
  
  public abstract Type getValidatedValueType(Type paramType);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\spi\valuehandling\ValidatedValueUnwrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */