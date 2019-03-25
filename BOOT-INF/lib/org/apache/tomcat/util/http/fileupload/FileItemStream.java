package org.apache.tomcat.util.http.fileupload;

import java.io.IOException;
import java.io.InputStream;

public abstract interface FileItemStream
  extends FileItemHeadersSupport
{
  public abstract InputStream openStream()
    throws IOException;
  
  public abstract String getContentType();
  
  public abstract String getName();
  
  public abstract String getFieldName();
  
  public abstract boolean isFormField();
  
  public static class ItemSkippedException
    extends IOException
  {
    private static final long serialVersionUID = -7280778431581963740L;
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\FileItemStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */