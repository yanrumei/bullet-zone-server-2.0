package javax.security.auth.message.config;

import javax.security.auth.message.MessageInfo;

public abstract interface AuthConfig
{
  public abstract String getMessageLayer();
  
  public abstract String getAppContext();
  
  public abstract String getAuthContextID(MessageInfo paramMessageInfo);
  
  public abstract void refresh();
  
  public abstract boolean isProtected();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\config\AuthConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */