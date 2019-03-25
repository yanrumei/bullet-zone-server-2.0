package org.springframework.jndi;

import javax.naming.Context;
import javax.naming.NamingException;

public abstract interface JndiCallback<T>
{
  public abstract T doInContext(Context paramContext)
    throws NamingException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jndi\JndiCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */