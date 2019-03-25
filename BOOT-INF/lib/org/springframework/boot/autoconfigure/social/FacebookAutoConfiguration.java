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
/*    */ import org.springframework.social.facebook.api.Facebook;
/*    */ import org.springframework.social.facebook.connect.FacebookConnectionFactory;
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
/*    */ @ConditionalOnClass({SocialConfigurerAdapter.class, FacebookConnectionFactory.class})
/*    */ @ConditionalOnProperty(prefix="spring.social.facebook", name={"app-id"})
/*    */ @AutoConfigureBefore({SocialWebAutoConfiguration.class})
/*    */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*    */ public class FacebookAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @EnableSocial
/*    */   @EnableConfigurationProperties({FacebookProperties.class})
/*    */   @ConditionalOnWebApplication
/*    */   protected static class FacebookConfigurerAdapter
/*    */     extends SocialAutoConfigurerAdapter
/*    */   {
/*    */     private final FacebookProperties properties;
/*    */     
/*    */     protected FacebookConfigurerAdapter(FacebookProperties properties)
/*    */     {
/* 64 */       this.properties = properties;
/*    */     }
/*    */     
/*    */     @Bean
/*    */     @ConditionalOnMissingBean({Facebook.class})
/*    */     @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
/*    */     public Facebook facebook(ConnectionRepository repository)
/*    */     {
/* 72 */       Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
/* 73 */       return connection != null ? (Facebook)connection.getApi() : null;
/*    */     }
/*    */     
/*    */     @Bean(name={"connect/facebookConnect", "connect/facebookConnected"})
/*    */     @ConditionalOnProperty(prefix="spring.social", name={"auto-connection-views"})
/*    */     public GenericConnectionStatusView facebookConnectView() {
/* 79 */       return new GenericConnectionStatusView("facebook", "Facebook");
/*    */     }
/*    */     
/*    */     protected ConnectionFactory<?> createConnectionFactory()
/*    */     {
/* 84 */       return new FacebookConnectionFactory(this.properties.getAppId(), this.properties
/* 85 */         .getAppSecret());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\social\FacebookAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */