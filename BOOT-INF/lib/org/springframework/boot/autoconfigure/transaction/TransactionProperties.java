/*    */ package org.springframework.boot.autoconfigure.transaction;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.transaction.support.AbstractPlatformTransactionManager;
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
/*    */ 
/*    */ @ConfigurationProperties(prefix="spring.transaction")
/*    */ public class TransactionProperties
/*    */   implements PlatformTransactionManagerCustomizer<AbstractPlatformTransactionManager>
/*    */ {
/*    */   private Integer defaultTimeout;
/*    */   private Boolean rollbackOnCommitFailure;
/*    */   
/*    */   public Integer getDefaultTimeout()
/*    */   {
/* 45 */     return this.defaultTimeout;
/*    */   }
/*    */   
/*    */   public void setDefaultTimeout(Integer defaultTimeout) {
/* 49 */     this.defaultTimeout = defaultTimeout;
/*    */   }
/*    */   
/*    */   public Boolean getRollbackOnCommitFailure() {
/* 53 */     return this.rollbackOnCommitFailure;
/*    */   }
/*    */   
/*    */   public void setRollbackOnCommitFailure(Boolean rollbackOnCommitFailure) {
/* 57 */     this.rollbackOnCommitFailure = rollbackOnCommitFailure;
/*    */   }
/*    */   
/*    */   public void customize(AbstractPlatformTransactionManager transactionManager)
/*    */   {
/* 62 */     if (this.defaultTimeout != null) {
/* 63 */       transactionManager.setDefaultTimeout(this.defaultTimeout.intValue());
/*    */     }
/* 65 */     if (this.rollbackOnCommitFailure != null) {
/* 66 */       transactionManager.setRollbackOnCommitFailure(this.rollbackOnCommitFailure.booleanValue());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\TransactionProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */