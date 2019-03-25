package org.springframework.core.task;

import java.util.concurrent.Callable;
import org.springframework.util.concurrent.ListenableFuture;

public abstract interface AsyncListenableTaskExecutor
  extends AsyncTaskExecutor
{
  public abstract ListenableFuture<?> submitListenable(Runnable paramRunnable);
  
  public abstract <T> ListenableFuture<T> submitListenable(Callable<T> paramCallable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\task\AsyncListenableTaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */