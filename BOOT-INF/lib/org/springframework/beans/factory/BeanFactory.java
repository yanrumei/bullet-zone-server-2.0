package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

public abstract interface BeanFactory
{
  public static final String FACTORY_BEAN_PREFIX = "&";
  
  public abstract Object getBean(String paramString)
    throws BeansException;
  
  public abstract <T> T getBean(String paramString, Class<T> paramClass)
    throws BeansException;
  
  public abstract Object getBean(String paramString, Object... paramVarArgs)
    throws BeansException;
  
  public abstract <T> T getBean(Class<T> paramClass)
    throws BeansException;
  
  public abstract <T> T getBean(Class<T> paramClass, Object... paramVarArgs)
    throws BeansException;
  
  public abstract boolean containsBean(String paramString);
  
  public abstract boolean isSingleton(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract boolean isPrototype(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract boolean isTypeMatch(String paramString, ResolvableType paramResolvableType)
    throws NoSuchBeanDefinitionException;
  
  public abstract boolean isTypeMatch(String paramString, Class<?> paramClass)
    throws NoSuchBeanDefinitionException;
  
  public abstract Class<?> getType(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract String[] getAliases(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\BeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */