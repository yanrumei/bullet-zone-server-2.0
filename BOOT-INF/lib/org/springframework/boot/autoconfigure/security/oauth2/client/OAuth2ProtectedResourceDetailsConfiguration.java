/*    */ package org.springframework.boot.autoconfigure.security.oauth2.client;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Primary;
/*    */ import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
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
/*    */ class OAuth2ProtectedResourceDetailsConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConfigurationProperties(prefix="security.oauth2.client")
/*    */   @Primary
/*    */   public AuthorizationCodeResourceDetails oauth2RemoteResource()
/*    */   {
/* 37 */     return new AuthorizationCodeResourceDetails();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\client\OAuth2ProtectedResourceDetailsConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */