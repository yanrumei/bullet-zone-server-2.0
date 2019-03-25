package org.springframework.boot.autoconfigure.data.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.repository.support.RedisRepositoryFactoryBean;
import redis.clients.jedis.Jedis;

@Configuration
@ConditionalOnClass({Jedis.class, EnableRedisRepositories.class})
@ConditionalOnProperty(prefix="spring.data.redis.repositories", name={"enabled"}, havingValue="true", matchIfMissing=true)
@ConditionalOnMissingBean({RedisRepositoryFactoryBean.class})
@Import({RedisRepositoriesAutoConfigureRegistrar.class})
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class RedisRepositoriesAutoConfiguration {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\redis\RedisRepositoriesAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */