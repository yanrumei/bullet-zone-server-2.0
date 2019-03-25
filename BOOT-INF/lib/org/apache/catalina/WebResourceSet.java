package org.apache.catalina;

import java.io.InputStream;
import java.net.URL;
import java.util.Set;

public abstract interface WebResourceSet
  extends Lifecycle
{
  public abstract WebResource getResource(String paramString);
  
  public abstract String[] list(String paramString);
  
  public abstract Set<String> listWebAppPaths(String paramString);
  
  public abstract boolean mkdir(String paramString);
  
  public abstract boolean write(String paramString, InputStream paramInputStream, boolean paramBoolean);
  
  public abstract void setRoot(WebResourceRoot paramWebResourceRoot);
  
  public abstract boolean getClassLoaderOnly();
  
  public abstract void setClassLoaderOnly(boolean paramBoolean);
  
  public abstract boolean getStaticOnly();
  
  public abstract void setStaticOnly(boolean paramBoolean);
  
  public abstract URL getBaseUrl();
  
  public abstract void setReadOnly(boolean paramBoolean);
  
  public abstract boolean isReadOnly();
  
  public abstract void gc();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\WebResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */