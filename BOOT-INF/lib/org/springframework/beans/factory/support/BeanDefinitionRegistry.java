package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

public abstract interface BeanDefinitionRegistry
  extends AliasRegistry
{
  public abstract void registerBeanDefinition(String paramString, BeanDefinition paramBeanDefinition)
    throws BeanDefinitionStoreException;
  
  public abstract void removeBeanDefinition(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract BeanDefinition getBeanDefinition(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract boolean containsBeanDefinition(String paramString);
  
  public abstract String[] getBeanDefinitionNames();
  
  public abstract int getBeanDefinitionCount();
  
  public abstract boolean isBeanNameInUse(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\BeanDefinitionRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */