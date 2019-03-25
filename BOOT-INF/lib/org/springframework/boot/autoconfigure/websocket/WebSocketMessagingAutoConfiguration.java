/*    */ package org.springframework.boot.autoconfigure.websocket;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.util.List;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.messaging.converter.ByteArrayMessageConverter;
/*    */ import org.springframework.messaging.converter.DefaultContentTypeResolver;
/*    */ import org.springframework.messaging.converter.MappingJackson2MessageConverter;
/*    */ import org.springframework.messaging.converter.MessageConverter;
/*    */ import org.springframework.messaging.converter.StringMessageConverter;
/*    */ import org.springframework.messaging.simp.config.AbstractMessageBrokerConfiguration;
/*    */ import org.springframework.util.MimeTypeUtils;
/*    */ import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
/*    */ import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;
/*    */ import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
/*    */ import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
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
/*    */ @ConditionalOnWebApplication
/*    */ @ConditionalOnClass({WebSocketMessageBrokerConfigurer.class})
/*    */ @AutoConfigureAfter({JacksonAutoConfiguration.class})
/*    */ public class WebSocketMessagingAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnBean({DelegatingWebSocketMessageBrokerConfiguration.class, ObjectMapper.class})
/*    */   @ConditionalOnClass({ObjectMapper.class, AbstractMessageBrokerConfiguration.class})
/*    */   static class WebSocketMessageConverterConfiguration
/*    */     extends AbstractWebSocketMessageBrokerConfigurer
/*    */   {
/*    */     private final ObjectMapper objectMapper;
/*    */     
/*    */     WebSocketMessageConverterConfiguration(ObjectMapper objectMapper)
/*    */     {
/* 63 */       this.objectMapper = objectMapper;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     public void registerStompEndpoints(StompEndpointRegistry registry) {}
/*    */     
/*    */ 
/*    */ 
/*    */     public boolean configureMessageConverters(List<MessageConverter> messageConverters)
/*    */     {
/* 74 */       MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
/* 75 */       converter.setObjectMapper(this.objectMapper);
/* 76 */       DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
/* 77 */       resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
/* 78 */       converter.setContentTypeResolver(resolver);
/* 79 */       messageConverters.add(new StringMessageConverter());
/* 80 */       messageConverters.add(new ByteArrayMessageConverter());
/* 81 */       messageConverters.add(converter);
/* 82 */       return false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\websocket\WebSocketMessagingAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */