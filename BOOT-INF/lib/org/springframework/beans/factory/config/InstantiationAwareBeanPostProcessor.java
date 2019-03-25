package org.springframework.beans.factory.config;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

public abstract interface InstantiationAwareBeanPostProcessor
  extends BeanPostProcessor
{
  public abstract Object postProcessBeforeInstantiation(Class<?> paramClass, String paramString)
    throws BeansException;
  
  public abstract boolean postProcessAfterInstantiation(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract PropertyValues postProcessPropertyValues(PropertyValues paramPropertyValues, PropertyDescriptor[] paramArrayOfPropertyDescriptor, Object paramObject, String paramString)
    throws BeansException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\InstantiationAwareBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */