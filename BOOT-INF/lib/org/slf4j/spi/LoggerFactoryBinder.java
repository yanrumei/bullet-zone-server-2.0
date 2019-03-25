package org.slf4j.spi;

import org.slf4j.ILoggerFactory;

public abstract interface LoggerFactoryBinder
{
  public abstract ILoggerFactory getLoggerFactory();
  
  public abstract String getLoggerFactoryClassStr();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\slf4j-api-1.7.25.jar!\org\slf4j\spi\LoggerFactoryBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */