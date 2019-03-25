package org.apache.catalina;

import java.beans.PropertyChangeListener;
import java.io.IOException;

public abstract interface Manager
{
  public abstract Context getContext();
  
  public abstract void setContext(Context paramContext);
  
  public abstract SessionIdGenerator getSessionIdGenerator();
  
  public abstract void setSessionIdGenerator(SessionIdGenerator paramSessionIdGenerator);
  
  public abstract long getSessionCounter();
  
  public abstract void setSessionCounter(long paramLong);
  
  public abstract int getMaxActive();
  
  public abstract void setMaxActive(int paramInt);
  
  public abstract int getActiveSessions();
  
  public abstract long getExpiredSessions();
  
  public abstract void setExpiredSessions(long paramLong);
  
  public abstract int getRejectedSessions();
  
  public abstract int getSessionMaxAliveTime();
  
  public abstract void setSessionMaxAliveTime(int paramInt);
  
  public abstract int getSessionAverageAliveTime();
  
  public abstract int getSessionCreateRate();
  
  public abstract int getSessionExpireRate();
  
  public abstract void add(Session paramSession);
  
  public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void changeSessionId(Session paramSession);
  
  public abstract void changeSessionId(Session paramSession, String paramString);
  
  public abstract Session createEmptySession();
  
  public abstract Session createSession(String paramString);
  
  public abstract Session findSession(String paramString)
    throws IOException;
  
  public abstract Session[] findSessions();
  
  public abstract void load()
    throws ClassNotFoundException, IOException;
  
  public abstract void remove(Session paramSession);
  
  public abstract void remove(Session paramSession, boolean paramBoolean);
  
  public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void unload()
    throws IOException;
  
  public abstract void backgroundProcess();
  
  public abstract boolean willAttributeDistribute(String paramString, Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Manager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */