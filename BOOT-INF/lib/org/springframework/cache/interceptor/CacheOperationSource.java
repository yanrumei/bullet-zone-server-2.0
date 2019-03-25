package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

public abstract interface CacheOperationSource
{
  public abstract Collection<CacheOperation> getCacheOperations(Method paramMethod, Class<?> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheOperationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */