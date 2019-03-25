package org.springframework.http;

import java.net.URI;

public abstract interface HttpRequest
  extends HttpMessage
{
  public abstract HttpMethod getMethod();
  
  public abstract URI getURI();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\HttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */