/*     */ package org.springframework.boot.autoconfigure.security.oauth2.client;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
/*     */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
/*     */ import org.springframework.security.oauth2.client.OAuth2RestOperations;
/*     */ import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
/*     */ import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
/*     */ import org.springframework.security.web.DefaultSecurityFilterChain;
/*     */ import org.springframework.security.web.authentication.HttpStatusEntryPoint;
/*     */ import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
/*     */ import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
/*     */ import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
/*     */ import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
/*     */ import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
/*     */ import org.springframework.web.accept.ContentNegotiationStrategy;
/*     */ import org.springframework.web.accept.HeaderContentNegotiationStrategy;
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
/*     */ class SsoSecurityConfigurer
/*     */ {
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */   SsoSecurityConfigurer(ApplicationContext applicationContext)
/*     */   {
/*  51 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   public void configure(HttpSecurity http) throws Exception
/*     */   {
/*  56 */     OAuth2SsoProperties sso = (OAuth2SsoProperties)this.applicationContext.getBean(OAuth2SsoProperties.class);
/*     */     
/*     */ 
/*  59 */     http.apply(new OAuth2ClientAuthenticationConfigurer(oauth2SsoFilter(sso)));
/*  60 */     addAuthenticationEntryPoint(http, sso);
/*     */   }
/*     */   
/*     */   private void addAuthenticationEntryPoint(HttpSecurity http, OAuth2SsoProperties sso) throws Exception
/*     */   {
/*  65 */     ExceptionHandlingConfigurer<HttpSecurity> exceptions = http.exceptionHandling();
/*     */     
/*  67 */     ContentNegotiationStrategy contentNegotiationStrategy = (ContentNegotiationStrategy)http.getSharedObject(ContentNegotiationStrategy.class);
/*  68 */     if (contentNegotiationStrategy == null) {
/*  69 */       contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
/*     */     }
/*  71 */     MediaTypeRequestMatcher preferredMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy, new MediaType[] { MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML, MediaType.TEXT_PLAIN });
/*     */     
/*     */ 
/*  74 */     preferredMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
/*  75 */     exceptions.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint(sso
/*  76 */       .getLoginPath()), preferredMatcher);
/*     */     
/*     */ 
/*  79 */     exceptions.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private OAuth2ClientAuthenticationProcessingFilter oauth2SsoFilter(OAuth2SsoProperties sso)
/*     */   {
/*  87 */     OAuth2RestOperations restTemplate = ((UserInfoRestTemplateFactory)this.applicationContext.getBean(UserInfoRestTemplateFactory.class)).getUserInfoRestTemplate();
/*     */     
/*  89 */     ResourceServerTokenServices tokenServices = (ResourceServerTokenServices)this.applicationContext.getBean(ResourceServerTokenServices.class);
/*     */     
/*  91 */     OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(sso.getLoginPath());
/*  92 */     filter.setRestTemplate(restTemplate);
/*  93 */     filter.setTokenServices(tokenServices);
/*  94 */     filter.setApplicationEventPublisher(this.applicationContext);
/*  95 */     return filter;
/*     */   }
/*     */   
/*     */   private static class OAuth2ClientAuthenticationConfigurer
/*     */     extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>
/*     */   {
/*     */     private OAuth2ClientAuthenticationProcessingFilter filter;
/*     */     
/*     */     OAuth2ClientAuthenticationConfigurer(OAuth2ClientAuthenticationProcessingFilter filter)
/*     */     {
/* 105 */       this.filter = filter;
/*     */     }
/*     */     
/*     */     public void configure(HttpSecurity builder) throws Exception
/*     */     {
/* 110 */       OAuth2ClientAuthenticationProcessingFilter ssoFilter = this.filter;
/* 111 */       ssoFilter.setSessionAuthenticationStrategy(
/* 112 */         (SessionAuthenticationStrategy)builder.getSharedObject(SessionAuthenticationStrategy.class));
/* 113 */       builder.addFilterAfter(ssoFilter, AbstractPreAuthenticatedProcessingFilter.class);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\client\SsoSecurityConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */