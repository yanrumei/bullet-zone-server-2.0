package org.springframework.cache.annotation;

import java.lang.reflect.Method;
import java.util.Collection;
import org.springframework.cache.interceptor.CacheOperation;

public abstract interface CacheAnnotationParser
{
  public abstract Collection<CacheOperation> parseCacheAnnotations(Class<?> paramClass);
  
  public abstract Collection<CacheOperation> parseCacheAnnotations(Method paramMethod);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\CacheAnnotationParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */