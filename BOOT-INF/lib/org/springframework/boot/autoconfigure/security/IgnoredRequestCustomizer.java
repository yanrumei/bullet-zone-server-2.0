package org.springframework.boot.autoconfigure.security;

import org.springframework.security.config.annotation.web.builders.WebSecurity.IgnoredRequestConfigurer;

public abstract interface IgnoredRequestCustomizer
{
  public abstract void customize(WebSecurity.IgnoredRequestConfigurer paramIgnoredRequestConfigurer);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\IgnoredRequestCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */