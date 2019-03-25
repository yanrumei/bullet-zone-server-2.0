package javax.websocket;

import java.util.List;
import java.util.Map;

public abstract interface HandshakeResponse
{
  public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
  
  public abstract Map<String, List<String>> getHeaders();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\HandshakeResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */