package org.springframework.scheduling;

public abstract interface SchedulingAwareRunnable
  extends Runnable
{
  public abstract boolean isLongLived();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\SchedulingAwareRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */