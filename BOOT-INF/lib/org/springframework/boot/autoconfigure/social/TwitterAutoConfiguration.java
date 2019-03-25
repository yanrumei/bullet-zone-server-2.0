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
/*    */ import org.springframework.social.twitter.api.Twitter;
/*    */ import org.springframework.social.twitter.api.impl.TwitterTemplate;
/*    */ import org.springframework.social.twitter.connect.TwitterConnectionFactory;
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
/*    */ @ConditionalOnClass({SocialConfigurerAdapter.class, TwitterConnectionFactory.class})
/*    */ @ConditionalOnProperty(prefix="spring.social.twitter", name={"app-id"})
/*    */ @AutoConfigureBefore({SocialWebAutoConfiguration.class})
/*    */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*    */ public class TwitterAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @EnableSocial
/*    */   @EnableConfigurationProperties({TwitterProperties.class})
/*    */   @ConditionalOnWebApplication
/*    */   protected static class TwitterConfigurerAdapter
/*    */     extends SocialAutoConfigurerAdapter
/*    */   {
/*    */     private final TwitterProperties properties;
/*    */     
/*    */     protected TwitterConfigurerAdapter(TwitterProperties properties)
/*    */     {
/* 65 */       this.properties = properties;
/*    */     }
/*    */     
/*    */     @Bean
/*    */     @ConditionalOnMissingBean
/*    */     @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
/*    */     public Twitter twitter(ConnectionRepository repository)
/*    */     {
/* 73 */       Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
/* 74 */       if (connection != null) {
/* 75 */         return (Twitter)connection.getApi();
/*    */       }
/* 77 */       return new TwitterTemplate(this.properties.getAppId(), this.properties
/* 78 */         .getAppSecret());
/*    */     }
/*    */     
/*    */     @Bean(name={"connect/twitterConnect", "connect/twitterConnected"})
/*    */     @ConditionalOnProperty(prefix="spring.social", name={"auto-connection-views"})
/*    */     public GenericConnectionStatusView twitterConnectView() {
/* 84 */       return new GenericConnectionStatusView("twitter", "Twitter");
/*    */     }
/*    */     
/*    */     protected ConnectionFactory<?> createConnectionFactory()
/*    */     {
/* 89 */       return new TwitterConnectionFactory(this.properties.getAppId(), this.properties
/* 90 */         .getAppSecret());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\social\TwitterAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */