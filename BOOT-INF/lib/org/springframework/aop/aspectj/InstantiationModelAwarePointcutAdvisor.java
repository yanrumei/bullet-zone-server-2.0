package org.springframework.aop.aspectj;

import org.springframework.aop.PointcutAdvisor;

public abstract interface InstantiationModelAwarePointcutAdvisor
  extends PointcutAdvisor
{
  public abstract boolean isLazy();
  
  public abstract boolean isAdviceInstantiated();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\InstantiationModelAwarePointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */