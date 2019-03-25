package javax.websocket.server;

import java.util.Set;
import javax.websocket.Endpoint;

public abstract interface ServerApplicationConfig
{
  public abstract Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> paramSet);
  
  public abstract Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> paramSet);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\server\ServerApplicationConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */