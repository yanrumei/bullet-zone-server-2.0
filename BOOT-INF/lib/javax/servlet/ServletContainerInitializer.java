package javax.servlet;

import java.util.Set;

public abstract interface ServletContainerInitializer
{
  public abstract void onStartup(Set<Class<?>> paramSet, ServletContext paramServletContext)
    throws ServletException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletContainerInitializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */