/*     */ package org.springframework.boot.autoconfigure.jdbc;
/*     */ 
/*     */ import com.zaxxer.hikari.HikariDataSource;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.jdbc.DatabaseDriver;
/*     */ import org.springframework.context.annotation.Bean;
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
/*     */ abstract class DataSourceConfiguration
/*     */ {
/*     */   protected <T> T createDataSource(DataSourceProperties properties, Class<? extends javax.sql.DataSource> type)
/*     */   {
/*  42 */     return properties.initializeDataSourceBuilder().type(type).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @ConditionalOnClass({org.apache.tomcat.jdbc.pool.DataSource.class})
/*     */   @ConditionalOnProperty(name={"spring.datasource.type"}, havingValue="org.apache.tomcat.jdbc.pool.DataSource", matchIfMissing=true)
/*     */   static class Tomcat
/*     */     extends DataSourceConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConfigurationProperties(prefix="spring.datasource.tomcat")
/*     */     public org.apache.tomcat.jdbc.pool.DataSource dataSource(DataSourceProperties properties)
/*     */     {
/*  56 */       org.apache.tomcat.jdbc.pool.DataSource dataSource = (org.apache.tomcat.jdbc.pool.DataSource)createDataSource(properties, org.apache.tomcat.jdbc.pool.DataSource.class);
/*     */       
/*     */ 
/*  59 */       DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
/*  60 */       String validationQuery = databaseDriver.getValidationQuery();
/*  61 */       if (validationQuery != null) {
/*  62 */         dataSource.setTestOnBorrow(true);
/*  63 */         dataSource.setValidationQuery(validationQuery);
/*     */       }
/*  65 */       return dataSource;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @ConditionalOnClass({HikariDataSource.class})
/*     */   @ConditionalOnProperty(name={"spring.datasource.type"}, havingValue="com.zaxxer.hikari.HikariDataSource", matchIfMissing=true)
/*     */   static class Hikari
/*     */     extends DataSourceConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConfigurationProperties(prefix="spring.datasource.hikari")
/*     */     public HikariDataSource dataSource(DataSourceProperties properties)
/*     */     {
/*  80 */       return (HikariDataSource)createDataSource(properties, HikariDataSource.class);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ConditionalOnClass({org.apache.commons.dbcp.BasicDataSource.class})
/*     */   @ConditionalOnProperty(name={"spring.datasource.type"}, havingValue="org.apache.commons.dbcp.BasicDataSource", matchIfMissing=true)
/*     */   @Deprecated
/*     */   static class Dbcp
/*     */     extends DataSourceConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConfigurationProperties(prefix="spring.datasource.dbcp")
/*     */     public org.apache.commons.dbcp.BasicDataSource dataSource(DataSourceProperties properties)
/*     */     {
/*  99 */       org.apache.commons.dbcp.BasicDataSource dataSource = (org.apache.commons.dbcp.BasicDataSource)createDataSource(properties, org.apache.commons.dbcp.BasicDataSource.class);
/*     */       
/*     */ 
/* 102 */       DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
/* 103 */       String validationQuery = databaseDriver.getValidationQuery();
/* 104 */       if (validationQuery != null) {
/* 105 */         dataSource.setTestOnBorrow(true);
/* 106 */         dataSource.setValidationQuery(validationQuery);
/*     */       }
/* 108 */       return dataSource;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ConditionalOnClass({org.apache.commons.dbcp2.BasicDataSource.class})
/*     */   @ConditionalOnProperty(name={"spring.datasource.type"}, havingValue="org.apache.commons.dbcp2.BasicDataSource", matchIfMissing=true)
/*     */   static class Dbcp2
/*     */     extends DataSourceConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConfigurationProperties(prefix="spring.datasource.dbcp2")
/*     */     public org.apache.commons.dbcp2.BasicDataSource dataSource(DataSourceProperties properties)
/*     */     {
/* 124 */       return (org.apache.commons.dbcp2.BasicDataSource)createDataSource(properties, org.apache.commons.dbcp2.BasicDataSource.class);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ConditionalOnMissingBean({javax.sql.DataSource.class})
/*     */   @ConditionalOnProperty(name={"spring.datasource.type"})
/*     */   static class Generic
/*     */   {
/*     */     @Bean
/*     */     public javax.sql.DataSource dataSource(DataSourceProperties properties)
/*     */     {
/* 139 */       return properties.initializeDataSourceBuilder().build();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */