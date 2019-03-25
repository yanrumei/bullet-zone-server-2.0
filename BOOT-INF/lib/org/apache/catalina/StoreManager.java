package org.apache.catalina;

public abstract interface StoreManager
  extends DistributedManager
{
  public abstract Store getStore();
  
  public abstract void removeSuper(Session paramSession);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\StoreManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */