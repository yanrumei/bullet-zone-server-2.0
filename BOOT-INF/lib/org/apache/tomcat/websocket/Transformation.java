package org.apache.tomcat.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import javax.websocket.Extension;

public abstract interface Transformation
{
  public abstract void setNext(Transformation paramTransformation);
  
  public abstract boolean validateRsvBits(int paramInt);
  
  public abstract Extension getExtensionResponse();
  
  public abstract TransformationResult getMoreData(byte paramByte, boolean paramBoolean, int paramInt, ByteBuffer paramByteBuffer)
    throws IOException;
  
  public abstract boolean validateRsv(int paramInt, byte paramByte);
  
  public abstract List<MessagePart> sendMessagePart(List<MessagePart> paramList);
  
  public abstract void close();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\Transformation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */