/*    */ package org.springframework.boot.autoconfigure.security.oauth2.client;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*    */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*    */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
/*    */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @Conditional({NeedsWebSecurityCondition.class})
/*    */ public class OAuth2SsoDefaultConfiguration
/*    */   extends WebSecurityConfigurerAdapter
/*    */   implements Ordered
/*    */ {
/*    */   private final ApplicationContext applicationContext;
/*    */   private final OAuth2SsoProperties sso;
/*    */   
/*    */   public OAuth2SsoDefaultConfiguration(ApplicationContext applicationContext, OAuth2SsoProperties sso)
/*    */   {
/* 52 */     this.applicationContext = applicationContext;
/* 53 */     this.sso = sso;
/*    */   }
/*    */   
/*    */   protected void configure(HttpSecurity http) throws Exception
/*    */   {
/* 58 */     ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.antMatcher("/**").authorizeRequests().anyRequest()).authenticated();
/* 59 */     new SsoSecurityConfigurer(this.applicationContext).configure(http);
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 64 */     if (this.sso.getFilterOrder() != null) {
/* 65 */       return this.sso.getFilterOrder().intValue();
/*    */     }
/* 67 */     if (ClassUtils.isPresent("org.springframework.boot.actuate.autoconfigure.ManagementServerProperties", null))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 72 */       return 2147483635;
/*    */     }
/* 74 */     return 2147483640;
/*    */   }
/*    */   
/*    */   protected static class NeedsWebSecurityCondition
/*    */     extends EnableOAuth2SsoCondition
/*    */   {
/*    */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */     {
/* 82 */       return ConditionOutcome.inverse(super.getMatchOutcome(context, metadata));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\client\OAuth2SsoDefaultConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */