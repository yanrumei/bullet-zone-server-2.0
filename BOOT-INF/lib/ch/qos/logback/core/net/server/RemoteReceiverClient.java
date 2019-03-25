package ch.qos.logback.core.net.server;

import ch.qos.logback.core.spi.ContextAware;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

abstract interface RemoteReceiverClient
  extends Client, ContextAware
{
  public abstract void setQueue(BlockingQueue<Serializable> paramBlockingQueue);
  
  public abstract boolean offer(Serializable paramSerializable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\server\RemoteReceiverClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */