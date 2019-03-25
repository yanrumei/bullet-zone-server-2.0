package org.apache.tomcat.jni;

public class Stdlib
{
  public static native boolean memread(byte[] paramArrayOfByte, long paramLong, int paramInt);
  
  public static native boolean memwrite(long paramLong, byte[] paramArrayOfByte, int paramInt);
  
  public static native boolean memset(long paramLong, int paramInt1, int paramInt2);
  
  public static native long malloc(int paramInt);
  
  public static native long realloc(long paramLong, int paramInt);
  
  public static native long calloc(int paramInt1, int paramInt2);
  
  public static native void free(long paramLong);
  
  public static native int getpid();
  
  public static native int getppid();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Stdlib.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */