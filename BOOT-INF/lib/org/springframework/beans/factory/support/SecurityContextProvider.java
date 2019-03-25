package org.springframework.beans.factory.support;

import java.security.AccessControlContext;

public abstract interface SecurityContextProvider
{
  public abstract AccessControlContext getAccessControlContext();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\SecurityContextProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */