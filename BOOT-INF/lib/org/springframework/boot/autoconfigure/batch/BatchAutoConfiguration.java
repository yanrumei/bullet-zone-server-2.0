/*     */ package org.springframework.boot.autoconfigure.batch;
/*     */ 
/*     */ import javax.persistence.EntityManagerFactory;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.batch.core.configuration.ListableJobLocator;
/*     */ import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
/*     */ import org.springframework.batch.core.converter.JobParametersConverter;
/*     */ import org.springframework.batch.core.explore.JobExplorer;
/*     */ import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
/*     */ import org.springframework.batch.core.launch.JobLauncher;
/*     */ import org.springframework.batch.core.launch.JobOperator;
/*     */ import org.springframework.batch.core.launch.support.SimpleJobOperator;
/*     */ import org.springframework.batch.core.repository.JobRepository;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.ExitCodeGenerator;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.jdbc.core.JdbcOperations;
/*     */ import org.springframework.transaction.PlatformTransactionManager;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({JobLauncher.class, DataSource.class, JdbcOperations.class})
/*     */ @AutoConfigureAfter({HibernateJpaAutoConfiguration.class})
/*     */ @ConditionalOnBean({JobLauncher.class})
/*     */ @EnableConfigurationProperties({BatchProperties.class})
/*     */ public class BatchAutoConfiguration
/*     */ {
/*     */   private final BatchProperties properties;
/*     */   private final JobParametersConverter jobParametersConverter;
/*     */   
/*     */   public BatchAutoConfiguration(BatchProperties properties, ObjectProvider<JobParametersConverter> jobParametersConverter)
/*     */   {
/*  77 */     this.properties = properties;
/*  78 */     this.jobParametersConverter = ((JobParametersConverter)jobParametersConverter.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   @ConditionalOnBean({DataSource.class})
/*     */   public BatchDatabaseInitializer batchDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader)
/*     */   {
/*  86 */     return new BatchDatabaseInitializer(dataSource, resourceLoader, this.properties);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   @ConditionalOnProperty(prefix="spring.batch.job", name={"enabled"}, havingValue="true", matchIfMissing=true)
/*     */   public JobLauncherCommandLineRunner jobLauncherCommandLineRunner(JobLauncher jobLauncher, JobExplorer jobExplorer)
/*     */   {
/*  94 */     JobLauncherCommandLineRunner runner = new JobLauncherCommandLineRunner(jobLauncher, jobExplorer);
/*     */     
/*  96 */     String jobNames = this.properties.getJob().getNames();
/*  97 */     if (StringUtils.hasText(jobNames)) {
/*  98 */       runner.setJobNames(jobNames);
/*     */     }
/* 100 */     return runner;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({ExitCodeGenerator.class})
/*     */   public JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator() {
/* 106 */     return new JobExecutionExitCodeGenerator();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   @ConditionalOnBean({DataSource.class})
/*     */   public JobExplorer jobExplorer(DataSource dataSource) throws Exception {
/* 113 */     JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
/* 114 */     factory.setDataSource(dataSource);
/* 115 */     String tablePrefix = this.properties.getTablePrefix();
/* 116 */     if (StringUtils.hasText(tablePrefix)) {
/* 117 */       factory.setTablePrefix(tablePrefix);
/*     */     }
/* 119 */     factory.afterPropertiesSet();
/* 120 */     return factory.getObject();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({JobOperator.class})
/*     */   public SimpleJobOperator jobOperator(JobExplorer jobExplorer, JobLauncher jobLauncher, ListableJobLocator jobRegistry, JobRepository jobRepository)
/*     */     throws Exception
/*     */   {
/* 128 */     SimpleJobOperator factory = new SimpleJobOperator();
/* 129 */     factory.setJobExplorer(jobExplorer);
/* 130 */     factory.setJobLauncher(jobLauncher);
/* 131 */     factory.setJobRegistry(jobRegistry);
/* 132 */     factory.setJobRepository(jobRepository);
/* 133 */     if (this.jobParametersConverter != null) {
/* 134 */       factory.setJobParametersConverter(this.jobParametersConverter);
/*     */     }
/* 136 */     return factory;
/*     */   }
/*     */   
/*     */   @EnableConfigurationProperties({BatchProperties.class})
/*     */   @ConditionalOnClass(value={PlatformTransactionManager.class}, name={"javax.persistence.EntityManagerFactory"})
/*     */   @ConditionalOnMissingBean({BatchConfigurer.class})
/*     */   @Configuration
/*     */   protected static class JpaBatchConfiguration
/*     */   {
/*     */     private final BatchProperties properties;
/*     */     
/*     */     protected JpaBatchConfiguration(BatchProperties properties) {
/* 148 */       this.properties = properties;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Bean
/*     */     @ConditionalOnBean(name={"entityManagerFactory"})
/*     */     public BasicBatchConfigurer jpaBatchConfigurer(DataSource dataSource, EntityManagerFactory entityManagerFactory, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*     */     {
/* 159 */       return new BasicBatchConfigurer(this.properties, dataSource, entityManagerFactory, 
/* 160 */         (TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean(name={"entityManagerFactory"})
/*     */     public BasicBatchConfigurer basicBatchConfigurer(DataSource dataSource, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*     */     {
/* 167 */       return new BasicBatchConfigurer(this.properties, dataSource, 
/* 168 */         (TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\batch\BatchAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */