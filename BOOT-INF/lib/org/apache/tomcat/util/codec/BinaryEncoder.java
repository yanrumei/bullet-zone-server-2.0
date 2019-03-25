package org.apache.tomcat.util.codec;

@Deprecated
public abstract interface BinaryEncoder
  extends Encoder
{
  public abstract byte[] encode(byte[] paramArrayOfByte)
    throws EncoderException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\codec\BinaryEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */