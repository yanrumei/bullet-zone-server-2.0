package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

public abstract interface BeanNameGenerator
{
  public abstract String generateBeanName(BeanDefinition paramBeanDefinition, BeanDefinitionRegistry paramBeanDefinitionRegistry);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\BeanNameGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */