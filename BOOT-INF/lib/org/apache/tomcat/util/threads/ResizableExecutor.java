package org.apache.tomcat.util.threads;

import java.util.concurrent.Executor;

public abstract interface ResizableExecutor
  extends Executor
{
  public abstract int getPoolSize();
  
  public abstract int getMaxThreads();
  
  public abstract int getActiveCount();
  
  public abstract boolean resizePool(int paramInt1, int paramInt2);
  
  public abstract boolean resizeQueue(int paramInt);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\threads\ResizableExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */