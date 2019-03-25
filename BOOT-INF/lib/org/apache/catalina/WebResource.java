package org.apache.catalina;

import java.io.InputStream;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.jar.Manifest;

public abstract interface WebResource
{
  public abstract long getLastModified();
  
  public abstract String getLastModifiedHttp();
  
  public abstract boolean exists();
  
  public abstract boolean isVirtual();
  
  public abstract boolean isDirectory();
  
  public abstract boolean isFile();
  
  public abstract boolean delete();
  
  public abstract String getName();
  
  public abstract long getContentLength();
  
  public abstract String getCanonicalPath();
  
  public abstract boolean canRead();
  
  public abstract String getWebappPath();
  
  public abstract String getETag();
  
  public abstract void setMimeType(String paramString);
  
  public abstract String getMimeType();
  
  public abstract InputStream getInputStream();
  
  public abstract byte[] getContent();
  
  public abstract long getCreation();
  
  public abstract URL getURL();
  
  public abstract URL getCodeBase();
  
  public abstract WebResourceRoot getWebResourceRoot();
  
  public abstract Certificate[] getCertificates();
  
  public abstract Manifest getManifest();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\WebResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */