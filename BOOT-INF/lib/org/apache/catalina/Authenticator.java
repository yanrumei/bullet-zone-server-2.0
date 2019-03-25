package org.apache.catalina;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Request;

public abstract interface Authenticator
{
  public abstract boolean authenticate(Request paramRequest, HttpServletResponse paramHttpServletResponse)
    throws IOException;
  
  public abstract void login(String paramString1, String paramString2, Request paramRequest)
    throws ServletException;
  
  public abstract void logout(Request paramRequest);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Authenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */