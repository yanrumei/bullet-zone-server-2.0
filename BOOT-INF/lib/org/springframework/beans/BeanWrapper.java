package org.springframework.beans;

import java.beans.PropertyDescriptor;

public abstract interface BeanWrapper
  extends ConfigurablePropertyAccessor
{
  public abstract void setAutoGrowCollectionLimit(int paramInt);
  
  public abstract int getAutoGrowCollectionLimit();
  
  public abstract Object getWrappedInstance();
  
  public abstract Class<?> getWrappedClass();
  
  public abstract PropertyDescriptor[] getPropertyDescriptors();
  
  public abstract PropertyDescriptor getPropertyDescriptor(String paramString)
    throws InvalidPropertyException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\BeanWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */