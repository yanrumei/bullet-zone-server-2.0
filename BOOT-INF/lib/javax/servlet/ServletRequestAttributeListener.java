package javax.servlet;

import java.util.EventListener;

public abstract interface ServletRequestAttributeListener
  extends EventListener
{
  public abstract void attributeAdded(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
  
  public abstract void attributeRemoved(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
  
  public abstract void attributeReplaced(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletRequestAttributeListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */