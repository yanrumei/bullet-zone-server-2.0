package org.aopalliance.intercept;

import java.lang.reflect.Constructor;

public abstract interface ConstructorInvocation
  extends Invocation
{
  public abstract Constructor<?> getConstructor();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\aopalliance\intercept\ConstructorInvocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */