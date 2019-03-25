/*    */ package org.springframework.boot.jta.narayana;
/*    */ 
/*    */ import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
/*    */ import javax.jms.ConnectionFactory;
/*    */ import javax.jms.XAConnectionFactory;
/*    */ import javax.transaction.TransactionManager;
/*    */ import org.jboss.narayana.jta.jms.ConnectionFactoryProxy;
/*    */ import org.jboss.narayana.jta.jms.JmsXAResourceRecoveryHelper;
/*    */ import org.jboss.narayana.jta.jms.TransactionHelperImpl;
/*    */ import org.springframework.boot.jta.XAConnectionFactoryWrapper;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NarayanaXAConnectionFactoryWrapper
/*    */   implements XAConnectionFactoryWrapper
/*    */ {
/*    */   private final TransactionManager transactionManager;
/*    */   private final NarayanaRecoveryManagerBean recoveryManager;
/*    */   private final NarayanaProperties properties;
/*    */   
/*    */   public NarayanaXAConnectionFactoryWrapper(TransactionManager transactionManager, NarayanaRecoveryManagerBean recoveryManager, NarayanaProperties properties)
/*    */   {
/* 54 */     Assert.notNull(transactionManager, "TransactionManager must not be null");
/* 55 */     Assert.notNull(recoveryManager, "RecoveryManager must not be null");
/* 56 */     Assert.notNull(properties, "Properties must not be null");
/* 57 */     this.transactionManager = transactionManager;
/* 58 */     this.recoveryManager = recoveryManager;
/* 59 */     this.properties = properties;
/*    */   }
/*    */   
/*    */ 
/*    */   public ConnectionFactory wrapConnectionFactory(XAConnectionFactory xaConnectionFactory)
/*    */   {
/* 65 */     XAResourceRecoveryHelper recoveryHelper = getRecoveryHelper(xaConnectionFactory);
/* 66 */     this.recoveryManager.registerXAResourceRecoveryHelper(recoveryHelper);
/* 67 */     return new ConnectionFactoryProxy(xaConnectionFactory, new TransactionHelperImpl(this.transactionManager));
/*    */   }
/*    */   
/*    */ 
/*    */   private XAResourceRecoveryHelper getRecoveryHelper(XAConnectionFactory xaConnectionFactory)
/*    */   {
/* 73 */     if ((this.properties.getRecoveryJmsUser() == null) && 
/* 74 */       (this.properties.getRecoveryJmsPass() == null)) {
/* 75 */       return new JmsXAResourceRecoveryHelper(xaConnectionFactory);
/*    */     }
/* 77 */     return new JmsXAResourceRecoveryHelper(xaConnectionFactory, this.properties
/* 78 */       .getRecoveryJmsUser(), this.properties
/* 79 */       .getRecoveryJmsPass());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\narayana\NarayanaXAConnectionFactoryWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */