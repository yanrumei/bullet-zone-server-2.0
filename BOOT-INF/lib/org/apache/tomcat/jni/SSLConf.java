package org.apache.tomcat.jni;

public final class SSLConf
{
  public static native long make(long paramLong, int paramInt)
    throws Exception;
  
  public static native void free(long paramLong);
  
  public static native int check(long paramLong, String paramString1, String paramString2)
    throws Exception;
  
  public static native void assign(long paramLong1, long paramLong2);
  
  public static native int apply(long paramLong, String paramString1, String paramString2)
    throws Exception;
  
  public static native int finish(long paramLong);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\SSLConf.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */