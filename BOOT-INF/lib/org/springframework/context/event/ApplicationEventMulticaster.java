package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ResolvableType;

public abstract interface ApplicationEventMulticaster
{
  public abstract void addApplicationListener(ApplicationListener<?> paramApplicationListener);
  
  public abstract void addApplicationListenerBean(String paramString);
  
  public abstract void removeApplicationListener(ApplicationListener<?> paramApplicationListener);
  
  public abstract void removeApplicationListenerBean(String paramString);
  
  public abstract void removeAllListeners();
  
  public abstract void multicastEvent(ApplicationEvent paramApplicationEvent);
  
  public abstract void multicastEvent(ApplicationEvent paramApplicationEvent, ResolvableType paramResolvableType);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\ApplicationEventMulticaster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */