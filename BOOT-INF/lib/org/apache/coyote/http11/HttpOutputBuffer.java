package org.apache.coyote.http11;

import java.io.IOException;
import org.apache.coyote.OutputBuffer;

public abstract interface HttpOutputBuffer
  extends OutputBuffer
{
  public abstract void end()
    throws IOException;
  
  public abstract void flush()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\HttpOutputBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */