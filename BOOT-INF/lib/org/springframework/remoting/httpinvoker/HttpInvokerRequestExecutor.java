package org.springframework.remoting.httpinvoker;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

public abstract interface HttpInvokerRequestExecutor
{
  public abstract RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration paramHttpInvokerClientConfiguration, RemoteInvocation paramRemoteInvocation)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\httpinvoker\HttpInvokerRequestExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */