/*     */ package org.springframework.boot.autoconfigure.transaction.jta;
/*     */ 
/*     */ import com.atomikos.icatch.config.UserTransactionService;
/*     */ import com.atomikos.icatch.config.UserTransactionServiceImp;
/*     */ import com.atomikos.icatch.jta.UserTransactionManager;
/*     */ import java.io.File;
/*     */ import java.util.Properties;
/*     */ import javax.jms.Message;
/*     */ import javax.transaction.TransactionManager;
/*     */ import javax.transaction.UserTransaction;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.ApplicationHome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.jta.XAConnectionFactoryWrapper;
/*     */ import org.springframework.boot.jta.XADataSourceWrapper;
/*     */ import org.springframework.boot.jta.atomikos.AtomikosDependsOnBeanFactoryPostProcessor;
/*     */ import org.springframework.boot.jta.atomikos.AtomikosProperties;
/*     */ import org.springframework.boot.jta.atomikos.AtomikosXAConnectionFactoryWrapper;
/*     */ import org.springframework.boot.jta.atomikos.AtomikosXADataSourceWrapper;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
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
/*     */ @EnableConfigurationProperties({AtomikosProperties.class, JtaProperties.class})
/*     */ @ConditionalOnClass({JtaTransactionManager.class, UserTransactionManager.class})
/*     */ @ConditionalOnMissingBean({PlatformTransactionManager.class})
/*     */ class AtomikosJtaConfiguration
/*     */ {
/*     */   private final JtaProperties jtaProperties;
/*     */   private final TransactionManagerCustomizers transactionManagerCustomizers;
/*     */   
/*     */   AtomikosJtaConfiguration(JtaProperties jtaProperties, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*     */   {
/*  70 */     this.jtaProperties = jtaProperties;
/*  71 */     this.transactionManagerCustomizers = 
/*  72 */       ((TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean(initMethod="init", destroyMethod="shutdownForce")
/*     */   @ConditionalOnMissingBean({UserTransactionService.class})
/*     */   public UserTransactionServiceImp userTransactionService(AtomikosProperties atomikosProperties)
/*     */   {
/*  79 */     Properties properties = new Properties();
/*  80 */     if (StringUtils.hasText(this.jtaProperties.getTransactionManagerId())) {
/*  81 */       properties.setProperty("com.atomikos.icatch.tm_unique_name", this.jtaProperties
/*  82 */         .getTransactionManagerId());
/*     */     }
/*  84 */     properties.setProperty("com.atomikos.icatch.log_base_dir", getLogBaseDir());
/*  85 */     properties.putAll(atomikosProperties.asProperties());
/*  86 */     return new UserTransactionServiceImp(properties);
/*     */   }
/*     */   
/*     */   private String getLogBaseDir() {
/*  90 */     if (StringUtils.hasLength(this.jtaProperties.getLogDir())) {
/*  91 */       return this.jtaProperties.getLogDir();
/*     */     }
/*  93 */     File home = new ApplicationHome().getDir();
/*  94 */     return new File(home, "transaction-logs").getAbsolutePath();
/*     */   }
/*     */   
/*     */   @Bean(initMethod="init", destroyMethod="close")
/*     */   @ConditionalOnMissingBean
/*     */   public UserTransactionManager atomikosTransactionManager(UserTransactionService userTransactionService) throws Exception
/*     */   {
/* 101 */     UserTransactionManager manager = new UserTransactionManager();
/* 102 */     manager.setStartupTransactionService(false);
/* 103 */     manager.setForceShutdown(true);
/* 104 */     return manager;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({XADataSourceWrapper.class})
/*     */   public AtomikosXADataSourceWrapper xaDataSourceWrapper() {
/* 110 */     return new AtomikosXADataSourceWrapper();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public static AtomikosDependsOnBeanFactoryPostProcessor atomikosDependsOnBeanFactoryPostProcessor() {
/* 116 */     return new AtomikosDependsOnBeanFactoryPostProcessor();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public JtaTransactionManager transactionManager(UserTransaction userTransaction, TransactionManager transactionManager)
/*     */   {
/* 122 */     JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction, transactionManager);
/*     */     
/* 124 */     if (this.transactionManagerCustomizers != null) {
/* 125 */       this.transactionManagerCustomizers.customize(jtaTransactionManager);
/*     */     }
/* 127 */     return jtaTransactionManager;
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({Message.class})
/*     */   static class AtomikosJtaJmsConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({XAConnectionFactoryWrapper.class})
/*     */     public AtomikosXAConnectionFactoryWrapper xaConnectionFactoryWrapper() {
/* 137 */       return new AtomikosXAConnectionFactoryWrapper();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\jta\AtomikosJtaConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */