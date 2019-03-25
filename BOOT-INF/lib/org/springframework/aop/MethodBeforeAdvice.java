package org.springframework.aop;

import java.lang.reflect.Method;

public abstract interface MethodBeforeAdvice
  extends BeforeAdvice
{
  public abstract void before(Method paramMethod, Object[] paramArrayOfObject, Object paramObject)
    throws Throwable;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\MethodBeforeAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */