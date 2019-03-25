package org.aopalliance.intercept;

public abstract interface MethodInterceptor
  extends Interceptor
{
  public abstract Object invoke(MethodInvocation paramMethodInvocation)
    throws Throwable;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\aopalliance\intercept\MethodInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */