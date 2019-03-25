package org.springframework.beans.factory;

public abstract interface FactoryBean<T>
{
  public abstract T getObject()
    throws Exception;
  
  public abstract Class<?> getObjectType();
  
  public abstract boolean isSingleton();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\FactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */