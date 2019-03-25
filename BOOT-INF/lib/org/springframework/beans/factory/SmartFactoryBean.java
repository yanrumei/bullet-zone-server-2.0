package org.springframework.beans.factory;

public abstract interface SmartFactoryBean<T>
  extends FactoryBean<T>
{
  public abstract boolean isPrototype();
  
  public abstract boolean isEagerInit();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\SmartFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */