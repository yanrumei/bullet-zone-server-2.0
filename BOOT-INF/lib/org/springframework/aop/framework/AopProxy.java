package org.springframework.aop.framework;

public abstract interface AopProxy
{
  public abstract Object getProxy();
  
  public abstract Object getProxy(ClassLoader paramClassLoader);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\AopProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */