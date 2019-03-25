/*     */ package org.springframework.boot.autoconfigure.transaction.jta;
/*     */ 
/*     */ import bitronix.tm.BitronixTransactionManager;
/*     */ import bitronix.tm.TransactionManagerServices;
/*     */ import bitronix.tm.jndi.BitronixContext;
/*     */ import java.io.File;
/*     */ import javax.jms.Message;
/*     */ import javax.transaction.TransactionManager;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.ApplicationHome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.jta.XAConnectionFactoryWrapper;
/*     */ import org.springframework.boot.jta.XADataSourceWrapper;
/*     */ import org.springframework.boot.jta.bitronix.BitronixDependentBeanFactoryPostProcessor;
/*     */ import org.springframework.boot.jta.bitronix.BitronixXAConnectionFactoryWrapper;
/*     */ import org.springframework.boot.jta.bitronix.BitronixXADataSourceWrapper;
/*     */ import org.springframework.context.annotation.Bean;
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
/*     */ @org.springframework.context.annotation.Configuration
/*     */ @EnableConfigurationProperties({JtaProperties.class})
/*     */ @ConditionalOnClass({JtaTransactionManager.class, BitronixContext.class})
/*     */ @ConditionalOnMissingBean({PlatformTransactionManager.class})
/*     */ class BitronixJtaConfiguration
/*     */ {
/*     */   private final JtaProperties jtaProperties;
/*     */   private final TransactionManagerCustomizers transactionManagerCustomizers;
/*     */   
/*     */   BitronixJtaConfiguration(JtaProperties jtaProperties, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*     */   {
/*  67 */     this.jtaProperties = jtaProperties;
/*  68 */     this.transactionManagerCustomizers = 
/*  69 */       ((TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   @ConfigurationProperties(prefix="spring.jta.bitronix.properties")
/*     */   public bitronix.tm.Configuration bitronixConfiguration() {
/*  76 */     bitronix.tm.Configuration config = TransactionManagerServices.getConfiguration();
/*  77 */     if (StringUtils.hasText(this.jtaProperties.getTransactionManagerId())) {
/*  78 */       config.setServerId(this.jtaProperties.getTransactionManagerId());
/*     */     }
/*  80 */     File logBaseDir = getLogBaseDir();
/*  81 */     config.setLogPart1Filename(new File(logBaseDir, "part1.btm").getAbsolutePath());
/*  82 */     config.setLogPart2Filename(new File(logBaseDir, "part2.btm").getAbsolutePath());
/*  83 */     config.setDisableJmx(true);
/*  84 */     return config;
/*     */   }
/*     */   
/*     */   private File getLogBaseDir() {
/*  88 */     if (StringUtils.hasLength(this.jtaProperties.getLogDir())) {
/*  89 */       return new File(this.jtaProperties.getLogDir());
/*     */     }
/*  91 */     File home = new ApplicationHome().getDir();
/*  92 */     return new File(home, "transaction-logs");
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({TransactionManager.class})
/*     */   public BitronixTransactionManager bitronixTransactionManager(bitronix.tm.Configuration configuration)
/*     */   {
/* 100 */     return TransactionManagerServices.getTransactionManager();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({XADataSourceWrapper.class})
/*     */   public BitronixXADataSourceWrapper xaDataSourceWrapper() {
/* 106 */     return new BitronixXADataSourceWrapper();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public static BitronixDependentBeanFactoryPostProcessor bitronixDependentBeanFactoryPostProcessor() {
/* 112 */     return new BitronixDependentBeanFactoryPostProcessor();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public JtaTransactionManager transactionManager(TransactionManager transactionManager)
/*     */   {
/* 118 */     JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(transactionManager);
/*     */     
/* 120 */     if (this.transactionManagerCustomizers != null) {
/* 121 */       this.transactionManagerCustomizers.customize(jtaTransactionManager);
/*     */     }
/* 123 */     return jtaTransactionManager;
/*     */   }
/*     */   
/*     */   @org.springframework.context.annotation.Configuration
/*     */   @ConditionalOnClass({Message.class})
/*     */   static class BitronixJtaJmsConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({XAConnectionFactoryWrapper.class})
/*     */     public BitronixXAConnectionFactoryWrapper xaConnectionFactoryWrapper() {
/* 133 */       return new BitronixXAConnectionFactoryWrapper();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\jta\BitronixJtaConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */