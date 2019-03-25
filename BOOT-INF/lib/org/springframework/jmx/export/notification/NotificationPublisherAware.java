package org.springframework.jmx.export.notification;

import org.springframework.beans.factory.Aware;

public abstract interface NotificationPublisherAware
  extends Aware
{
  public abstract void setNotificationPublisher(NotificationPublisher paramNotificationPublisher);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\notification\NotificationPublisherAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */