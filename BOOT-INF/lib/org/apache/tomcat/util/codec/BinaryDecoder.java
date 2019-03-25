package org.apache.tomcat.util.codec;

@Deprecated
public abstract interface BinaryDecoder
  extends Decoder
{
  public abstract byte[] decode(byte[] paramArrayOfByte)
    throws DecoderException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\codec\BinaryDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */