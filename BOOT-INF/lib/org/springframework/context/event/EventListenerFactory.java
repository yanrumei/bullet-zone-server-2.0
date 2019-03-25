package org.springframework.context.event;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationListener;

public abstract interface EventListenerFactory
{
  public abstract boolean supportsMethod(Method paramMethod);
  
  public abstract ApplicationListener<?> createApplicationListener(String paramString, Class<?> paramClass, Method paramMethod);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\EventListenerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */