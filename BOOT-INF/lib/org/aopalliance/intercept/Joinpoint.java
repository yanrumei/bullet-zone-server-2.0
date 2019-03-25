package org.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;

public abstract interface Joinpoint
{
  public abstract Object proceed()
    throws Throwable;
  
  public abstract Object getThis();
  
  public abstract AccessibleObject getStaticPart();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\aopalliance\intercept\Joinpoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */