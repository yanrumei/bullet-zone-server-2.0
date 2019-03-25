package org.springframework.aop.framework;

public abstract interface AopProxyFactory
{
  public abstract AopProxy createAopProxy(AdvisedSupport paramAdvisedSupport)
    throws AopConfigException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\AopProxyFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */