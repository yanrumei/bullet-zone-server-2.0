package org.hibernate.validator.spi.group;

import java.util.List;

public abstract interface DefaultGroupSequenceProvider<T>
{
  public abstract List<Class<?>> getValidationGroups(T paramT);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\spi\group\DefaultGroupSequenceProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */