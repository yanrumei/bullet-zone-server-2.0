package org.springframework.beans.factory.config;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

public abstract interface ConfigurableBeanFactory
  extends HierarchicalBeanFactory, SingletonBeanRegistry
{
  public static final String SCOPE_SINGLETON = "singleton";
  public static final String SCOPE_PROTOTYPE = "prototype";
  
  public abstract void setParentBeanFactory(BeanFactory paramBeanFactory)
    throws IllegalStateException;
  
  public abstract void setBeanClassLoader(ClassLoader paramClassLoader);
  
  public abstract ClassLoader getBeanClassLoader();
  
  public abstract void setTempClassLoader(ClassLoader paramClassLoader);
  
  public abstract ClassLoader getTempClassLoader();
  
  public abstract void setCacheBeanMetadata(boolean paramBoolean);
  
  public abstract boolean isCacheBeanMetadata();
  
  public abstract void setBeanExpressionResolver(BeanExpressionResolver paramBeanExpressionResolver);
  
  public abstract BeanExpressionResolver getBeanExpressionResolver();
  
  public abstract void setConversionService(ConversionService paramConversionService);
  
  public abstract ConversionService getConversionService();
  
  public abstract void addPropertyEditorRegistrar(PropertyEditorRegistrar paramPropertyEditorRegistrar);
  
  public abstract void registerCustomEditor(Class<?> paramClass, Class<? extends PropertyEditor> paramClass1);
  
  public abstract void copyRegisteredEditorsTo(PropertyEditorRegistry paramPropertyEditorRegistry);
  
  public abstract void setTypeConverter(TypeConverter paramTypeConverter);
  
  public abstract TypeConverter getTypeConverter();
  
  public abstract void addEmbeddedValueResolver(StringValueResolver paramStringValueResolver);
  
  public abstract boolean hasEmbeddedValueResolver();
  
  public abstract String resolveEmbeddedValue(String paramString);
  
  public abstract void addBeanPostProcessor(BeanPostProcessor paramBeanPostProcessor);
  
  public abstract int getBeanPostProcessorCount();
  
  public abstract void registerScope(String paramString, Scope paramScope);
  
  public abstract String[] getRegisteredScopeNames();
  
  public abstract Scope getRegisteredScope(String paramString);
  
  public abstract AccessControlContext getAccessControlContext();
  
  public abstract void copyConfigurationFrom(ConfigurableBeanFactory paramConfigurableBeanFactory);
  
  public abstract void registerAlias(String paramString1, String paramString2)
    throws BeanDefinitionStoreException;
  
  public abstract void resolveAliases(StringValueResolver paramStringValueResolver);
  
  public abstract BeanDefinition getMergedBeanDefinition(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract boolean isFactoryBean(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract void setCurrentlyInCreation(String paramString, boolean paramBoolean);
  
  public abstract boolean isCurrentlyInCreation(String paramString);
  
  public abstract void registerDependentBean(String paramString1, String paramString2);
  
  public abstract String[] getDependentBeans(String paramString);
  
  public abstract String[] getDependenciesForBean(String paramString);
  
  public abstract void destroyBean(String paramString, Object paramObject);
  
  public abstract void destroyScopedBean(String paramString);
  
  public abstract void destroySingletons();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\ConfigurableBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */