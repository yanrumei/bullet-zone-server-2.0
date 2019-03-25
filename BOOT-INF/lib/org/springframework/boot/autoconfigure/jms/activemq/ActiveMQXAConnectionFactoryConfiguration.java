/*    */ package org.springframework.boot.autoconfigure.jms.activemq;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.jms.ConnectionFactory;
/*    */ import javax.transaction.TransactionManager;
/*    */ import org.apache.activemq.ActiveMQConnectionFactory;
/*    */ import org.apache.activemq.ActiveMQXAConnectionFactory;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.jta.XAConnectionFactoryWrapper;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Primary;
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
/*    */ @ConditionalOnClass({TransactionManager.class})
/*    */ @ConditionalOnBean({XAConnectionFactoryWrapper.class})
/*    */ @ConditionalOnMissingBean({ConnectionFactory.class})
/*    */ class ActiveMQXAConnectionFactoryConfiguration
/*    */ {
/*    */   @Primary
/*    */   @Bean(name={"jmsConnectionFactory", "xaJmsConnectionFactory"})
/*    */   public ConnectionFactory jmsConnectionFactory(ActiveMQProperties properties, ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers, XAConnectionFactoryWrapper wrapper)
/*    */     throws Exception
/*    */   {
/* 57 */     ActiveMQXAConnectionFactory connectionFactory = (ActiveMQXAConnectionFactory)new ActiveMQConnectionFactoryFactory(properties, (List)factoryCustomizers.getIfAvailable()).createConnectionFactory(ActiveMQXAConnectionFactory.class);
/* 58 */     return wrapper.wrapConnectionFactory(connectionFactory);
/*    */   }
/*    */   
/*    */ 
/*    */   @Bean
/*    */   @ConditionalOnProperty(prefix="spring.activemq.pool", name={"enabled"}, havingValue="false", matchIfMissing=true)
/*    */   public ActiveMQConnectionFactory nonXaJmsConnectionFactory(ActiveMQProperties properties, ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers)
/*    */   {
/* 66 */     return 
/*    */     
/* 68 */       new ActiveMQConnectionFactoryFactory(properties, (List)factoryCustomizers.getIfAvailable()).createConnectionFactory(ActiveMQConnectionFactory.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\activemq\ActiveMQXAConnectionFactoryConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */