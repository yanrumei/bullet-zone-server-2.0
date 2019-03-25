package org.springframework.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public abstract interface WebApplicationInitializer
{
  public abstract void onStartup(ServletContext paramServletContext)
    throws ServletException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\WebApplicationInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */