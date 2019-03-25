package org.apache.tomcat.util.net;

import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;

public abstract interface SSLContext
{
  public abstract void init(KeyManager[] paramArrayOfKeyManager, TrustManager[] paramArrayOfTrustManager, SecureRandom paramSecureRandom)
    throws KeyManagementException;
  
  public abstract void destroy();
  
  public abstract SSLSessionContext getServerSessionContext();
  
  public abstract SSLEngine createSSLEngine();
  
  public abstract SSLServerSocketFactory getServerSocketFactory();
  
  public abstract SSLParameters getSupportedSSLParameters();
  
  public abstract X509Certificate[] getCertificateChain(String paramString);
  
  public abstract X509Certificate[] getAcceptedIssuers();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SSLContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */