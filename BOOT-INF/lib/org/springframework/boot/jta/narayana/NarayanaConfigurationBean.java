/*     */ package org.springframework.boot.jta.narayana;
/*     */ 
/*     */ import com.arjuna.ats.arjuna.common.CoordinatorEnvironmentBean;
/*     */ import com.arjuna.ats.arjuna.common.CoreEnvironmentBean;
/*     */ import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;
/*     */ import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
/*     */ import com.arjuna.ats.arjuna.common.RecoveryEnvironmentBean;
/*     */ import com.arjuna.ats.jta.common.JTAEnvironmentBean;
/*     */ import com.arjuna.common.internal.util.propertyservice.BeanPopulator;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NarayanaConfigurationBean
/*     */   implements InitializingBean
/*     */ {
/*     */   private static final String JBOSSTS_PROPERTIES_FILE_NAME = "jbossts-properties.xml";
/*     */   private final NarayanaProperties properties;
/*     */   
/*     */   public NarayanaConfigurationBean(NarayanaProperties narayanaProperties)
/*     */   {
/*  44 */     this.properties = narayanaProperties;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  49 */     if (isPropertiesFileAvailable()) {
/*  50 */       return;
/*     */     }
/*  52 */     setNodeIdentifier(this.properties.getTransactionManagerId());
/*  53 */     setObjectStoreDir(this.properties.getLogDir());
/*  54 */     setCommitOnePhase(this.properties.isOnePhaseCommit());
/*  55 */     setDefaultTimeout(this.properties.getDefaultTimeout());
/*  56 */     setPeriodicRecoveryPeriod(this.properties.getPeriodicRecoveryPeriod());
/*  57 */     setRecoveryBackoffPeriod(this.properties.getRecoveryBackoffPeriod());
/*  58 */     setXaResourceOrphanFilters(this.properties.getXaResourceOrphanFilters());
/*  59 */     setRecoveryModules(this.properties.getRecoveryModules());
/*  60 */     setExpiryScanners(this.properties.getExpiryScanners());
/*     */   }
/*     */   
/*     */   private boolean isPropertiesFileAvailable() {
/*  64 */     return 
/*  65 */       Thread.currentThread().getContextClassLoader().getResource("jbossts-properties.xml") != null;
/*     */   }
/*     */   
/*     */   private void setNodeIdentifier(String nodeIdentifier) throws CoreEnvironmentBeanException
/*     */   {
/*  70 */     ((CoreEnvironmentBean)getPopulator(CoreEnvironmentBean.class)).setNodeIdentifier(nodeIdentifier);
/*     */   }
/*     */   
/*     */   private void setObjectStoreDir(String objectStoreDir) {
/*  74 */     if (objectStoreDir != null)
/*     */     {
/*  76 */       ((ObjectStoreEnvironmentBean)getPopulator(ObjectStoreEnvironmentBean.class)).setObjectStoreDir(objectStoreDir);
/*  77 */       ((ObjectStoreEnvironmentBean)getPopulator(ObjectStoreEnvironmentBean.class, "communicationStore"))
/*  78 */         .setObjectStoreDir(objectStoreDir);
/*  79 */       ((ObjectStoreEnvironmentBean)getPopulator(ObjectStoreEnvironmentBean.class, "stateStore"))
/*  80 */         .setObjectStoreDir(objectStoreDir);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setCommitOnePhase(boolean isCommitOnePhase)
/*     */   {
/*  86 */     ((CoordinatorEnvironmentBean)getPopulator(CoordinatorEnvironmentBean.class)).setCommitOnePhase(isCommitOnePhase);
/*     */   }
/*     */   
/*     */   private void setDefaultTimeout(int defaultTimeout) {
/*  90 */     ((CoordinatorEnvironmentBean)getPopulator(CoordinatorEnvironmentBean.class)).setDefaultTimeout(defaultTimeout);
/*     */   }
/*     */   
/*     */   private void setPeriodicRecoveryPeriod(int periodicRecoveryPeriod)
/*     */   {
/*  95 */     ((RecoveryEnvironmentBean)getPopulator(RecoveryEnvironmentBean.class)).setPeriodicRecoveryPeriod(periodicRecoveryPeriod);
/*     */   }
/*     */   
/*     */   private void setRecoveryBackoffPeriod(int recoveryBackoffPeriod)
/*     */   {
/* 100 */     ((RecoveryEnvironmentBean)getPopulator(RecoveryEnvironmentBean.class)).setRecoveryBackoffPeriod(recoveryBackoffPeriod);
/*     */   }
/*     */   
/*     */   private void setXaResourceOrphanFilters(List<String> xaResourceOrphanFilters)
/*     */   {
/* 105 */     ((JTAEnvironmentBean)getPopulator(JTAEnvironmentBean.class)).setXaResourceOrphanFilterClassNames(xaResourceOrphanFilters);
/*     */   }
/*     */   
/*     */   private void setRecoveryModules(List<String> recoveryModules)
/*     */   {
/* 110 */     ((RecoveryEnvironmentBean)getPopulator(RecoveryEnvironmentBean.class)).setRecoveryModuleClassNames(recoveryModules);
/*     */   }
/*     */   
/*     */   private void setExpiryScanners(List<String> expiryScanners)
/*     */   {
/* 115 */     ((RecoveryEnvironmentBean)getPopulator(RecoveryEnvironmentBean.class)).setExpiryScannerClassNames(expiryScanners);
/*     */   }
/*     */   
/*     */   private <T> T getPopulator(Class<T> beanClass) {
/* 119 */     return (T)BeanPopulator.getDefaultInstance(beanClass);
/*     */   }
/*     */   
/*     */   private <T> T getPopulator(Class<T> beanClass, String name) {
/* 123 */     return (T)BeanPopulator.getNamedInstance(beanClass, name);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\narayana\NarayanaConfigurationBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */