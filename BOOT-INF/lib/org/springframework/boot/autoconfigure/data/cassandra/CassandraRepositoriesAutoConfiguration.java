package org.springframework.boot.autoconfigure.data.cassandra;

import com.datastax.driver.core.Session;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.support.CassandraRepositoryFactoryBean;

@Configuration
@ConditionalOnClass({Session.class, CassandraRepository.class})
@ConditionalOnProperty(prefix="spring.data.cassandra.repositories", name={"enabled"}, havingValue="true", matchIfMissing=true)
@ConditionalOnMissingBean({CassandraRepositoryFactoryBean.class})
@Import({CassandraRepositoriesAutoConfigureRegistrar.class})
public class CassandraRepositoriesAutoConfiguration {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\cassandra\CassandraRepositoriesAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */