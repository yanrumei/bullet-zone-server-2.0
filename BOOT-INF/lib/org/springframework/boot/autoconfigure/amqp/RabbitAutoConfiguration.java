/*     */ package org.springframework.boot.autoconfigure.amqp;
/*     */ 
/*     */ import com.rabbitmq.client.Channel;
/*     */ import org.springframework.amqp.core.AmqpAdmin;
/*     */ import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
/*     */ import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
/*     */ import org.springframework.amqp.rabbit.core.RabbitAdmin;
/*     */ import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
/*     */ import org.springframework.amqp.rabbit.core.RabbitTemplate;
/*     */ import org.springframework.amqp.support.converter.MessageConverter;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.retry.backoff.ExponentialBackOffPolicy;
/*     */ import org.springframework.retry.policy.SimpleRetryPolicy;
/*     */ import org.springframework.retry.support.RetryTemplate;
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
/*     */ @ConditionalOnClass({RabbitTemplate.class, Channel.class})
/*     */ @EnableConfigurationProperties({RabbitProperties.class})
/*     */ @Import({RabbitAnnotationDrivenConfiguration.class})
/*     */ public class RabbitAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({org.springframework.amqp.rabbit.connection.ConnectionFactory.class})
/*     */   protected static class RabbitConnectionFactoryCreator
/*     */   {
/*     */     @Bean
/*     */     public CachingConnectionFactory rabbitConnectionFactory(RabbitProperties config)
/*     */       throws Exception
/*     */     {
/*  93 */       RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
/*  94 */       if (config.determineHost() != null) {
/*  95 */         factory.setHost(config.determineHost());
/*     */       }
/*  97 */       factory.setPort(config.determinePort());
/*  98 */       if (config.determineUsername() != null) {
/*  99 */         factory.setUsername(config.determineUsername());
/*     */       }
/* 101 */       if (config.determinePassword() != null) {
/* 102 */         factory.setPassword(config.determinePassword());
/*     */       }
/* 104 */       if (config.determineVirtualHost() != null) {
/* 105 */         factory.setVirtualHost(config.determineVirtualHost());
/*     */       }
/* 107 */       if (config.getRequestedHeartbeat() != null) {
/* 108 */         factory.setRequestedHeartbeat(config.getRequestedHeartbeat().intValue());
/*     */       }
/* 110 */       RabbitProperties.Ssl ssl = config.getSsl();
/* 111 */       if (ssl.isEnabled()) {
/* 112 */         factory.setUseSSL(true);
/* 113 */         if (ssl.getAlgorithm() != null) {
/* 114 */           factory.setSslAlgorithm(ssl.getAlgorithm());
/*     */         }
/* 116 */         factory.setKeyStore(ssl.getKeyStore());
/* 117 */         factory.setKeyStorePassphrase(ssl.getKeyStorePassword());
/* 118 */         factory.setTrustStore(ssl.getTrustStore());
/* 119 */         factory.setTrustStorePassphrase(ssl.getTrustStorePassword());
/*     */       }
/* 121 */       if (config.getConnectionTimeout() != null) {
/* 122 */         factory.setConnectionTimeout(config.getConnectionTimeout().intValue());
/*     */       }
/* 124 */       factory.afterPropertiesSet();
/*     */       
/* 126 */       CachingConnectionFactory connectionFactory = new CachingConnectionFactory((com.rabbitmq.client.ConnectionFactory)factory.getObject());
/* 127 */       connectionFactory.setAddresses(config.determineAddresses());
/* 128 */       connectionFactory.setPublisherConfirms(config.isPublisherConfirms());
/* 129 */       connectionFactory.setPublisherReturns(config.isPublisherReturns());
/* 130 */       if (config.getCache().getChannel().getSize() != null)
/*     */       {
/* 132 */         connectionFactory.setChannelCacheSize(config.getCache().getChannel().getSize().intValue());
/*     */       }
/* 134 */       if (config.getCache().getConnection().getMode() != null)
/*     */       {
/* 136 */         connectionFactory.setCacheMode(config.getCache().getConnection().getMode());
/*     */       }
/* 138 */       if (config.getCache().getConnection().getSize() != null) {
/* 139 */         connectionFactory.setConnectionCacheSize(config
/* 140 */           .getCache().getConnection().getSize().intValue());
/*     */       }
/* 142 */       if (config.getCache().getChannel().getCheckoutTimeout() != null) {
/* 143 */         connectionFactory.setChannelCheckoutTimeout(config
/* 144 */           .getCache().getChannel().getCheckoutTimeout().longValue());
/*     */       }
/* 146 */       return connectionFactory;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @Import({RabbitAutoConfiguration.RabbitConnectionFactoryCreator.class})
/*     */   protected static class RabbitTemplateConfiguration
/*     */   {
/*     */     private final ObjectProvider<MessageConverter> messageConverter;
/*     */     
/*     */     private final RabbitProperties properties;
/*     */     
/*     */ 
/*     */     public RabbitTemplateConfiguration(ObjectProvider<MessageConverter> messageConverter, RabbitProperties properties)
/*     */     {
/* 162 */       this.messageConverter = messageConverter;
/* 163 */       this.properties = properties;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnSingleCandidate(org.springframework.amqp.rabbit.connection.ConnectionFactory.class)
/*     */     @ConditionalOnMissingBean({RabbitTemplate.class})
/*     */     public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
/* 170 */       RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
/* 171 */       MessageConverter messageConverter = (MessageConverter)this.messageConverter.getIfUnique();
/* 172 */       if (messageConverter != null) {
/* 173 */         rabbitTemplate.setMessageConverter(messageConverter);
/*     */       }
/* 175 */       rabbitTemplate.setMandatory(determineMandatoryFlag());
/* 176 */       RabbitProperties.Template templateProperties = this.properties.getTemplate();
/* 177 */       RabbitProperties.Retry retryProperties = templateProperties.getRetry();
/* 178 */       if (retryProperties.isEnabled()) {
/* 179 */         rabbitTemplate.setRetryTemplate(createRetryTemplate(retryProperties));
/*     */       }
/* 181 */       if (templateProperties.getReceiveTimeout() != null) {
/* 182 */         rabbitTemplate.setReceiveTimeout(templateProperties.getReceiveTimeout().longValue());
/*     */       }
/* 184 */       if (templateProperties.getReplyTimeout() != null) {
/* 185 */         rabbitTemplate.setReplyTimeout(templateProperties.getReplyTimeout().longValue());
/*     */       }
/* 187 */       return rabbitTemplate;
/*     */     }
/*     */     
/*     */     private boolean determineMandatoryFlag() {
/* 191 */       Boolean mandatory = this.properties.getTemplate().getMandatory();
/* 192 */       return mandatory != null ? mandatory.booleanValue() : this.properties.isPublisherReturns();
/*     */     }
/*     */     
/*     */     private RetryTemplate createRetryTemplate(RabbitProperties.Retry properties) {
/* 196 */       RetryTemplate template = new RetryTemplate();
/* 197 */       SimpleRetryPolicy policy = new SimpleRetryPolicy();
/* 198 */       policy.setMaxAttempts(properties.getMaxAttempts());
/* 199 */       template.setRetryPolicy(policy);
/* 200 */       ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
/* 201 */       backOffPolicy.setInitialInterval(properties.getInitialInterval());
/* 202 */       backOffPolicy.setMultiplier(properties.getMultiplier());
/* 203 */       backOffPolicy.setMaxInterval(properties.getMaxInterval());
/* 204 */       template.setBackOffPolicy(backOffPolicy);
/* 205 */       return template;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnSingleCandidate(org.springframework.amqp.rabbit.connection.ConnectionFactory.class)
/*     */     @ConditionalOnProperty(prefix="spring.rabbitmq", name={"dynamic"}, matchIfMissing=true)
/*     */     @ConditionalOnMissingBean({AmqpAdmin.class})
/*     */     public AmqpAdmin amqpAdmin(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
/* 213 */       return new RabbitAdmin(connectionFactory);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({RabbitMessagingTemplate.class})
/*     */   @ConditionalOnMissingBean({RabbitMessagingTemplate.class})
/*     */   @Import({RabbitAutoConfiguration.RabbitTemplateConfiguration.class})
/*     */   protected static class MessagingTemplateConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnSingleCandidate(RabbitTemplate.class)
/*     */     public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate)
/*     */     {
/* 228 */       return new RabbitMessagingTemplate(rabbitTemplate);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\amqp\RabbitAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */