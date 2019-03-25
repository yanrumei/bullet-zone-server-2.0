/*    */ package org.springframework.boot.autoconfigure.amqp;
/*    */ 
/*    */ import org.springframework.amqp.rabbit.annotation.EnableRabbit;
/*    */ import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
/*    */ import org.springframework.amqp.rabbit.connection.ConnectionFactory;
/*    */ import org.springframework.amqp.rabbit.retry.MessageRecoverer;
/*    */ import org.springframework.amqp.support.converter.MessageConverter;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({EnableRabbit.class})
/*    */ class RabbitAnnotationDrivenConfiguration
/*    */ {
/*    */   private final ObjectProvider<MessageConverter> messageConverter;
/*    */   private final ObjectProvider<MessageRecoverer> messageRecoverer;
/*    */   private final RabbitProperties properties;
/*    */   
/*    */   RabbitAnnotationDrivenConfiguration(ObjectProvider<MessageConverter> messageConverter, ObjectProvider<MessageRecoverer> messageRecoverer, RabbitProperties properties)
/*    */   {
/* 51 */     this.messageConverter = messageConverter;
/* 52 */     this.messageRecoverer = messageRecoverer;
/* 53 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public SimpleRabbitListenerContainerFactoryConfigurer rabbitListenerContainerFactoryConfigurer() {
/* 59 */     SimpleRabbitListenerContainerFactoryConfigurer configurer = new SimpleRabbitListenerContainerFactoryConfigurer();
/* 60 */     configurer.setMessageConverter((MessageConverter)this.messageConverter.getIfUnique());
/* 61 */     configurer.setMessageRecoverer((MessageRecoverer)this.messageRecoverer.getIfUnique());
/* 62 */     configurer.setRabbitProperties(this.properties);
/* 63 */     return configurer;
/*    */   }
/*    */   
/*    */ 
/*    */   @Bean
/*    */   @ConditionalOnMissingBean(name={"rabbitListenerContainerFactory"})
/*    */   public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory)
/*    */   {
/* 71 */     SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
/* 72 */     configurer.configure(factory, connectionFactory);
/* 73 */     return factory;
/*    */   }
/*    */   
/*    */   @EnableRabbit
/*    */   @ConditionalOnMissingBean(name={"org.springframework.amqp.rabbit.config.internalRabbitListenerAnnotationProcessor"})
/*    */   protected static class EnableRabbitConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\amqp\RabbitAnnotationDrivenConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */