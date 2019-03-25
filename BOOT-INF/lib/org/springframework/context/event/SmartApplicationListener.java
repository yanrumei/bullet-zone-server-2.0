package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public abstract interface SmartApplicationListener
  extends ApplicationListener<ApplicationEvent>, Ordered
{
  public abstract boolean supportsEventType(Class<? extends ApplicationEvent> paramClass);
  
  public abstract boolean supportsSourceType(Class<?> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\SmartApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */