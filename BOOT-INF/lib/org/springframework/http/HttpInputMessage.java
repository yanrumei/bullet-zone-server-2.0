package org.springframework.http;

import java.io.IOException;
import java.io.InputStream;

public abstract interface HttpInputMessage
  extends HttpMessage
{
  public abstract InputStream getBody()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\HttpInputMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */