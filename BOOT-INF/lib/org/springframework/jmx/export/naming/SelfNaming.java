package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public abstract interface SelfNaming
{
  public abstract ObjectName getObjectName()
    throws MalformedObjectNameException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\naming\SelfNaming.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */