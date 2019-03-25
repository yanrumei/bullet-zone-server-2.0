package org.springframework.aop.framework.autoproxy;

import org.springframework.aop.TargetSource;

public abstract interface TargetSourceCreator
{
  public abstract TargetSource getTargetSource(Class<?> paramClass, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\autoproxy\TargetSourceCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */