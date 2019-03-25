package org.springframework.context.annotation;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

public abstract interface ConditionContext
{
  public abstract BeanDefinitionRegistry getRegistry();
  
  public abstract ConfigurableListableBeanFactory getBeanFactory();
  
  public abstract Environment getEnvironment();
  
  public abstract ResourceLoader getResourceLoader();
  
  public abstract ClassLoader getClassLoader();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ConditionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */