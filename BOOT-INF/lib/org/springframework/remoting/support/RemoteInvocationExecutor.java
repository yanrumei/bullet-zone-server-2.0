package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;

public abstract interface RemoteInvocationExecutor
{
  public abstract Object invoke(RemoteInvocation paramRemoteInvocation, Object paramObject)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\remoting\support\RemoteInvocationExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */