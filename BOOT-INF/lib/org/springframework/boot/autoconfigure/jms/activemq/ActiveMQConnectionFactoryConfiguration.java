/*    */ package org.springframework.boot.autoconfigure.jms.activemq;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.jms.ConnectionFactory;
/*    */ import org.apache.activemq.ActiveMQConnectionFactory;
/*    */ import org.apache.activemq.pool.PooledConnectionFactory;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnMissingBean({ConnectionFactory.class})
/*    */ class ActiveMQConnectionFactoryConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnProperty(prefix="spring.activemq.pool", name={"enabled"}, havingValue="false", matchIfMissing=true)
/*    */   public ActiveMQConnectionFactory jmsConnectionFactory(ActiveMQProperties properties, ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers)
/*    */   {
/* 52 */     return 
/*    */     
/* 54 */       new ActiveMQConnectionFactoryFactory(properties, (List)factoryCustomizers.getIfAvailable()).createConnectionFactory(ActiveMQConnectionFactory.class);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @Configuration
/*    */   @ConditionalOnClass({PooledConnectionFactory.class})
/*    */   static class PooledConnectionFactoryConfiguration
/*    */   {
/*    */     @Bean(destroyMethod="stop")
/*    */     @ConditionalOnProperty(prefix="spring.activemq.pool", name={"enabled"}, havingValue="true", matchIfMissing=false)
/*    */     @ConfigurationProperties(prefix="spring.activemq.pool.configuration")
/*    */     public PooledConnectionFactory pooledJmsConnectionFactory(ActiveMQProperties properties, ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers)
/*    */     {
/* 69 */       PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(new ActiveMQConnectionFactoryFactory(properties, (List)factoryCustomizers.getIfAvailable()).createConnectionFactory(ActiveMQConnectionFactory.class));
/*    */       
/* 71 */       ActiveMQProperties.Pool pool = properties.getPool();
/* 72 */       pooledConnectionFactory.setBlockIfSessionPoolIsFull(pool.isBlockIfFull());
/* 73 */       pooledConnectionFactory
/* 74 */         .setBlockIfSessionPoolIsFullTimeout(pool.getBlockIfFullTimeout());
/* 75 */       pooledConnectionFactory
/* 76 */         .setCreateConnectionOnStartup(pool.isCreateConnectionOnStartup());
/* 77 */       pooledConnectionFactory.setExpiryTimeout(pool.getExpiryTimeout());
/* 78 */       pooledConnectionFactory.setIdleTimeout(pool.getIdleTimeout());
/* 79 */       pooledConnectionFactory.setMaxConnections(pool.getMaxConnections());
/* 80 */       pooledConnectionFactory.setMaximumActiveSessionPerConnection(pool
/* 81 */         .getMaximumActiveSessionPerConnection());
/* 82 */       pooledConnectionFactory
/* 83 */         .setReconnectOnException(pool.isReconnectOnException());
/* 84 */       pooledConnectionFactory.setTimeBetweenExpirationCheckMillis(pool
/* 85 */         .getTimeBetweenExpirationCheck());
/* 86 */       pooledConnectionFactory
/* 87 */         .setUseAnonymousProducers(pool.isUseAnonymousProducers());
/* 88 */       return pooledConnectionFactory;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\activemq\ActiveMQConnectionFactoryConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */