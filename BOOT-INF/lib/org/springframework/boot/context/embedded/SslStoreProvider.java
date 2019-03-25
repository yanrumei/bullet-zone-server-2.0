package org.springframework.boot.context.embedded;

import java.security.KeyStore;

public abstract interface SslStoreProvider
{
  public abstract KeyStore getKeyStore()
    throws Exception;
  
  public abstract KeyStore getTrustStore()
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\SslStoreProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */