package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

public abstract interface GenericApplicationListener
  extends ApplicationListener<ApplicationEvent>, Ordered
{
  public abstract boolean supportsEventType(ResolvableType paramResolvableType);
  
  public abstract boolean supportsSourceType(Class<?> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\GenericApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */