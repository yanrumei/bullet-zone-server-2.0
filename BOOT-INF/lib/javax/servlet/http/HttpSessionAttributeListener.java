package javax.servlet.http;

import java.util.EventListener;

public abstract interface HttpSessionAttributeListener
  extends EventListener
{
  public abstract void attributeAdded(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  public abstract void attributeRemoved(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  public abstract void attributeReplaced(HttpSessionBindingEvent paramHttpSessionBindingEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpSessionAttributeListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */