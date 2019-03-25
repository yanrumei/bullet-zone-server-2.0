package org.springframework.web.context.request.async;

import org.springframework.web.context.request.NativeWebRequest;

public abstract interface AsyncWebRequest
  extends NativeWebRequest
{
  public abstract void setTimeout(Long paramLong);
  
  public abstract void addTimeoutHandler(Runnable paramRunnable);
  
  public abstract void addCompletionHandler(Runnable paramRunnable);
  
  public abstract void startAsync();
  
  public abstract boolean isAsyncStarted();
  
  public abstract void dispatch();
  
  public abstract boolean isAsyncComplete();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\async\AsyncWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */