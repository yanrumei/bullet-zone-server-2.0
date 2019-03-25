package org.apache.catalina;

import javax.management.MBeanRegistration;
import javax.management.ObjectName;

public abstract interface JmxEnabled
  extends MBeanRegistration
{
  public abstract String getDomain();
  
  public abstract void setDomain(String paramString);
  
  public abstract ObjectName getObjectName();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\JmxEnabled.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */