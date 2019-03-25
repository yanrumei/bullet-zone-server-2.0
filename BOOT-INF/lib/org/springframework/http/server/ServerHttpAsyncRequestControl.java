package org.springframework.http.server;

public abstract interface ServerHttpAsyncRequestControl
{
  public abstract void start();
  
  public abstract void start(long paramLong);
  
  public abstract boolean isStarted();
  
  public abstract void complete();
  
  public abstract boolean isCompleted();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\server\ServerHttpAsyncRequestControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */