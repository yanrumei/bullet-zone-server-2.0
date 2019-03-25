package org.springframework.boot.autoconfigure.security.oauth2.resource;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;

public abstract interface AuthoritiesExtractor
{
  public abstract List<GrantedAuthority> extractAuthorities(Map<String, Object> paramMap);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\AuthoritiesExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */