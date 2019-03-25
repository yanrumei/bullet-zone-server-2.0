package org.springframework.aop.target;

public abstract interface PoolingConfig
{
  public abstract int getMaxSize();
  
  public abstract int getActiveCount()
    throws UnsupportedOperationException;
  
  public abstract int getIdleCount()
    throws UnsupportedOperationException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\target\PoolingConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */