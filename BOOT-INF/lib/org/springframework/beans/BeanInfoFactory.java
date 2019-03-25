package org.springframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public abstract interface BeanInfoFactory
{
  public abstract BeanInfo getBeanInfo(Class<?> paramClass)
    throws IntrospectionException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\BeanInfoFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */