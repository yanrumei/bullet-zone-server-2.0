/*    */ package org.springframework.boot.jta.narayana;
/*    */ 
/*    */ import com.arjuna.ats.internal.jta.recovery.arjunacore.XARecoveryModule;
/*    */ import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
/*    */ import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
/*    */ import org.springframework.beans.factory.DisposableBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public class NarayanaRecoveryManagerBean
/*    */   implements InitializingBean, DisposableBean
/*    */ {
/*    */   private final RecoveryManagerService recoveryManagerService;
/*    */   
/*    */   public NarayanaRecoveryManagerBean(RecoveryManagerService recoveryManagerService)
/*    */   {
/* 38 */     Assert.notNull(recoveryManagerService, "RecoveryManagerService must not be null");
/* 39 */     this.recoveryManagerService = recoveryManagerService;
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet() throws Exception
/*    */   {
/* 44 */     this.recoveryManagerService.create();
/* 45 */     this.recoveryManagerService.start();
/*    */   }
/*    */   
/*    */   public void destroy() throws Exception
/*    */   {
/* 50 */     this.recoveryManagerService.stop();
/* 51 */     this.recoveryManagerService.destroy();
/*    */   }
/*    */   
/*    */   void registerXAResourceRecoveryHelper(XAResourceRecoveryHelper xaResourceRecoveryHelper)
/*    */   {
/* 56 */     getXARecoveryModule().addXAResourceRecoveryHelper(xaResourceRecoveryHelper);
/*    */   }
/*    */   
/*    */   private XARecoveryModule getXARecoveryModule()
/*    */   {
/* 61 */     XARecoveryModule xaRecoveryModule = XARecoveryModule.getRegisteredXARecoveryModule();
/* 62 */     if (xaRecoveryModule != null) {
/* 63 */       return xaRecoveryModule;
/*    */     }
/* 65 */     throw new IllegalStateException("XARecoveryModule is not registered with recovery manager");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\narayana\NarayanaRecoveryManagerBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */