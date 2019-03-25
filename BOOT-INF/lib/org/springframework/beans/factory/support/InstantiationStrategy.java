package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public abstract interface InstantiationStrategy
{
  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory)
    throws BeansException;
  
  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory, Constructor<?> paramConstructor, Object... paramVarArgs)
    throws BeansException;
  
  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory, Object paramObject, Method paramMethod, Object... paramVarArgs)
    throws BeansException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\InstantiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */