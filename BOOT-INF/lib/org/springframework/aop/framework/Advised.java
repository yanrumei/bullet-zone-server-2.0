package org.springframework.aop.framework;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.TargetSource;

public abstract interface Advised
  extends TargetClassAware
{
  public abstract boolean isFrozen();
  
  public abstract boolean isProxyTargetClass();
  
  public abstract Class<?>[] getProxiedInterfaces();
  
  public abstract boolean isInterfaceProxied(Class<?> paramClass);
  
  public abstract void setTargetSource(TargetSource paramTargetSource);
  
  public abstract TargetSource getTargetSource();
  
  public abstract void setExposeProxy(boolean paramBoolean);
  
  public abstract boolean isExposeProxy();
  
  public abstract void setPreFiltered(boolean paramBoolean);
  
  public abstract boolean isPreFiltered();
  
  public abstract Advisor[] getAdvisors();
  
  public abstract void addAdvisor(Advisor paramAdvisor)
    throws AopConfigException;
  
  public abstract void addAdvisor(int paramInt, Advisor paramAdvisor)
    throws AopConfigException;
  
  public abstract boolean removeAdvisor(Advisor paramAdvisor);
  
  public abstract void removeAdvisor(int paramInt)
    throws AopConfigException;
  
  public abstract int indexOf(Advisor paramAdvisor);
  
  public abstract boolean replaceAdvisor(Advisor paramAdvisor1, Advisor paramAdvisor2)
    throws AopConfigException;
  
  public abstract void addAdvice(Advice paramAdvice)
    throws AopConfigException;
  
  public abstract void addAdvice(int paramInt, Advice paramAdvice)
    throws AopConfigException;
  
  public abstract boolean removeAdvice(Advice paramAdvice);
  
  public abstract int indexOf(Advice paramAdvice);
  
  public abstract String toProxyConfigString();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\Advised.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */