package org.apache.tomcat.util.net;

import java.io.IOException;
import java.security.cert.X509Certificate;

public abstract interface SSLSupport
{
  public static final String CIPHER_SUITE_KEY = "javax.servlet.request.cipher_suite";
  public static final String KEY_SIZE_KEY = "javax.servlet.request.key_size";
  public static final String CERTIFICATE_KEY = "javax.servlet.request.X509Certificate";
  public static final String SESSION_ID_KEY = "javax.servlet.request.ssl_session_id";
  public static final String SESSION_MGR = "javax.servlet.request.ssl_session_mgr";
  public static final String PROTOCOL_VERSION_KEY = "org.apache.tomcat.util.net.secure_protocol_version";
  
  public abstract String getCipherSuite()
    throws IOException;
  
  public abstract X509Certificate[] getPeerCertificateChain()
    throws IOException;
  
  public abstract Integer getKeySize()
    throws IOException;
  
  public abstract String getSessionId()
    throws IOException;
  
  public abstract String getProtocol()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SSLSupport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */