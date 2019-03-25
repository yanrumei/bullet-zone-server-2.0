package org.apache.log4j;

import org.apache.log4j.spi.OptionHandler;

public class AppenderSkeleton
  implements OptionHandler
{
  public void setLayout(Layout layout) {}
  
  public void setName(String name) {}
  
  public void activateOptions() {}
  
  public void setThreshold(Priority threshold) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\AppenderSkeleton.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */