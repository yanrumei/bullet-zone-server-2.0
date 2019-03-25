package javax.servlet;

import java.util.EventListener;

public abstract interface ServletContextAttributeListener
  extends EventListener
{
  public abstract void attributeAdded(ServletContextAttributeEvent paramServletContextAttributeEvent);
  
  public abstract void attributeRemoved(ServletContextAttributeEvent paramServletContextAttributeEvent);
  
  public abstract void attributeReplaced(ServletContextAttributeEvent paramServletContextAttributeEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletContextAttributeListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */