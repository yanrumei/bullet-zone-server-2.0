package org.apache.catalina;

public abstract interface Engine
  extends Container
{
  public abstract String getDefaultHost();
  
  public abstract void setDefaultHost(String paramString);
  
  public abstract String getJvmRoute();
  
  public abstract void setJvmRoute(String paramString);
  
  public abstract Service getService();
  
  public abstract void setService(Service paramService);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Engine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */