package org.apache.catalina;

import java.util.EventListener;

public abstract interface SessionListener
  extends EventListener
{
  public abstract void sessionEvent(SessionEvent paramSessionEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\SessionListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */