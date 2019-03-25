package javax.security.auth.message.config;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;

public abstract interface AuthConfigProvider
{
  public abstract ClientAuthConfig getClientAuthConfig(String paramString1, String paramString2, CallbackHandler paramCallbackHandler)
    throws AuthException;
  
  public abstract ServerAuthConfig getServerAuthConfig(String paramString1, String paramString2, CallbackHandler paramCallbackHandler)
    throws AuthException;
  
  public abstract void refresh();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\config\AuthConfigProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */