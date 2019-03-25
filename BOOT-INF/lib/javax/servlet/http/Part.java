package javax.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public abstract interface Part
{
  public abstract InputStream getInputStream()
    throws IOException;
  
  public abstract String getContentType();
  
  public abstract String getName();
  
  public abstract String getSubmittedFileName();
  
  public abstract long getSize();
  
  public abstract void write(String paramString)
    throws IOException;
  
  public abstract void delete()
    throws IOException;
  
  public abstract String getHeader(String paramString);
  
  public abstract Collection<String> getHeaders(String paramString);
  
  public abstract Collection<String> getHeaderNames();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\Part.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */