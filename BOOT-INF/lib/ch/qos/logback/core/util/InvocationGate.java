package ch.qos.logback.core.util;

public abstract interface InvocationGate
{
  public static final long TIME_UNAVAILABLE = -1L;
  
  public abstract boolean isTooSoon(long paramLong);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\cor\\util\InvocationGate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */