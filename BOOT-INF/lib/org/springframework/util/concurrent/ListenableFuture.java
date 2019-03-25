package org.springframework.util.concurrent;

import java.util.concurrent.Future;

public abstract interface ListenableFuture<T>
  extends Future<T>
{
  public abstract void addCallback(ListenableFutureCallback<? super T> paramListenableFutureCallback);
  
  public abstract void addCallback(SuccessCallback<? super T> paramSuccessCallback, FailureCallback paramFailureCallback);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\concurrent\ListenableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */