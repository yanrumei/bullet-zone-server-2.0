package org.springframework.aop;

import org.aopalliance.aop.Advice;

public abstract interface Advisor
{
  public abstract Advice getAdvice();
  
  public abstract boolean isPerInstance();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\Advisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */