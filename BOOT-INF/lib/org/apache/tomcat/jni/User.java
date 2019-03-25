package org.apache.tomcat.jni;

public class User
{
  public static native long uidCurrent(long paramLong)
    throws Error;
  
  public static native long gidCurrent(long paramLong)
    throws Error;
  
  public static native long uid(String paramString, long paramLong)
    throws Error;
  
  public static native long usergid(String paramString, long paramLong)
    throws Error;
  
  public static native long gid(String paramString, long paramLong)
    throws Error;
  
  public static native String username(long paramLong1, long paramLong2)
    throws Error;
  
  public static native String groupname(long paramLong1, long paramLong2)
    throws Error;
  
  public static native int uidcompare(long paramLong1, long paramLong2);
  
  public static native int gidcompare(long paramLong1, long paramLong2);
  
  public static native String homepath(String paramString, long paramLong)
    throws Error;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\User.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */