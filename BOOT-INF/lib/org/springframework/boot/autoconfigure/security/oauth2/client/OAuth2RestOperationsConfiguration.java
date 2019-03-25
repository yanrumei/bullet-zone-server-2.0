/*     */ package org.springframework.boot.autoconfigure.security.oauth2.client;
/*     */ 
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.autoconfigure.security.SecurityProperties;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.web.servlet.FilterRegistrationBean;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.context.annotation.Primary;
/*     */ import org.springframework.context.annotation.Scope;
/*     */ import org.springframework.context.annotation.ScopedProxyMode;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.security.core.Authentication;
/*     */ import org.springframework.security.core.context.SecurityContext;
/*     */ import org.springframework.security.core.context.SecurityContextHolder;
/*     */ import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
/*     */ import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
/*     */ import org.springframework.security.oauth2.client.token.AccessTokenRequest;
/*     */ import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
/*     */ import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
/*     */ import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
/*     */ import org.springframework.security.oauth2.provider.OAuth2Authentication;
/*     */ import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @ConditionalOnClass({EnableOAuth2Client.class})
/*     */ public class OAuth2RestOperationsConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @Conditional({OAuth2RestOperationsConfiguration.ClientCredentialsCondition.class})
/*     */   protected static class SingletonScopedConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConfigurationProperties(prefix="security.oauth2.client")
/*     */     @Primary
/*     */     public ClientCredentialsResourceDetails oauth2RemoteResource()
/*     */     {
/*  77 */       ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
/*  78 */       return details;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public DefaultOAuth2ClientContext oauth2ClientContext() {
/*  83 */       return new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnBean({OAuth2ClientConfiguration.class})
/*     */   @Conditional({OAuth2RestOperationsConfiguration.OAuth2ClientIdCondition.class, OAuth2RestOperationsConfiguration.NoClientCredentialsCondition.class})
/*     */   @Import({OAuth2ProtectedResourceDetailsConfiguration.class})
/*     */   protected static class SessionScopedConfiguration
/*     */   {
/*     */     @Bean
/*     */     public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter, SecurityProperties security)
/*     */     {
/*  97 */       FilterRegistrationBean registration = new FilterRegistrationBean();
/*  98 */       registration.setFilter(filter);
/*  99 */       registration.setOrder(security.getFilterOrder() - 10);
/* 100 */       return registration;
/*     */     }
/*     */     
/*     */     @Configuration
/*     */     protected static class ClientContextConfiguration
/*     */     {
/*     */       private final AccessTokenRequest accessTokenRequest;
/*     */       
/*     */       public ClientContextConfiguration(@Qualifier("accessTokenRequest") ObjectProvider<AccessTokenRequest> accessTokenRequest)
/*     */       {
/* 110 */         this.accessTokenRequest = ((AccessTokenRequest)accessTokenRequest.getIfAvailable());
/*     */       }
/*     */       
/*     */       @Bean
/*     */       @Scope(value="session", proxyMode=ScopedProxyMode.INTERFACES)
/*     */       public DefaultOAuth2ClientContext oauth2ClientContext() {
/* 116 */         return new DefaultOAuth2ClientContext(this.accessTokenRequest);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({OAuth2ClientConfiguration.class})
/*     */   @Conditional({OAuth2RestOperationsConfiguration.OAuth2ClientIdCondition.class, OAuth2RestOperationsConfiguration.NoClientCredentialsCondition.class})
/*     */   @Import({OAuth2ProtectedResourceDetailsConfiguration.class})
/*     */   protected static class RequestScopedConfiguration
/*     */   {
/*     */     @Bean
/*     */     @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
/*     */     public DefaultOAuth2ClientContext oauth2ClientContext()
/*     */     {
/* 136 */       DefaultOAuth2ClientContext context = new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
/*     */       
/*     */ 
/* 139 */       Authentication principal = SecurityContextHolder.getContext().getAuthentication();
/* 140 */       if ((principal instanceof OAuth2Authentication)) {
/* 141 */         OAuth2Authentication authentication = (OAuth2Authentication)principal;
/* 142 */         Object details = authentication.getDetails();
/* 143 */         if ((details instanceof OAuth2AuthenticationDetails)) {
/* 144 */           OAuth2AuthenticationDetails oauthsDetails = (OAuth2AuthenticationDetails)details;
/* 145 */           String token = oauthsDetails.getTokenValue();
/* 146 */           context.setAccessToken(new DefaultOAuth2AccessToken(token));
/*     */         }
/*     */       }
/* 149 */       return context;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class OAuth2ClientIdCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 163 */       PropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "security.oauth2.client.");
/* 164 */       String clientId = resolver.getProperty("client-id");
/*     */       
/* 166 */       ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth Client ID", new Object[0]);
/* 167 */       if (StringUtils.hasLength(clientId)) {
/* 168 */         return ConditionOutcome.match(message
/* 169 */           .foundExactly("security.oauth2.client.client-id property"));
/*     */       }
/* 171 */       return ConditionOutcome.noMatch(message
/* 172 */         .didNotFind("security.oauth2.client.client-id property").atAll());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class NoClientCredentialsCondition
/*     */     extends NoneNestedConditions
/*     */   {
/*     */     NoClientCredentialsCondition()
/*     */     {
/* 183 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @Conditional({OAuth2RestOperationsConfiguration.ClientCredentialsCondition.class})
/*     */     static class ClientCredentialsActivated {}
/*     */   }
/*     */   
/*     */ 
/*     */   static class ClientCredentialsCondition
/*     */     extends AnyNestedCondition
/*     */   {
/*     */     ClientCredentialsCondition()
/*     */     {
/* 198 */       super();
/*     */     }
/*     */     
/*     */     @ConditionalOnNotWebApplication
/*     */     static class NoWebApplication {}
/*     */     
/*     */     @ConditionalOnProperty(prefix="security.oauth2.client", name={"grant-type"}, havingValue="client_credentials", matchIfMissing=false)
/*     */     static class ClientCredentialsConfigured {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\client\OAuth2RestOperationsConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */