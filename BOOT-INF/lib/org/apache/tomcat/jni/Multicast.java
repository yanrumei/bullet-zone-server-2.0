package org.apache.tomcat.jni;

public class Multicast
{
  public static native int join(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  public static native int leave(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  public static native int hops(long paramLong, int paramInt);
  
  public static native int loopback(long paramLong, boolean paramBoolean);
  
  public static native int ointerface(long paramLong1, long paramLong2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Multicast.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */