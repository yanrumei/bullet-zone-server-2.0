package org.springframework.core.env;

import java.util.Map;

public abstract interface ConfigurableEnvironment
  extends Environment, ConfigurablePropertyResolver
{
  public abstract void setActiveProfiles(String... paramVarArgs);
  
  public abstract void addActiveProfile(String paramString);
  
  public abstract void setDefaultProfiles(String... paramVarArgs);
  
  public abstract MutablePropertySources getPropertySources();
  
  public abstract Map<String, Object> getSystemEnvironment();
  
  public abstract Map<String, Object> getSystemProperties();
  
  public abstract void merge(ConfigurableEnvironment paramConfigurableEnvironment);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\ConfigurableEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */