package org.springframework.beans.factory.config;

import java.util.Iterator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public abstract interface ConfigurableListableBeanFactory
  extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory
{
  public abstract void ignoreDependencyType(Class<?> paramClass);
  
  public abstract void ignoreDependencyInterface(Class<?> paramClass);
  
  public abstract void registerResolvableDependency(Class<?> paramClass, Object paramObject);
  
  public abstract boolean isAutowireCandidate(String paramString, DependencyDescriptor paramDependencyDescriptor)
    throws NoSuchBeanDefinitionException;
  
  public abstract BeanDefinition getBeanDefinition(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract Iterator<String> getBeanNamesIterator();
  
  public abstract void clearMetadataCache();
  
  public abstract void freezeConfiguration();
  
  public abstract boolean isConfigurationFrozen();
  
  public abstract void preInstantiateSingletons()
    throws BeansException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\ConfigurableListableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */