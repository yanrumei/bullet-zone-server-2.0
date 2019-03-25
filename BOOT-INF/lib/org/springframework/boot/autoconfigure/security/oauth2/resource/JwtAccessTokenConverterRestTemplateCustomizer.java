package org.springframework.boot.autoconfigure.security.oauth2.resource;

import org.springframework.web.client.RestTemplate;

public abstract interface JwtAccessTokenConverterRestTemplateCustomizer
{
  public abstract void customize(RestTemplate paramRestTemplate);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\JwtAccessTokenConverterRestTemplateCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */