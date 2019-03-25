package javax.security.auth.message;

import java.util.Map;

public abstract interface MessageInfo
{
  public abstract Object getRequestMessage();
  
  public abstract Object getResponseMessage();
  
  public abstract void setRequestMessage(Object paramObject);
  
  public abstract void setResponseMessage(Object paramObject);
  
  public abstract Map getMap();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\MessageInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */