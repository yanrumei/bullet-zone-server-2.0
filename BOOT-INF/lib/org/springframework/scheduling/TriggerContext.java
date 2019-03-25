package org.springframework.scheduling;

import java.util.Date;

public abstract interface TriggerContext
{
  public abstract Date lastScheduledExecutionTime();
  
  public abstract Date lastActualExecutionTime();
  
  public abstract Date lastCompletionTime();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\TriggerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */