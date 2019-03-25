package org.apache.catalina;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;

public abstract interface Valve
{
  public abstract Valve getNext();
  
  public abstract void setNext(Valve paramValve);
  
  public abstract void backgroundProcess();
  
  public abstract void invoke(Request paramRequest, Response paramResponse)
    throws IOException, ServletException;
  
  public abstract boolean isAsyncSupported();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Valve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */