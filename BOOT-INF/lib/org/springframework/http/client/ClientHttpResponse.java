package org.springframework.http.client;

import java.io.Closeable;
import java.io.IOException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;

public abstract interface ClientHttpResponse
  extends HttpInputMessage, Closeable
{
  public abstract HttpStatus getStatusCode()
    throws IOException;
  
  public abstract int getRawStatusCode()
    throws IOException;
  
  public abstract String getStatusText()
    throws IOException;
  
  public abstract void close();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\ClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */