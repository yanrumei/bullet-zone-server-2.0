package org.apache.catalina;

import java.io.Closeable;

public abstract interface TrackedWebResource
  extends Closeable
{
  public abstract Exception getCreatedBy();
  
  public abstract String getName();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\TrackedWebResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */