package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

public abstract interface TypeConverter
{
  public abstract boolean canConvert(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
  
  public abstract Object convertValue(Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\TypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */