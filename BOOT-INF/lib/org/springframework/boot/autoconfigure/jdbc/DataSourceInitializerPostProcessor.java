/*    */ package org.springframework.boot.autoconfigure.jdbc;
/*    */ 
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*    */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.core.type.AnnotationMetadata;
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
/*    */ class DataSourceInitializerPostProcessor
/*    */   implements BeanPostProcessor, Ordered
/*    */ {
/* 41 */   private int order = Integer.MIN_VALUE;
/*    */   @Autowired
/*    */   private BeanFactory beanFactory;
/*    */   
/* 45 */   public int getOrder() { return this.order; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*    */     throws BeansException
/*    */   {
/* 54 */     return bean;
/*    */   }
/*    */   
/*    */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*    */     throws BeansException
/*    */   {
/* 60 */     if ((bean instanceof DataSource))
/*    */     {
/* 62 */       this.beanFactory.getBean(DataSourceInitializer.class);
/*    */     }
/* 64 */     return bean;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static class Registrar
/*    */     implements ImportBeanDefinitionRegistrar
/*    */   {
/*    */     private static final String BEAN_NAME = "dataSourceInitializerPostProcessor";
/*    */     
/*    */ 
/*    */ 
/*    */     public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*    */     {
/* 79 */       if (!registry.containsBeanDefinition("dataSourceInitializerPostProcessor")) {
/* 80 */         GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 81 */         beanDefinition.setBeanClass(DataSourceInitializerPostProcessor.class);
/* 82 */         beanDefinition.setRole(2);
/*    */         
/*    */ 
/* 85 */         beanDefinition.setSynthetic(true);
/* 86 */         registry.registerBeanDefinition("dataSourceInitializerPostProcessor", beanDefinition);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceInitializerPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */