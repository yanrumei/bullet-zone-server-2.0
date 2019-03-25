package org.apache.coyote;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.tomcat.util.buf.ByteChunk;

public abstract interface OutputBuffer
{
  @Deprecated
  public abstract int doWrite(ByteChunk paramByteChunk)
    throws IOException;
  
  public abstract int doWrite(ByteBuffer paramByteBuffer)
    throws IOException;
  
  public abstract long getBytesWritten();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\OutputBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */