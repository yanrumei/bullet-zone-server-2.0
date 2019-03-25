package javax.websocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;

public abstract interface Encoder
{
  public abstract void init(EndpointConfig paramEndpointConfig);
  
  public abstract void destroy();
  
  public static abstract interface BinaryStream<T>
    extends Encoder
  {
    public abstract void encode(T paramT, OutputStream paramOutputStream)
      throws EncodeException, IOException;
  }
  
  public static abstract interface Binary<T>
    extends Encoder
  {
    public abstract ByteBuffer encode(T paramT)
      throws EncodeException;
  }
  
  public static abstract interface TextStream<T>
    extends Encoder
  {
    public abstract void encode(T paramT, Writer paramWriter)
      throws EncodeException, IOException;
  }
  
  public static abstract interface Text<T>
    extends Encoder
  {
    public abstract String encode(T paramT)
      throws EncodeException;
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\Encoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */