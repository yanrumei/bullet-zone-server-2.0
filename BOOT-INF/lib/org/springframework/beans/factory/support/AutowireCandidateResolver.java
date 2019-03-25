package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

public abstract interface AutowireCandidateResolver
{
  public abstract boolean isAutowireCandidate(BeanDefinitionHolder paramBeanDefinitionHolder, DependencyDescriptor paramDependencyDescriptor);
  
  public abstract Object getSuggestedValue(DependencyDescriptor paramDependencyDescriptor);
  
  public abstract Object getLazyResolutionProxyIfNecessary(DependencyDescriptor paramDependencyDescriptor, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\AutowireCandidateResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */