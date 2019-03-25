/*    */ package org.springframework.boot.autoconfigure.reactor;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import reactor.Environment;
/*    */ import reactor.bus.EventBus;
/*    */ import reactor.spring.context.config.EnableReactor;
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
/*    */ @ConditionalOnClass({EnableReactor.class, Environment.class})
/*    */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*    */ public class ReactorAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({EventBus.class})
/*    */   public EventBus eventBus(Environment environment)
/*    */   {
/* 44 */     return EventBus.create(environment);
/*    */   }
/*    */   
/*    */   @Configuration
/*    */   @ConditionalOnMissingBean({Environment.class})
/*    */   @EnableReactor
/*    */   protected static class ReactorConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\reactor\ReactorAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */