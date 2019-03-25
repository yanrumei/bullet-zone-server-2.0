package org.springframework.context;

public abstract interface Lifecycle
{
  public abstract void start();
  
  public abstract void stop();
  
  public abstract boolean isRunning();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\Lifecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */