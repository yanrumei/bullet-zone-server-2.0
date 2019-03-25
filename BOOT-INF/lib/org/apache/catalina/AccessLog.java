package org.apache.catalina;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;

public abstract interface AccessLog
{
  public static final String REMOTE_ADDR_ATTRIBUTE = "org.apache.catalina.AccessLog.RemoteAddr";
  public static final String REMOTE_HOST_ATTRIBUTE = "org.apache.catalina.AccessLog.RemoteHost";
  public static final String PROTOCOL_ATTRIBUTE = "org.apache.catalina.AccessLog.Protocol";
  public static final String SERVER_PORT_ATTRIBUTE = "org.apache.catalina.AccessLog.ServerPort";
  
  public abstract void log(Request paramRequest, Response paramResponse, long paramLong);
  
  public abstract void setRequestAttributesEnabled(boolean paramBoolean);
  
  public abstract boolean getRequestAttributesEnabled();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\AccessLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */