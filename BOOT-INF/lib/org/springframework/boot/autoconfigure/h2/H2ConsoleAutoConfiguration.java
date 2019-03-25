/*     */ package org.springframework.boot.autoconfigure.h2;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.h2.server.web.WebServlet;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.security.SecurityAuthorizeMode;
/*     */ import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.security.SecurityProperties;
/*     */ import org.springframework.boot.autoconfigure.security.SecurityProperties.Basic;
/*     */ import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.web.servlet.ServletRegistrationBean;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.security.config.annotation.ObjectPostProcessor;
/*     */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*     */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*     */ import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
/*     */ import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
/*     */ import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
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
/*     */ @ConditionalOnWebApplication
/*     */ @ConditionalOnClass({WebServlet.class})
/*     */ @ConditionalOnProperty(prefix="spring.h2.console", name={"enabled"}, havingValue="true", matchIfMissing=false)
/*     */ @EnableConfigurationProperties({H2ConsoleProperties.class})
/*     */ @AutoConfigureAfter({SecurityAutoConfiguration.class})
/*     */ public class H2ConsoleAutoConfiguration
/*     */ {
/*     */   private final H2ConsoleProperties properties;
/*     */   
/*     */   public H2ConsoleAutoConfiguration(H2ConsoleProperties properties)
/*     */   {
/*  59 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public ServletRegistrationBean h2Console() {
/*  64 */     String path = this.properties.getPath();
/*  65 */     String urlMapping = path + "/*";
/*  66 */     ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet(), new String[] { urlMapping });
/*     */     
/*  68 */     H2ConsoleProperties.Settings settings = this.properties.getSettings();
/*  69 */     if (settings.isTrace()) {
/*  70 */       registration.addInitParameter("trace", "");
/*     */     }
/*  72 */     if (settings.isWebAllowOthers()) {
/*  73 */       registration.addInitParameter("webAllowOthers", "");
/*     */     }
/*  75 */     return registration;
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({WebSecurityConfigurerAdapter.class})
/*     */   @ConditionalOnBean({ObjectPostProcessor.class})
/*     */   @ConditionalOnProperty(prefix="security.basic", name={"enabled"}, matchIfMissing=true)
/*     */   static class H2ConsoleSecurityConfiguration
/*     */   {
/*     */     @Bean
/*     */     public WebSecurityConfigurerAdapter h2ConsoleSecurityConfigurer() {
/*  86 */       return new H2ConsoleSecurityConfigurer(null);
/*     */     }
/*     */     
/*     */     @Order(2147483632)
/*     */     private static class H2ConsoleSecurityConfigurer
/*     */       extends WebSecurityConfigurerAdapter
/*     */     {
/*     */       @Autowired
/*     */       private H2ConsoleProperties console;
/*     */       @Autowired
/*     */       private SecurityProperties security;
/*     */       
/*     */       public void configure(HttpSecurity http)
/*     */         throws Exception
/*     */       {
/* 101 */         String path = this.console.getPath();
/* 102 */         String antPattern = path + "/**";
/* 103 */         HttpSecurity h2Console = http.antMatcher(antPattern);
/* 104 */         h2Console.csrf().disable();
/* 105 */         h2Console.httpBasic();
/* 106 */         h2Console.headers().frameOptions().sameOrigin();
/* 107 */         String[] roles = (String[])this.security.getUser().getRole().toArray(new String[0]);
/* 108 */         SecurityAuthorizeMode mode = this.security.getBasic().getAuthorizeMode();
/* 109 */         if ((mode == null) || (mode == SecurityAuthorizeMode.ROLE)) {
/* 110 */           ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).hasAnyRole(roles);
/*     */         }
/* 112 */         else if (mode == SecurityAuthorizeMode.AUTHENTICATED) {
/* 113 */           ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\h2\H2ConsoleAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */