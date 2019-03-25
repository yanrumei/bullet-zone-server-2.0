package org.springframework.boot.autoconfigure.security;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@ConditionalOnProperty(prefix="security.basic", name={"enabled"}, havingValue="false")
@ConditionalOnBean({WebSecurityConfigurerAdapter.class})
@ConditionalOnClass({EnableWebSecurity.class})
@ConditionalOnMissingBean({WebSecurityConfiguration.class})
@ConditionalOnWebApplication
@AutoConfigureAfter({SecurityAutoConfiguration.class})
@EnableWebSecurity
public class FallbackWebSecurityAutoConfiguration {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\FallbackWebSecurityAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */