/*     */ package org.springframework.boot.autoconfigure.security.oauth2.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
/*     */ import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.client.ClientHttpRequestExecution;
/*     */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.security.crypto.codec.Base64;
/*     */ import org.springframework.security.oauth2.client.OAuth2ClientContext;
/*     */ import org.springframework.security.oauth2.client.OAuth2RestOperations;
/*     */ import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
/*     */ import org.springframework.security.oauth2.client.token.AccessTokenRequest;
/*     */ import org.springframework.security.oauth2.client.token.RequestEnhancer;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
/*     */ import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
/*     */ import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
/*     */ import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
/*     */ import org.springframework.security.oauth2.provider.token.TokenStore;
/*     */ import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
/*     */ import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
/*     */ import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
/*     */ import org.springframework.social.connect.ConnectionFactoryLocator;
/*     */ import org.springframework.social.connect.support.OAuth2ConnectionFactory;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.client.RestTemplate;
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
/*     */ @ConditionalOnMissingBean({AuthorizationServerEndpointsConfiguration.class})
/*     */ public class ResourceServerTokenServicesConfiguration
/*     */ {
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public UserInfoRestTemplateFactory userInfoRestTemplateFactory(ObjectProvider<List<UserInfoRestTemplateCustomizer>> customizers, ObjectProvider<OAuth2ProtectedResourceDetails> details, ObjectProvider<OAuth2ClientContext> oauth2ClientContext)
/*     */   {
/*  88 */     return new DefaultUserInfoRestTemplateFactory(customizers, details, oauth2ClientContext);
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @Conditional({ResourceServerTokenServicesConfiguration.RemoteTokenCondition.class})
/*     */   protected static class RemoteTokenServicesConfiguration
/*     */   {
/*     */     @Configuration
/*     */     @Conditional({ResourceServerTokenServicesConfiguration.TokenInfoCondition.class})
/*     */     protected static class TokenInfoServicesConfiguration
/*     */     {
/*     */       private final ResourceServerProperties resource;
/*     */       
/*     */       protected TokenInfoServicesConfiguration(ResourceServerProperties resource)
/*     */       {
/* 103 */         this.resource = resource;
/*     */       }
/*     */       
/*     */       @Bean
/*     */       public RemoteTokenServices remoteTokenServices() {
/* 108 */         RemoteTokenServices services = new RemoteTokenServices();
/* 109 */         services.setCheckTokenEndpointUrl(this.resource.getTokenInfoUri());
/* 110 */         services.setClientId(this.resource.getClientId());
/* 111 */         services.setClientSecret(this.resource.getClientSecret());
/* 112 */         return services;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @Configuration
/*     */     @ConditionalOnClass({OAuth2ConnectionFactory.class})
/*     */     @Conditional({ResourceServerTokenServicesConfiguration.NotTokenInfoCondition.class})
/*     */     protected static class SocialTokenServicesConfiguration
/*     */     {
/*     */       private final ResourceServerProperties sso;
/*     */       
/*     */ 
/*     */       private final OAuth2ConnectionFactory<?> connectionFactory;
/*     */       
/*     */       private final OAuth2RestOperations restTemplate;
/*     */       
/*     */       private final AuthoritiesExtractor authoritiesExtractor;
/*     */       
/*     */       private final PrincipalExtractor principalExtractor;
/*     */       
/*     */ 
/*     */       public SocialTokenServicesConfiguration(ResourceServerProperties sso, ObjectProvider<OAuth2ConnectionFactory<?>> connectionFactory, UserInfoRestTemplateFactory restTemplateFactory, ObjectProvider<AuthoritiesExtractor> authoritiesExtractor, ObjectProvider<PrincipalExtractor> principalExtractor)
/*     */       {
/* 137 */         this.sso = sso;
/* 138 */         this.connectionFactory = ((OAuth2ConnectionFactory)connectionFactory.getIfAvailable());
/* 139 */         this.restTemplate = restTemplateFactory.getUserInfoRestTemplate();
/* 140 */         this.authoritiesExtractor = ((AuthoritiesExtractor)authoritiesExtractor.getIfAvailable());
/* 141 */         this.principalExtractor = ((PrincipalExtractor)principalExtractor.getIfAvailable());
/*     */       }
/*     */       
/*     */       @Bean
/*     */       @ConditionalOnBean({ConnectionFactoryLocator.class})
/*     */       @ConditionalOnMissingBean({ResourceServerTokenServices.class})
/*     */       public SpringSocialTokenServices socialTokenServices() {
/* 148 */         return new SpringSocialTokenServices(this.connectionFactory, this.sso
/* 149 */           .getClientId());
/*     */       }
/*     */       
/*     */ 
/*     */       @Bean
/*     */       @ConditionalOnMissingBean({ConnectionFactoryLocator.class, ResourceServerTokenServices.class})
/*     */       public UserInfoTokenServices userInfoTokenServices()
/*     */       {
/* 157 */         UserInfoTokenServices services = new UserInfoTokenServices(this.sso.getUserInfoUri(), this.sso.getClientId());
/* 158 */         services.setTokenType(this.sso.getTokenType());
/* 159 */         services.setRestTemplate(this.restTemplate);
/* 160 */         if (this.authoritiesExtractor != null) {
/* 161 */           services.setAuthoritiesExtractor(this.authoritiesExtractor);
/*     */         }
/* 163 */         if (this.principalExtractor != null) {
/* 164 */           services.setPrincipalExtractor(this.principalExtractor);
/*     */         }
/* 166 */         return services;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @Configuration
/*     */     @ConditionalOnMissingClass({"org.springframework.social.connect.support.OAuth2ConnectionFactory"})
/*     */     @Conditional({ResourceServerTokenServicesConfiguration.NotTokenInfoCondition.class})
/*     */     protected static class UserInfoTokenServicesConfiguration
/*     */     {
/*     */       private final ResourceServerProperties sso;
/*     */       
/*     */       private final OAuth2RestOperations restTemplate;
/*     */       
/*     */       private final AuthoritiesExtractor authoritiesExtractor;
/*     */       
/*     */       private final PrincipalExtractor principalExtractor;
/*     */       
/*     */ 
/*     */       public UserInfoTokenServicesConfiguration(ResourceServerProperties sso, UserInfoRestTemplateFactory restTemplateFactory, ObjectProvider<AuthoritiesExtractor> authoritiesExtractor, ObjectProvider<PrincipalExtractor> principalExtractor)
/*     */       {
/* 188 */         this.sso = sso;
/* 189 */         this.restTemplate = restTemplateFactory.getUserInfoRestTemplate();
/* 190 */         this.authoritiesExtractor = ((AuthoritiesExtractor)authoritiesExtractor.getIfAvailable());
/* 191 */         this.principalExtractor = ((PrincipalExtractor)principalExtractor.getIfAvailable());
/*     */       }
/*     */       
/*     */       @Bean
/*     */       @ConditionalOnMissingBean({ResourceServerTokenServices.class})
/*     */       public UserInfoTokenServices userInfoTokenServices()
/*     */       {
/* 198 */         UserInfoTokenServices services = new UserInfoTokenServices(this.sso.getUserInfoUri(), this.sso.getClientId());
/* 199 */         services.setRestTemplate(this.restTemplate);
/* 200 */         services.setTokenType(this.sso.getTokenType());
/* 201 */         if (this.authoritiesExtractor != null) {
/* 202 */           services.setAuthoritiesExtractor(this.authoritiesExtractor);
/*     */         }
/* 204 */         if (this.principalExtractor != null) {
/* 205 */           services.setPrincipalExtractor(this.principalExtractor);
/*     */         }
/* 207 */         return services;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @Conditional({ResourceServerTokenServicesConfiguration.JwkCondition.class})
/*     */   protected static class JwkTokenStoreConfiguration
/*     */   {
/*     */     private final ResourceServerProperties resource;
/*     */     
/*     */     public JwkTokenStoreConfiguration(ResourceServerProperties resource)
/*     */     {
/* 221 */       this.resource = resource;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({ResourceServerTokenServices.class})
/*     */     public DefaultTokenServices jwkTokenServices(TokenStore jwkTokenStore) {
/* 227 */       DefaultTokenServices services = new DefaultTokenServices();
/* 228 */       services.setTokenStore(jwkTokenStore);
/* 229 */       return services;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({TokenStore.class})
/*     */     public TokenStore jwkTokenStore() {
/* 235 */       return new JwkTokenStore(this.resource.getJwk().getKeySetUri());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @Conditional({ResourceServerTokenServicesConfiguration.JwtTokenCondition.class})
/*     */   protected static class JwtTokenServicesConfiguration
/*     */   {
/*     */     private final ResourceServerProperties resource;
/*     */     
/*     */     private final List<JwtAccessTokenConverterConfigurer> configurers;
/*     */     
/*     */     private final List<JwtAccessTokenConverterRestTemplateCustomizer> customizers;
/*     */     
/*     */     public JwtTokenServicesConfiguration(ResourceServerProperties resource, ObjectProvider<List<JwtAccessTokenConverterConfigurer>> configurers, ObjectProvider<List<JwtAccessTokenConverterRestTemplateCustomizer>> customizers)
/*     */     {
/* 252 */       this.resource = resource;
/* 253 */       this.configurers = ((List)configurers.getIfAvailable());
/* 254 */       this.customizers = ((List)customizers.getIfAvailable());
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({ResourceServerTokenServices.class})
/*     */     public DefaultTokenServices jwtTokenServices(TokenStore jwtTokenStore) {
/* 260 */       DefaultTokenServices services = new DefaultTokenServices();
/* 261 */       services.setTokenStore(jwtTokenStore);
/* 262 */       return services;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({TokenStore.class})
/*     */     public TokenStore jwtTokenStore() {
/* 268 */       return new JwtTokenStore(jwtTokenEnhancer());
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public JwtAccessTokenConverter jwtTokenEnhancer() {
/* 273 */       JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
/* 274 */       String keyValue = this.resource.getJwt().getKeyValue();
/* 275 */       if (!StringUtils.hasText(keyValue)) {
/* 276 */         keyValue = getKeyFromServer();
/*     */       }
/* 278 */       if ((StringUtils.hasText(keyValue)) && (!keyValue.startsWith("-----BEGIN"))) {
/* 279 */         converter.setSigningKey(keyValue);
/*     */       }
/* 281 */       if (keyValue != null) {
/* 282 */         converter.setVerifierKey(keyValue);
/*     */       }
/* 284 */       if (!CollectionUtils.isEmpty(this.configurers)) {
/* 285 */         AnnotationAwareOrderComparator.sort(this.configurers);
/* 286 */         for (JwtAccessTokenConverterConfigurer configurer : this.configurers) {
/* 287 */           configurer.configure(converter);
/*     */         }
/*     */       }
/* 290 */       return converter;
/*     */     }
/*     */     
/*     */     private String getKeyFromServer() {
/* 294 */       RestTemplate keyUriRestTemplate = new RestTemplate();
/* 295 */       if (!CollectionUtils.isEmpty(this.customizers)) {
/* 296 */         for (JwtAccessTokenConverterRestTemplateCustomizer customizer : this.customizers) {
/* 297 */           customizer.customize(keyUriRestTemplate);
/*     */         }
/*     */       }
/* 300 */       HttpHeaders headers = new HttpHeaders();
/* 301 */       String username = this.resource.getClientId();
/* 302 */       String password = this.resource.getClientSecret();
/* 303 */       if ((username != null) && (password != null)) {
/* 304 */         byte[] token = Base64.encode((username + ":" + password).getBytes());
/* 305 */         headers.add("Authorization", "Basic " + new String(token));
/*     */       }
/* 307 */       HttpEntity<Void> request = new HttpEntity(headers);
/* 308 */       String url = this.resource.getJwt().getKeyUri();
/* 309 */       return 
/*     */       
/* 311 */         (String)((Map)keyUriRestTemplate.exchange(url, HttpMethod.GET, request, Map.class, new Object[0]).getBody()).get("value");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class TokenInfoCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 322 */       ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth TokenInfo Condition", new Object[0]);
/* 323 */       Environment environment = context.getEnvironment();
/* 324 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "security.oauth2.resource.");
/*     */       
/* 326 */       Boolean preferTokenInfo = (Boolean)resolver.getProperty("prefer-token-info", Boolean.class);
/*     */       
/* 328 */       if (preferTokenInfo == null) {
/* 329 */         preferTokenInfo = Boolean.valueOf(environment
/* 330 */           .resolvePlaceholders("${OAUTH2_RESOURCE_PREFERTOKENINFO:true}")
/* 331 */           .equals("true"));
/*     */       }
/* 333 */       String tokenInfoUri = resolver.getProperty("token-info-uri");
/* 334 */       String userInfoUri = resolver.getProperty("user-info-uri");
/* 335 */       if ((!StringUtils.hasLength(userInfoUri)) && 
/* 336 */         (!StringUtils.hasLength(tokenInfoUri))) {
/* 337 */         return 
/* 338 */           ConditionOutcome.match(message.didNotFind("user-info-uri property").atAll());
/*     */       }
/* 340 */       if ((StringUtils.hasLength(tokenInfoUri)) && (preferTokenInfo.booleanValue())) {
/* 341 */         return 
/* 342 */           ConditionOutcome.match(message.foundExactly("preferred token-info-uri property"));
/*     */       }
/* 344 */       return ConditionOutcome.noMatch(message.didNotFind("token info").atAll());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class JwtTokenCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 355 */       ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth JWT Condition", new Object[0]);
/*     */       
/* 357 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "security.oauth2.resource.jwt.");
/* 358 */       String keyValue = resolver.getProperty("key-value");
/* 359 */       String keyUri = resolver.getProperty("key-uri");
/* 360 */       if ((StringUtils.hasText(keyValue)) || (StringUtils.hasText(keyUri))) {
/* 361 */         return 
/* 362 */           ConditionOutcome.match(message.foundExactly("provided public key"));
/*     */       }
/* 364 */       return 
/* 365 */         ConditionOutcome.noMatch(message.didNotFind("provided public key").atAll());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class JwkCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 376 */       ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth JWK Condition", new Object[0]);
/*     */       
/* 378 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "security.oauth2.resource.jwk.");
/* 379 */       String keyUri = resolver.getProperty("key-set-uri");
/* 380 */       if (StringUtils.hasText(keyUri)) {
/* 381 */         return 
/* 382 */           ConditionOutcome.match(message.foundExactly("provided jwk key set URI"));
/*     */       }
/* 384 */       return 
/* 385 */         ConditionOutcome.noMatch(message.didNotFind("key jwk set URI not provided").atAll());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NotTokenInfoCondition
/*     */     extends SpringBootCondition
/*     */   {
/* 392 */     private ResourceServerTokenServicesConfiguration.TokenInfoCondition tokenInfoCondition = new ResourceServerTokenServicesConfiguration.TokenInfoCondition(null);
/*     */     
/*     */ 
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 397 */       return 
/* 398 */         ConditionOutcome.inverse(this.tokenInfoCondition.getMatchOutcome(context, metadata));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RemoteTokenCondition extends NoneNestedConditions
/*     */   {
/*     */     RemoteTokenCondition()
/*     */     {
/* 406 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     @Conditional({ResourceServerTokenServicesConfiguration.JwkCondition.class})
/*     */     static class HasJwkConfiguration {}
/*     */     
/*     */ 
/*     */     @Conditional({ResourceServerTokenServicesConfiguration.JwtTokenCondition.class})
/*     */     static class HasJwtConfiguration {}
/*     */   }
/*     */   
/*     */ 
/*     */   static class AcceptJsonRequestInterceptor
/*     */     implements ClientHttpRequestInterceptor
/*     */   {
/*     */     public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
/*     */       throws IOException
/*     */     {
/* 425 */       request.getHeaders().setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
/* 426 */       return execution.execute(request, body);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class AcceptJsonRequestEnhancer
/*     */     implements RequestEnhancer
/*     */   {
/*     */     public void enhance(AccessTokenRequest request, OAuth2ProtectedResourceDetails resource, MultiValueMap<String, String> form, HttpHeaders headers)
/*     */     {
/* 437 */       headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\ResourceServerTokenServicesConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */