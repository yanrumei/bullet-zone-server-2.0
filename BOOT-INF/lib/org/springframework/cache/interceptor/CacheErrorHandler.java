package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;

public abstract interface CacheErrorHandler
{
  public abstract void handleCacheGetError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject);
  
  public abstract void handleCachePutError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject1, Object paramObject2);
  
  public abstract void handleCacheEvictError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject);
  
  public abstract void handleCacheClearError(RuntimeException paramRuntimeException, Cache paramCache);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */