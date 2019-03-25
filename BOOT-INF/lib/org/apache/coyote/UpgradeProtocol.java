package org.apache.coyote;

import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
import org.apache.tomcat.util.net.SocketWrapperBase;

public abstract interface UpgradeProtocol
{
  public abstract String getHttpUpgradeName(boolean paramBoolean);
  
  public abstract byte[] getAlpnIdentifier();
  
  public abstract String getAlpnName();
  
  public abstract Processor getProcessor(SocketWrapperBase<?> paramSocketWrapperBase, Adapter paramAdapter);
  
  public abstract InternalHttpUpgradeHandler getInternalUpgradeHandler(Adapter paramAdapter, Request paramRequest);
  
  public abstract boolean accept(Request paramRequest);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\UpgradeProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */