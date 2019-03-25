package org.apache.tomcat.util.http.fileupload;

import java.io.IOException;
import java.io.InputStream;

public abstract interface RequestContext
{
  public abstract String getCharacterEncoding();
  
  public abstract String getContentType();
  
  public abstract InputStream getInputStream()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\RequestContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */