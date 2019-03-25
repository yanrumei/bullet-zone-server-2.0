package javax.servlet.http;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

public abstract interface WebConnection
  extends AutoCloseable
{
  public abstract ServletInputStream getInputStream()
    throws IOException;
  
  public abstract ServletOutputStream getOutputStream()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\WebConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */