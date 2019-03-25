package org.springframework.boot.autoconfigure.data.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.solr.repository.SolrRepository;
import org.springframework.data.solr.repository.config.SolrRepositoryConfigExtension;
import org.springframework.data.solr.repository.support.SolrRepositoryFactoryBean;

@Configuration
@ConditionalOnClass({SolrClient.class, SolrRepository.class})
@ConditionalOnMissingBean({SolrRepositoryFactoryBean.class, SolrRepositoryConfigExtension.class})
@ConditionalOnProperty(prefix="spring.data.solr.repositories", name={"enabled"}, havingValue="true", matchIfMissing=true)
@Import({SolrRepositoriesRegistrar.class})
public class SolrRepositoriesAutoConfiguration {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\solr\SolrRepositoriesAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */