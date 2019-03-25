package org.springframework.core.convert.converter;

public abstract interface ConverterRegistry
{
  public abstract void addConverter(Converter<?, ?> paramConverter);
  
  public abstract <S, T> void addConverter(Class<S> paramClass, Class<T> paramClass1, Converter<? super S, ? extends T> paramConverter);
  
  public abstract void addConverter(GenericConverter paramGenericConverter);
  
  public abstract void addConverterFactory(ConverterFactory<?, ?> paramConverterFactory);
  
  public abstract void removeConvertible(Class<?> paramClass1, Class<?> paramClass2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\converter\ConverterRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */