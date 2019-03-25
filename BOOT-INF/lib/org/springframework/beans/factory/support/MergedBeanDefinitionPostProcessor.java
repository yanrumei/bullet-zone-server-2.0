package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanPostProcessor;

public abstract interface MergedBeanDefinitionPostProcessor
  extends BeanPostProcessor
{
  public abstract void postProcessMergedBeanDefinition(RootBeanDefinition paramRootBeanDefinition, Class<?> paramClass, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\MergedBeanDefinitionPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */