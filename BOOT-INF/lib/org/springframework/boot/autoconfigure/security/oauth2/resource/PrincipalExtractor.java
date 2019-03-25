package org.springframework.boot.autoconfigure.security.oauth2.resource;

import java.util.Map;

public abstract interface PrincipalExtractor
{
  public abstract Object extractPrincipal(Map<String, Object> paramMap);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\PrincipalExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */