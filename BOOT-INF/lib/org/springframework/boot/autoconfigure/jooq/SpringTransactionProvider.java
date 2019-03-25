/*    */ package org.springframework.boot.autoconfigure.jooq;
/*    */ 
/*    */ import org.jooq.TransactionContext;
/*    */ import org.jooq.TransactionProvider;
/*    */ import org.springframework.transaction.PlatformTransactionManager;
/*    */ import org.springframework.transaction.TransactionDefinition;
/*    */ import org.springframework.transaction.TransactionStatus;
/*    */ import org.springframework.transaction.support.DefaultTransactionDefinition;
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
/*    */ public class SpringTransactionProvider
/*    */   implements TransactionProvider
/*    */ {
/*    */   private final PlatformTransactionManager transactionManager;
/*    */   
/*    */   public SpringTransactionProvider(PlatformTransactionManager transactionManager)
/*    */   {
/* 42 */     this.transactionManager = transactionManager;
/*    */   }
/*    */   
/*    */   public void begin(TransactionContext context)
/*    */   {
/* 47 */     TransactionDefinition definition = new DefaultTransactionDefinition(6);
/*    */     
/* 49 */     TransactionStatus status = this.transactionManager.getTransaction(definition);
/* 50 */     context.transaction(new SpringTransaction(status));
/*    */   }
/*    */   
/*    */   public void commit(TransactionContext ctx)
/*    */   {
/* 55 */     this.transactionManager.commit(getTransactionStatus(ctx));
/*    */   }
/*    */   
/*    */   public void rollback(TransactionContext ctx)
/*    */   {
/* 60 */     this.transactionManager.rollback(getTransactionStatus(ctx));
/*    */   }
/*    */   
/*    */   private TransactionStatus getTransactionStatus(TransactionContext ctx) {
/* 64 */     SpringTransaction transaction = (SpringTransaction)ctx.transaction();
/* 65 */     return transaction.getTxStatus();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jooq\SpringTransactionProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */