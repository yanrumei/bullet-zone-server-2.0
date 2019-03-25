package org.springframework.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public abstract interface Resource
  extends InputStreamSource
{
  public abstract boolean exists();
  
  public abstract boolean isReadable();
  
  public abstract boolean isOpen();
  
  public abstract URL getURL()
    throws IOException;
  
  public abstract URI getURI()
    throws IOException;
  
  public abstract File getFile()
    throws IOException;
  
  public abstract long contentLength()
    throws IOException;
  
  public abstract long lastModified()
    throws IOException;
  
  public abstract Resource createRelative(String paramString)
    throws IOException;
  
  public abstract String getFilename();
  
  public abstract String getDescription();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\Resource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */