package org.springframework.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract interface AsyncTaskExecutor
  extends TaskExecutor
{
  public static final long TIMEOUT_IMMEDIATE = 0L;
  public static final long TIMEOUT_INDEFINITE = Long.MAX_VALUE;
  
  public abstract void execute(Runnable paramRunnable, long paramLong);
  
  public abstract Future<?> submit(Runnable paramRunnable);
  
  public abstract <T> Future<T> submit(Callable<T> paramCallable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\task\AsyncTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */