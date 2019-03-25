/*     */ package org.springframework.boot.autoconfigure.orm.jpa;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.persistence.EntityManagerFactory;
/*     */ import javax.sql.DataSource;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.boot.autoconfigure.jdbc.DataSourceInitializedEvent;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*     */ import org.springframework.core.type.AnnotationMetadata;
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
/*     */ class DataSourceInitializedPublisher
/*     */   implements BeanPostProcessor
/*     */ {
/*     */   @Autowired
/*     */   private ApplicationContext applicationContext;
/*     */   private DataSource dataSource;
/*     */   private JpaProperties properties;
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/*  54 */     return bean;
/*     */   }
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/*  60 */     if ((bean instanceof DataSource))
/*     */     {
/*  62 */       this.dataSource = ((DataSource)bean);
/*     */     }
/*  64 */     if ((bean instanceof JpaProperties)) {
/*  65 */       this.properties = ((JpaProperties)bean);
/*     */     }
/*  67 */     if ((bean instanceof EntityManagerFactory)) {
/*  68 */       publishEventIfRequired((EntityManagerFactory)bean);
/*     */     }
/*  70 */     return bean;
/*     */   }
/*     */   
/*     */   private void publishEventIfRequired(EntityManagerFactory entityManagerFactory) {
/*  74 */     DataSource dataSource = findDataSource(entityManagerFactory);
/*  75 */     if ((dataSource != null) && (isInitializingDatabase(dataSource)))
/*     */     {
/*  77 */       this.applicationContext.publishEvent(new DataSourceInitializedEvent(dataSource));
/*     */     }
/*     */   }
/*     */   
/*     */   private DataSource findDataSource(EntityManagerFactory entityManagerFactory)
/*     */   {
/*  83 */     Object dataSource = entityManagerFactory.getProperties().get("javax.persistence.nonJtaDataSource");
/*  84 */     return (dataSource != null) && ((dataSource instanceof DataSource)) ? (DataSource)dataSource : this.dataSource;
/*     */   }
/*     */   
/*     */   private boolean isInitializingDatabase(DataSource dataSource)
/*     */   {
/*  89 */     if (this.properties == null) {
/*  90 */       return true;
/*     */     }
/*     */     
/*  93 */     Map<String, String> hibernate = this.properties.getHibernateProperties(dataSource);
/*  94 */     if (hibernate.containsKey("hibernate.hbm2ddl.auto")) {
/*  95 */       return true;
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Registrar
/*     */     implements ImportBeanDefinitionRegistrar
/*     */   {
/*     */     private static final String BEAN_NAME = "dataSourceInitializedPublisher";
/*     */     
/*     */ 
/*     */ 
/*     */     public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*     */     {
/* 112 */       if (!registry.containsBeanDefinition("dataSourceInitializedPublisher")) {
/* 113 */         GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 114 */         beanDefinition.setBeanClass(DataSourceInitializedPublisher.class);
/* 115 */         beanDefinition.setRole(2);
/*     */         
/*     */ 
/* 118 */         beanDefinition.setSynthetic(true);
/* 119 */         registry.registerBeanDefinition("dataSourceInitializedPublisher", beanDefinition);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\orm\jpa\DataSourceInitializedPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */