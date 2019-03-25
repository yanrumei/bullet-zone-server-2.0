package org.springframework.http.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

public abstract interface GenericHttpMessageConverter<T>
  extends HttpMessageConverter<T>
{
  public abstract boolean canRead(Type paramType, Class<?> paramClass, MediaType paramMediaType);
  
  public abstract T read(Type paramType, Class<?> paramClass, HttpInputMessage paramHttpInputMessage)
    throws IOException, HttpMessageNotReadableException;
  
  public abstract boolean canWrite(Type paramType, Class<?> paramClass, MediaType paramMediaType);
  
  public abstract void write(T paramT, Type paramType, MediaType paramMediaType, HttpOutputMessage paramHttpOutputMessage)
    throws IOException, HttpMessageNotWritableException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\GenericHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */