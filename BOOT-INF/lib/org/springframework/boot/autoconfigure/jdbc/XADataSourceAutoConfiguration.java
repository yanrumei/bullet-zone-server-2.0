/*     */ package org.springframework.boot.autoconfigure.jdbc;
/*     */ 
/*     */ import javax.sql.DataSource;
/*     */ import javax.sql.XADataSource;
/*     */ import javax.transaction.TransactionManager;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.bind.RelaxedDataBinder;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.jdbc.DatabaseDriver;
/*     */ import org.springframework.boot.jta.XADataSourceWrapper;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ @AutoConfigureBefore({DataSourceAutoConfiguration.class})
/*     */ @EnableConfigurationProperties({DataSourceProperties.class})
/*     */ @ConditionalOnClass({DataSource.class, TransactionManager.class, EmbeddedDatabaseType.class})
/*     */ @ConditionalOnBean({XADataSourceWrapper.class})
/*     */ @ConditionalOnMissingBean({DataSource.class})
/*     */ public class XADataSourceAutoConfiguration
/*     */   implements BeanClassLoaderAware
/*     */ {
/*     */   @Autowired
/*     */   private XADataSourceWrapper wrapper;
/*     */   @Autowired
/*     */   private DataSourceProperties properties;
/*     */   @Autowired(required=false)
/*     */   private XADataSource xaDataSource;
/*     */   private ClassLoader classLoader;
/*     */   
/*     */   @Bean
/*     */   public DataSource dataSource()
/*     */     throws Exception
/*     */   {
/*  70 */     XADataSource xaDataSource = this.xaDataSource;
/*  71 */     if (xaDataSource == null) {
/*  72 */       xaDataSource = createXaDataSource();
/*     */     }
/*  74 */     return this.wrapper.wrapDataSource(xaDataSource);
/*     */   }
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/*  79 */     this.classLoader = classLoader;
/*     */   }
/*     */   
/*     */   private XADataSource createXaDataSource() {
/*  83 */     String className = this.properties.getXa().getDataSourceClassName();
/*  84 */     if (!StringUtils.hasLength(className))
/*     */     {
/*  86 */       className = DatabaseDriver.fromJdbcUrl(this.properties.determineUrl()).getXaDataSourceClassName();
/*     */     }
/*  88 */     Assert.state(StringUtils.hasLength(className), "No XA DataSource class name specified");
/*     */     
/*  90 */     XADataSource dataSource = createXaDataSourceInstance(className);
/*  91 */     bindXaProperties(dataSource, this.properties);
/*  92 */     return dataSource;
/*     */   }
/*     */   
/*     */   private XADataSource createXaDataSourceInstance(String className) {
/*     */     try {
/*  97 */       Class<?> dataSourceClass = ClassUtils.forName(className, this.classLoader);
/*  98 */       Object instance = BeanUtils.instantiate(dataSourceClass);
/*  99 */       Assert.isInstanceOf(XADataSource.class, instance);
/* 100 */       return (XADataSource)instance;
/*     */     }
/*     */     catch (Exception ex) {
/* 103 */       throw new IllegalStateException("Unable to create XADataSource instance from '" + className + "'");
/*     */     }
/*     */   }
/*     */   
/*     */   private void bindXaProperties(XADataSource target, DataSourceProperties properties)
/*     */   {
/* 109 */     MutablePropertyValues values = new MutablePropertyValues();
/* 110 */     values.add("user", this.properties.determineUsername());
/* 111 */     values.add("password", this.properties.determinePassword());
/* 112 */     values.add("url", this.properties.determineUrl());
/* 113 */     values.addPropertyValues(properties.getXa().getProperties());
/* 114 */     new RelaxedDataBinder(target).withAlias("user", new String[] { "username" }).bind(values);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\XADataSourceAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */