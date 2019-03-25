/*    */ package org.springframework.boot.orm.jpa.hibernate;
/*    */ 
/*    */ import javax.transaction.TransactionManager;
/*    */ import javax.transaction.UserTransaction;
/*    */ import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;
/*    */ import org.springframework.transaction.jta.JtaTransactionManager;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class SpringJtaPlatform
/*    */   extends AbstractJtaPlatform
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final JtaTransactionManager transactionManager;
/*    */   
/*    */   public SpringJtaPlatform(JtaTransactionManager transactionManager)
/*    */   {
/* 43 */     Assert.notNull(transactionManager, "TransactionManager must not be null");
/* 44 */     this.transactionManager = transactionManager;
/*    */   }
/*    */   
/*    */   protected TransactionManager locateTransactionManager()
/*    */   {
/* 49 */     return this.transactionManager.getTransactionManager();
/*    */   }
/*    */   
/*    */   protected UserTransaction locateUserTransaction()
/*    */   {
/* 54 */     return this.transactionManager.getUserTransaction();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\orm\jpa\hibernate\SpringJtaPlatform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */