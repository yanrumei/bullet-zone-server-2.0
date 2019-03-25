package org.apache.coyote;

import java.io.IOException;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.net.ApplicationBufferHandler;

public abstract interface InputBuffer
{
  @Deprecated
  public abstract int doRead(ByteChunk paramByteChunk)
    throws IOException;
  
  public abstract int doRead(ApplicationBufferHandler paramApplicationBufferHandler)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\InputBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */