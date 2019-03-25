/*    */ package org.springframework.boot.autoconfigure.social;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Scope;
/*    */ import org.springframework.context.annotation.ScopedProxyMode;
/*    */ import org.springframework.social.config.annotation.EnableSocial;
/*    */ import org.springframework.social.config.annotation.SocialConfigurerAdapter;
/*    */ import org.springframework.social.connect.Connection;
/*    */ import org.springframework.social.connect.ConnectionFactory;
/*    */ import org.springframework.social.connect.ConnectionRepository;
/*    */ import org.springframework.social.connect.web.GenericConnectionStatusView;
/*    */ import org.springframework.social.linkedin.api.LinkedIn;
/*    */ import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
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
/*    */ @ConditionalOnClass({SocialConfigurerAdapter.class, LinkedInConnectionFactory.class})
/*    */ @ConditionalOnProperty(prefix="spring.social.linkedin", name={"app-id"})
/*    */ @AutoConfigureBefore({SocialWebAutoConfiguration.class})
/*    */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*    */ public class LinkedInAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @EnableSocial
/*    */   @EnableConfigurationProperties({LinkedInProperties.class})
/*    */   @ConditionalOnWebApplication
/*    */   protected static class LinkedInConfigurerAdapter
/*    */     extends SocialAutoConfigurerAdapter
/*    */   {
/*    */     private final LinkedInProperties properties;
/*    */     
/*    */     protected LinkedInConfigurerAdapter(LinkedInProperties properties)
/*    */     {
/* 64 */       this.properties = properties;
/*    */     }
/*    */     
/*    */     @Bean
/*    */     @ConditionalOnMissingBean({LinkedIn.class})
/*    */     @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
/*    */     public LinkedIn linkedin(ConnectionRepository repository)
/*    */     {
/* 72 */       Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
/* 73 */       return connection != null ? (LinkedIn)connection.getApi() : null;
/*    */     }
/*    */     
/*    */     @Bean(name={"connect/linkedinConnect", "connect/linkedinConnected"})
/*    */     @ConditionalOnProperty(prefix="spring.social", name={"auto-connection-views"})
/*    */     public GenericConnectionStatusView linkedInConnectView() {
/* 79 */       return new GenericConnectionStatusView("linkedin", "LinkedIn");
/*    */     }
/*    */     
/*    */     protected ConnectionFactory<?> createConnectionFactory()
/*    */     {
/* 84 */       return new LinkedInConnectionFactory(this.properties.getAppId(), this.properties
/* 85 */         .getAppSecret());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\social\LinkedInAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */