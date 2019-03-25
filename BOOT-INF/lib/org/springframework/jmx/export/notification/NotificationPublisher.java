package org.springframework.jmx.export.notification;

import javax.management.Notification;

public abstract interface NotificationPublisher
{
  public abstract void sendNotification(Notification paramNotification)
    throws UnableToSendNotificationException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\notification\NotificationPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */