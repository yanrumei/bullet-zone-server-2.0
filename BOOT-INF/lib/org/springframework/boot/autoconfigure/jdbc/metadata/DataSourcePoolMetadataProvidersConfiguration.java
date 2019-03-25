/*     */ package org.springframework.boot.autoconfigure.jdbc.metadata;
/*     */ 
/*     */ import com.zaxxer.hikari.HikariDataSource;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
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
/*     */ public class DataSourcePoolMetadataProvidersConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnClass({org.apache.tomcat.jdbc.pool.DataSource.class})
/*     */   static class TomcatDataSourcePoolMetadataProviderConfiguration
/*     */   {
/*     */     @Bean
/*     */     public DataSourcePoolMetadataProvider tomcatPoolDataSourceMetadataProvider()
/*     */     {
/*  44 */       new DataSourcePoolMetadataProvider()
/*     */       {
/*     */         public DataSourcePoolMetadata getDataSourcePoolMetadata(javax.sql.DataSource dataSource)
/*     */         {
/*  48 */           if ((dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource)) {
/*  49 */             return new TomcatDataSourcePoolMetadata((org.apache.tomcat.jdbc.pool.DataSource)dataSource);
/*     */           }
/*     */           
/*  52 */           return null;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({HikariDataSource.class})
/*     */   static class HikariPoolDataSourceMetadataProviderConfiguration
/*     */   {
/*     */     @Bean
/*     */     public DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider()
/*     */     {
/*  65 */       new DataSourcePoolMetadataProvider()
/*     */       {
/*     */         public DataSourcePoolMetadata getDataSourcePoolMetadata(javax.sql.DataSource dataSource)
/*     */         {
/*  69 */           if ((dataSource instanceof HikariDataSource)) {
/*  70 */             return new HikariDataSourcePoolMetadata((HikariDataSource)dataSource);
/*     */           }
/*     */           
/*  73 */           return null;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({org.apache.commons.dbcp.BasicDataSource.class})
/*     */   @Deprecated
/*     */   static class CommonsDbcpPoolDataSourceMetadataProviderConfiguration
/*     */   {
/*     */     @Bean
/*     */     public DataSourcePoolMetadataProvider commonsDbcpPoolDataSourceMetadataProvider()
/*     */     {
/*  87 */       new DataSourcePoolMetadataProvider()
/*     */       {
/*     */         public DataSourcePoolMetadata getDataSourcePoolMetadata(javax.sql.DataSource dataSource)
/*     */         {
/*  91 */           if ((dataSource instanceof org.apache.commons.dbcp.BasicDataSource)) {
/*  92 */             return new CommonsDbcpDataSourcePoolMetadata((org.apache.commons.dbcp.BasicDataSource)dataSource);
/*     */           }
/*     */           
/*  95 */           return null;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({org.apache.commons.dbcp2.BasicDataSource.class})
/*     */   static class CommonsDbcp2PoolDataSourceMetadataProviderConfiguration
/*     */   {
/*     */     @Bean
/*     */     public DataSourcePoolMetadataProvider commonsDbcp2PoolDataSourceMetadataProvider()
/*     */     {
/* 108 */       new DataSourcePoolMetadataProvider()
/*     */       {
/*     */         public DataSourcePoolMetadata getDataSourcePoolMetadata(javax.sql.DataSource dataSource)
/*     */         {
/* 112 */           if ((dataSource instanceof org.apache.commons.dbcp2.BasicDataSource)) {
/* 113 */             return new CommonsDbcp2DataSourcePoolMetadata((org.apache.commons.dbcp2.BasicDataSource)dataSource);
/*     */           }
/*     */           
/* 116 */           return null;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\DataSourcePoolMetadataProvidersConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */