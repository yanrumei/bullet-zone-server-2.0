package org.apache.catalina;

import java.util.Set;

public abstract interface DistributedManager
{
  public abstract int getActiveSessionsFull();
  
  public abstract Set<String> getSessionIdsFull();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\DistributedManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */