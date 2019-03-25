package org.springframework.http.server;

import java.net.InetSocketAddress;
import java.security.Principal;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;

public abstract interface ServerHttpRequest
  extends HttpRequest, HttpInputMessage
{
  public abstract Principal getPrincipal();
  
  public abstract InetSocketAddress getLocalAddress();
  
  public abstract InetSocketAddress getRemoteAddress();
  
  public abstract ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse paramServerHttpResponse);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\server\ServerHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */