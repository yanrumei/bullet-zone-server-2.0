package ch.qos.logback.classic.spi;

public abstract interface IThrowableProxy
{
  public abstract String getMessage();
  
  public abstract String getClassName();
  
  public abstract StackTraceElementProxy[] getStackTraceElementProxyArray();
  
  public abstract int getCommonFrames();
  
  public abstract IThrowableProxy getCause();
  
  public abstract IThrowableProxy[] getSuppressed();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\spi\IThrowableProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */