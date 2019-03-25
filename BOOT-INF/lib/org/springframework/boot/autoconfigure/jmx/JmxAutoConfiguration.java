/*     */ package org.springframework.boot.autoconfigure.jmx;
/*     */ 
/*     */ import javax.management.MBeanServer;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.SearchStrategy;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.MBeanExportConfiguration.SpecificPlatform;
/*     */ import org.springframework.context.annotation.Primary;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.jmx.export.MBeanExporter;
/*     */ import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
/*     */ import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
/*     */ import org.springframework.jmx.export.naming.ObjectNamingStrategy;
/*     */ import org.springframework.jmx.support.MBeanServerFactoryBean;
/*     */ import org.springframework.jmx.support.RegistrationPolicy;
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
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({MBeanExporter.class})
/*     */ @ConditionalOnProperty(prefix="spring.jmx", name={"enabled"}, havingValue="true", matchIfMissing=true)
/*     */ public class JmxAutoConfiguration
/*     */   implements EnvironmentAware, BeanFactoryAware
/*     */ {
/*     */   private RelaxedPropertyResolver propertyResolver;
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/*  64 */     this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.jmx.");
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */   {
/*  69 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @Primary
/*     */   @ConditionalOnMissingBean(value={MBeanExporter.class}, search=SearchStrategy.CURRENT)
/*     */   public AnnotationMBeanExporter mbeanExporter(ObjectNamingStrategy namingStrategy) {
/*  76 */     AnnotationMBeanExporter exporter = new AnnotationMBeanExporter();
/*  77 */     exporter.setRegistrationPolicy(RegistrationPolicy.FAIL_ON_EXISTING);
/*  78 */     exporter.setNamingStrategy(namingStrategy);
/*  79 */     String server = this.propertyResolver.getProperty("server", "mbeanServer");
/*  80 */     if (StringUtils.hasLength(server)) {
/*  81 */       exporter.setServer((MBeanServer)this.beanFactory.getBean(server, MBeanServer.class));
/*     */     }
/*  83 */     return exporter;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean(value={ObjectNamingStrategy.class}, search=SearchStrategy.CURRENT)
/*     */   public ParentAwareNamingStrategy objectNamingStrategy() {
/*  89 */     ParentAwareNamingStrategy namingStrategy = new ParentAwareNamingStrategy(new AnnotationJmxAttributeSource());
/*     */     
/*  91 */     String defaultDomain = this.propertyResolver.getProperty("default-domain");
/*  92 */     if (StringUtils.hasLength(defaultDomain)) {
/*  93 */       namingStrategy.setDefaultDomain(defaultDomain);
/*     */     }
/*  95 */     return namingStrategy;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({MBeanServer.class})
/*     */   public MBeanServer mbeanServer() {
/* 101 */     MBeanExportConfiguration.SpecificPlatform platform = MBeanExportConfiguration.SpecificPlatform.get();
/* 102 */     if (platform != null) {
/* 103 */       return platform.getMBeanServer();
/*     */     }
/* 105 */     MBeanServerFactoryBean factory = new MBeanServerFactoryBean();
/* 106 */     factory.setLocateExistingServerIfPossible(true);
/* 107 */     factory.afterPropertiesSet();
/* 108 */     return factory.getObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jmx\JmxAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */