package org.apache.tomcat.websocket;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;

public abstract interface AsyncChannelWrapper
{
  public abstract Future<Integer> read(ByteBuffer paramByteBuffer);
  
  public abstract <B, A extends B> void read(ByteBuffer paramByteBuffer, A paramA, CompletionHandler<Integer, B> paramCompletionHandler);
  
  public abstract Future<Integer> write(ByteBuffer paramByteBuffer);
  
  public abstract <B, A extends B> void write(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<Long, B> paramCompletionHandler);
  
  public abstract void close();
  
  public abstract Future<Void> handshake()
    throws SSLException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\AsyncChannelWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */