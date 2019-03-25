package org.apache.coyote;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;

public abstract interface Processor
{
  public abstract AbstractEndpoint.Handler.SocketState process(SocketWrapperBase<?> paramSocketWrapperBase, SocketEvent paramSocketEvent)
    throws IOException;
  
  public abstract UpgradeToken getUpgradeToken();
  
  public abstract boolean isUpgrade();
  
  public abstract boolean isAsync();
  
  public abstract void timeoutAsync(long paramLong);
  
  public abstract Request getRequest();
  
  public abstract void recycle();
  
  public abstract void setSslSupport(SSLSupport paramSSLSupport);
  
  public abstract ByteBuffer getLeftoverInput();
  
  public abstract void pause();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\Processor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */