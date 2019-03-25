package org.apache.tomcat.util.codec;

@Deprecated
public abstract interface Encoder
{
  public abstract Object encode(Object paramObject)
    throws EncoderException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\codec\Encoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */