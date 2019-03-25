package org.springframework.core.env;

public abstract interface PropertySources
  extends Iterable<PropertySource<?>>
{
  public abstract boolean contains(String paramString);
  
  public abstract PropertySource<?> get(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\PropertySources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */