/*     */ package org.springframework.boot.autoconfigure.batch;
/*     */ 
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.persistence.EntityManagerFactory;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
/*     */ import org.springframework.batch.core.explore.JobExplorer;
/*     */ import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
/*     */ import org.springframework.batch.core.launch.JobLauncher;
/*     */ import org.springframework.batch.core.launch.support.SimpleJobLauncher;
/*     */ import org.springframework.batch.core.repository.JobRepository;
/*     */ import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*     */ import org.springframework.jdbc.datasource.DataSourceTransactionManager;
/*     */ import org.springframework.orm.jpa.JpaTransactionManager;
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
/*     */ public class BasicBatchConfigurer
/*     */   implements BatchConfigurer
/*     */ {
/*  48 */   private static final Log logger = LogFactory.getLog(BasicBatchConfigurer.class);
/*     */   
/*     */ 
/*     */   private final BatchProperties properties;
/*     */   
/*     */ 
/*     */   private final DataSource dataSource;
/*     */   
/*     */ 
/*     */   private final EntityManagerFactory entityManagerFactory;
/*     */   
/*     */ 
/*     */   private PlatformTransactionManager transactionManager;
/*     */   
/*     */ 
/*     */   private final TransactionManagerCustomizers transactionManagerCustomizers;
/*     */   
/*     */ 
/*     */   private JobRepository jobRepository;
/*     */   
/*     */   private JobLauncher jobLauncher;
/*     */   
/*     */   private JobExplorer jobExplorer;
/*     */   
/*     */ 
/*     */   protected BasicBatchConfigurer(BatchProperties properties, DataSource dataSource, TransactionManagerCustomizers transactionManagerCustomizers)
/*     */   {
/*  75 */     this(properties, dataSource, null, transactionManagerCustomizers);
/*     */   }
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
/*     */   protected BasicBatchConfigurer(BatchProperties properties, DataSource dataSource, EntityManagerFactory entityManagerFactory, TransactionManagerCustomizers transactionManagerCustomizers)
/*     */   {
/*  89 */     this.properties = properties;
/*  90 */     this.entityManagerFactory = entityManagerFactory;
/*  91 */     this.dataSource = dataSource;
/*  92 */     this.transactionManagerCustomizers = transactionManagerCustomizers;
/*     */   }
/*     */   
/*     */   public JobRepository getJobRepository()
/*     */   {
/*  97 */     return this.jobRepository;
/*     */   }
/*     */   
/*     */   public PlatformTransactionManager getTransactionManager()
/*     */   {
/* 102 */     return this.transactionManager;
/*     */   }
/*     */   
/*     */   public JobLauncher getJobLauncher()
/*     */   {
/* 107 */     return this.jobLauncher;
/*     */   }
/*     */   
/*     */   public JobExplorer getJobExplorer() throws Exception
/*     */   {
/* 112 */     return this.jobExplorer;
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   public void initialize() {
/*     */     try {
/* 118 */       this.transactionManager = createTransactionManager();
/* 119 */       this.jobRepository = createJobRepository();
/* 120 */       this.jobLauncher = createJobLauncher();
/* 121 */       this.jobExplorer = createJobExplorer();
/*     */     }
/*     */     catch (Exception ex) {
/* 124 */       throw new IllegalStateException("Unable to initialize Spring Batch", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected JobExplorer createJobExplorer() throws Exception {
/* 129 */     JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
/* 130 */     jobExplorerFactoryBean.setDataSource(this.dataSource);
/* 131 */     String tablePrefix = this.properties.getTablePrefix();
/* 132 */     if (StringUtils.hasText(tablePrefix)) {
/* 133 */       jobExplorerFactoryBean.setTablePrefix(tablePrefix);
/*     */     }
/* 135 */     jobExplorerFactoryBean.afterPropertiesSet();
/* 136 */     return jobExplorerFactoryBean.getObject();
/*     */   }
/*     */   
/*     */   protected JobLauncher createJobLauncher() throws Exception {
/* 140 */     SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
/* 141 */     jobLauncher.setJobRepository(getJobRepository());
/* 142 */     jobLauncher.afterPropertiesSet();
/* 143 */     return jobLauncher;
/*     */   }
/*     */   
/*     */   protected JobRepository createJobRepository() throws Exception {
/* 147 */     JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
/* 148 */     factory.setDataSource(this.dataSource);
/* 149 */     if (this.entityManagerFactory != null) {
/* 150 */       logger.warn("JPA does not support custom isolation levels, so locks may not be taken when launching Jobs");
/*     */       
/* 152 */       factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
/*     */     }
/* 154 */     String tablePrefix = this.properties.getTablePrefix();
/* 155 */     if (StringUtils.hasText(tablePrefix)) {
/* 156 */       factory.setTablePrefix(tablePrefix);
/*     */     }
/* 158 */     factory.setTransactionManager(getTransactionManager());
/* 159 */     factory.afterPropertiesSet();
/* 160 */     return factory.getObject();
/*     */   }
/*     */   
/*     */   protected PlatformTransactionManager createTransactionManager() {
/* 164 */     PlatformTransactionManager transactionManager = createAppropriateTransactionManager();
/* 165 */     if (this.transactionManagerCustomizers != null) {
/* 166 */       this.transactionManagerCustomizers.customize(transactionManager);
/*     */     }
/* 168 */     return transactionManager;
/*     */   }
/*     */   
/*     */   private PlatformTransactionManager createAppropriateTransactionManager() {
/* 172 */     if (this.entityManagerFactory != null) {
/* 173 */       return new JpaTransactionManager(this.entityManagerFactory);
/*     */     }
/* 175 */     return new DataSourceTransactionManager(this.dataSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\batch\BasicBatchConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */