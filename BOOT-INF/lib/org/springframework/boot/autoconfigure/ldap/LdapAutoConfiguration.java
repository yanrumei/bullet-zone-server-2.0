/*    */ package org.springframework.boot.autoconfigure.ldap;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.ldap.core.ContextSource;
/*    */ import org.springframework.ldap.core.support.LdapContextSource;
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
/*    */ @ConditionalOnClass({ContextSource.class})
/*    */ @EnableConfigurationProperties({LdapProperties.class})
/*    */ public class LdapAutoConfiguration
/*    */ {
/*    */   private final LdapProperties properties;
/*    */   private final Environment environment;
/*    */   
/*    */   public LdapAutoConfiguration(LdapProperties properties, Environment environment)
/*    */   {
/* 47 */     this.properties = properties;
/* 48 */     this.environment = environment;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public ContextSource ldapContextSource() {
/* 54 */     LdapContextSource source = new LdapContextSource();
/* 55 */     source.setUserDn(this.properties.getUsername());
/* 56 */     source.setPassword(this.properties.getPassword());
/* 57 */     source.setBase(this.properties.getBase());
/* 58 */     source.setUrls(this.properties.determineUrls(this.environment));
/* 59 */     source.setBaseEnvironmentProperties(
/* 60 */       Collections.unmodifiableMap(this.properties.getBaseEnvironment()));
/* 61 */     return source;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\ldap\LdapAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */