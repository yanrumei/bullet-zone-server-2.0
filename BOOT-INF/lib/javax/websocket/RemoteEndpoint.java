package javax.websocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public abstract interface RemoteEndpoint
{
  public abstract void setBatchingAllowed(boolean paramBoolean)
    throws IOException;
  
  public abstract boolean getBatchingAllowed();
  
  public abstract void flushBatch()
    throws IOException;
  
  public abstract void sendPing(ByteBuffer paramByteBuffer)
    throws IOException, IllegalArgumentException;
  
  public abstract void sendPong(ByteBuffer paramByteBuffer)
    throws IOException, IllegalArgumentException;
  
  public static abstract interface Basic
    extends RemoteEndpoint
  {
    public abstract void sendText(String paramString)
      throws IOException;
    
    public abstract void sendBinary(ByteBuffer paramByteBuffer)
      throws IOException;
    
    public abstract void sendText(String paramString, boolean paramBoolean)
      throws IOException;
    
    public abstract void sendBinary(ByteBuffer paramByteBuffer, boolean paramBoolean)
      throws IOException;
    
    public abstract OutputStream getSendStream()
      throws IOException;
    
    public abstract Writer getSendWriter()
      throws IOException;
    
    public abstract void sendObject(Object paramObject)
      throws IOException, EncodeException;
  }
  
  public static abstract interface Async
    extends RemoteEndpoint
  {
    public abstract long getSendTimeout();
    
    public abstract void setSendTimeout(long paramLong);
    
    public abstract void sendText(String paramString, SendHandler paramSendHandler);
    
    public abstract Future<Void> sendText(String paramString);
    
    public abstract Future<Void> sendBinary(ByteBuffer paramByteBuffer);
    
    public abstract void sendBinary(ByteBuffer paramByteBuffer, SendHandler paramSendHandler);
    
    public abstract Future<Void> sendObject(Object paramObject);
    
    public abstract void sendObject(Object paramObject, SendHandler paramSendHandler);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\RemoteEndpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */