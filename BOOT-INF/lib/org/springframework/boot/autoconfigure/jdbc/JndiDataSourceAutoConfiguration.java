/*    */ package org.springframework.boot.autoconfigure.jdbc;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
/*    */ import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
/*    */ import org.springframework.jmx.export.MBeanExporter;
/*    */ import org.springframework.jmx.support.JmxUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @AutoConfigureBefore({XADataSourceAutoConfiguration.class, DataSourceAutoConfiguration.class})
/*    */ @ConditionalOnClass({DataSource.class, EmbeddedDatabaseType.class})
/*    */ @ConditionalOnProperty(prefix="spring.datasource", name={"jndi-name"})
/*    */ @EnableConfigurationProperties({DataSourceProperties.class})
/*    */ public class JndiDataSourceAutoConfiguration
/*    */ {
/*    */   private final ApplicationContext context;
/*    */   
/*    */   public JndiDataSourceAutoConfiguration(ApplicationContext context)
/*    */   {
/* 54 */     this.context = context;
/*    */   }
/*    */   
/*    */   @Bean(destroyMethod="")
/*    */   @ConditionalOnMissingBean
/*    */   public DataSource dataSource(DataSourceProperties properties) {
/* 60 */     JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
/* 61 */     DataSource dataSource = dataSourceLookup.getDataSource(properties.getJndiName());
/* 62 */     excludeMBeanIfNecessary(dataSource, "dataSource");
/* 63 */     return dataSource;
/*    */   }
/*    */   
/*    */   private void excludeMBeanIfNecessary(Object candidate, String beanName) {
/* 67 */     for (MBeanExporter mbeanExporter : this.context
/* 68 */       .getBeansOfType(MBeanExporter.class).values()) {
/* 69 */       if (JmxUtils.isMBean(candidate.getClass())) {
/* 70 */         mbeanExporter.addExcludedBean(beanName);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\JndiDataSourceAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */