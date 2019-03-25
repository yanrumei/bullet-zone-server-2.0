package org.apache.tomcat.jni;

public class Address
{
  public static final String APR_ANYADDR = "0.0.0.0";
  
  public static native boolean fill(Sockaddr paramSockaddr, long paramLong);
  
  public static native Sockaddr getInfo(long paramLong);
  
  public static native long info(String paramString, int paramInt1, int paramInt2, int paramInt3, long paramLong)
    throws Exception;
  
  public static native String getnameinfo(long paramLong, int paramInt);
  
  public static native String getip(long paramLong);
  
  public static native int getservbyname(long paramLong, String paramString);
  
  public static native long get(int paramInt, long paramLong)
    throws Exception;
  
  public static native boolean equal(long paramLong1, long paramLong2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Address.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */