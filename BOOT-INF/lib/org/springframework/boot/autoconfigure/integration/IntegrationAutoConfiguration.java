/*     */ package org.springframework.boot.autoconfigure.integration;
/*     */ 
/*     */ import javax.management.MBeanServer;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.SearchStrategy;
/*     */ import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.integration.config.EnableIntegration;
/*     */ import org.springframework.integration.config.EnableIntegrationManagement;
/*     */ import org.springframework.integration.gateway.GatewayProxyFactoryBean;
/*     */ import org.springframework.integration.jmx.config.EnableIntegrationMBeanExport;
/*     */ import org.springframework.integration.monitor.IntegrationMBeanExporter;
/*     */ import org.springframework.integration.support.management.IntegrationManagementConfigurer;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass({EnableIntegration.class})
/*     */ @AutoConfigureAfter({JmxAutoConfiguration.class})
/*     */ public class IntegrationAutoConfiguration
/*     */ {
/*     */   @ConditionalOnMissingBean({GatewayProxyFactoryBean.class})
/*     */   @Import({IntegrationAutoConfigurationScanRegistrar.class})
/*     */   protected static class IntegrationComponentScanAutoConfiguration {}
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({EnableIntegrationManagement.class, EnableIntegrationMBeanExport.class})
/*     */   @ConditionalOnMissingBean(value={IntegrationManagementConfigurer.class}, name={"integrationManagementConfigurer"}, search=SearchStrategy.CURRENT)
/*     */   @ConditionalOnProperty(prefix="spring.jmx", name={"enabled"}, havingValue="true", matchIfMissing=true)
/*     */   protected static class IntegrationManagementConfiguration
/*     */   {
/*     */     @Configuration
/*     */     @EnableIntegrationManagement(defaultCountsEnabled="true", defaultStatsEnabled="true")
/*     */     protected static class EnableIntegrationManagementConfiguration {}
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({EnableIntegrationMBeanExport.class})
/*     */   @ConditionalOnMissingBean(value={IntegrationMBeanExporter.class}, search=SearchStrategy.CURRENT)
/*     */   @ConditionalOnProperty(prefix="spring.jmx", name={"enabled"}, havingValue="true", matchIfMissing=true)
/*     */   protected static class IntegrationJmxConfiguration
/*     */     implements EnvironmentAware, BeanFactoryAware
/*     */   {
/*     */     private BeanFactory beanFactory;
/*     */     private RelaxedPropertyResolver propertyResolver;
/*     */     
/*     */     public void setBeanFactory(BeanFactory beanFactory)
/*     */       throws BeansException
/*     */     {
/*  83 */       this.beanFactory = beanFactory;
/*     */     }
/*     */     
/*     */     public void setEnvironment(Environment environment)
/*     */     {
/*  88 */       this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.jmx.");
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public IntegrationMBeanExporter integrationMbeanExporter()
/*     */     {
/*  94 */       IntegrationMBeanExporter exporter = new IntegrationMBeanExporter();
/*  95 */       String defaultDomain = this.propertyResolver.getProperty("default-domain");
/*  96 */       if (StringUtils.hasLength(defaultDomain)) {
/*  97 */         exporter.setDefaultDomain(defaultDomain);
/*     */       }
/*  99 */       String server = this.propertyResolver.getProperty("server", "mbeanServer");
/* 100 */       if (StringUtils.hasLength(server)) {
/* 101 */         exporter.setServer((MBeanServer)this.beanFactory.getBean(server, MBeanServer.class));
/*     */       }
/* 103 */       return exporter;
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @EnableIntegration
/*     */   protected static class IntegrationConfiguration {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\integration\IntegrationAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */