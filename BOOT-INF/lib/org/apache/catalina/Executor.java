package org.apache.catalina;

import java.util.concurrent.TimeUnit;

public abstract interface Executor
  extends java.util.concurrent.Executor, Lifecycle
{
  public abstract String getName();
  
  public abstract void execute(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Executor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */