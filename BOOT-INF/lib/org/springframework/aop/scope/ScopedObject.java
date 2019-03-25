package org.springframework.aop.scope;

import org.springframework.aop.RawTargetAccess;

public abstract interface ScopedObject
  extends RawTargetAccess
{
  public abstract Object getTargetObject();
  
  public abstract void removeFromScope();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\scope\ScopedObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */