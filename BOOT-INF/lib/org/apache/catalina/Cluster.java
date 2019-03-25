package org.apache.catalina;

public abstract interface Cluster
{
  public abstract String getClusterName();
  
  public abstract void setClusterName(String paramString);
  
  public abstract void setContainer(Container paramContainer);
  
  public abstract Container getContainer();
  
  public abstract Manager createManager(String paramString);
  
  public abstract void registerManager(Manager paramManager);
  
  public abstract void removeManager(Manager paramManager);
  
  public abstract void backgroundProcess();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Cluster.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */