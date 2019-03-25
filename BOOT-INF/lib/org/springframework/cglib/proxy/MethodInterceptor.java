package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public abstract interface MethodInterceptor
  extends Callback
{
  public abstract Object intercept(Object paramObject, Method paramMethod, Object[] paramArrayOfObject, MethodProxy paramMethodProxy)
    throws Throwable;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\proxy\MethodInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */