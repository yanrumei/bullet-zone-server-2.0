/*    */ package org.springframework.boot.autoconfigure.jms.artemis;
/*    */ 
/*    */ import javax.jms.ConnectionFactory;
/*    */ import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
/*    */ import org.springframework.beans.factory.ListableBeanFactory;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
/*    */ @Configuration
/*    */ @ConditionalOnMissingBean({ConnectionFactory.class})
/*    */ class ArtemisConnectionFactoryConfiguration
/*    */ {
/*    */   @Bean
/*    */   public ActiveMQConnectionFactory jmsConnectionFactory(ListableBeanFactory beanFactory, ArtemisProperties properties)
/*    */   {
/* 41 */     return 
/* 42 */       new ArtemisConnectionFactoryFactory(beanFactory, properties).createConnectionFactory(ActiveMQConnectionFactory.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\artemis\ArtemisConnectionFactoryConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */