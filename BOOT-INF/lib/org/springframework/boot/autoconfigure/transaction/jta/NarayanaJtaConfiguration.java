/*     */ package org.springframework.boot.autoconfigure.transaction.jta;
/*     */ 
/*     */ import com.arjuna.ats.arjuna.recovery.RecoveryManager;
/*     */ import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
/*     */ import java.io.File;
/*     */ import javax.jms.Message;
/*     */ import org.jboss.narayana.jta.jms.TransactionHelper;
/*     */ import org.jboss.tm.XAResourceRecoveryRegistry;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.ApplicationHome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.jta.XAConnectionFactoryWrapper;
/*     */ import org.springframework.boot.jta.XADataSourceWrapper;
/*     */ import org.springframework.boot.jta.narayana.NarayanaBeanFactoryPostProcessor;
/*     */ import org.springframework.boot.jta.narayana.NarayanaConfigurationBean;
/*     */ import org.springframework.boot.jta.narayana.NarayanaProperties;
/*     */ import org.springframework.boot.jta.narayana.NarayanaRecoveryManagerBean;
/*     */ import org.springframework.boot.jta.narayana.NarayanaXAConnectionFactoryWrapper;
/*     */ import org.springframework.boot.jta.narayana.NarayanaXADataSourceWrapper;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.DependsOn;
/*     */ import org.springframework.transaction.PlatformTransactionManager;
/*     */ import org.springframework.transaction.jta.JtaTransactionManager;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({JtaTransactionManager.class, com.arjuna.ats.jta.UserTransaction.class, XAResourceRecoveryRegistry.class})
/*     */ @ConditionalOnMissingBean({PlatformTransactionManager.class})
/*     */ @EnableConfigurationProperties({JtaProperties.class})
/*     */ public class NarayanaJtaConfiguration
/*     */ {
/*     */   private final JtaProperties jtaProperties;
/*     */   private final TransactionManagerCustomizers transactionManagerCustomizers;
/*     */   
/*     */   public NarayanaJtaConfiguration(JtaProperties jtaProperties, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*     */   {
/*  71 */     this.jtaProperties = jtaProperties;
/*  72 */     this.transactionManagerCustomizers = 
/*  73 */       ((TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public NarayanaProperties narayanaProperties() {
/*  79 */     return new NarayanaProperties();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public NarayanaConfigurationBean narayanaConfiguration(NarayanaProperties properties)
/*     */   {
/*  86 */     properties.setLogDir(getLogDir().getAbsolutePath());
/*  87 */     if (this.jtaProperties.getTransactionManagerId() != null) {
/*  88 */       properties.setTransactionManagerId(this.jtaProperties
/*  89 */         .getTransactionManagerId());
/*     */     }
/*  91 */     return new NarayanaConfigurationBean(properties);
/*     */   }
/*     */   
/*     */   private File getLogDir() {
/*  95 */     if (StringUtils.hasLength(this.jtaProperties.getLogDir())) {
/*  96 */       return new File(this.jtaProperties.getLogDir());
/*     */     }
/*  98 */     File home = new ApplicationHome().getDir();
/*  99 */     return new File(home, "transaction-logs");
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @DependsOn({"narayanaConfiguration"})
/*     */   @ConditionalOnMissingBean
/*     */   public javax.transaction.UserTransaction narayanaUserTransaction() {
/* 106 */     return com.arjuna.ats.jta.UserTransaction.userTransaction();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @DependsOn({"narayanaConfiguration"})
/*     */   @ConditionalOnMissingBean
/*     */   public javax.transaction.TransactionManager narayanaTransactionManager() {
/* 113 */     return com.arjuna.ats.jta.TransactionManager.transactionManager();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @DependsOn({"narayanaConfiguration"})
/*     */   public RecoveryManagerService narayanaRecoveryManagerService() {
/* 119 */     RecoveryManager.delayRecoveryManagerThread();
/* 120 */     return new RecoveryManagerService();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public NarayanaRecoveryManagerBean narayanaRecoveryManager(RecoveryManagerService recoveryManagerService)
/*     */   {
/* 127 */     return new NarayanaRecoveryManagerBean(recoveryManagerService);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public JtaTransactionManager transactionManager(javax.transaction.UserTransaction userTransaction, javax.transaction.TransactionManager transactionManager)
/*     */   {
/* 133 */     JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction, transactionManager);
/*     */     
/* 135 */     if (this.transactionManagerCustomizers != null) {
/* 136 */       this.transactionManagerCustomizers.customize(jtaTransactionManager);
/*     */     }
/* 138 */     return jtaTransactionManager;
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({XADataSourceWrapper.class})
/*     */   public XADataSourceWrapper xaDataSourceWrapper(NarayanaRecoveryManagerBean narayanaRecoveryManagerBean, NarayanaProperties narayanaProperties)
/*     */   {
/* 146 */     return new NarayanaXADataSourceWrapper(narayanaRecoveryManagerBean, narayanaProperties);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public static NarayanaBeanFactoryPostProcessor narayanaBeanFactoryPostProcessor()
/*     */   {
/* 153 */     return new NarayanaBeanFactoryPostProcessor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({Message.class, TransactionHelper.class})
/*     */   static class NarayanaJtaJmsConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({XAConnectionFactoryWrapper.class})
/*     */     public NarayanaXAConnectionFactoryWrapper xaConnectionFactoryWrapper(javax.transaction.TransactionManager transactionManager, NarayanaRecoveryManagerBean narayanaRecoveryManagerBean, NarayanaProperties narayanaProperties)
/*     */     {
/* 166 */       return new NarayanaXAConnectionFactoryWrapper(transactionManager, narayanaRecoveryManagerBean, narayanaProperties);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\jta\NarayanaJtaConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */