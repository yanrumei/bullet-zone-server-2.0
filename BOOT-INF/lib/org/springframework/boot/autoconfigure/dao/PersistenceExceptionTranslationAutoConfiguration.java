/*    */ package org.springframework.boot.autoconfigure.dao;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
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
/*    */ @ConditionalOnClass({PersistenceExceptionTranslationPostProcessor.class})
/*    */ public class PersistenceExceptionTranslationAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({PersistenceExceptionTranslationPostProcessor.class})
/*    */   @ConditionalOnProperty(prefix="spring.dao.exceptiontranslation", name={"enabled"}, matchIfMissing=true)
/*    */   public static PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(Environment environment)
/*    */   {
/* 44 */     PersistenceExceptionTranslationPostProcessor postProcessor = new PersistenceExceptionTranslationPostProcessor();
/* 45 */     postProcessor.setProxyTargetClass(determineProxyTargetClass(environment));
/* 46 */     return postProcessor;
/*    */   }
/*    */   
/*    */   private static boolean determineProxyTargetClass(Environment environment) {
/* 50 */     RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.aop.");
/*    */     
/* 52 */     Boolean value = (Boolean)resolver.getProperty("proxyTargetClass", Boolean.class);
/* 53 */     return value != null ? value.booleanValue() : true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\dao\PersistenceExceptionTranslationAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */