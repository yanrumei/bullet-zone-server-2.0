package org.springframework.scheduling;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public abstract interface TaskScheduler
{
  public abstract ScheduledFuture<?> schedule(Runnable paramRunnable, Trigger paramTrigger);
  
  public abstract ScheduledFuture<?> schedule(Runnable paramRunnable, Date paramDate);
  
  public abstract ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, Date paramDate, long paramLong);
  
  public abstract ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, long paramLong);
  
  public abstract ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, Date paramDate, long paramLong);
  
  public abstract ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, long paramLong);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\TaskScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */