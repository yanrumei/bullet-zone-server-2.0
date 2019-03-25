package org.apache.coyote;

import java.util.concurrent.Executor;
import org.apache.tomcat.util.net.SSLHostConfig;

public abstract interface ProtocolHandler
{
  public abstract void setAdapter(Adapter paramAdapter);
  
  public abstract Adapter getAdapter();
  
  public abstract Executor getExecutor();
  
  public abstract void init()
    throws Exception;
  
  public abstract void start()
    throws Exception;
  
  public abstract void pause()
    throws Exception;
  
  public abstract void resume()
    throws Exception;
  
  public abstract void stop()
    throws Exception;
  
  public abstract void destroy()
    throws Exception;
  
  public abstract boolean isAprRequired();
  
  public abstract boolean isSendfileSupported();
  
  public abstract void addSslHostConfig(SSLHostConfig paramSSLHostConfig);
  
  public abstract SSLHostConfig[] findSslHostConfigs();
  
  public abstract void addUpgradeProtocol(UpgradeProtocol paramUpgradeProtocol);
  
  public abstract UpgradeProtocol[] findUpgradeProtocols();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ProtocolHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */