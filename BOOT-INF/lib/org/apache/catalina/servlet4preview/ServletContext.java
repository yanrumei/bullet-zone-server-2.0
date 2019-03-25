package org.apache.catalina.servlet4preview;

import javax.servlet.ServletRegistration.Dynamic;

@Deprecated
public abstract interface ServletContext
  extends javax.servlet.ServletContext
{
  public abstract int getSessionTimeout();
  
  public abstract void setSessionTimeout(int paramInt);
  
  public abstract ServletRegistration.Dynamic addJspFile(String paramString1, String paramString2);
  
  public abstract String getRequestCharacterEncoding();
  
  public abstract void setRequestCharacterEncoding(String paramString);
  
  public abstract String getResponseCharacterEncoding();
  
  public abstract void setResponseCharacterEncoding(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlet4preview\ServletContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */