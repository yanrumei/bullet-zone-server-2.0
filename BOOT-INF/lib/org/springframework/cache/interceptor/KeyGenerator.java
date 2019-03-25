package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

public abstract interface KeyGenerator
{
  public abstract Object generate(Object paramObject, Method paramMethod, Object... paramVarArgs);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\KeyGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */