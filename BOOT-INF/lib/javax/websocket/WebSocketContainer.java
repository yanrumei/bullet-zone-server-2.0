package javax.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

public abstract interface WebSocketContainer
{
  public abstract long getDefaultAsyncSendTimeout();
  
  public abstract void setAsyncSendTimeout(long paramLong);
  
  public abstract Session connectToServer(Object paramObject, URI paramURI)
    throws DeploymentException, IOException;
  
  public abstract Session connectToServer(Class<?> paramClass, URI paramURI)
    throws DeploymentException, IOException;
  
  public abstract Session connectToServer(Endpoint paramEndpoint, ClientEndpointConfig paramClientEndpointConfig, URI paramURI)
    throws DeploymentException, IOException;
  
  public abstract Session connectToServer(Class<? extends Endpoint> paramClass, ClientEndpointConfig paramClientEndpointConfig, URI paramURI)
    throws DeploymentException, IOException;
  
  public abstract long getDefaultMaxSessionIdleTimeout();
  
  public abstract void setDefaultMaxSessionIdleTimeout(long paramLong);
  
  public abstract int getDefaultMaxBinaryMessageBufferSize();
  
  public abstract void setDefaultMaxBinaryMessageBufferSize(int paramInt);
  
  public abstract int getDefaultMaxTextMessageBufferSize();
  
  public abstract void setDefaultMaxTextMessageBufferSize(int paramInt);
  
  public abstract Set<Extension> getInstalledExtensions();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\WebSocketContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */