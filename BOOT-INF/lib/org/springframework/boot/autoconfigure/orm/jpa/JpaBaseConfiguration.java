/*     */ package org.springframework.boot.autoconfigure.orm.jpa;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.persistence.EntityManagerFactory;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
/*     */ import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder.Builder;
/*     */ import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder.EntityManagerFactoryBeanCallback;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.context.annotation.Primary;
/*     */ import org.springframework.orm.jpa.JpaTransactionManager;
/*     */ import org.springframework.orm.jpa.JpaVendorAdapter;
/*     */ import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
/*     */ import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
/*     */ import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
/*     */ import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
/*     */ import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
/*     */ import org.springframework.transaction.PlatformTransactionManager;
/*     */ import org.springframework.transaction.jta.JtaTransactionManager;
/*     */ import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
/*     */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
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
/*     */ @EnableConfigurationProperties({JpaProperties.class})
/*     */ @Import({DataSourceInitializedPublisher.Registrar.class})
/*     */ public abstract class JpaBaseConfiguration
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   private final DataSource dataSource;
/*     */   private final JpaProperties properties;
/*     */   private final JtaTransactionManager jtaTransactionManager;
/*     */   private final TransactionManagerCustomizers transactionManagerCustomizers;
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */   
/*     */   protected JpaBaseConfiguration(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*     */   {
/*  83 */     this.dataSource = dataSource;
/*  84 */     this.properties = properties;
/*  85 */     this.jtaTransactionManager = ((JtaTransactionManager)jtaTransactionManager.getIfAvailable());
/*  86 */     this.transactionManagerCustomizers = 
/*  87 */       ((TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({PlatformTransactionManager.class})
/*     */   public PlatformTransactionManager transactionManager() {
/*  93 */     JpaTransactionManager transactionManager = new JpaTransactionManager();
/*  94 */     if (this.transactionManagerCustomizers != null) {
/*  95 */       this.transactionManagerCustomizers.customize(transactionManager);
/*     */     }
/*  97 */     return transactionManager;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public JpaVendorAdapter jpaVendorAdapter() {
/* 103 */     AbstractJpaVendorAdapter adapter = createJpaVendorAdapter();
/* 104 */     adapter.setShowSql(this.properties.isShowSql());
/* 105 */     adapter.setDatabase(this.properties.determineDatabase(this.dataSource));
/* 106 */     adapter.setDatabasePlatform(this.properties.getDatabasePlatform());
/* 107 */     adapter.setGenerateDdl(this.properties.isGenerateDdl());
/* 108 */     return adapter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter, ObjectProvider<PersistenceUnitManager> persistenceUnitManager)
/*     */   {
/* 118 */     EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, this.properties.getProperties(), (PersistenceUnitManager)persistenceUnitManager.getIfAvailable());
/* 119 */     builder.setCallback(getVendorCallback());
/* 120 */     return builder;
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   @Primary
/*     */   @ConditionalOnMissingBean({LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class})
/*     */   public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder)
/*     */   {
/* 129 */     Map<String, Object> vendorProperties = getVendorProperties();
/* 130 */     customizeVendorProperties(vendorProperties);
/* 131 */     return factoryBuilder.dataSource(this.dataSource).packages(getPackagesToScan())
/* 132 */       .properties(vendorProperties).jta(isJta()).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract AbstractJpaVendorAdapter createJpaVendorAdapter();
/*     */   
/*     */ 
/*     */   protected abstract Map<String, Object> getVendorProperties();
/*     */   
/*     */ 
/*     */   protected void customizeVendorProperties(Map<String, Object> vendorProperties) {}
/*     */   
/*     */ 
/*     */   protected EntityManagerFactoryBuilder.EntityManagerFactoryBeanCallback getVendorCallback()
/*     */   {
/* 148 */     return null;
/*     */   }
/*     */   
/*     */   protected String[] getPackagesToScan()
/*     */   {
/* 153 */     List<String> packages = EntityScanPackages.get(this.beanFactory).getPackageNames();
/* 154 */     if ((packages.isEmpty()) && (AutoConfigurationPackages.has(this.beanFactory))) {
/* 155 */       packages = AutoConfigurationPackages.get(this.beanFactory);
/*     */     }
/* 157 */     return (String[])packages.toArray(new String[packages.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JtaTransactionManager getJtaTransactionManager()
/*     */   {
/* 165 */     return this.jtaTransactionManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean isJta()
/*     */   {
/* 173 */     return this.jtaTransactionManager != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JpaProperties getProperties()
/*     */   {
/* 181 */     return this.properties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final DataSource getDataSource()
/*     */   {
/* 189 */     return this.dataSource;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */   {
/* 194 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnWebApplication
/*     */   @ConditionalOnClass({WebMvcConfigurerAdapter.class})
/*     */   @ConditionalOnMissingBean({OpenEntityManagerInViewInterceptor.class, OpenEntityManagerInViewFilter.class})
/*     */   @ConditionalOnProperty(prefix="spring.jpa", name={"open-in-view"}, havingValue="true", matchIfMissing=true)
/*     */   protected static class JpaWebConfiguration
/*     */   {
/*     */     @Configuration
/*     */     protected static class JpaWebMvcConfiguration
/*     */       extends WebMvcConfigurerAdapter
/*     */     {
/*     */       @Bean
/*     */       public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor()
/*     */       {
/* 212 */         return new OpenEntityManagerInViewInterceptor();
/*     */       }
/*     */       
/*     */       public void addInterceptors(InterceptorRegistry registry)
/*     */       {
/* 217 */         registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\orm\jpa\JpaBaseConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */