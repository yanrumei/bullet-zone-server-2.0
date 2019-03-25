package org.springframework.boot.autoconfigure;

import java.util.Set;

public abstract interface AutoConfigurationMetadata
{
  public abstract boolean wasProcessed(String paramString);
  
  public abstract Integer getInteger(String paramString1, String paramString2);
  
  public abstract Integer getInteger(String paramString1, String paramString2, Integer paramInteger);
  
  public abstract Set<String> getSet(String paramString1, String paramString2);
  
  public abstract Set<String> getSet(String paramString1, String paramString2, Set<String> paramSet);
  
  public abstract String get(String paramString1, String paramString2);
  
  public abstract String get(String paramString1, String paramString2, String paramString3);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */