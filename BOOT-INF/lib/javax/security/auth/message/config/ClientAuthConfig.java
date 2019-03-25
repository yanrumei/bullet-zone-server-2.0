package javax.security.auth.message.config;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;

public abstract interface ClientAuthConfig
  extends AuthConfig
{
  public abstract ClientAuthContext getAuthContext(String paramString, Subject paramSubject, Map paramMap)
    throws AuthException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\config\ClientAuthConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */