package org.springframework.aop.interceptor;

import java.lang.reflect.Method;

public abstract interface AsyncUncaughtExceptionHandler
{
  public abstract void handleUncaughtException(Throwable paramThrowable, Method paramMethod, Object... paramVarArgs);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\interceptor\AsyncUncaughtExceptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */