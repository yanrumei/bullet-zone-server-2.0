package org.springframework.aop.target;

public abstract interface ThreadLocalTargetSourceStats
{
  public abstract int getInvocationCount();
  
  public abstract int getHitCount();
  
  public abstract int getObjectCount();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\target\ThreadLocalTargetSourceStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */