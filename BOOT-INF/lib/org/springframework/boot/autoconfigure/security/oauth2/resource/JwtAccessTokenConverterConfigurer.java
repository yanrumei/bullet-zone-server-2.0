package org.springframework.boot.autoconfigure.security.oauth2.resource;

import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public abstract interface JwtAccessTokenConverterConfigurer
{
  public abstract void configure(JwtAccessTokenConverter paramJwtAccessTokenConverter);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\JwtAccessTokenConverterConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */