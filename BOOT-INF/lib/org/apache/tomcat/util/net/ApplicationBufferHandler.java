package org.apache.tomcat.util.net;

import java.nio.ByteBuffer;

public abstract interface ApplicationBufferHandler
{
  public abstract void setByteBuffer(ByteBuffer paramByteBuffer);
  
  public abstract ByteBuffer getByteBuffer();
  
  public abstract void expand(int paramInt);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\ApplicationBufferHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */