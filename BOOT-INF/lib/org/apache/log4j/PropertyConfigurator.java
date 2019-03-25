package org.apache.log4j;

import java.net.URL;
import java.util.Properties;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;

public class PropertyConfigurator
  implements Configurator
{
  public static void configure(Properties properties) {}
  
  public static void configure(String configFilename) {}
  
  public static void configure(URL configURL) {}
  
  public static void configureAndWatch(String configFilename) {}
  
  public static void configureAndWatch(String configFilename, long delay) {}
  
  public void doConfigure(Properties properties, LoggerRepository hierarchy) {}
  
  public void doConfigure(String configFileName, LoggerRepository hierarchy) {}
  
  public void doConfigure(URL configURL, LoggerRepository hierarchy) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\PropertyConfigurator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */