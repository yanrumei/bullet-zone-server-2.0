package org.springframework.boot.autoconfigure.data.ldap;

import javax.naming.ldap.LdapContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.data.ldap.repository.support.LdapRepositoryFactoryBean;

@Configuration
@ConditionalOnClass({LdapContext.class, LdapRepository.class})
@ConditionalOnProperty(prefix="spring.data.ldap.repositories", name={"enabled"}, havingValue="true", matchIfMissing=true)
@ConditionalOnMissingBean({LdapRepositoryFactoryBean.class})
@Import({LdapRepositoriesRegistrar.class})
public class LdapRepositoriesAutoConfiguration {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\ldap\LdapRepositoriesAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */