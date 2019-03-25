package org.springframework.scheduling;

import org.springframework.core.task.AsyncTaskExecutor;

public abstract interface SchedulingTaskExecutor
  extends AsyncTaskExecutor
{
  public abstract boolean prefersShortLivedTasks();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\SchedulingTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */