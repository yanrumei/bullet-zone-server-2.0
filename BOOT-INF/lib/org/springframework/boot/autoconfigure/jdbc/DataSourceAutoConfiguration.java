/*     */ package org.springframework.boot.autoconfigure.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import javax.sql.DataSource;
/*     */ import javax.sql.XADataSource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.tomcat.jdbc.pool.ConnectionPool;
/*     */ import org.apache.tomcat.jdbc.pool.DataSourceProxy;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Condition;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
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
/*     */ @ConditionalOnClass({DataSource.class, EmbeddedDatabaseType.class})
/*     */ @EnableConfigurationProperties({DataSourceProperties.class})
/*     */ @Import({DataSourceInitializerPostProcessor.Registrar.class, DataSourcePoolMetadataProvidersConfiguration.class})
/*     */ public class DataSourceAutoConfiguration
/*     */ {
/*  70 */   private static final Log logger = LogFactory.getLog(DataSourceAutoConfiguration.class);
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public DataSourceInitializer dataSourceInitializer(DataSourceProperties properties, ApplicationContext applicationContext)
/*     */   {
/*  76 */     return new DataSourceInitializer(properties, applicationContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean containsAutoConfiguredDataSource(ConfigurableListableBeanFactory beanFactory)
/*     */   {
/*     */     try
/*     */     {
/*  88 */       BeanDefinition beanDefinition = beanFactory.getBeanDefinition("dataSource");
/*  89 */       return EmbeddedDataSourceConfiguration.class.getName()
/*  90 */         .equals(beanDefinition.getFactoryBeanName());
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {}
/*  93 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @Conditional({DataSourceAutoConfiguration.EmbeddedDatabaseCondition.class})
/*     */   @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
/*     */   @Import({EmbeddedDataSourceConfiguration.class})
/*     */   protected static class EmbeddedDatabaseConfiguration {}
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @Conditional({DataSourceAutoConfiguration.PooledDataSourceCondition.class})
/*     */   @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
/*     */   @Import({DataSourceConfiguration.Tomcat.class, DataSourceConfiguration.Hikari.class, DataSourceConfiguration.Dbcp.class, DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.Generic.class})
/*     */   protected static class PooledDataSourceConfiguration {}
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnProperty(prefix="spring.datasource", name={"jmx-enabled"})
/*     */   @ConditionalOnClass(name={"org.apache.tomcat.jdbc.pool.DataSourceProxy"})
/*     */   @Conditional({DataSourceAutoConfiguration.DataSourceAvailableCondition.class})
/*     */   @ConditionalOnMissingBean(name={"dataSourceMBean"})
/*     */   protected static class TomcatDataSourceJmxConfiguration
/*     */   {
/*     */     @Bean
/*     */     public Object dataSourceMBean(DataSource dataSource)
/*     */     {
/* 125 */       if ((dataSource instanceof DataSourceProxy)) {
/*     */         try {
/* 127 */           return ((DataSourceProxy)dataSource).createPool().getJmxPool();
/*     */         }
/*     */         catch (SQLException ex) {
/* 130 */           DataSourceAutoConfiguration.logger.warn("Cannot expose DataSource to JMX (could not connect)");
/*     */         }
/*     */       }
/* 133 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class PooledDataSourceCondition
/*     */     extends AnyNestedCondition
/*     */   {
/*     */     PooledDataSourceCondition()
/*     */     {
/* 145 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @Conditional({DataSourceAutoConfiguration.PooledDataSourceAvailableCondition.class})
/*     */     static class PooledDataSourceAvailable {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @ConditionalOnProperty(prefix="spring.datasource", name={"type"})
/*     */     static class ExplicitType {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class PooledDataSourceAvailableCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 169 */       ConditionMessage.Builder message = ConditionMessage.forCondition("PooledDataSource", new Object[0]);
/* 170 */       if (getDataSourceClassLoader(context) != null) {
/* 171 */         return 
/* 172 */           ConditionOutcome.match(message.foundExactly("supported DataSource"));
/*     */       }
/* 174 */       return 
/* 175 */         ConditionOutcome.noMatch(message.didNotFind("supported DataSource").atAll());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private ClassLoader getDataSourceClassLoader(ConditionContext context)
/*     */     {
/* 186 */       Class<?> dataSourceClass = new DataSourceBuilder(context.getClassLoader()).findType();
/* 187 */       return dataSourceClass == null ? null : dataSourceClass.getClassLoader();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class EmbeddedDatabaseCondition
/*     */     extends SpringBootCondition
/*     */   {
/* 199 */     private final SpringBootCondition pooledCondition = new DataSourceAutoConfiguration.PooledDataSourceCondition();
/*     */     
/*     */ 
/*     */ 
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 205 */       ConditionMessage.Builder message = ConditionMessage.forCondition("EmbeddedDataSource", new Object[0]);
/* 206 */       if (anyMatches(context, metadata, new Condition[] { this.pooledCondition })) {
/* 207 */         return 
/* 208 */           ConditionOutcome.noMatch(message.foundExactly("supported pooled data source"));
/*     */       }
/*     */       
/* 211 */       EmbeddedDatabaseType type = EmbeddedDatabaseConnection.get(context.getClassLoader()).getType();
/* 212 */       if (type == null) {
/* 213 */         return 
/* 214 */           ConditionOutcome.noMatch(message.didNotFind("embedded database").atAll());
/*     */       }
/* 216 */       return ConditionOutcome.match(message.found("embedded database").items(new Object[] { type }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Order(2147483637)
/*     */   static class DataSourceAvailableCondition
/*     */     extends SpringBootCondition
/*     */   {
/* 228 */     private final SpringBootCondition pooledCondition = new DataSourceAutoConfiguration.PooledDataSourceCondition();
/*     */     
/* 230 */     private final SpringBootCondition embeddedCondition = new DataSourceAutoConfiguration.EmbeddedDatabaseCondition();
/*     */     
/*     */ 
/*     */ 
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 236 */       ConditionMessage.Builder message = ConditionMessage.forCondition("DataSourceAvailable", new Object[0]);
/* 237 */       if ((hasBean(context, DataSource.class)) || 
/* 238 */         (hasBean(context, XADataSource.class))) {
/* 239 */         return 
/* 240 */           ConditionOutcome.match(message.foundExactly("existing data source bean"));
/*     */       }
/* 242 */       if (anyMatches(context, metadata, new Condition[] { this.pooledCondition, this.embeddedCondition }))
/*     */       {
/* 244 */         return ConditionOutcome.match(message
/* 245 */           .foundExactly("existing auto-configured data source bean"));
/*     */       }
/* 247 */       return 
/* 248 */         ConditionOutcome.noMatch(message.didNotFind("any existing data source bean").atAll());
/*     */     }
/*     */     
/*     */     private boolean hasBean(ConditionContext context, Class<?> type) {
/* 252 */       return BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context
/* 253 */         .getBeanFactory(), type, true, false).length > 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */