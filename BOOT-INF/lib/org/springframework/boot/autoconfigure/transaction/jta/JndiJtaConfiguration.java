/*    */ package org.springframework.boot.autoconfigure.transaction.jta;
/*    */ 
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.transaction.PlatformTransactionManager;
/*    */ import org.springframework.transaction.config.JtaTransactionManagerFactoryBean;
/*    */ import org.springframework.transaction.jta.JtaTransactionManager;
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
/*    */ @ConditionalOnClass({JtaTransactionManager.class})
/*    */ @ConditionalOnJndi({"java:comp/UserTransaction", "java:comp/TransactionManager", "java:appserver/TransactionManager", "java:pm/TransactionManager", "java:/TransactionManager"})
/*    */ @ConditionalOnMissingBean({PlatformTransactionManager.class})
/*    */ class JndiJtaConfiguration
/*    */ {
/*    */   private final TransactionManagerCustomizers transactionManagerCustomizers;
/*    */   
/*    */   JndiJtaConfiguration(ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*    */   {
/* 51 */     this.transactionManagerCustomizers = ((TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public JtaTransactionManager transactionManager()
/*    */   {
/* 57 */     JtaTransactionManager jtaTransactionManager = new JtaTransactionManagerFactoryBean().getObject();
/* 58 */     if (this.transactionManagerCustomizers != null) {
/* 59 */       this.transactionManagerCustomizers.customize(jtaTransactionManager);
/*    */     }
/* 61 */     return jtaTransactionManager;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\jta\JndiJtaConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */