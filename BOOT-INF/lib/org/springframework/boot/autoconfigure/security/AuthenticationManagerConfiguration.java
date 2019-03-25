/*     */ package org.springframework.boot.autoconfigure.security;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Primary;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.security.authentication.AuthenticationEventPublisher;
/*     */ import org.springframework.security.authentication.AuthenticationManager;
/*     */ import org.springframework.security.authentication.ProviderManager;
/*     */ import org.springframework.security.config.annotation.ObjectPostProcessor;
/*     */ import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
/*     */ import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
/*     */ import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
/*     */ import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
/*     */ import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnBean({ObjectPostProcessor.class})
/*     */ @ConditionalOnMissingBean({AuthenticationManager.class})
/*     */ @Order(0)
/*     */ public class AuthenticationManagerConfiguration
/*     */ {
/*  72 */   private static final Log logger = LogFactory.getLog(AuthenticationManagerConfiguration.class);
/*     */   
/*     */   @Bean
/*     */   @Primary
/*     */   public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
/*     */   {
/*  78 */     return configuration.getAuthenticationManager();
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   public static SpringBootAuthenticationConfigurerAdapter springBootAuthenticationConfigurerAdapter(SecurityProperties securityProperties, List<SecurityPrerequisite> dependencies)
/*     */   {
/*  85 */     return new SpringBootAuthenticationConfigurerAdapter(securityProperties);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public AuthenticationManagerConfigurationListener authenticationManagerConfigurationListener() {
/*  90 */     return new AuthenticationManagerConfigurationListener();
/*     */   }
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
/*     */   @Order(2147483547)
/*     */   private static class SpringBootAuthenticationConfigurerAdapter
/*     */     extends GlobalAuthenticationConfigurerAdapter
/*     */   {
/*     */     private final SecurityProperties securityProperties;
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
/*     */     SpringBootAuthenticationConfigurerAdapter(SecurityProperties securityProperties)
/*     */     {
/* 124 */       this.securityProperties = securityProperties;
/*     */     }
/*     */     
/*     */     public void init(AuthenticationManagerBuilder auth) throws Exception
/*     */     {
/* 129 */       auth.apply(new AuthenticationManagerConfiguration.DefaultInMemoryUserDetailsManagerConfigurer(this.securityProperties));
/*     */     }
/*     */   }
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
/*     */   private static class DefaultInMemoryUserDetailsManagerConfigurer
/*     */     extends InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>
/*     */   {
/*     */     private final SecurityProperties securityProperties;
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
/*     */     DefaultInMemoryUserDetailsManagerConfigurer(SecurityProperties securityProperties)
/*     */     {
/* 163 */       this.securityProperties = securityProperties;
/*     */     }
/*     */     
/*     */     public void configure(AuthenticationManagerBuilder auth) throws Exception
/*     */     {
/* 168 */       if (auth.isConfigured()) {
/* 169 */         return;
/*     */       }
/* 171 */       SecurityProperties.User user = this.securityProperties.getUser();
/* 172 */       if (user.isDefaultPassword()) {
/* 173 */         AuthenticationManagerConfiguration.logger.info(String.format("%n%nUsing default security password: %s%n", new Object[] {user
/* 174 */           .getPassword() }));
/*     */       }
/* 176 */       Set<String> roles = new LinkedHashSet(user.getRole());
/* 177 */       withUser(user.getName()).password(user.getPassword())
/* 178 */         .roles((String[])roles.toArray(new String[roles.size()]));
/* 179 */       setField(auth, "defaultUserDetailsService", getUserDetailsService());
/* 180 */       super.configure(auth);
/*     */     }
/*     */     
/*     */     private void setField(Object target, String name, Object value) {
/*     */       try {
/* 185 */         Field field = ReflectionUtils.findField(target.getClass(), name);
/* 186 */         ReflectionUtils.makeAccessible(field);
/* 187 */         ReflectionUtils.setField(field, target, value);
/*     */       }
/*     */       catch (Exception ex) {
/* 190 */         AuthenticationManagerConfiguration.logger.info("Could not set " + name);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class AuthenticationManagerConfigurationListener
/*     */     implements SmartInitializingSingleton
/*     */   {
/*     */     @Autowired
/*     */     private AuthenticationEventPublisher eventPublisher;
/*     */     
/*     */ 
/*     */     @Autowired
/*     */     private ApplicationContext context;
/*     */     
/*     */ 
/*     */     public void afterSingletonsInstantiated()
/*     */     {
/*     */       try
/*     */       {
/* 212 */         configureAuthenticationManager(
/* 213 */           (AuthenticationManager)this.context.getBean(AuthenticationManager.class));
/*     */       }
/*     */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*     */     }
/*     */     
/*     */ 
/*     */     private void configureAuthenticationManager(AuthenticationManager manager)
/*     */     {
/* 221 */       if ((manager instanceof ProviderManager))
/*     */       {
/* 223 */         ((ProviderManager)manager).setAuthenticationEventPublisher(this.eventPublisher);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\AuthenticationManagerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */