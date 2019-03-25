package org.springframework.core.env;

public abstract interface PropertyResolver
{
  public abstract boolean containsProperty(String paramString);
  
  public abstract String getProperty(String paramString);
  
  public abstract String getProperty(String paramString1, String paramString2);
  
  public abstract <T> T getProperty(String paramString, Class<T> paramClass);
  
  public abstract <T> T getProperty(String paramString, Class<T> paramClass, T paramT);
  
  @Deprecated
  public abstract <T> Class<T> getPropertyAsClass(String paramString, Class<T> paramClass);
  
  public abstract String getRequiredProperty(String paramString)
    throws IllegalStateException;
  
  public abstract <T> T getRequiredProperty(String paramString, Class<T> paramClass)
    throws IllegalStateException;
  
  public abstract String resolvePlaceholders(String paramString);
  
  public abstract String resolveRequiredPlaceholders(String paramString)
    throws IllegalArgumentException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\PropertyResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */