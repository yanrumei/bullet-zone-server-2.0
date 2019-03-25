package org.springframework.beans.factory.access;

import org.springframework.beans.factory.BeanFactory;

public abstract interface BeanFactoryReference
{
  public abstract BeanFactory getFactory();
  
  public abstract void release();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\access\BeanFactoryReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */