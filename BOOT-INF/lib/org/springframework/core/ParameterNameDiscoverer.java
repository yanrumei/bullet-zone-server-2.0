package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract interface ParameterNameDiscoverer
{
  public abstract String[] getParameterNames(Method paramMethod);
  
  public abstract String[] getParameterNames(Constructor<?> paramConstructor);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\ParameterNameDiscoverer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */