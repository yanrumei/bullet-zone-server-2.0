package ch.qos.logback.core.status;

import java.util.List;

public abstract interface StatusManager
{
  public abstract void add(Status paramStatus);
  
  public abstract List<Status> getCopyOfStatusList();
  
  public abstract int getCount();
  
  public abstract boolean add(StatusListener paramStatusListener);
  
  public abstract void remove(StatusListener paramStatusListener);
  
  public abstract void clear();
  
  public abstract List<StatusListener> getCopyOfStatusListenerList();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\status\StatusManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */