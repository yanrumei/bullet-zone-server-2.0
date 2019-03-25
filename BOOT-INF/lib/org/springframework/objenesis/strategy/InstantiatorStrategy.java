package org.springframework.objenesis.strategy;

import org.springframework.objenesis.instantiator.ObjectInstantiator;

public abstract interface InstantiatorStrategy
{
  public abstract <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\strategy\InstantiatorStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */