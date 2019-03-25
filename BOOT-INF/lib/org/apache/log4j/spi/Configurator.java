package org.apache.log4j.spi;

import java.net.URL;

public abstract interface Configurator
{
  public static final String INHERITED = "inherited";
  public static final String NULL = "null";
  
  public abstract void doConfigure(URL paramURL, LoggerRepository paramLoggerRepository);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\spi\Configurator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */