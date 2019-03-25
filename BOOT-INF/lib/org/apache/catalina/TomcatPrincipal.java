package org.apache.catalina;

import java.security.Principal;
import org.ietf.jgss.GSSCredential;

public abstract interface TomcatPrincipal
  extends Principal
{
  public abstract Principal getUserPrincipal();
  
  public abstract GSSCredential getGssCredential();
  
  public abstract void logout()
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\TomcatPrincipal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */