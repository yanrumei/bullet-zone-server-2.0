/*    */ package org.springframework.boot.autoconfigure.security.oauth2.resource;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*    */ import org.springframework.security.oauth2.client.OAuth2ClientContext;
/*    */ import org.springframework.security.oauth2.client.OAuth2RestTemplate;
/*    */ import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
/*    */ import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
/*    */ import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
/*    */ import org.springframework.util.CollectionUtils;
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
/*    */ public class DefaultUserInfoRestTemplateFactory
/*    */   implements UserInfoRestTemplateFactory
/*    */ {
/*    */   private static final AuthorizationCodeResourceDetails DEFAULT_RESOURCE_DETAILS;
/*    */   private final List<UserInfoRestTemplateCustomizer> customizers;
/*    */   private final OAuth2ProtectedResourceDetails details;
/*    */   private final OAuth2ClientContext oauth2ClientContext;
/*    */   private OAuth2RestTemplate oauth2RestTemplate;
/*    */   
/*    */   static
/*    */   {
/* 45 */     AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
/* 46 */     details.setClientId("<N/A>");
/* 47 */     details.setUserAuthorizationUri("Not a URI because there is no client");
/* 48 */     details.setAccessTokenUri("Not a URI because there is no client");
/* 49 */     DEFAULT_RESOURCE_DETAILS = details;
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
/*    */ 
/*    */ 
/*    */   public DefaultUserInfoRestTemplateFactory(ObjectProvider<List<UserInfoRestTemplateCustomizer>> customizers, ObjectProvider<OAuth2ProtectedResourceDetails> details, ObjectProvider<OAuth2ClientContext> oauth2ClientContext)
/*    */   {
/* 64 */     this.customizers = ((List)customizers.getIfAvailable());
/* 65 */     this.details = ((OAuth2ProtectedResourceDetails)details.getIfAvailable());
/* 66 */     this.oauth2ClientContext = ((OAuth2ClientContext)oauth2ClientContext.getIfAvailable());
/*    */   }
/*    */   
/*    */   public OAuth2RestTemplate getUserInfoRestTemplate()
/*    */   {
/* 71 */     if (this.oauth2RestTemplate == null) {
/* 72 */       this.oauth2RestTemplate = createOAuth2RestTemplate(this.details == null ? DEFAULT_RESOURCE_DETAILS : this.details);
/*    */       
/* 74 */       this.oauth2RestTemplate.getInterceptors()
/* 75 */         .add(new ResourceServerTokenServicesConfiguration.AcceptJsonRequestInterceptor());
/* 76 */       AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
/* 77 */       accessTokenProvider.setTokenRequestEnhancer(new ResourceServerTokenServicesConfiguration.AcceptJsonRequestEnhancer());
/* 78 */       this.oauth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
/* 79 */       if (!CollectionUtils.isEmpty(this.customizers)) {
/* 80 */         AnnotationAwareOrderComparator.sort(this.customizers);
/* 81 */         for (UserInfoRestTemplateCustomizer customizer : this.customizers) {
/* 82 */           customizer.customize(this.oauth2RestTemplate);
/*    */         }
/*    */       }
/*    */     }
/* 86 */     return this.oauth2RestTemplate;
/*    */   }
/*    */   
/*    */   private OAuth2RestTemplate createOAuth2RestTemplate(OAuth2ProtectedResourceDetails details)
/*    */   {
/* 91 */     if (this.oauth2ClientContext == null) {
/* 92 */       return new OAuth2RestTemplate(details);
/*    */     }
/* 94 */     return new OAuth2RestTemplate(details, this.oauth2ClientContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\DefaultUserInfoRestTemplateFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */