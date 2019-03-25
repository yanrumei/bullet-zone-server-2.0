package javax.servlet.http;

import java.util.EventListener;

public abstract interface HttpSessionIdListener
  extends EventListener
{
  public abstract void sessionIdChanged(HttpSessionEvent paramHttpSessionEvent, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpSessionIdListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */