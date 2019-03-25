/*    */ package org.springframework.boot.autoconfigure.transaction;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*    */ import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
/*    */ import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
/*    */ import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
/*    */ import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.transaction.PlatformTransactionManager;
/*    */ import org.springframework.transaction.annotation.AbstractTransactionManagementConfiguration;
/*    */ import org.springframework.transaction.annotation.EnableTransactionManagement;
/*    */ import org.springframework.transaction.support.TransactionTemplate;
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
/*    */ @ConditionalOnClass({PlatformTransactionManager.class})
/*    */ @AutoConfigureAfter({JtaAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, Neo4jDataAutoConfiguration.class})
/*    */ @EnableConfigurationProperties({TransactionProperties.class})
/*    */ public class TransactionAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnBean({PlatformTransactionManager.class})
/*    */   @ConditionalOnMissingBean({AbstractTransactionManagementConfiguration.class})
/*    */   public static class EnableTransactionManagementConfiguration
/*    */   {
/*    */     @Configuration
/*    */     @EnableTransactionManagement(proxyTargetClass=true)
/*    */     @ConditionalOnProperty(prefix="spring.aop", name={"proxy-target-class"}, havingValue="true", matchIfMissing=true)
/*    */     public static class CglibAutoProxyConfiguration {}
/*    */     
/*    */     @Configuration
/*    */     @EnableTransactionManagement(proxyTargetClass=false)
/*    */     @ConditionalOnProperty(prefix="spring.aop", name={"proxy-target-class"}, havingValue="false", matchIfMissing=false)
/*    */     public static class JdkDynamicAutoProxyConfiguration {}
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public TransactionManagerCustomizers platformTransactionManagerCustomizers(ObjectProvider<List<PlatformTransactionManagerCustomizer<?>>> customizers)
/*    */   {
/* 59 */     return new TransactionManagerCustomizers((Collection)customizers.getIfAvailable());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Configuration
/*    */   @ConditionalOnSingleCandidate(PlatformTransactionManager.class)
/*    */   public static class TransactionTemplateConfiguration
/*    */   {
/*    */     public TransactionTemplateConfiguration(PlatformTransactionManager transactionManager)
/*    */     {
/* 70 */       this.transactionManager = transactionManager;
/*    */     }
/*    */     
/*    */     private final PlatformTransactionManager transactionManager;
/*    */     @Bean
/*    */     @ConditionalOnMissingBean
/* 76 */     public TransactionTemplate transactionTemplate() { return new TransactionTemplate(this.transactionManager); }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\TransactionAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */