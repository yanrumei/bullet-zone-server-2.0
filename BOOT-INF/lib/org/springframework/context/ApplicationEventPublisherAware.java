package org.springframework.context;

import org.springframework.beans.factory.Aware;

public abstract interface ApplicationEventPublisherAware
  extends Aware
{
  public abstract void setApplicationEventPublisher(ApplicationEventPublisher paramApplicationEventPublisher);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\ApplicationEventPublisherAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */