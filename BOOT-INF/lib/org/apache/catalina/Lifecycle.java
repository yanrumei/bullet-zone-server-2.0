package org.apache.catalina;

public abstract interface Lifecycle
{
  public static final String BEFORE_INIT_EVENT = "before_init";
  public static final String AFTER_INIT_EVENT = "after_init";
  public static final String START_EVENT = "start";
  public static final String BEFORE_START_EVENT = "before_start";
  public static final String AFTER_START_EVENT = "after_start";
  public static final String STOP_EVENT = "stop";
  public static final String BEFORE_STOP_EVENT = "before_stop";
  public static final String AFTER_STOP_EVENT = "after_stop";
  public static final String AFTER_DESTROY_EVENT = "after_destroy";
  public static final String BEFORE_DESTROY_EVENT = "before_destroy";
  public static final String PERIODIC_EVENT = "periodic";
  public static final String CONFIGURE_START_EVENT = "configure_start";
  public static final String CONFIGURE_STOP_EVENT = "configure_stop";
  
  public abstract void addLifecycleListener(LifecycleListener paramLifecycleListener);
  
  public abstract LifecycleListener[] findLifecycleListeners();
  
  public abstract void removeLifecycleListener(LifecycleListener paramLifecycleListener);
  
  public abstract void init()
    throws LifecycleException;
  
  public abstract void start()
    throws LifecycleException;
  
  public abstract void stop()
    throws LifecycleException;
  
  public abstract void destroy()
    throws LifecycleException;
  
  public abstract LifecycleState getState();
  
  public abstract String getStateName();
  
  public static abstract interface SingleUse {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Lifecycle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */