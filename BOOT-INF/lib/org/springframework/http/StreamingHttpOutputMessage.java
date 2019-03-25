package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface StreamingHttpOutputMessage
  extends HttpOutputMessage
{
  public abstract void setBody(Body paramBody);
  
  public static abstract interface Body
  {
    public abstract void writeTo(OutputStream paramOutputStream)
      throws IOException;
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\StreamingHttpOutputMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */