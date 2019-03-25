package org.springframework.beans.factory.config;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeansException;

public abstract interface SmartInstantiationAwareBeanPostProcessor
  extends InstantiationAwareBeanPostProcessor
{
  public abstract Class<?> predictBeanType(Class<?> paramClass, String paramString)
    throws BeansException;
  
  public abstract Constructor<?>[] determineCandidateConstructors(Class<?> paramClass, String paramString)
    throws BeansException;
  
  public abstract Object getEarlyBeanReference(Object paramObject, String paramString)
    throws BeansException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\SmartInstantiationAwareBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */