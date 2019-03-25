package javax.websocket.server;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public abstract interface HandshakeRequest
{
  public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
  public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
  public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
  public static final String SEC_WEBSOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";
  
  public abstract Map<String, List<String>> getHeaders();
  
  public abstract Principal getUserPrincipal();
  
  public abstract URI getRequestURI();
  
  public abstract boolean isUserInRole(String paramString);
  
  public abstract Object getHttpSession();
  
  public abstract Map<String, List<String>> getParameterMap();
  
  public abstract String getQueryString();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\server\HandshakeRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */