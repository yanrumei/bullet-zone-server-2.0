/*     */ package org.springframework.boot.autoconfigure;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.boot.type.classreading.ConcurrentReferenceCachingMetadataReaderFactory;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
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
/*     */ class SharedMetadataReaderFactoryContextInitializer
/*     */   implements ApplicationContextInitializer<ConfigurableApplicationContext>
/*     */ {
/*     */   public static final String BEAN_NAME = "org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory";
/*     */   
/*     */   public void initialize(ConfigurableApplicationContext applicationContext)
/*     */   {
/*  57 */     applicationContext.addBeanFactoryPostProcessor(new CachingMetadataReaderFactoryPostProcessor(null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class CachingMetadataReaderFactoryPostProcessor
/*     */     implements BeanDefinitionRegistryPostProcessor, PriorityOrdered
/*     */   {
/*     */     public int getOrder()
/*     */     {
/*  72 */       return Integer.MIN_VALUE;
/*     */     }
/*     */     
/*     */ 
/*     */     public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */       throws BeansException
/*     */     {}
/*     */     
/*     */     public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
/*     */       throws BeansException
/*     */     {
/*  83 */       register(registry);
/*  84 */       configureConfigurationClassPostProcessor(registry);
/*     */     }
/*     */     
/*     */     private void register(BeanDefinitionRegistry registry) {
/*  88 */       RootBeanDefinition definition = new RootBeanDefinition(SharedMetadataReaderFactoryContextInitializer.SharedMetadataReaderFactoryBean.class);
/*     */       
/*  90 */       registry.registerBeanDefinition("org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory", definition);
/*     */     }
/*     */     
/*     */     private void configureConfigurationClassPostProcessor(BeanDefinitionRegistry registry)
/*     */     {
/*     */       try {
/*  96 */         BeanDefinition definition = registry.getBeanDefinition("org.springframework.context.annotation.internalConfigurationAnnotationProcessor");
/*     */         
/*  98 */         definition.getPropertyValues().add("metadataReaderFactory", new RuntimeBeanReference("org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory"));
/*     */       }
/*     */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class SharedMetadataReaderFactoryBean
/*     */     implements FactoryBean<ConcurrentReferenceCachingMetadataReaderFactory>, BeanClassLoaderAware, ApplicationListener<ContextRefreshedEvent>
/*     */   {
/*     */     private ConcurrentReferenceCachingMetadataReaderFactory metadataReaderFactory;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setBeanClassLoader(ClassLoader classLoader)
/*     */     {
/* 118 */       this.metadataReaderFactory = new ConcurrentReferenceCachingMetadataReaderFactory(classLoader);
/*     */     }
/*     */     
/*     */ 
/*     */     public ConcurrentReferenceCachingMetadataReaderFactory getObject()
/*     */       throws Exception
/*     */     {
/* 125 */       return this.metadataReaderFactory;
/*     */     }
/*     */     
/*     */     public Class<?> getObjectType()
/*     */     {
/* 130 */       return CachingMetadataReaderFactory.class;
/*     */     }
/*     */     
/*     */     public boolean isSingleton()
/*     */     {
/* 135 */       return true;
/*     */     }
/*     */     
/*     */     public void onApplicationEvent(ContextRefreshedEvent event)
/*     */     {
/* 140 */       this.metadataReaderFactory.clearCache();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\SharedMetadataReaderFactoryContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */