package javax.servlet;

import java.util.EventListener;

public abstract interface ServletRequestListener
  extends EventListener
{
  public abstract void requestDestroyed(ServletRequestEvent paramServletRequestEvent);
  
  public abstract void requestInitialized(ServletRequestEvent paramServletRequestEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletRequestListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */