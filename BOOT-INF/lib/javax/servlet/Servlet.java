package javax.servlet;

import java.io.IOException;

public abstract interface Servlet
{
  public abstract void init(ServletConfig paramServletConfig)
    throws ServletException;
  
  public abstract ServletConfig getServletConfig();
  
  public abstract void service(ServletRequest paramServletRequest, ServletResponse paramServletResponse)
    throws ServletException, IOException;
  
  public abstract String getServletInfo();
  
  public abstract void destroy();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\Servlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */