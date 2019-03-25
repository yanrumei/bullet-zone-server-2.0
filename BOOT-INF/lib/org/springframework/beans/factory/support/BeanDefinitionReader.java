package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract interface BeanDefinitionReader
{
  public abstract BeanDefinitionRegistry getRegistry();
  
  public abstract ResourceLoader getResourceLoader();
  
  public abstract ClassLoader getBeanClassLoader();
  
  public abstract BeanNameGenerator getBeanNameGenerator();
  
  public abstract int loadBeanDefinitions(Resource paramResource)
    throws BeanDefinitionStoreException;
  
  public abstract int loadBeanDefinitions(Resource... paramVarArgs)
    throws BeanDefinitionStoreException;
  
  public abstract int loadBeanDefinitions(String paramString)
    throws BeanDefinitionStoreException;
  
  public abstract int loadBeanDefinitions(String... paramVarArgs)
    throws BeanDefinitionStoreException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\BeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */