package org.aopalliance.intercept;

public abstract interface ConstructorInterceptor
  extends Interceptor
{
  public abstract Object construct(ConstructorInvocation paramConstructorInvocation)
    throws Throwable;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\aopalliance\intercept\ConstructorInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */