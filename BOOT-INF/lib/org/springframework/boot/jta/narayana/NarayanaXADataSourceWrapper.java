/*    */ package org.springframework.boot.jta.narayana;
/*    */ 
/*    */ import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
/*    */ import javax.sql.DataSource;
/*    */ import javax.sql.XADataSource;
/*    */ import org.springframework.boot.jta.XADataSourceWrapper;
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
/*    */ public class NarayanaXADataSourceWrapper
/*    */   implements XADataSourceWrapper
/*    */ {
/*    */   private final NarayanaRecoveryManagerBean recoveryManager;
/*    */   private final NarayanaProperties properties;
/*    */   
/*    */   public NarayanaXADataSourceWrapper(NarayanaRecoveryManagerBean recoveryManager, NarayanaProperties properties)
/*    */   {
/* 47 */     Assert.notNull(recoveryManager, "RecoveryManager must not be null");
/* 48 */     Assert.notNull(properties, "Properties must not be null");
/* 49 */     this.recoveryManager = recoveryManager;
/* 50 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   public DataSource wrapDataSource(XADataSource dataSource)
/*    */   {
/* 55 */     XAResourceRecoveryHelper recoveryHelper = getRecoveryHelper(dataSource);
/* 56 */     this.recoveryManager.registerXAResourceRecoveryHelper(recoveryHelper);
/* 57 */     return new NarayanaDataSourceBean(dataSource);
/*    */   }
/*    */   
/*    */   private XAResourceRecoveryHelper getRecoveryHelper(XADataSource dataSource) {
/* 61 */     if ((this.properties.getRecoveryDbUser() == null) && 
/* 62 */       (this.properties.getRecoveryDbPass() == null)) {
/* 63 */       return new DataSourceXAResourceRecoveryHelper(dataSource);
/*    */     }
/* 65 */     return new DataSourceXAResourceRecoveryHelper(dataSource, this.properties
/* 66 */       .getRecoveryDbUser(), this.properties.getRecoveryDbPass());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\narayana\NarayanaXADataSourceWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */