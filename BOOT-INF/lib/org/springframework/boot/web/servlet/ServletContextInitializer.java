package org.springframework.boot.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public abstract interface ServletContextInitializer
{
  public abstract void onStartup(ServletContext paramServletContext)
    throws ServletException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ServletContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */