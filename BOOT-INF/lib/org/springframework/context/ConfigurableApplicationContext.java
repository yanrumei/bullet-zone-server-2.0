package org.springframework.context;

import java.io.Closeable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ProtocolResolver;

public abstract interface ConfigurableApplicationContext
  extends ApplicationContext, Lifecycle, Closeable
{
  public static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
  public static final String CONVERSION_SERVICE_BEAN_NAME = "conversionService";
  public static final String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";
  public static final String ENVIRONMENT_BEAN_NAME = "environment";
  public static final String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";
  public static final String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";
  
  public abstract void setId(String paramString);
  
  public abstract void setParent(ApplicationContext paramApplicationContext);
  
  public abstract void setEnvironment(ConfigurableEnvironment paramConfigurableEnvironment);
  
  public abstract ConfigurableEnvironment getEnvironment();
  
  public abstract void addBeanFactoryPostProcessor(BeanFactoryPostProcessor paramBeanFactoryPostProcessor);
  
  public abstract void addApplicationListener(ApplicationListener<?> paramApplicationListener);
  
  public abstract void addProtocolResolver(ProtocolResolver paramProtocolResolver);
  
  public abstract void refresh()
    throws BeansException, IllegalStateException;
  
  public abstract void registerShutdownHook();
  
  public abstract void close();
  
  public abstract boolean isActive();
  
  public abstract ConfigurableListableBeanFactory getBeanFactory()
    throws IllegalStateException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\ConfigurableApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */