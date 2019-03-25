package org.apache.tomcat.jni;

public class SSLSocket
{
  public static native int attach(long paramLong1, long paramLong2)
    throws Exception;
  
  public static native int handshake(long paramLong);
  
  public static native int renegotiate(long paramLong);
  
  public static native void setVerify(long paramLong, int paramInt1, int paramInt2);
  
  public static native byte[] getInfoB(long paramLong, int paramInt)
    throws Exception;
  
  public static native String getInfoS(long paramLong, int paramInt)
    throws Exception;
  
  public static native int getInfoI(long paramLong, int paramInt)
    throws Exception;
  
  public static native int getALPN(long paramLong, byte[] paramArrayOfByte);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\SSLSocket.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */