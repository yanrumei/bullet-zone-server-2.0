package javax.servlet;

import java.util.Enumeration;

public abstract interface ServletConfig
{
  public abstract String getServletName();
  
  public abstract ServletContext getServletContext();
  
  public abstract String getInitParameter(String paramString);
  
  public abstract Enumeration<String> getInitParameterNames();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */