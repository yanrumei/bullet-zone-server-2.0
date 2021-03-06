package org.springframework.beans.factory.config;

import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;

public abstract interface AutowireCapableBeanFactory
  extends BeanFactory
{
  public static final int AUTOWIRE_NO = 0;
  public static final int AUTOWIRE_BY_NAME = 1;
  public static final int AUTOWIRE_BY_TYPE = 2;
  public static final int AUTOWIRE_CONSTRUCTOR = 3;
  @Deprecated
  public static final int AUTOWIRE_AUTODETECT = 4;
  
  public abstract <T> T createBean(Class<T> paramClass)
    throws BeansException;
  
  public abstract void autowireBean(Object paramObject)
    throws BeansException;
  
  public abstract Object configureBean(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object createBean(Class<?> paramClass, int paramInt, boolean paramBoolean)
    throws BeansException;
  
  public abstract Object autowire(Class<?> paramClass, int paramInt, boolean paramBoolean)
    throws BeansException;
  
  public abstract void autowireBeanProperties(Object paramObject, int paramInt, boolean paramBoolean)
    throws BeansException;
  
  public abstract void applyBeanPropertyValues(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object initializeBean(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object applyBeanPostProcessorsBeforeInitialization(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object applyBeanPostProcessorsAfterInitialization(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract void destroyBean(Object paramObject);
  
  public abstract <T> NamedBeanHolder<T> resolveNamedBean(Class<T> paramClass)
    throws BeansException;
  
  public abstract Object resolveDependency(DependencyDescriptor paramDependencyDescriptor, String paramString)
    throws BeansException;
  
  public abstract Object resolveDependency(DependencyDescriptor paramDependencyDescriptor, String paramString, Set<String> paramSet, TypeConverter paramTypeConverter)
    throws BeansException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\AutowireCapableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */