package org.springframework.core.convert;

public abstract interface ConversionService
{
  public abstract boolean canConvert(Class<?> paramClass1, Class<?> paramClass2);
  
  public abstract boolean canConvert(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
  
  public abstract <T> T convert(Object paramObject, Class<T> paramClass);
  
  public abstract Object convert(Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\ConversionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */