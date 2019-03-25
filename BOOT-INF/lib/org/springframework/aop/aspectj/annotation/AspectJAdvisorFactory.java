package org.springframework.aop.aspectj.annotation;

import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.AopConfigException;

public abstract interface AspectJAdvisorFactory
{
  public abstract boolean isAspect(Class<?> paramClass);
  
  public abstract void validate(Class<?> paramClass)
    throws AopConfigException;
  
  public abstract List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory);
  
  public abstract Advisor getAdvisor(Method paramMethod, MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory, int paramInt, String paramString);
  
  public abstract Advice getAdvice(Method paramMethod, AspectJExpressionPointcut paramAspectJExpressionPointcut, MetadataAwareAspectInstanceFactory paramMetadataAwareAspectInstanceFactory, int paramInt, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\annotation\AspectJAdvisorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */