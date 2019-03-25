package org.apache.tomcat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Manifest;

public abstract interface Jar
  extends AutoCloseable
{
  public abstract URL getJarFileURL();
  
  @Deprecated
  public abstract boolean entryExists(String paramString)
    throws IOException;
  
  public abstract InputStream getInputStream(String paramString)
    throws IOException;
  
  public abstract long getLastModified(String paramString)
    throws IOException;
  
  public abstract void close();
  
  public abstract void nextEntry();
  
  public abstract String getEntryName();
  
  public abstract InputStream getEntryInputStream()
    throws IOException;
  
  public abstract String getURL(String paramString);
  
  public abstract Manifest getManifest()
    throws IOException;
  
  public abstract void reset()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\Jar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */