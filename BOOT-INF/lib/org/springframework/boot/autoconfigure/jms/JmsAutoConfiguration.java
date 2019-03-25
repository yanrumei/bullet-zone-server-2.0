/*     */ package org.springframework.boot.autoconfigure.jms;
/*     */ 
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.Message;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.jms.core.JmsMessagingTemplate;
/*     */ import org.springframework.jms.core.JmsTemplate;
/*     */ import org.springframework.jms.support.converter.MessageConverter;
/*     */ import org.springframework.jms.support.destination.DestinationResolver;
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
/*     */ @ConditionalOnClass({Message.class, JmsTemplate.class})
/*     */ @ConditionalOnBean({ConnectionFactory.class})
/*     */ @EnableConfigurationProperties({JmsProperties.class})
/*     */ @Import({JmsAnnotationDrivenConfiguration.class})
/*     */ public class JmsAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   protected static class JmsTemplateConfiguration
/*     */   {
/*     */     private final JmsProperties properties;
/*     */     private final ObjectProvider<DestinationResolver> destinationResolver;
/*     */     private final ObjectProvider<MessageConverter> messageConverter;
/*     */     
/*     */     public JmsTemplateConfiguration(JmsProperties properties, ObjectProvider<DestinationResolver> destinationResolver, ObjectProvider<MessageConverter> messageConverter)
/*     */     {
/*  62 */       this.properties = properties;
/*  63 */       this.destinationResolver = destinationResolver;
/*  64 */       this.messageConverter = messageConverter;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     @ConditionalOnSingleCandidate(ConnectionFactory.class)
/*     */     public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
/*  71 */       JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
/*  72 */       jmsTemplate.setPubSubDomain(this.properties.isPubSubDomain());
/*     */       
/*  74 */       DestinationResolver destinationResolver = (DestinationResolver)this.destinationResolver.getIfUnique();
/*  75 */       if (destinationResolver != null) {
/*  76 */         jmsTemplate.setDestinationResolver(destinationResolver);
/*     */       }
/*  78 */       MessageConverter messageConverter = (MessageConverter)this.messageConverter.getIfUnique();
/*  79 */       if (messageConverter != null) {
/*  80 */         jmsTemplate.setMessageConverter(messageConverter);
/*     */       }
/*  82 */       JmsProperties.Template template = this.properties.getTemplate();
/*  83 */       if (template.getDefaultDestination() != null) {
/*  84 */         jmsTemplate.setDefaultDestinationName(template.getDefaultDestination());
/*     */       }
/*  86 */       if (template.getDeliveryDelay() != null) {
/*  87 */         jmsTemplate.setDeliveryDelay(template.getDeliveryDelay().longValue());
/*     */       }
/*  89 */       jmsTemplate.setExplicitQosEnabled(template.determineQosEnabled());
/*  90 */       if (template.getDeliveryMode() != null) {
/*  91 */         jmsTemplate.setDeliveryMode(template.getDeliveryMode().getValue());
/*     */       }
/*  93 */       if (template.getPriority() != null) {
/*  94 */         jmsTemplate.setPriority(template.getPriority().intValue());
/*     */       }
/*  96 */       if (template.getTimeToLive() != null) {
/*  97 */         jmsTemplate.setTimeToLive(template.getTimeToLive().longValue());
/*     */       }
/*  99 */       if (template.getReceiveTimeout() != null) {
/* 100 */         jmsTemplate.setReceiveTimeout(template.getReceiveTimeout().longValue());
/*     */       }
/* 102 */       return jmsTemplate;
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({JmsMessagingTemplate.class})
/*     */   @Import({JmsAutoConfiguration.JmsTemplateConfiguration.class})
/*     */   protected static class MessagingTemplateConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     @ConditionalOnSingleCandidate(JmsTemplate.class)
/*     */     public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate)
/*     */     {
/* 116 */       return new JmsMessagingTemplate(jmsTemplate);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\JmsAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */