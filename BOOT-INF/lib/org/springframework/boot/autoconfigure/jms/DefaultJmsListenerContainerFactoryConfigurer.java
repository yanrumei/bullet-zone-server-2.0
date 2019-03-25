/*     */ package org.springframework.boot.autoconfigure.jms;
/*     */ 
/*     */ import javax.jms.ConnectionFactory;
/*     */ import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
/*     */ import org.springframework.jms.support.converter.MessageConverter;
/*     */ import org.springframework.jms.support.destination.DestinationResolver;
/*     */ import org.springframework.transaction.jta.JtaTransactionManager;
/*     */ import org.springframework.util.Assert;
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
/*     */ public final class DefaultJmsListenerContainerFactoryConfigurer
/*     */ {
/*     */   private DestinationResolver destinationResolver;
/*     */   private MessageConverter messageConverter;
/*     */   private JtaTransactionManager transactionManager;
/*     */   private JmsProperties jmsProperties;
/*     */   
/*     */   void setDestinationResolver(DestinationResolver destinationResolver)
/*     */   {
/*  49 */     this.destinationResolver = destinationResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setMessageConverter(MessageConverter messageConverter)
/*     */   {
/*  58 */     this.messageConverter = messageConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setTransactionManager(JtaTransactionManager transactionManager)
/*     */   {
/*  67 */     this.transactionManager = transactionManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void setJmsProperties(JmsProperties jmsProperties)
/*     */   {
/*  75 */     this.jmsProperties = jmsProperties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configure(DefaultJmsListenerContainerFactory factory, ConnectionFactory connectionFactory)
/*     */   {
/*  86 */     Assert.notNull(factory, "Factory must not be null");
/*  87 */     Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
/*  88 */     factory.setConnectionFactory(connectionFactory);
/*  89 */     factory.setPubSubDomain(Boolean.valueOf(this.jmsProperties.isPubSubDomain()));
/*  90 */     if (this.transactionManager != null) {
/*  91 */       factory.setTransactionManager(this.transactionManager);
/*     */     }
/*     */     else {
/*  94 */       factory.setSessionTransacted(Boolean.valueOf(true));
/*     */     }
/*  96 */     if (this.destinationResolver != null) {
/*  97 */       factory.setDestinationResolver(this.destinationResolver);
/*     */     }
/*  99 */     if (this.messageConverter != null) {
/* 100 */       factory.setMessageConverter(this.messageConverter);
/*     */     }
/* 102 */     JmsProperties.Listener listener = this.jmsProperties.getListener();
/* 103 */     factory.setAutoStartup(listener.isAutoStartup());
/* 104 */     if (listener.getAcknowledgeMode() != null) {
/* 105 */       factory.setSessionAcknowledgeMode(Integer.valueOf(listener.getAcknowledgeMode().getMode()));
/*     */     }
/* 107 */     String concurrency = listener.formatConcurrency();
/* 108 */     if (concurrency != null) {
/* 109 */       factory.setConcurrency(concurrency);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\DefaultJmsListenerContainerFactoryConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */