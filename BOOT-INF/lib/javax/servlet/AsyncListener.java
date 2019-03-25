package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public abstract interface AsyncListener
  extends EventListener
{
  public abstract void onComplete(AsyncEvent paramAsyncEvent)
    throws IOException;
  
  public abstract void onTimeout(AsyncEvent paramAsyncEvent)
    throws IOException;
  
  public abstract void onError(AsyncEvent paramAsyncEvent)
    throws IOException;
  
  public abstract void onStartAsync(AsyncEvent paramAsyncEvent)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\AsyncListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */