package org.apache.tomcat.jni;

public class Global
{
  public static native long create(String paramString, int paramInt, long paramLong)
    throws Error;
  
  public static native long childInit(String paramString, long paramLong)
    throws Error;
  
  public static native int lock(long paramLong);
  
  public static native int trylock(long paramLong);
  
  public static native int unlock(long paramLong);
  
  public static native int destroy(long paramLong);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Global.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */