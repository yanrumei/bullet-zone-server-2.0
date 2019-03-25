package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

public abstract interface RemoteInvocationFactory
{
  public abstract RemoteInvocation createRemoteInvocation(MethodInvocation paramMethodInvocation);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\remoting\support\RemoteInvocationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */