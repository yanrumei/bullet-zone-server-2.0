package org.springframework.http.converter;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

public abstract interface HttpMessageConverter<T>
{
  public abstract boolean canRead(Class<?> paramClass, MediaType paramMediaType);
  
  public abstract boolean canWrite(Class<?> paramClass, MediaType paramMediaType);
  
  public abstract List<MediaType> getSupportedMediaTypes();
  
  public abstract T read(Class<? extends T> paramClass, HttpInputMessage paramHttpInputMessage)
    throws IOException, HttpMessageNotReadableException;
  
  public abstract void write(T paramT, MediaType paramMediaType, HttpOutputMessage paramHttpOutputMessage)
    throws IOException, HttpMessageNotWritableException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\HttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */