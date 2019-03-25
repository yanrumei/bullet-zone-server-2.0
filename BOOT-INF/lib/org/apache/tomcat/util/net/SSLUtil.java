package org.apache.tomcat.util.net;

import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;

public abstract interface SSLUtil
{
  public abstract SSLContext createSSLContext(List<String> paramList)
    throws Exception;
  
  public abstract KeyManager[] getKeyManagers()
    throws Exception;
  
  public abstract TrustManager[] getTrustManagers()
    throws Exception;
  
  public abstract void configureSessionContext(SSLSessionContext paramSSLSessionContext);
  
  public abstract String[] getEnabledProtocols()
    throws IllegalArgumentException;
  
  public abstract String[] getEnabledCiphers()
    throws IllegalArgumentException;
  
  public static abstract interface ProtocolInfo
  {
    public abstract String getNegotiatedProtocol();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SSLUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */