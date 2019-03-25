package ch.qos.logback.core.net;

import java.net.Socket;
import java.util.concurrent.Callable;
import javax.net.SocketFactory;

public abstract interface SocketConnector
  extends Callable<Socket>
{
  public abstract Socket call()
    throws InterruptedException;
  
  public abstract void setExceptionHandler(ExceptionHandler paramExceptionHandler);
  
  public abstract void setSocketFactory(SocketFactory paramSocketFactory);
  
  public static abstract interface ExceptionHandler
  {
    public abstract void connectionFailed(SocketConnector paramSocketConnector, Exception paramException);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\SocketConnector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */