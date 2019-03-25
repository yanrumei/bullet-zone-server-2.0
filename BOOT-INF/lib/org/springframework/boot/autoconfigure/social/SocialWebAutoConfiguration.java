/*     */ package org.springframework.boot.autoconfigure.social;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.security.core.Authentication;
/*     */ import org.springframework.security.core.context.SecurityContext;
/*     */ import org.springframework.security.core.context.SecurityContextHolder;
/*     */ import org.springframework.social.UserIdSource;
/*     */ import org.springframework.social.config.annotation.EnableSocial;
/*     */ import org.springframework.social.config.annotation.SocialConfigurerAdapter;
/*     */ import org.springframework.social.connect.ConnectionFactoryLocator;
/*     */ import org.springframework.social.connect.ConnectionRepository;
/*     */ import org.springframework.social.connect.UsersConnectionRepository;
/*     */ import org.springframework.social.connect.web.ConnectController;
/*     */ import org.springframework.social.connect.web.ConnectInterceptor;
/*     */ import org.springframework.social.connect.web.DisconnectInterceptor;
/*     */ import org.springframework.social.connect.web.ProviderSignInController;
/*     */ import org.springframework.social.connect.web.ProviderSignInInterceptor;
/*     */ import org.springframework.social.connect.web.SignInAdapter;
/*     */ import org.springframework.social.connect.web.thymeleaf.SpringSocialDialect;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.servlet.view.BeanNameViewResolver;
/*     */ import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({ConnectController.class, SocialConfigurerAdapter.class})
/*     */ @ConditionalOnBean({ConnectionFactoryLocator.class, UsersConnectionRepository.class})
/*     */ @AutoConfigureBefore({ThymeleafAutoConfiguration.class})
/*     */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*     */ public class SocialWebAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @EnableSocial
/*     */   @ConditionalOnWebApplication
/*     */   protected static class SocialAutoConfigurationAdapter
/*     */     extends SocialConfigurerAdapter
/*     */   {
/*     */     private final List<ConnectInterceptor<?>> connectInterceptors;
/*     */     private final List<DisconnectInterceptor<?>> disconnectInterceptors;
/*     */     private final List<ProviderSignInInterceptor<?>> signInInterceptors;
/*     */     
/*     */     public SocialAutoConfigurationAdapter(ObjectProvider<List<ConnectInterceptor<?>>> connectInterceptorsProvider, ObjectProvider<List<DisconnectInterceptor<?>>> disconnectInterceptorsProvider, ObjectProvider<List<ProviderSignInInterceptor<?>>> signInInterceptorsProvider)
/*     */     {
/*  88 */       this.connectInterceptors = ((List)connectInterceptorsProvider.getIfAvailable());
/*  89 */       this.disconnectInterceptors = ((List)disconnectInterceptorsProvider.getIfAvailable());
/*  90 */       this.signInInterceptors = ((List)signInInterceptorsProvider.getIfAvailable());
/*     */     }
/*     */     
/*     */ 
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({ConnectController.class})
/*     */     public ConnectController connectController(ConnectionFactoryLocator factoryLocator, ConnectionRepository repository)
/*     */     {
/*  98 */       ConnectController controller = new ConnectController(factoryLocator, repository);
/*     */       
/* 100 */       if (!CollectionUtils.isEmpty(this.connectInterceptors)) {
/* 101 */         controller.setConnectInterceptors(this.connectInterceptors);
/*     */       }
/* 103 */       if (!CollectionUtils.isEmpty(this.disconnectInterceptors)) {
/* 104 */         controller.setDisconnectInterceptors(this.disconnectInterceptors);
/*     */       }
/* 106 */       return controller;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     @ConditionalOnProperty(prefix="spring.social", name={"auto-connection-views"})
/*     */     public BeanNameViewResolver beanNameViewResolver() {
/* 113 */       BeanNameViewResolver viewResolver = new BeanNameViewResolver();
/* 114 */       viewResolver.setOrder(Integer.MIN_VALUE);
/* 115 */       return viewResolver;
/*     */     }
/*     */     
/*     */ 
/*     */     @Bean
/*     */     @ConditionalOnBean({SignInAdapter.class})
/*     */     @ConditionalOnMissingBean
/*     */     public ProviderSignInController signInController(ConnectionFactoryLocator factoryLocator, UsersConnectionRepository usersRepository, SignInAdapter signInAdapter)
/*     */     {
/* 124 */       ProviderSignInController controller = new ProviderSignInController(factoryLocator, usersRepository, signInAdapter);
/*     */       
/* 126 */       if (!CollectionUtils.isEmpty(this.signInInterceptors)) {
/* 127 */         controller.setSignInInterceptors(this.signInInterceptors);
/*     */       }
/* 129 */       return controller;
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @EnableSocial
/*     */   @ConditionalOnWebApplication
/*     */   @ConditionalOnMissingClass({"org.springframework.security.core.context.SecurityContextHolder"})
/*     */   protected static class AnonymousUserIdSourceConfig
/*     */     extends SocialConfigurerAdapter
/*     */   {
/*     */     public UserIdSource getUserIdSource()
/*     */     {
/* 142 */       new UserIdSource()
/*     */       {
/*     */         public String getUserId() {
/* 145 */           return "anonymous";
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @EnableSocial
/*     */   @ConditionalOnWebApplication
/*     */   @ConditionalOnClass({SecurityContextHolder.class})
/*     */   protected static class AuthenticationUserIdSourceConfig
/*     */     extends SocialConfigurerAdapter
/*     */   {
/*     */     public UserIdSource getUserIdSource()
/*     */     {
/* 161 */       return new SocialWebAutoConfiguration.SecurityContextUserIdSource(null);
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({SpringResourceResourceResolver.class})
/*     */   protected static class SpringSocialThymeleafConfig
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     public SpringSocialDialect springSocialDialect()
/*     */     {
/* 173 */       return new SpringSocialDialect();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SecurityContextUserIdSource
/*     */     implements UserIdSource
/*     */   {
/*     */     public String getUserId()
/*     */     {
/* 182 */       SecurityContext context = SecurityContextHolder.getContext();
/* 183 */       Authentication authentication = context.getAuthentication();
/* 184 */       Assert.state(authentication != null, "Unable to get a ConnectionRepository: no user signed in");
/*     */       
/* 186 */       return authentication.getName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\social\SocialWebAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */