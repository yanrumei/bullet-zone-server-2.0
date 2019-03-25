package org.apache.log4j;

import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public abstract interface Appender
{
  public abstract void addFilter(Filter paramFilter);
  
  public abstract Filter getFilter();
  
  public abstract void clearFilters();
  
  public abstract void close();
  
  public abstract void doAppend(LoggingEvent paramLoggingEvent);
  
  public abstract String getName();
  
  public abstract void setErrorHandler(ErrorHandler paramErrorHandler);
  
  public abstract ErrorHandler getErrorHandler();
  
  public abstract void setLayout(Layout paramLayout);
  
  public abstract Layout getLayout();
  
  public abstract void setName(String paramString);
  
  public abstract boolean requiresLayout();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\Appender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */