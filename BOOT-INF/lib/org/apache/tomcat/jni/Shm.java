package org.apache.tomcat.jni;

import java.nio.ByteBuffer;

public class Shm
{
  public static native long create(long paramLong1, String paramString, long paramLong2)
    throws Error;
  
  public static native int remove(String paramString, long paramLong);
  
  public static native int destroy(long paramLong);
  
  public static native long attach(String paramString, long paramLong)
    throws Error;
  
  public static native int detach(long paramLong);
  
  public static native long baseaddr(long paramLong);
  
  public static native long size(long paramLong);
  
  public static native ByteBuffer buffer(long paramLong);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Shm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */