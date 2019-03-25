/*     */ package org.springframework.boot.autoconfigure.amqp;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
/*     */ import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
/*     */ import org.springframework.amqp.rabbit.connection.ConnectionFactory;
/*     */ import org.springframework.amqp.rabbit.retry.MessageRecoverer;
/*     */ import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
/*     */ import org.springframework.amqp.support.converter.MessageConverter;
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
/*     */ public final class SimpleRabbitListenerContainerFactoryConfigurer
/*     */ {
/*     */   private MessageConverter messageConverter;
/*     */   private MessageRecoverer messageRecoverer;
/*     */   private RabbitProperties rabbitProperties;
/*     */   
/*     */   void setMessageConverter(MessageConverter messageConverter)
/*     */   {
/*  50 */     this.messageConverter = messageConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void setMessageRecoverer(MessageRecoverer messageRecoverer)
/*     */   {
/*  58 */     this.messageRecoverer = messageRecoverer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void setRabbitProperties(RabbitProperties rabbitProperties)
/*     */   {
/*  66 */     this.rabbitProperties = rabbitProperties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configure(SimpleRabbitListenerContainerFactory factory, ConnectionFactory connectionFactory)
/*     */   {
/*  78 */     Assert.notNull(factory, "Factory must not be null");
/*  79 */     Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
/*  80 */     factory.setConnectionFactory(connectionFactory);
/*  81 */     if (this.messageConverter != null) {
/*  82 */       factory.setMessageConverter(this.messageConverter);
/*     */     }
/*     */     
/*  85 */     RabbitProperties.AmqpContainer config = this.rabbitProperties.getListener().getSimple();
/*  86 */     factory.setAutoStartup(Boolean.valueOf(config.isAutoStartup()));
/*  87 */     if (config.getAcknowledgeMode() != null) {
/*  88 */       factory.setAcknowledgeMode(config.getAcknowledgeMode());
/*     */     }
/*  90 */     if (config.getConcurrency() != null) {
/*  91 */       factory.setConcurrentConsumers(config.getConcurrency());
/*     */     }
/*  93 */     if (config.getMaxConcurrency() != null) {
/*  94 */       factory.setMaxConcurrentConsumers(config.getMaxConcurrency());
/*     */     }
/*  96 */     if (config.getPrefetch() != null) {
/*  97 */       factory.setPrefetchCount(config.getPrefetch());
/*     */     }
/*  99 */     if (config.getTransactionSize() != null) {
/* 100 */       factory.setTxSize(config.getTransactionSize());
/*     */     }
/* 102 */     if (config.getDefaultRequeueRejected() != null) {
/* 103 */       factory.setDefaultRequeueRejected(config.getDefaultRequeueRejected());
/*     */     }
/* 105 */     if (config.getIdleEventInterval() != null) {
/* 106 */       factory.setIdleEventInterval(config.getIdleEventInterval());
/*     */     }
/* 108 */     RabbitProperties.ListenerRetry retryConfig = config.getRetry();
/* 109 */     if (retryConfig.isEnabled())
/*     */     {
/*     */ 
/* 112 */       RetryInterceptorBuilder<?> builder = retryConfig.isStateless() ? RetryInterceptorBuilder.stateless() : RetryInterceptorBuilder.stateful();
/* 113 */       builder.maxAttempts(retryConfig.getMaxAttempts());
/* 114 */       builder.backOffOptions(retryConfig.getInitialInterval(), retryConfig
/* 115 */         .getMultiplier(), retryConfig.getMaxInterval());
/* 116 */       MessageRecoverer recoverer = this.messageRecoverer != null ? this.messageRecoverer : new RejectAndDontRequeueRecoverer();
/*     */       
/* 118 */       builder.recoverer(recoverer);
/* 119 */       factory.setAdviceChain(new Advice[] { builder.build() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\amqp\SimpleRabbitListenerContainerFactoryConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */