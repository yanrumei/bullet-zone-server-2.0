package org.springframework.core;

public abstract interface AliasRegistry
{
  public abstract void registerAlias(String paramString1, String paramString2);
  
  public abstract void removeAlias(String paramString);
  
  public abstract boolean isAlias(String paramString);
  
  public abstract String[] getAliases(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\AliasRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */