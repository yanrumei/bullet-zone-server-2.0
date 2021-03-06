package org.springframework.core.env;

public abstract interface Environment
  extends PropertyResolver
{
  public abstract String[] getActiveProfiles();
  
  public abstract String[] getDefaultProfiles();
  
  public abstract boolean acceptsProfiles(String... paramVarArgs);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\Environment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */