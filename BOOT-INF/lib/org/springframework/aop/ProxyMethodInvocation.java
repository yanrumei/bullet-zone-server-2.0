package org.springframework.aop;

import org.aopalliance.intercept.MethodInvocation;

public abstract interface ProxyMethodInvocation
  extends MethodInvocation
{
  public abstract Object getProxy();
  
  public abstract MethodInvocation invocableClone();
  
  public abstract MethodInvocation invocableClone(Object... paramVarArgs);
  
  public abstract void setArguments(Object... paramVarArgs);
  
  public abstract void setUserAttribute(String paramString, Object paramObject);
  
  public abstract Object getUserAttribute(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\ProxyMethodInvocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */