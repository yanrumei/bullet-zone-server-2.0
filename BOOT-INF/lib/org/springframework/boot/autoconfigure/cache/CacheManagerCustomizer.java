package org.springframework.boot.autoconfigure.cache;

import org.springframework.cache.CacheManager;

public abstract interface CacheManagerCustomizer<T extends CacheManager>
{
  public abstract void customize(T paramT);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\CacheManagerCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */