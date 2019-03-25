package org.springframework.aop.target.dynamic;

public abstract interface Refreshable
{
  public abstract void refresh();
  
  public abstract long getRefreshCount();
  
  public abstract long getLastRefreshTime();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\target\dynamic\Refreshable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */