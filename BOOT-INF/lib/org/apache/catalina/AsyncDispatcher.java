package org.apache.catalina;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract interface AsyncDispatcher
{
  public abstract void dispatch(ServletRequest paramServletRequest, ServletResponse paramServletResponse)
    throws ServletException, IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\AsyncDispatcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */