package org.springframework.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

public abstract interface AdvisorChainFactory
{
  public abstract List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised paramAdvised, Method paramMethod, Class<?> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\AdvisorChainFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */