/*    */ package org.springframework.boot.autoconfigure.security;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.ApplicationEventPublisher;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.security.authentication.AuthenticationEventPublisher;
/*    */ import org.springframework.security.authentication.AuthenticationManager;
/*    */ import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
/*    */ import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({AuthenticationManager.class, GlobalAuthenticationConfigurerAdapter.class})
/*    */ @EnableConfigurationProperties
/*    */ @Import({SpringBootWebSecurityConfiguration.class, AuthenticationManagerConfiguration.class, BootGlobalAuthenticationConfiguration.class, SecurityDataConfiguration.class})
/*    */ public class SecurityAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({AuthenticationEventPublisher.class})
/*    */   public DefaultAuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publisher)
/*    */   {
/* 60 */     return new DefaultAuthenticationEventPublisher(publisher);
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public SecurityProperties securityProperties() {
/* 66 */     return new SecurityProperties();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\SecurityAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */