package org.apache.tomcat.jni;

public abstract interface BIOCallback
{
  public abstract int write(byte[] paramArrayOfByte);
  
  public abstract int read(byte[] paramArrayOfByte);
  
  public abstract int puts(String paramString);
  
  public abstract String gets(int paramInt);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\BIOCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */