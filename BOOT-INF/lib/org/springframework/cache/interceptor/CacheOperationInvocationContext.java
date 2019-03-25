package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

public abstract interface CacheOperationInvocationContext<O extends BasicOperation>
{
  public abstract O getOperation();
  
  public abstract Object getTarget();
  
  public abstract Method getMethod();
  
  public abstract Object[] getArgs();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheOperationInvocationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */