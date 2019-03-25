/*     */ package org.springframework.boot.autoconfigure.liquibase;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.sql.DataSource;
/*     */ import liquibase.exception.LiquibaseException;
/*     */ import liquibase.integration.spring.SpringLiquibase;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
/*     */ import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
/*     */ import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
/*     */ import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({SpringLiquibase.class})
/*     */ @ConditionalOnBean({DataSource.class})
/*     */ @ConditionalOnProperty(prefix="liquibase", name={"enabled"}, matchIfMissing=true)
/*     */ @AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
/*     */ public class LiquibaseAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({SpringLiquibase.class})
/*     */   @EnableConfigurationProperties({LiquibaseProperties.class})
/*     */   @Import({LiquibaseAutoConfiguration.LiquibaseJpaDependencyConfiguration.class})
/*     */   public static class LiquibaseConfiguration
/*     */   {
/*     */     private final LiquibaseProperties properties;
/*     */     private final ResourceLoader resourceLoader;
/*     */     private final DataSource dataSource;
/*     */     private final DataSource liquibaseDataSource;
/*     */     
/*     */     public LiquibaseConfiguration(LiquibaseProperties properties, ResourceLoader resourceLoader, ObjectProvider<DataSource> dataSource, @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource)
/*     */     {
/*  85 */       this.properties = properties;
/*  86 */       this.resourceLoader = resourceLoader;
/*  87 */       this.dataSource = ((DataSource)dataSource.getIfUnique());
/*  88 */       this.liquibaseDataSource = ((DataSource)liquibaseDataSource.getIfAvailable());
/*     */     }
/*     */     
/*     */     @PostConstruct
/*     */     public void checkChangelogExists() {
/*  93 */       if (this.properties.isCheckChangeLogLocation())
/*     */       {
/*  95 */         Resource resource = this.resourceLoader.getResource(this.properties.getChangeLog());
/*  96 */         Assert.state(resource.exists(), "Cannot find changelog location: " + resource + " (please add changelog or check your Liquibase configuration)");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @Bean
/*     */     public SpringLiquibase liquibase()
/*     */     {
/* 105 */       SpringLiquibase liquibase = createSpringLiquibase();
/* 106 */       liquibase.setChangeLog(this.properties.getChangeLog());
/* 107 */       liquibase.setContexts(this.properties.getContexts());
/* 108 */       liquibase.setDefaultSchema(this.properties.getDefaultSchema());
/* 109 */       liquibase.setDropFirst(this.properties.isDropFirst());
/* 110 */       liquibase.setShouldRun(this.properties.isEnabled());
/* 111 */       liquibase.setLabels(this.properties.getLabels());
/* 112 */       liquibase.setChangeLogParameters(this.properties.getParameters());
/* 113 */       liquibase.setRollbackFile(this.properties.getRollbackFile());
/* 114 */       return liquibase;
/*     */     }
/*     */     
/*     */     private SpringLiquibase createSpringLiquibase() {
/* 118 */       DataSource liquibaseDataSource = getDataSource();
/* 119 */       if (liquibaseDataSource != null) {
/* 120 */         SpringLiquibase liquibase = new SpringLiquibase();
/* 121 */         liquibase.setDataSource(liquibaseDataSource);
/* 122 */         return liquibase;
/*     */       }
/* 124 */       SpringLiquibase liquibase = new LiquibaseAutoConfiguration.DataSourceClosingSpringLiquibase(null);
/* 125 */       liquibase.setDataSource(createNewDataSource());
/* 126 */       return liquibase;
/*     */     }
/*     */     
/*     */     private DataSource getDataSource() {
/* 130 */       if (this.liquibaseDataSource != null) {
/* 131 */         return this.liquibaseDataSource;
/*     */       }
/* 133 */       if (this.properties.getUrl() == null) {
/* 134 */         return this.dataSource;
/*     */       }
/* 136 */       return null;
/*     */     }
/*     */     
/*     */     private DataSource createNewDataSource() {
/* 140 */       return 
/*     */       
/* 142 */         DataSourceBuilder.create().url(this.properties.getUrl()).username(this.properties.getUser()).password(this.properties.getPassword()).build();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
/*     */   @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
/*     */   protected static class LiquibaseJpaDependencyConfiguration
/*     */     extends EntityManagerFactoryDependsOnPostProcessor
/*     */   {
/*     */     public LiquibaseJpaDependencyConfiguration()
/*     */     {
/* 158 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class DataSourceClosingSpringLiquibase
/*     */     extends SpringLiquibase
/*     */   {
/*     */     public void afterPropertiesSet()
/*     */       throws LiquibaseException
/*     */     {
/* 171 */       super.afterPropertiesSet();
/* 172 */       closeDataSource();
/*     */     }
/*     */     
/*     */     private void closeDataSource() {
/* 176 */       Class<?> dataSourceClass = getDataSource().getClass();
/* 177 */       Method closeMethod = ReflectionUtils.findMethod(dataSourceClass, "close");
/* 178 */       if (closeMethod != null) {
/* 179 */         ReflectionUtils.invokeMethod(closeMethod, getDataSource());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\liquibase\LiquibaseAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */