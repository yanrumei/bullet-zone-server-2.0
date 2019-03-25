package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.springframework.remoting.support.RemoteInvocation;

public abstract interface RmiInvocationHandler
  extends Remote
{
  public abstract String getTargetInterfaceName()
    throws RemoteException;
  
  public abstract Object invoke(RemoteInvocation paramRemoteInvocation)
    throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\remoting\rmi\RmiInvocationHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */