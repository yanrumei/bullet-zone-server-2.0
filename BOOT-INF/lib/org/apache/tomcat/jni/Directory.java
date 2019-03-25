package org.apache.tomcat.jni;

public class Directory
{
  public static native int make(String paramString, int paramInt, long paramLong);
  
  public static native int makeRecursive(String paramString, int paramInt, long paramLong);
  
  public static native int remove(String paramString, long paramLong);
  
  public static native String tempGet(long paramLong);
  
  public static native long open(String paramString, long paramLong)
    throws Error;
  
  public static native int close(long paramLong);
  
  public static native int rewind(long paramLong);
  
  public static native int read(FileInfo paramFileInfo, int paramInt, long paramLong);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Directory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */