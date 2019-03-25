package org.springframework.aop.aspectj;

import org.springframework.core.Ordered;

public abstract interface AspectInstanceFactory
  extends Ordered
{
  public abstract Object getAspectInstance();
  
  public abstract ClassLoader getAspectClassLoader();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\AspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */