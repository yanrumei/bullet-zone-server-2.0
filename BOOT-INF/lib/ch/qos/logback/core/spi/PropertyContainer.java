package ch.qos.logback.core.spi;

import java.util.Map;

public abstract interface PropertyContainer
{
  public abstract String getProperty(String paramString);
  
  public abstract Map<String, String> getCopyOfPropertyMap();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\spi\PropertyContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */