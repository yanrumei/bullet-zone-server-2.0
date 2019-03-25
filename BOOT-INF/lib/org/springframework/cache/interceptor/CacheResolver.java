package org.springframework.cache.interceptor;

import java.util.Collection;
import org.springframework.cache.Cache;

public abstract interface CacheResolver
{
  public abstract Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> paramCacheOperationInvocationContext);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */