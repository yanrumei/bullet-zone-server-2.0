package org.apache.coyote.http11.upgrade;

import javax.servlet.http.HttpUpgradeHandler;
import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;

public abstract interface InternalHttpUpgradeHandler
  extends HttpUpgradeHandler
{
  public abstract AbstractEndpoint.Handler.SocketState upgradeDispatch(SocketEvent paramSocketEvent);
  
  public abstract void setSocketWrapper(SocketWrapperBase<?> paramSocketWrapperBase);
  
  public abstract void setSslSupport(SSLSupport paramSSLSupport);
  
  public abstract void pause();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http1\\upgrade\InternalHttpUpgradeHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */