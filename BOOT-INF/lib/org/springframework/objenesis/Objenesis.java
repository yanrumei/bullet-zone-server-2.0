package org.springframework.objenesis;

import org.springframework.objenesis.instantiator.ObjectInstantiator;

public abstract interface Objenesis
{
  public abstract <T> T newInstance(Class<T> paramClass);
  
  public abstract <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\Objenesis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */