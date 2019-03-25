/*    */ package org.springframework.boot.autoconfigure.kafka;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.kafka.core.ConsumerFactory;
/*    */ import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
/*    */ import org.springframework.kafka.core.DefaultKafkaProducerFactory;
/*    */ import org.springframework.kafka.core.KafkaTemplate;
/*    */ import org.springframework.kafka.core.ProducerFactory;
/*    */ import org.springframework.kafka.support.LoggingProducerListener;
/*    */ import org.springframework.kafka.support.ProducerListener;
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
/*    */ @ConditionalOnClass({KafkaTemplate.class})
/*    */ @EnableConfigurationProperties({KafkaProperties.class})
/*    */ @Import({KafkaAnnotationDrivenConfiguration.class})
/*    */ public class KafkaAutoConfiguration
/*    */ {
/*    */   private final KafkaProperties properties;
/*    */   
/*    */   public KafkaAutoConfiguration(KafkaProperties properties)
/*    */   {
/* 49 */     this.properties = properties;
/*    */   }
/*    */   
/*    */ 
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({KafkaTemplate.class})
/*    */   public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory, ProducerListener<Object, Object> kafkaProducerListener)
/*    */   {
/* 57 */     KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate(kafkaProducerFactory);
/*    */     
/* 59 */     kafkaTemplate.setProducerListener(kafkaProducerListener);
/* 60 */     kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
/* 61 */     return kafkaTemplate;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({ProducerListener.class})
/*    */   public ProducerListener<Object, Object> kafkaProducerListener() {
/* 67 */     return new LoggingProducerListener();
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({ConsumerFactory.class})
/*    */   public ConsumerFactory<?, ?> kafkaConsumerFactory() {
/* 73 */     return new DefaultKafkaConsumerFactory(this.properties
/* 74 */       .buildConsumerProperties());
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({ProducerFactory.class})
/*    */   public ProducerFactory<?, ?> kafkaProducerFactory() {
/* 80 */     return new DefaultKafkaProducerFactory(this.properties
/* 81 */       .buildProducerProperties());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\kafka\KafkaAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */