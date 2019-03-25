package javax.websocket;

import java.util.List;
import java.util.Map;

public abstract interface EndpointConfig
{
  public abstract List<Class<? extends Encoder>> getEncoders();
  
  public abstract List<Class<? extends Decoder>> getDecoders();
  
  public abstract Map<String, Object> getUserProperties();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\EndpointConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */