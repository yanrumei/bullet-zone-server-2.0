package org.springframework.boot.autoconfigure;

public abstract interface AutoConfigurationImportFilter
{
  public abstract boolean[] match(String[] paramArrayOfString, AutoConfigurationMetadata paramAutoConfigurationMetadata);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationImportFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */