package org.apache.tomcat.util.http.fileupload.util;

import java.io.IOException;

public abstract interface Closeable
{
  public abstract void close()
    throws IOException;
  
  public abstract boolean isClosed()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileuploa\\util\Closeable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */