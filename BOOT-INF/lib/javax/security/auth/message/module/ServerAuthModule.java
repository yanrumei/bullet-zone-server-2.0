package javax.security.auth.message.module;

import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.ServerAuth;

public abstract interface ServerAuthModule
  extends ServerAuth
{
  public abstract void initialize(MessagePolicy paramMessagePolicy1, MessagePolicy paramMessagePolicy2, CallbackHandler paramCallbackHandler, Map paramMap)
    throws AuthException;
  
  public abstract Class[] getSupportedMessageTypes();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\module\ServerAuthModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */