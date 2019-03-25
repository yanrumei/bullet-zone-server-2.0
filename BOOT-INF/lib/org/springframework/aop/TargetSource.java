package org.springframework.aop;

public abstract interface TargetSource
  extends TargetClassAware
{
  public abstract Class<?> getTargetClass();
  
  public abstract boolean isStatic();
  
  public abstract Object getTarget()
    throws Exception;
  
  public abstract void releaseTarget(Object paramObject)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\TargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */