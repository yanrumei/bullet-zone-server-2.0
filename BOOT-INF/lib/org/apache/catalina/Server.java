package org.apache.catalina;

import java.io.File;
import javax.naming.Context;
import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.catalina.startup.Catalina;

public abstract interface Server
  extends Lifecycle
{
  public abstract NamingResourcesImpl getGlobalNamingResources();
  
  public abstract void setGlobalNamingResources(NamingResourcesImpl paramNamingResourcesImpl);
  
  public abstract Context getGlobalNamingContext();
  
  public abstract int getPort();
  
  public abstract void setPort(int paramInt);
  
  public abstract String getAddress();
  
  public abstract void setAddress(String paramString);
  
  public abstract String getShutdown();
  
  public abstract void setShutdown(String paramString);
  
  public abstract ClassLoader getParentClassLoader();
  
  public abstract void setParentClassLoader(ClassLoader paramClassLoader);
  
  public abstract Catalina getCatalina();
  
  public abstract void setCatalina(Catalina paramCatalina);
  
  public abstract File getCatalinaBase();
  
  public abstract void setCatalinaBase(File paramFile);
  
  public abstract File getCatalinaHome();
  
  public abstract void setCatalinaHome(File paramFile);
  
  public abstract void addService(Service paramService);
  
  public abstract void await();
  
  public abstract Service findService(String paramString);
  
  public abstract Service[] findServices();
  
  public abstract void removeService(Service paramService);
  
  public abstract Object getNamingToken();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Server.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */