package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;

public abstract interface HierarchyEventListener
{
  public abstract void addAppenderEvent(Category paramCategory, Appender paramAppender);
  
  public abstract void removeAppenderEvent(Category paramCategory, Appender paramAppender);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\spi\HierarchyEventListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */