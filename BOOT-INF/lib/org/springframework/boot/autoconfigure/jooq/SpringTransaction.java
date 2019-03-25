/*    */ package org.springframework.boot.autoconfigure.jooq;
/*    */ 
/*    */ import org.jooq.Transaction;
/*    */ import org.springframework.transaction.TransactionStatus;
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
/*    */ class SpringTransaction
/*    */   implements Transaction
/*    */ {
/*    */   private final TransactionStatus transactionStatus;
/*    */   
/*    */   SpringTransaction(TransactionStatus transactionStatus)
/*    */   {
/* 37 */     this.transactionStatus = transactionStatus;
/*    */   }
/*    */   
/*    */   public TransactionStatus getTxStatus() {
/* 41 */     return this.transactionStatus;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jooq\SpringTransaction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */