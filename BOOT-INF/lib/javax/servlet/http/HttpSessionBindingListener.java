package javax.servlet.http;

import java.util.EventListener;

public abstract interface HttpSessionBindingListener
  extends EventListener
{
  public abstract void valueBound(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  public abstract void valueUnbound(HttpSessionBindingEvent paramHttpSessionBindingEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpSessionBindingListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */