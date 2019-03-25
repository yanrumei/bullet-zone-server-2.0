package org.springframework.aop;

public abstract interface IntroductionAdvisor
  extends Advisor, IntroductionInfo
{
  public abstract ClassFilter getClassFilter();
  
  public abstract void validateInterfaces()
    throws IllegalArgumentException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\IntroductionAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */