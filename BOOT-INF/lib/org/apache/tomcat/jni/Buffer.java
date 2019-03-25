package org.apache.tomcat.jni;

import java.nio.ByteBuffer;

public class Buffer
{
  public static native ByteBuffer malloc(int paramInt);
  
  public static native ByteBuffer calloc(int paramInt1, int paramInt2);
  
  public static native ByteBuffer palloc(long paramLong, int paramInt);
  
  public static native ByteBuffer pcalloc(long paramLong, int paramInt);
  
  public static native ByteBuffer create(long paramLong, int paramInt);
  
  public static native void free(ByteBuffer paramByteBuffer);
  
  public static native long address(ByteBuffer paramByteBuffer);
  
  public static native long size(ByteBuffer paramByteBuffer);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Buffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */