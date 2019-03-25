package org.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;

public abstract interface AdvisorAdapterRegistry
{
  public abstract Advisor wrap(Object paramObject)
    throws UnknownAdviceTypeException;
  
  public abstract MethodInterceptor[] getInterceptors(Advisor paramAdvisor)
    throws UnknownAdviceTypeException;
  
  public abstract void registerAdvisorAdapter(AdvisorAdapter paramAdvisorAdapter);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\adapter\AdvisorAdapterRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */