package ch.qos.logback.classic.net.server;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.net.server.Client;

abstract interface RemoteAppenderClient
  extends Client
{
  public abstract void setLoggerContext(LoggerContext paramLoggerContext);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\server\RemoteAppenderClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */