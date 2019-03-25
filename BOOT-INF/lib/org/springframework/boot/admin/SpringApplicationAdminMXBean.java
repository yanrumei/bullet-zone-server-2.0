package org.springframework.boot.admin;

public abstract interface SpringApplicationAdminMXBean
{
  public abstract boolean isReady();
  
  public abstract boolean isEmbeddedWebApplication();
  
  public abstract String getProperty(String paramString);
  
  public abstract void shutdown();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\admin\SpringApplicationAdminMXBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */