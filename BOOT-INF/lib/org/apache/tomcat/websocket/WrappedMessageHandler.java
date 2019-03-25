package org.apache.tomcat.websocket;

import javax.websocket.MessageHandler;

public abstract interface WrappedMessageHandler
{
  public abstract long getMaxMessageSize();
  
  public abstract MessageHandler getWrappedHandler();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WrappedMessageHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */