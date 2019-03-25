package org.springframework.boot.autoconfigure.data.elasticsearch;

import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;

@Configuration
@ConditionalOnClass({Client.class, ElasticsearchRepository.class})
@ConditionalOnProperty(prefix="spring.data.elasticsearch.repositories", name={"enabled"}, havingValue="true", matchIfMissing=true)
@ConditionalOnMissingBean({ElasticsearchRepositoryFactoryBean.class})
@Import({ElasticsearchRepositoriesRegistrar.class})
public class ElasticsearchRepositoriesAutoConfiguration {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\elasticsearch\ElasticsearchRepositoriesAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */