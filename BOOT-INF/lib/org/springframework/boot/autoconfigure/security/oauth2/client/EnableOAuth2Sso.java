package org.springframework.boot.autoconfigure.security.oauth2.client;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableOAuth2Client
@EnableConfigurationProperties({OAuth2SsoProperties.class})
@Import({OAuth2SsoDefaultConfiguration.class, OAuth2SsoCustomConfiguration.class, ResourceServerTokenServicesConfiguration.class})
public @interface EnableOAuth2Sso {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\client\EnableOAuth2Sso.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */