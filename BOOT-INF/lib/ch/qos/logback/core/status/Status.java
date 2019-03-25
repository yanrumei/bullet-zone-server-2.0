package ch.qos.logback.core.status;

import java.util.Iterator;

public abstract interface Status
{
  public static final int INFO = 0;
  public static final int WARN = 1;
  public static final int ERROR = 2;
  
  public abstract int getLevel();
  
  public abstract int getEffectiveLevel();
  
  public abstract Object getOrigin();
  
  public abstract String getMessage();
  
  public abstract Throwable getThrowable();
  
  public abstract Long getDate();
  
  public abstract boolean hasChildren();
  
  public abstract void add(Status paramStatus);
  
  public abstract boolean remove(Status paramStatus);
  
  public abstract Iterator<Status> iterator();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\status\Status.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */