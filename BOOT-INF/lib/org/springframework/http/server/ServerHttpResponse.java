package org.springframework.http.server;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;

public abstract interface ServerHttpResponse
  extends HttpOutputMessage, Flushable, Closeable
{
  public abstract void setStatusCode(HttpStatus paramHttpStatus);
  
  public abstract void flush()
    throws IOException;
  
  public abstract void close();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\server\ServerHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */