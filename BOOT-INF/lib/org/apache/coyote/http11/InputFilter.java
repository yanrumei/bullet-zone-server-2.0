package org.apache.coyote.http11;

import java.io.IOException;
import org.apache.coyote.InputBuffer;
import org.apache.coyote.Request;
import org.apache.tomcat.util.buf.ByteChunk;

public abstract interface InputFilter
  extends InputBuffer
{
  public abstract void setRequest(Request paramRequest);
  
  public abstract void recycle();
  
  public abstract ByteChunk getEncodingName();
  
  public abstract void setBuffer(InputBuffer paramInputBuffer);
  
  public abstract long end()
    throws IOException;
  
  public abstract int available();
  
  public abstract boolean isFinished();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\InputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */