package org.apache.catalina.realm;

import java.security.cert.X509Certificate;

public abstract interface X509UsernameRetriever
{
  public abstract String getUsername(X509Certificate paramX509Certificate);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\X509UsernameRetriever.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */