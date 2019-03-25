package org.springframework.scheduling;

import java.util.Date;

public abstract interface Trigger
{
  public abstract Date nextExecutionTime(TriggerContext paramTriggerContext);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\Trigger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */