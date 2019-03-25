/*     */ package org.springframework.boot.autoconfigure.data.redis;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPool;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.data.redis.connection.RedisClusterConfiguration;
/*     */ import org.springframework.data.redis.connection.RedisConnectionFactory;
/*     */ import org.springframework.data.redis.connection.RedisNode;
/*     */ import org.springframework.data.redis.connection.RedisSentinelConfiguration;
/*     */ import org.springframework.data.redis.connection.jedis.JedisConnection;
/*     */ import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
/*     */ import org.springframework.data.redis.core.RedisOperations;
/*     */ import org.springframework.data.redis.core.RedisTemplate;
/*     */ import org.springframework.data.redis.core.StringRedisTemplate;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import redis.clients.jedis.Jedis;
/*     */ import redis.clients.jedis.JedisPoolConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({JedisConnection.class, RedisOperations.class, Jedis.class})
/*     */ @EnableConfigurationProperties({RedisProperties.class})
/*     */ public class RedisAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnClass({GenericObjectPool.class})
/*     */   protected static class RedisConnectionConfiguration
/*     */   {
/*     */     private final RedisProperties properties;
/*     */     private final RedisSentinelConfiguration sentinelConfiguration;
/*     */     private final RedisClusterConfiguration clusterConfiguration;
/*     */     
/*     */     public RedisConnectionConfiguration(RedisProperties properties, ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration, ObjectProvider<RedisClusterConfiguration> clusterConfiguration)
/*     */     {
/*  83 */       this.properties = properties;
/*  84 */       this.sentinelConfiguration = ((RedisSentinelConfiguration)sentinelConfiguration.getIfAvailable());
/*  85 */       this.clusterConfiguration = ((RedisClusterConfiguration)clusterConfiguration.getIfAvailable());
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({RedisConnectionFactory.class})
/*     */     public JedisConnectionFactory redisConnectionFactory() throws UnknownHostException
/*     */     {
/*  92 */       return applyProperties(createJedisConnectionFactory());
/*     */     }
/*     */     
/*     */     protected final JedisConnectionFactory applyProperties(JedisConnectionFactory factory)
/*     */     {
/*  97 */       configureConnection(factory);
/*  98 */       if (this.properties.isSsl()) {
/*  99 */         factory.setUseSsl(true);
/*     */       }
/* 101 */       factory.setDatabase(this.properties.getDatabase());
/* 102 */       if (this.properties.getTimeout() > 0) {
/* 103 */         factory.setTimeout(this.properties.getTimeout());
/*     */       }
/* 105 */       return factory;
/*     */     }
/*     */     
/*     */     private void configureConnection(JedisConnectionFactory factory) {
/* 109 */       if (StringUtils.hasText(this.properties.getUrl())) {
/* 110 */         configureConnectionFromUrl(factory);
/*     */       }
/*     */       else {
/* 113 */         factory.setHostName(this.properties.getHost());
/* 114 */         factory.setPort(this.properties.getPort());
/* 115 */         if (this.properties.getPassword() != null) {
/* 116 */           factory.setPassword(this.properties.getPassword());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void configureConnectionFromUrl(JedisConnectionFactory factory) {
/* 122 */       String url = this.properties.getUrl();
/* 123 */       if (url.startsWith("rediss://")) {
/* 124 */         factory.setUseSsl(true);
/*     */       }
/*     */       try {
/* 127 */         URI uri = new URI(url);
/* 128 */         factory.setHostName(uri.getHost());
/* 129 */         factory.setPort(uri.getPort());
/* 130 */         if (uri.getUserInfo() != null) {
/* 131 */           String password = uri.getUserInfo();
/* 132 */           int index = password.indexOf(":");
/* 133 */           if (index >= 0) {
/* 134 */             password = password.substring(index + 1);
/*     */           }
/* 136 */           factory.setPassword(password);
/*     */         }
/*     */       }
/*     */       catch (URISyntaxException ex) {
/* 140 */         throw new IllegalArgumentException("Malformed 'spring.redis.url' " + url, ex);
/*     */       }
/*     */     }
/*     */     
/*     */     protected final RedisSentinelConfiguration getSentinelConfig()
/*     */     {
/* 146 */       if (this.sentinelConfiguration != null) {
/* 147 */         return this.sentinelConfiguration;
/*     */       }
/* 149 */       RedisProperties.Sentinel sentinelProperties = this.properties.getSentinel();
/* 150 */       if (sentinelProperties != null) {
/* 151 */         RedisSentinelConfiguration config = new RedisSentinelConfiguration();
/* 152 */         config.master(sentinelProperties.getMaster());
/* 153 */         config.setSentinels(createSentinels(sentinelProperties));
/* 154 */         return config;
/*     */       }
/* 156 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final RedisClusterConfiguration getClusterConfiguration()
/*     */     {
/* 164 */       if (this.clusterConfiguration != null) {
/* 165 */         return this.clusterConfiguration;
/*     */       }
/* 167 */       if (this.properties.getCluster() == null) {
/* 168 */         return null;
/*     */       }
/* 170 */       RedisProperties.Cluster clusterProperties = this.properties.getCluster();
/*     */       
/* 172 */       RedisClusterConfiguration config = new RedisClusterConfiguration(clusterProperties.getNodes());
/*     */       
/* 174 */       if (clusterProperties.getMaxRedirects() != null) {
/* 175 */         config.setMaxRedirects(clusterProperties.getMaxRedirects().intValue());
/*     */       }
/* 177 */       return config;
/*     */     }
/*     */     
/*     */     private List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {
/* 181 */       List<RedisNode> nodes = new ArrayList();
/* 182 */       for (String node : StringUtils.commaDelimitedListToStringArray(sentinel.getNodes())) {
/*     */         try {
/* 185 */           String[] parts = StringUtils.split(node, ":");
/* 186 */           Assert.state(parts.length == 2, "Must be defined as 'host:port'");
/* 187 */           nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1]).intValue()));
/*     */         }
/*     */         catch (RuntimeException ex) {
/* 190 */           throw new IllegalStateException("Invalid redis sentinel property '" + node + "'", ex);
/*     */         }
/*     */       }
/*     */       
/* 194 */       return nodes;
/*     */     }
/*     */     
/*     */     private JedisConnectionFactory createJedisConnectionFactory()
/*     */     {
/* 199 */       JedisPoolConfig poolConfig = this.properties.getPool() != null ? jedisPoolConfig() : new JedisPoolConfig();
/*     */       
/* 201 */       if (getSentinelConfig() != null) {
/* 202 */         return new JedisConnectionFactory(getSentinelConfig(), poolConfig);
/*     */       }
/* 204 */       if (getClusterConfiguration() != null) {
/* 205 */         return new JedisConnectionFactory(getClusterConfiguration(), poolConfig);
/*     */       }
/* 207 */       return new JedisConnectionFactory(poolConfig);
/*     */     }
/*     */     
/*     */     private JedisPoolConfig jedisPoolConfig() {
/* 211 */       JedisPoolConfig config = new JedisPoolConfig();
/* 212 */       RedisProperties.Pool props = this.properties.getPool();
/* 213 */       config.setMaxTotal(props.getMaxActive());
/* 214 */       config.setMaxIdle(props.getMaxIdle());
/* 215 */       config.setMinIdle(props.getMinIdle());
/* 216 */       config.setMaxWaitMillis(props.getMaxWait());
/* 217 */       return config;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   protected static class RedisConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean(name={"redisTemplate"})
/*     */     public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
/*     */       throws UnknownHostException
/*     */     {
/* 233 */       RedisTemplate<Object, Object> template = new RedisTemplate();
/* 234 */       template.setConnectionFactory(redisConnectionFactory);
/* 235 */       return template;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({StringRedisTemplate.class})
/*     */     public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
/*     */       throws UnknownHostException
/*     */     {
/* 243 */       StringRedisTemplate template = new StringRedisTemplate();
/* 244 */       template.setConnectionFactory(redisConnectionFactory);
/* 245 */       return template;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\redis\RedisAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */