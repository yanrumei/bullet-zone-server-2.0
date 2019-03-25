package org.apache.tomcat.util.http.fileupload;

import java.util.Iterator;

public abstract interface FileItemHeaders
{
  public abstract String getHeader(String paramString);
  
  public abstract Iterator<String> getHeaders(String paramString);
  
  public abstract Iterator<String> getHeaderNames();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\FileItemHeaders.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */