package org.springframework.cache;

import java.util.Collection;

public abstract interface CacheManager
{
  public abstract Cache getCache(String paramString);
  
  public abstract Collection<String> getCacheNames();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\CacheManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */