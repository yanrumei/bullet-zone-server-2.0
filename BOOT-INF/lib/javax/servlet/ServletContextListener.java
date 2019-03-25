package javax.servlet;

import java.util.EventListener;

public abstract interface ServletContextListener
  extends EventListener
{
  public abstract void contextInitialized(ServletContextEvent paramServletContextEvent);
  
  public abstract void contextDestroyed(ServletContextEvent paramServletContextEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletContextListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */