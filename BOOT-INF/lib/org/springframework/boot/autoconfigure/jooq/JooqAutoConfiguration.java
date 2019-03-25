/*     */ package org.springframework.boot.autoconfigure.jooq;
/*     */ 
/*     */ import javax.sql.DataSource;
/*     */ import org.jooq.ConnectionProvider;
/*     */ import org.jooq.DSLContext;
/*     */ import org.jooq.ExecuteListenerProvider;
/*     */ import org.jooq.RecordListenerProvider;
/*     */ import org.jooq.RecordMapperProvider;
/*     */ import org.jooq.TransactionProvider;
/*     */ import org.jooq.VisitListenerProvider;
/*     */ import org.jooq.conf.Settings;
/*     */ import org.jooq.impl.DataSourceConnectionProvider;
/*     */ import org.jooq.impl.DefaultConfiguration;
/*     */ import org.jooq.impl.DefaultDSLContext;
/*     */ import org.jooq.impl.DefaultExecuteListenerProvider;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
/*     */ import org.springframework.transaction.PlatformTransactionManager;
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
/*     */ @ConditionalOnClass({DSLContext.class})
/*     */ @ConditionalOnBean({DataSource.class})
/*     */ @AutoConfigureAfter({DataSourceAutoConfiguration.class, TransactionAutoConfiguration.class})
/*     */ public class JooqAutoConfiguration
/*     */ {
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({DataSourceConnectionProvider.class})
/*     */   public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource)
/*     */   {
/*  65 */     return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnBean({PlatformTransactionManager.class})
/*     */   public SpringTransactionProvider transactionProvider(PlatformTransactionManager txManager)
/*     */   {
/*  73 */     return new SpringTransactionProvider(txManager);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public DefaultExecuteListenerProvider jooqExceptionTranslatorExecuteListenerProvider() {
/*  78 */     return new DefaultExecuteListenerProvider(new JooqExceptionTranslator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @org.springframework.context.annotation.Configuration
/*     */   @ConditionalOnMissingBean({DSLContext.class})
/*     */   @EnableConfigurationProperties({JooqProperties.class})
/*     */   public static class DslContextConfiguration
/*     */   {
/*     */     private final JooqProperties properties;
/*     */     
/*     */ 
/*     */     private final ConnectionProvider connection;
/*     */     
/*     */ 
/*     */     private final TransactionProvider transactionProvider;
/*     */     
/*     */ 
/*     */     private final RecordMapperProvider recordMapperProvider;
/*     */     
/*     */     private final Settings settings;
/*     */     
/*     */     private final RecordListenerProvider[] recordListenerProviders;
/*     */     
/*     */     private final ExecuteListenerProvider[] executeListenerProviders;
/*     */     
/*     */     private final VisitListenerProvider[] visitListenerProviders;
/*     */     
/*     */ 
/*     */     public DslContextConfiguration(JooqProperties properties, ConnectionProvider connectionProvider, ObjectProvider<TransactionProvider> transactionProvider, ObjectProvider<RecordMapperProvider> recordMapperProvider, ObjectProvider<Settings> settings, ObjectProvider<RecordListenerProvider[]> recordListenerProviders, ExecuteListenerProvider[] executeListenerProviders, ObjectProvider<VisitListenerProvider[]> visitListenerProviders)
/*     */     {
/* 110 */       this.properties = properties;
/* 111 */       this.connection = connectionProvider;
/* 112 */       this.transactionProvider = ((TransactionProvider)transactionProvider.getIfAvailable());
/* 113 */       this.recordMapperProvider = ((RecordMapperProvider)recordMapperProvider.getIfAvailable());
/* 114 */       this.settings = ((Settings)settings.getIfAvailable());
/* 115 */       this.recordListenerProviders = ((RecordListenerProvider[])recordListenerProviders.getIfAvailable());
/* 116 */       this.executeListenerProviders = executeListenerProviders;
/* 117 */       this.visitListenerProviders = ((VisitListenerProvider[])visitListenerProviders.getIfAvailable());
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public DefaultDSLContext dslContext(org.jooq.Configuration configuration) {
/* 122 */       return new DefaultDSLContext(configuration);
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({org.jooq.Configuration.class})
/*     */     public DefaultConfiguration jooqConfiguration() {
/* 128 */       DefaultConfiguration configuration = new DefaultConfiguration();
/* 129 */       if (this.properties.getSqlDialect() != null) {
/* 130 */         configuration.set(this.properties.getSqlDialect());
/*     */       }
/* 132 */       configuration.set(this.connection);
/* 133 */       if (this.transactionProvider != null) {
/* 134 */         configuration.set(this.transactionProvider);
/*     */       }
/* 136 */       if (this.recordMapperProvider != null) {
/* 137 */         configuration.set(this.recordMapperProvider);
/*     */       }
/* 139 */       if (this.settings != null) {
/* 140 */         configuration.set(this.settings);
/*     */       }
/* 142 */       configuration.set(this.recordListenerProviders);
/* 143 */       configuration.set(this.executeListenerProviders);
/* 144 */       configuration.set(this.visitListenerProviders);
/* 145 */       return configuration;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jooq\JooqAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */