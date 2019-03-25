/*    */ package org.springframework.boot.autoconfigure.security;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
/*    */ import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
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
/*    */ @ConditionalOnClass({SecurityEvaluationContextExtension.class, EvaluationContextExtensionSupport.class})
/*    */ public class SecurityDataConfiguration
/*    */ {
/*    */   @ConditionalOnMissingBean({SecurityEvaluationContextExtension.class})
/*    */   @Bean
/*    */   public SecurityEvaluationContextExtension securityEvaluationContextExtension()
/*    */   {
/* 40 */     return new SecurityEvaluationContextExtension();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\SecurityDataConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */