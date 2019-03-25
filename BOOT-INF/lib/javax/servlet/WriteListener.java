package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public abstract interface WriteListener
  extends EventListener
{
  public abstract void onWritePossible()
    throws IOException;
  
  public abstract void onError(Throwable paramThrowable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\WriteListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */