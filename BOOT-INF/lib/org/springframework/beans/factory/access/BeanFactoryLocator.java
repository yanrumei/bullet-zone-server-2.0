package org.springframework.beans.factory.access;

import org.springframework.beans.BeansException;

public abstract interface BeanFactoryLocator
{
  public abstract BeanFactoryReference useBeanFactory(String paramString)
    throws BeansException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\access\BeanFactoryLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */