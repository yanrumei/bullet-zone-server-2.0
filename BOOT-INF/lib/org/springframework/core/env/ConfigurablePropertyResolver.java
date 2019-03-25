package org.springframework.core.env;

import org.springframework.core.convert.support.ConfigurableConversionService;

public abstract interface ConfigurablePropertyResolver
  extends PropertyResolver
{
  public abstract ConfigurableConversionService getConversionService();
  
  public abstract void setConversionService(ConfigurableConversionService paramConfigurableConversionService);
  
  public abstract void setPlaceholderPrefix(String paramString);
  
  public abstract void setPlaceholderSuffix(String paramString);
  
  public abstract void setValueSeparator(String paramString);
  
  public abstract void setIgnoreUnresolvableNestedPlaceholders(boolean paramBoolean);
  
  public abstract void setRequiredProperties(String... paramVarArgs);
  
  public abstract void validateRequiredProperties()
    throws MissingRequiredPropertiesException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\ConfigurablePropertyResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */