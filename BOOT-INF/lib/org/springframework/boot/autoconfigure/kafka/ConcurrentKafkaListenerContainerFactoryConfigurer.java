/*    */ package org.springframework.boot.autoconfigure.kafka;
/*    */ 
/*    */ import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
/*    */ import org.springframework.kafka.core.ConsumerFactory;
/*    */ import org.springframework.kafka.listener.config.ContainerProperties;
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
/*    */ public class ConcurrentKafkaListenerContainerFactoryConfigurer
/*    */ {
/*    */   private KafkaProperties properties;
/*    */   
/*    */   void setKafkaProperties(KafkaProperties properties)
/*    */   {
/* 39 */     this.properties = properties;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void configure(ConcurrentKafkaListenerContainerFactory<Object, Object> listenerContainerFactory, ConsumerFactory<Object, Object> consumerFactory)
/*    */   {
/* 52 */     listenerContainerFactory.setConsumerFactory(consumerFactory);
/* 53 */     KafkaProperties.Listener container = this.properties.getListener();
/*    */     
/* 55 */     ContainerProperties containerProperties = listenerContainerFactory.getContainerProperties();
/* 56 */     if (container.getAckMode() != null) {
/* 57 */       containerProperties.setAckMode(container.getAckMode());
/*    */     }
/* 59 */     if (container.getAckCount() != null) {
/* 60 */       containerProperties.setAckCount(container.getAckCount().intValue());
/*    */     }
/* 62 */     if (container.getAckTime() != null) {
/* 63 */       containerProperties.setAckTime(container.getAckTime().longValue());
/*    */     }
/* 65 */     if (container.getPollTimeout() != null) {
/* 66 */       containerProperties.setPollTimeout(container.getPollTimeout().longValue());
/*    */     }
/* 68 */     if (container.getConcurrency() != null) {
/* 69 */       listenerContainerFactory.setConcurrency(container.getConcurrency());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\kafka\ConcurrentKafkaListenerContainerFactoryConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */