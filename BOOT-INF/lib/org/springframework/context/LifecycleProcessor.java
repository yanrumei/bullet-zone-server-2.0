package org.springframework.context;

public abstract interface LifecycleProcessor
  extends Lifecycle
{
  public abstract void onRefresh();
  
  public abstract void onClose();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\LifecycleProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */