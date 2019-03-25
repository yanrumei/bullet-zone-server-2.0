/*     */ package org.springframework.boot.autoconfigure.security.oauth2.authserver;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.PostConstruct;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.security.authentication.AuthenticationManager;
/*     */ import org.springframework.security.core.authority.AuthorityUtils;
/*     */ import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
/*     */ import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder.ClientBuilder;
/*     */ import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
/*     */ import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
/*     */ import org.springframework.security.oauth2.provider.client.BaseClientDetails;
/*     */ import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
/*     */ import org.springframework.security.oauth2.provider.token.TokenStore;
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
/*     */ @ConditionalOnClass({EnableAuthorizationServer.class})
/*     */ @ConditionalOnMissingBean({AuthorizationServerConfigurer.class})
/*     */ @ConditionalOnBean({AuthorizationServerEndpointsConfiguration.class})
/*     */ @EnableConfigurationProperties({AuthorizationServerProperties.class})
/*     */ public class OAuth2AuthorizationServerConfiguration
/*     */   extends AuthorizationServerConfigurerAdapter
/*     */ {
/*  70 */   private static final Log logger = LogFactory.getLog(OAuth2AuthorizationServerConfiguration.class);
/*     */   
/*     */ 
/*     */   private final BaseClientDetails details;
/*     */   
/*     */ 
/*     */   private final AuthenticationManager authenticationManager;
/*     */   
/*     */   private final TokenStore tokenStore;
/*     */   
/*     */   private final AccessTokenConverter tokenConverter;
/*     */   
/*     */   private final AuthorizationServerProperties properties;
/*     */   
/*     */ 
/*     */   public OAuth2AuthorizationServerConfiguration(BaseClientDetails details, AuthenticationManager authenticationManager, ObjectProvider<TokenStore> tokenStore, ObjectProvider<AccessTokenConverter> tokenConverter, AuthorizationServerProperties properties)
/*     */   {
/*  87 */     this.details = details;
/*  88 */     this.authenticationManager = authenticationManager;
/*  89 */     this.tokenStore = ((TokenStore)tokenStore.getIfAvailable());
/*  90 */     this.tokenConverter = ((AccessTokenConverter)tokenConverter.getIfAvailable());
/*  91 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public void configure(ClientDetailsServiceConfigurer clients)
/*     */     throws Exception
/*     */   {
/*  97 */     ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder builder = clients.inMemory().withClient(this.details.getClientId());
/*  98 */     builder.secret(this.details.getClientSecret())
/*  99 */       .resourceIds((String[])this.details.getResourceIds().toArray(new String[0]))
/* 100 */       .authorizedGrantTypes(
/* 101 */       (String[])this.details.getAuthorizedGrantTypes().toArray(new String[0]))
/* 102 */       .authorities(
/*     */       
/* 104 */       (String[])AuthorityUtils.authorityListToSet(this.details.getAuthorities()).toArray(new String[0]))
/* 105 */       .scopes((String[])this.details.getScope().toArray(new String[0]));
/*     */     
/* 107 */     if (this.details.getAutoApproveScopes() != null) {
/* 108 */       builder.autoApprove(
/* 109 */         (String[])this.details.getAutoApproveScopes().toArray(new String[0]));
/*     */     }
/* 111 */     if (this.details.getAccessTokenValiditySeconds() != null) {
/* 112 */       builder.accessTokenValiditySeconds(this.details
/* 113 */         .getAccessTokenValiditySeconds().intValue());
/*     */     }
/* 115 */     if (this.details.getRefreshTokenValiditySeconds() != null) {
/* 116 */       builder.refreshTokenValiditySeconds(this.details
/* 117 */         .getRefreshTokenValiditySeconds().intValue());
/*     */     }
/* 119 */     if (this.details.getRegisteredRedirectUri() != null) {
/* 120 */       builder.redirectUris(
/* 121 */         (String[])this.details.getRegisteredRedirectUri().toArray(new String[0]));
/*     */     }
/*     */   }
/*     */   
/*     */   public void configure(AuthorizationServerEndpointsConfigurer endpoints)
/*     */     throws Exception
/*     */   {
/* 128 */     if (this.tokenConverter != null) {
/* 129 */       endpoints.accessTokenConverter(this.tokenConverter);
/*     */     }
/* 131 */     if (this.tokenStore != null) {
/* 132 */       endpoints.tokenStore(this.tokenStore);
/*     */     }
/* 134 */     if (this.details.getAuthorizedGrantTypes().contains("password")) {
/* 135 */       endpoints.authenticationManager(this.authenticationManager);
/*     */     }
/*     */   }
/*     */   
/*     */   public void configure(AuthorizationServerSecurityConfigurer security)
/*     */     throws Exception
/*     */   {
/* 142 */     if (this.properties.getCheckTokenAccess() != null) {
/* 143 */       security.checkTokenAccess(this.properties.getCheckTokenAccess());
/*     */     }
/* 145 */     if (this.properties.getTokenKeyAccess() != null) {
/* 146 */       security.tokenKeyAccess(this.properties.getTokenKeyAccess());
/*     */     }
/* 148 */     if (this.properties.getRealm() != null) {
/* 149 */       security.realm(this.properties.getRealm());
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   protected static class ClientDetailsLogger
/*     */   {
/*     */     private final OAuth2ClientProperties credentials;
/*     */     
/*     */     protected ClientDetailsLogger(OAuth2ClientProperties credentials) {
/* 159 */       this.credentials = credentials;
/*     */     }
/*     */     
/*     */     @PostConstruct
/*     */     public void init() {
/* 164 */       String prefix = "security.oauth2.client";
/* 165 */       boolean defaultSecret = this.credentials.isDefaultSecret();
/* 166 */       OAuth2AuthorizationServerConfiguration.logger.info(String.format("Initialized OAuth2 Client%n%n%s.client-id = %s%n%s.client-secret = %s%n%n", new Object[] { prefix, this.credentials
/*     */       
/*     */ 
/* 169 */         .getClientId(), prefix, defaultSecret ? this.credentials
/* 170 */         .getClientSecret() : "****" }));
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({BaseClientDetails.class})
/*     */   protected static class BaseClientDetailsConfiguration
/*     */   {
/*     */     private final OAuth2ClientProperties client;
/*     */     
/*     */     protected BaseClientDetailsConfiguration(OAuth2ClientProperties client)
/*     */     {
/* 182 */       this.client = client;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConfigurationProperties(prefix="security.oauth2.client")
/*     */     public BaseClientDetails oauth2ClientDetails() {
/* 188 */       BaseClientDetails details = new BaseClientDetails();
/* 189 */       if (this.client.getClientId() == null) {
/* 190 */         this.client.setClientId(UUID.randomUUID().toString());
/*     */       }
/* 192 */       details.setClientId(this.client.getClientId());
/* 193 */       details.setClientSecret(this.client.getClientSecret());
/* 194 */       details.setAuthorizedGrantTypes(Arrays.asList(new String[] { "authorization_code", "password", "client_credentials", "implicit", "refresh_token" }));
/*     */       
/* 196 */       details.setAuthorities(
/* 197 */         AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
/* 198 */       details.setRegisteredRedirectUri(Collections.emptySet());
/* 199 */       return details;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\authserver\OAuth2AuthorizationServerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */