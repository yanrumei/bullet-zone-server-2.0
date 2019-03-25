package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public abstract interface ObjectProvider<T>
  extends ObjectFactory<T>
{
  public abstract T getObject(Object... paramVarArgs)
    throws BeansException;
  
  public abstract T getIfAvailable()
    throws BeansException;
  
  public abstract T getIfUnique()
    throws BeansException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\ObjectProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */