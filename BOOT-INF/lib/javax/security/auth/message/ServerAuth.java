package javax.security.auth.message;

import javax.security.auth.Subject;

public abstract interface ServerAuth
{
  public abstract AuthStatus validateRequest(MessageInfo paramMessageInfo, Subject paramSubject1, Subject paramSubject2)
    throws AuthException;
  
  public abstract AuthStatus secureResponse(MessageInfo paramMessageInfo, Subject paramSubject)
    throws AuthException;
  
  public abstract void cleanSubject(MessageInfo paramMessageInfo, Subject paramSubject)
    throws AuthException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\ServerAuth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */