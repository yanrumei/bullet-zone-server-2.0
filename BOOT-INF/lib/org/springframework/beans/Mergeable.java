package org.springframework.beans;

public abstract interface Mergeable
{
  public abstract boolean isMergeEnabled();
  
  public abstract Object merge(Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\Mergeable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */