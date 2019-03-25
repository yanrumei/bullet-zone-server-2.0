package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public abstract interface LoggerContextListener
{
  public abstract boolean isResetResistant();
  
  public abstract void onStart(LoggerContext paramLoggerContext);
  
  public abstract void onReset(LoggerContext paramLoggerContext);
  
  public abstract void onStop(LoggerContext paramLoggerContext);
  
  public abstract void onLevelChange(Logger paramLogger, Level paramLevel);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\spi\LoggerContextListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */