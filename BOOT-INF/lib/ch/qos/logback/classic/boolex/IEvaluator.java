package ch.qos.logback.classic.boolex;

import ch.qos.logback.classic.spi.ILoggingEvent;

public abstract interface IEvaluator
{
  public abstract boolean doEvaluate(ILoggingEvent paramILoggingEvent);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\boolex\IEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */