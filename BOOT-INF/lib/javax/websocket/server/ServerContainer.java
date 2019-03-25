package javax.websocket.server;

import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

public abstract interface ServerContainer
  extends WebSocketContainer
{
  public abstract void addEndpoint(Class<?> paramClass)
    throws DeploymentException;
  
  public abstract void addEndpoint(ServerEndpointConfig paramServerEndpointConfig)
    throws DeploymentException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\server\ServerContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */