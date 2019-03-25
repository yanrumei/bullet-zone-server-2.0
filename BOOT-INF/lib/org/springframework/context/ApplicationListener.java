package org.springframework.context;

import java.util.EventListener;

public abstract interface ApplicationListener<E extends ApplicationEvent>
  extends EventListener
{
  public abstract void onApplicationEvent(E paramE);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\ApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */