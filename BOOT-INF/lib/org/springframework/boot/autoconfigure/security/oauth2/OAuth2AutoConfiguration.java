/*    */ package org.springframework.boot.autoconfigure.security.oauth2;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
/*    */ import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2RestOperationsConfiguration;
/*    */ import org.springframework.boot.autoconfigure.security.oauth2.method.OAuth2MethodSecurityConfiguration;
/*    */ import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerConfiguration;
/*    */ import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
/*    */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.security.oauth2.common.OAuth2AccessToken;
/*    */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
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
/*    */ @ConditionalOnClass({OAuth2AccessToken.class, WebMvcConfigurerAdapter.class})
/*    */ @Import({OAuth2AuthorizationServerConfiguration.class, OAuth2MethodSecurityConfiguration.class, OAuth2ResourceServerConfiguration.class, OAuth2RestOperationsConfiguration.class})
/*    */ @AutoConfigureBefore({WebMvcAutoConfiguration.class})
/*    */ @EnableConfigurationProperties({OAuth2ClientProperties.class})
/*    */ public class OAuth2AutoConfiguration
/*    */ {
/*    */   private final OAuth2ClientProperties credentials;
/*    */   
/*    */   public OAuth2AutoConfiguration(OAuth2ClientProperties credentials)
/*    */   {
/* 54 */     this.credentials = credentials;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public ResourceServerProperties resourceServerProperties() {
/* 59 */     return new ResourceServerProperties(this.credentials.getClientId(), this.credentials
/* 60 */       .getClientSecret());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\OAuth2AutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */