package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public abstract interface BeanFactoryAware
  extends Aware
{
  public abstract void setBeanFactory(BeanFactory paramBeanFactory)
    throws BeansException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\BeanFactoryAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */