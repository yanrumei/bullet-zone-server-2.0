package org.springframework.core.convert.converter;

public abstract interface ConverterFactory<S, R>
{
  public abstract <T extends R> Converter<S, T> getConverter(Class<T> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\converter\ConverterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */