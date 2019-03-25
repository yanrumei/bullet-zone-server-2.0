package org.springframework.util.backoff;

public abstract interface BackOffExecution
{
  public static final long STOP = -1L;
  
  public abstract long nextBackOff();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\backoff\BackOffExecution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */