package org.springframework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

public abstract interface ListableBeanFactory
  extends BeanFactory
{
  public abstract boolean containsBeanDefinition(String paramString);
  
  public abstract int getBeanDefinitionCount();
  
  public abstract String[] getBeanDefinitionNames();
  
  public abstract String[] getBeanNamesForType(ResolvableType paramResolvableType);
  
  public abstract String[] getBeanNamesForType(Class<?> paramClass);
  
  public abstract String[] getBeanNamesForType(Class<?> paramClass, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract <T> Map<String, T> getBeansOfType(Class<T> paramClass)
    throws BeansException;
  
  public abstract <T> Map<String, T> getBeansOfType(Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2)
    throws BeansException;
  
  public abstract String[] getBeanNamesForAnnotation(Class<? extends Annotation> paramClass);
  
  public abstract Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> paramClass)
    throws BeansException;
  
  public abstract <A extends Annotation> A findAnnotationOnBean(String paramString, Class<A> paramClass)
    throws NoSuchBeanDefinitionException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\ListableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */