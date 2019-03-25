/*     */ package org.springframework.boot.autoconfigure.jms;
/*     */ 
/*     */ import javax.jms.ConnectionFactory;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.jms.annotation.EnableJms;
/*     */ import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
/*     */ import org.springframework.jms.support.converter.MessageConverter;
/*     */ import org.springframework.jms.support.destination.DestinationResolver;
/*     */ import org.springframework.jms.support.destination.JndiDestinationResolver;
/*     */ import org.springframework.transaction.jta.JtaTransactionManager;
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
/*     */ @ConditionalOnClass({EnableJms.class})
/*     */ class JmsAnnotationDrivenConfiguration
/*     */ {
/*     */   private final ObjectProvider<DestinationResolver> destinationResolver;
/*     */   private final ObjectProvider<JtaTransactionManager> transactionManager;
/*     */   private final ObjectProvider<MessageConverter> messageConverter;
/*     */   private final JmsProperties properties;
/*     */   
/*     */   JmsAnnotationDrivenConfiguration(ObjectProvider<DestinationResolver> destinationResolver, ObjectProvider<JtaTransactionManager> transactionManager, ObjectProvider<MessageConverter> messageConverter, JmsProperties properties)
/*     */   {
/*  58 */     this.destinationResolver = destinationResolver;
/*  59 */     this.transactionManager = transactionManager;
/*  60 */     this.messageConverter = messageConverter;
/*  61 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public DefaultJmsListenerContainerFactoryConfigurer jmsListenerContainerFactoryConfigurer() {
/*  67 */     DefaultJmsListenerContainerFactoryConfigurer configurer = new DefaultJmsListenerContainerFactoryConfigurer();
/*  68 */     configurer.setDestinationResolver((DestinationResolver)this.destinationResolver.getIfUnique());
/*  69 */     configurer.setTransactionManager((JtaTransactionManager)this.transactionManager.getIfUnique());
/*  70 */     configurer.setMessageConverter((MessageConverter)this.messageConverter.getIfUnique());
/*  71 */     configurer.setJmsProperties(this.properties);
/*  72 */     return configurer;
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnMissingBean(name={"jmsListenerContainerFactory"})
/*     */   public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(DefaultJmsListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory)
/*     */   {
/*  80 */     DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
/*  81 */     configurer.configure(factory, connectionFactory);
/*  82 */     return factory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnJndi
/*     */   protected static class JndiConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({DestinationResolver.class})
/*     */     public JndiDestinationResolver destinationResolver()
/*     */     {
/*  99 */       JndiDestinationResolver resolver = new JndiDestinationResolver();
/* 100 */       resolver.setFallbackToDynamicDestination(true);
/* 101 */       return resolver;
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @EnableJms
/*     */   @ConditionalOnMissingBean(name={"org.springframework.jms.config.internalJmsListenerAnnotationProcessor"})
/*     */   protected static class EnableJmsConfiguration {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\JmsAnnotationDrivenConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */