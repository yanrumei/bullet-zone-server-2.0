/*    */ package org.springframework.boot.autoconfigure.jms.artemis;
/*    */ 
/*    */ import javax.jms.ConnectionFactory;
/*    */ import javax.transaction.TransactionManager;
/*    */ import org.apache.activemq.artemis.jms.client.ActiveMQXAConnectionFactory;
/*    */ import org.springframework.beans.factory.ListableBeanFactory;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
/*    */ @Configuration
/*    */ @ConditionalOnMissingBean({ConnectionFactory.class})
/*    */ @ConditionalOnClass({TransactionManager.class})
/*    */ @ConditionalOnBean({XAConnectionFactoryWrapper.class})
/*    */ class ArtemisXAConnectionFactoryConfiguration
/*    */ {
/*    */   @Primary
/*    */   @Bean(name={"jmsConnectionFactory", "xaJmsConnectionFactory"})
/*    */   public ConnectionFactory jmsConnectionFactory(ListableBeanFactory beanFactory, ArtemisProperties properties, XAConnectionFactoryWrapper wrapper)
/*    */     throws Exception
/*    */   {
/* 51 */     return wrapper.wrapConnectionFactory(new ArtemisConnectionFactoryFactory(beanFactory, properties)
/*    */     
/* 53 */       .createConnectionFactory(ActiveMQXAConnectionFactory.class));
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public ActiveMQXAConnectionFactory nonXaJmsConnectionFactory(ListableBeanFactory beanFactory, ArtemisProperties properties)
/*    */   {
/* 59 */     return 
/* 60 */       (ActiveMQXAConnectionFactory)new ArtemisConnectionFactoryFactory(beanFactory, properties).createConnectionFactory(ActiveMQXAConnectionFactory.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\artemis\ArtemisXAConnectionFactoryConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */