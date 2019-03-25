/*    */ package org.springframework.boot.autoconfigure.jdbc;
/*    */ 
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureOrder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*    */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ import org.springframework.jdbc.datasource.DataSourceTransactionManager;
/*    */ import org.springframework.transaction.PlatformTransactionManager;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({JdbcTemplate.class, PlatformTransactionManager.class})
/*    */ @AutoConfigureOrder(Integer.MAX_VALUE)
/*    */ @EnableConfigurationProperties({DataSourceProperties.class})
/*    */ public class DataSourceTransactionManagerAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnSingleCandidate(DataSource.class)
/*    */   static class DataSourceTransactionManagerConfiguration
/*    */   {
/*    */     private final DataSource dataSource;
/*    */     private final TransactionManagerCustomizers transactionManagerCustomizers;
/*    */     
/*    */     DataSourceTransactionManagerConfiguration(DataSource dataSource, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*    */     {
/* 61 */       this.dataSource = dataSource;
/* 62 */       this.transactionManagerCustomizers = 
/* 63 */         ((TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*    */     }
/*    */     
/*    */     @Bean
/*    */     @ConditionalOnMissingBean({PlatformTransactionManager.class})
/*    */     public DataSourceTransactionManager transactionManager(DataSourceProperties properties)
/*    */     {
/* 70 */       DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(this.dataSource);
/*    */       
/* 72 */       if (this.transactionManagerCustomizers != null) {
/* 73 */         this.transactionManagerCustomizers.customize(transactionManager);
/*    */       }
/* 75 */       return transactionManager;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceTransactionManagerAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */