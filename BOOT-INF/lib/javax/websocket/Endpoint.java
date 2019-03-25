package javax.websocket;

public abstract class Endpoint
{
  public abstract void onOpen(Session paramSession, EndpointConfig paramEndpointConfig);
  
  public void onClose(Session session, CloseReason closeReason) {}
  
  public void onError(Session session, Throwable throwable) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\Endpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */