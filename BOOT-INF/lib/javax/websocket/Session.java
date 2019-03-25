package javax.websocket;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract interface Session
  extends Closeable
{
  public abstract WebSocketContainer getContainer();
  
  public abstract void addMessageHandler(MessageHandler paramMessageHandler)
    throws IllegalStateException;
  
  public abstract Set<MessageHandler> getMessageHandlers();
  
  public abstract void removeMessageHandler(MessageHandler paramMessageHandler);
  
  public abstract String getProtocolVersion();
  
  public abstract String getNegotiatedSubprotocol();
  
  public abstract List<Extension> getNegotiatedExtensions();
  
  public abstract boolean isSecure();
  
  public abstract boolean isOpen();
  
  public abstract long getMaxIdleTimeout();
  
  public abstract void setMaxIdleTimeout(long paramLong);
  
  public abstract void setMaxBinaryMessageBufferSize(int paramInt);
  
  public abstract int getMaxBinaryMessageBufferSize();
  
  public abstract void setMaxTextMessageBufferSize(int paramInt);
  
  public abstract int getMaxTextMessageBufferSize();
  
  public abstract RemoteEndpoint.Async getAsyncRemote();
  
  public abstract RemoteEndpoint.Basic getBasicRemote();
  
  public abstract String getId();
  
  public abstract void close()
    throws IOException;
  
  public abstract void close(CloseReason paramCloseReason)
    throws IOException;
  
  public abstract URI getRequestURI();
  
  public abstract Map<String, List<String>> getRequestParameterMap();
  
  public abstract String getQueryString();
  
  public abstract Map<String, String> getPathParameters();
  
  public abstract Map<String, Object> getUserProperties();
  
  public abstract Principal getUserPrincipal();
  
  public abstract Set<Session> getOpenSessions();
  
  public abstract <T> void addMessageHandler(Class<T> paramClass, MessageHandler.Partial<T> paramPartial)
    throws IllegalStateException;
  
  public abstract <T> void addMessageHandler(Class<T> paramClass, MessageHandler.Whole<T> paramWhole)
    throws IllegalStateException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\Session.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */