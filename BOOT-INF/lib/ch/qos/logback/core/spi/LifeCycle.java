package ch.qos.logback.core.spi;

public abstract interface LifeCycle
{
  public abstract void start();
  
  public abstract void stop();
  
  public abstract boolean isStarted();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\spi\LifeCycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */