package org.apache.tomcat.jni;

public class Local
{
  public static native long create(String paramString, long paramLong)
    throws Exception;
  
  public static native int bind(long paramLong1, long paramLong2);
  
  public static native int listen(long paramLong, int paramInt);
  
  public static native long accept(long paramLong)
    throws Exception;
  
  public static native int connect(long paramLong1, long paramLong2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Local.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */