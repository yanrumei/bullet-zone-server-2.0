package org.springframework.context;

public abstract interface ApplicationEventPublisher
{
  public abstract void publishEvent(ApplicationEvent paramApplicationEvent);
  
  public abstract void publishEvent(Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\ApplicationEventPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */