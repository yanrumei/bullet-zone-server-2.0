package javax.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

public abstract interface Decoder
{
  public abstract void init(EndpointConfig paramEndpointConfig);
  
  public abstract void destroy();
  
  public static abstract interface TextStream<T>
    extends Decoder
  {
    public abstract T decode(Reader paramReader)
      throws DecodeException, IOException;
  }
  
  public static abstract interface Text<T>
    extends Decoder
  {
    public abstract T decode(String paramString)
      throws DecodeException;
    
    public abstract boolean willDecode(String paramString);
  }
  
  public static abstract interface BinaryStream<T>
    extends Decoder
  {
    public abstract T decode(InputStream paramInputStream)
      throws DecodeException, IOException;
  }
  
  public static abstract interface Binary<T>
    extends Decoder
  {
    public abstract T decode(ByteBuffer paramByteBuffer)
      throws DecodeException;
    
    public abstract boolean willDecode(ByteBuffer paramByteBuffer);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\Decoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */