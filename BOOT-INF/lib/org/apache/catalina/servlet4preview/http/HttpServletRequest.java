package org.apache.catalina.servlet4preview.http;

public abstract interface HttpServletRequest
  extends javax.servlet.http.HttpServletRequest
{
  public abstract ServletMapping getServletMapping();
  
  public abstract PushBuilder newPushBuilder();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlet4preview\http\HttpServletRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */