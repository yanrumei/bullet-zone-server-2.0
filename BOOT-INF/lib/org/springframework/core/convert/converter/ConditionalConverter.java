package org.springframework.core.convert.converter;

import org.springframework.core.convert.TypeDescriptor;

public abstract interface ConditionalConverter
{
  public abstract boolean matches(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\converter\ConditionalConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */