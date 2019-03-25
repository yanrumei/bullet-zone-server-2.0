/*    */ package org.springframework.boot.autoconfigure.data.ldap;
/*    */ 
/*    */ import javax.naming.ldap.LdapContext;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.data.ldap.repository.LdapRepository;
/*    */ import org.springframework.ldap.core.ContextSource;
/*    */ import org.springframework.ldap.core.LdapOperations;
/*    */ import org.springframework.ldap.core.LdapTemplate;
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
/*    */ @ConditionalOnClass({LdapContext.class, LdapRepository.class})
/*    */ @AutoConfigureAfter({LdapAutoConfiguration.class})
/*    */ public class LdapDataAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({LdapOperations.class})
/*    */   public LdapTemplate ldapTemplate(ContextSource contextSource)
/*    */   {
/* 47 */     return new LdapTemplate(contextSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\ldap\LdapDataAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */