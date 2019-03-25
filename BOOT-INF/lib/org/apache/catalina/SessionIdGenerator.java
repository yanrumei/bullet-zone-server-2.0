package org.apache.catalina;

public abstract interface SessionIdGenerator
{
  public abstract String getJvmRoute();
  
  public abstract void setJvmRoute(String paramString);
  
  public abstract int getSessionIdLength();
  
  public abstract void setSessionIdLength(int paramInt);
  
  public abstract String generateSessionId();
  
  public abstract String generateSessionId(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\SessionIdGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */