/*    */ package org.springframework.boot.autoconfigure.kafka;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.kafka.annotation.EnableKafka;
/*    */ import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
/*    */ import org.springframework.kafka.core.ConsumerFactory;
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
/*    */ @ConditionalOnClass({EnableKafka.class})
/*    */ class KafkaAnnotationDrivenConfiguration
/*    */ {
/*    */   private final KafkaProperties properties;
/*    */   
/*    */   KafkaAnnotationDrivenConfiguration(KafkaProperties properties)
/*    */   {
/* 41 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public ConcurrentKafkaListenerContainerFactoryConfigurer kafkaListenerContainerFactoryConfigurer() {
/* 47 */     ConcurrentKafkaListenerContainerFactoryConfigurer configurer = new ConcurrentKafkaListenerContainerFactoryConfigurer();
/* 48 */     configurer.setKafkaProperties(this.properties);
/* 49 */     return configurer;
/*    */   }
/*    */   
/*    */ 
/*    */   @Bean
/*    */   @ConditionalOnMissingBean(name={"kafkaListenerContainerFactory"})
/*    */   public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ConsumerFactory<Object, Object> kafkaConsumerFactory)
/*    */   {
/* 57 */     ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory();
/* 58 */     configurer.configure(factory, kafkaConsumerFactory);
/* 59 */     return factory;
/*    */   }
/*    */   
/*    */   @Configuration
/*    */   @EnableKafka
/*    */   @ConditionalOnMissingBean(name={"org.springframework.kafka.config.internalKafkaListenerAnnotationProcessor"})
/*    */   protected static class EnableKafkaConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\kafka\KafkaAnnotationDrivenConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */