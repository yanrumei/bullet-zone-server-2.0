package org.springframework.jmx.export;

import javax.management.ObjectName;

public abstract interface MBeanExporterListener
{
  public abstract void mbeanRegistered(ObjectName paramObjectName);
  
  public abstract void mbeanUnregistered(ObjectName paramObjectName);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\MBeanExporterListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */