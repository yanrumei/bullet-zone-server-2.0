package org.springframework.core.task;

import java.util.concurrent.Executor;

public abstract interface TaskExecutor
  extends Executor
{
  public abstract void execute(Runnable paramRunnable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\task\TaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */